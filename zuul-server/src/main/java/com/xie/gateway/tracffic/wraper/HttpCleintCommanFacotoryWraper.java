package com.xie.gateway.tracffic.wraper;

import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.ribbon.apache.RibbonLoadBalancingHttpClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.route.apache.HttpClientRibbonCommand;

import java.util.Collections;
import java.util.Set;

/**
 * Created by xieyang on 18/6/14.
 */
public class HttpCleintCommanFacotoryWraper extends AbstractCommandFactoryWraper {

    private final SpringClientFactory clientFactory;

    private final ZuulProperties defaultZuulProperties;

    public HttpCleintCommanFacotoryWraper(SpringClientFactory clientFactory, ZuulProperties zuulProperties) {
        this(clientFactory, zuulProperties, Collections.<FallbackProvider>emptySet());
    }

    public HttpCleintCommanFacotoryWraper(SpringClientFactory clientFactory, ZuulProperties zuulProperties,
                                          Set<FallbackProvider> fallbackProviders) {
        super(fallbackProviders);
        this.clientFactory = clientFactory;
        this.defaultZuulProperties = zuulProperties;
    }

    @Override
    public HttpClientRibbonCommand create(final RibbonCommandContext context) {
        FallbackProvider zuulFallbackProvider = getFallbackProvider(context.getServiceId());
        final String serviceId = context.getServiceId();
        final RibbonLoadBalancingHttpClient client = this.clientFactory.getClient(
                serviceId, RibbonLoadBalancingHttpClient.class);
        client.setLoadBalancer(this.clientFactory.getLoadBalancer(serviceId));
        ZuulProperties zuulProperties = servicesProperties.get(serviceId);
        return new HttpClientRibbonCommand(serviceId, client, context, zuulProperties==null?defaultZuulProperties:zuulProperties, zuulFallbackProvider,
                clientFactory.getClientConfig(serviceId));
    }
}
