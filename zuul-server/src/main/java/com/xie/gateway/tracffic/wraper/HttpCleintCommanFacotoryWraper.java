package com.xie.gateway.tracffic.wraper;

import com.xie.gateway.tracffic.SingleServiceProperties;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.ribbon.apache.RibbonLoadBalancingHttpClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.cloud.netflix.zuul.filters.route.apache.HttpClientRibbonCommand;

import java.util.Collections;
import java.util.Set;

/**
 * Created by xieyang on 18/6/14.
 */
public class HttpCleintCommanFacotoryWraper extends AbstractCommandFactoryWraper {

    private final SpringClientFactory clientFactory;

    private final ZuulProperties zuulProperties;

    public HttpCleintCommanFacotoryWraper(SpringClientFactory clientFactory, ZuulProperties zuulProperties) {
        this(clientFactory, zuulProperties, Collections.<FallbackProvider>emptySet());
    }

    public HttpCleintCommanFacotoryWraper(SpringClientFactory clientFactory, ZuulProperties zuulProperties,
                                          Set<FallbackProvider> fallbackProviders) {
        super(fallbackProviders);
        this.clientFactory = clientFactory;
        this.zuulProperties = zuulProperties;
    }

    @Override
    public HttpClientRibbonCommand create(final RibbonCommandContext context) {
        FallbackProvider zuulFallbackProvider = getFallbackProvider(context.getServiceId());
        final String serviceId = context.getServiceId();
        final RibbonLoadBalancingHttpClient client = this.clientFactory.getClient(
                serviceId, RibbonLoadBalancingHttpClient.class);
        client.setLoadBalancer(this.clientFactory.getLoadBalancer(serviceId));
        // todo 替换掉统一的zuulProperties
        SingleServiceProperties singleServiceProperties = servicesProperties.get(serviceId);

        return new HttpClientRibbonCommand(serviceId, client, context, zuulProperties, zuulFallbackProvider,
                clientFactory.getClientConfig(serviceId));
    }
}
