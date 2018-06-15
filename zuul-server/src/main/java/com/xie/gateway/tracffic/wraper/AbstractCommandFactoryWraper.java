package com.xie.gateway.tracffic.wraper;

import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractCommandFactoryWraper implements CommanFactoryWraper {

	private Map<String, FallbackProvider> fallbackProviderCache;
    private FallbackProvider defaultFallbackProvider = null;

	protected  static ConcurrentHashMap<String,ZuulProperties> servicesProperties = new ConcurrentHashMap<>();

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
