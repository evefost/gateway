package com.xie.gateway.tracffic.wraper;

import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractCommandFactoryWraper implements CommanFactoryWraper {

	private Map<String, FallbackProvider> fallbackProviderCache;

    private FallbackProvider defaultFallbackProvider = null;

	protected volatile   SpringClientFactory clientFactory;

	protected volatile ZuulProperties defaultZuulProperties;

	protected  static ConcurrentHashMap<String,ZuulProperties> servicesProperties = new ConcurrentHashMap<>();

	public AbstractCommandFactoryWraper(SpringClientFactory clientFactory) {
		this(clientFactory, new ZuulProperties(), Collections.<FallbackProvider>emptySet());
	}

	public AbstractCommandFactoryWraper(SpringClientFactory clientFactory,
										  ZuulProperties zuulProperties,
										  Set<FallbackProvider> zuulFallbackProviders) {
		this(zuulFallbackProviders);
		this.clientFactory = clientFactory;
		this.defaultZuulProperties = zuulProperties;
	}

	public AbstractCommandFactoryWraper(Set<FallbackProvider> fallbackProviders){
		this.fallbackProviderCache = new HashMap<>();
		for(FallbackProvider provider : fallbackProviders) {
			String route = provider.getRoute();
			if("*".equals(route) || route == null) {
				defaultFallbackProvider = provider;
			} else {
				fallbackProviderCache.put(route, provider);
			}
		}
	}

	protected FallbackProvider getFallbackProvider(String route) {
		FallbackProvider provider = fallbackProviderCache.get(route);
		if(provider == null) {
			provider = defaultFallbackProvider;
		}
		return provider;
	}
}
