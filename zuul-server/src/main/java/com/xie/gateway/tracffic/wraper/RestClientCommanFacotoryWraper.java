package com.xie.gateway.tracffic.wraper;

import com.netflix.niws.client.http.RestClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.cloud.netflix.zuul.filters.route.RestClientRibbonCommand;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;

import java.util.Collections;
import java.util.Set;

/**
 * Created by xieyang on 18/6/14.
 */
public class RestClientCommanFacotoryWraper extends AbstractCommandFactoryWraper {


    private SpringClientFactory clientFactory;

    private final ZuulProperties defaultZuulProperties;

    public RestClientCommanFacotoryWraper(SpringClientFactory clientFactory) {
        this(clientFactory, new ZuulProperties(), Collections.<FallbackProvider>emptySet());
    }

    public RestClientCommanFacotoryWraper(SpringClientFactory clientFactory,
                                          ZuulProperties zuulProperties,
                                          Set<FallbackProvider> zuulFallbackProviders) {
        super(zuulFallbackProviders);
        this.clientFactory = clientFactory;
        this.defaultZuulProperties = zuulProperties;
    }

    @Override
    @SuppressWarnings("deprecation")
    public RestClientRibbonCommand create(RibbonCommandContext context) {
        String serviceId = context.getServiceId();
        FallbackProvider fallbackProvider = getFallbackProvider(serviceId);
        RestClient restClient = this.clientFactory.getClient(serviceId,
                RestClient.class);
        ZuulProperties zuulProperties = servicesProperties.get(serviceId);
        return new RestClientRibbonCommand(context.getServiceId(), restClient, context,
                (zuulProperties==null)?defaultZuulProperties:zuulProperties, fallbackProvider, clientFactory.getClientConfig(serviceId));
    }

    public SpringClientFactory getClientFactory() {
        return clientFactory;
    }

}
