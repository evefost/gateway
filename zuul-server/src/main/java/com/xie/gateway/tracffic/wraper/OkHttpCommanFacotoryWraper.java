package com.xie.gateway.tracffic.wraper;

import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.ribbon.okhttp.OkHttpLoadBalancingClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.cloud.netflix.zuul.filters.route.okhttp.OkHttpRibbonCommand;

import java.util.Collections;
import java.util.Set;

/**
 * Created by xieyang on 18/6/14.
 */
public class OkHttpCommanFacotoryWraper extends AbstractCommandFactoryWraper  {

    private SpringClientFactory clientFactory;

    private final ZuulProperties defaultZuulProperties;

    public OkHttpCommanFacotoryWraper(SpringClientFactory clientFactory, ZuulProperties zuulProperties) {
        this(clientFactory, zuulProperties, Collections.<FallbackProvider>emptySet());
    }

    public OkHttpCommanFacotoryWraper(SpringClientFactory clientFactory, ZuulProperties zuulProperties,
                                      Set<FallbackProvider> zuulFallbackProviders) {
        super(zuulFallbackProviders);
        this.clientFactory = clientFactory;
        this.defaultZuulProperties = zuulProperties;
    }

    @Override
    public OkHttpRibbonCommand create(final RibbonCommandContext context) {
        final String serviceId = context.getServiceId();
        FallbackProvider fallbackProvider = getFallbackProvider(serviceId);
        final OkHttpLoadBalancingClient client = this.clientFactory.getClient(
                serviceId, OkHttpLoadBalancingClient.class);
        client.setLoadBalancer(this.clientFactory.getLoadBalancer(serviceId));
        ZuulProperties zuulProperties = servicesProperties.get(serviceId);
        return new OkHttpRibbonCommand(serviceId, client, context, (zuulProperties==null)?defaultZuulProperties:zuulProperties, fallbackProvider,
                clientFactory.getClientConfig(serviceId));
    }

}
