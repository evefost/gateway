package com.xie.gateway.cmd.factory;

import com.netflix.niws.client.http.RestClient;
import com.xie.gateway.cmd.CommandListener;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.RestClientRibbonCommand;
import org.springframework.cloud.netflix.zuul.filters.route.RestClientRibbonCommandFactory;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;

import java.util.Collections;
import java.util.Set;

/**
 * Created by xieyang on 19/11/9.
 */
public class ReRestClientRibbonCommandFactory extends RestClientRibbonCommandFactory {

    private final SpringClientFactory clientFactory;

    private ZuulProperties zuulProperties;

    private  CommandListener commandListener;

    public ReRestClientRibbonCommandFactory(SpringClientFactory clientFactory) {
        this(clientFactory, new ZuulProperties(), Collections.<ZuulFallbackProvider>emptySet(),null);
    }

    public ReRestClientRibbonCommandFactory(SpringClientFactory clientFactory, ZuulProperties zuulProperties, Set<ZuulFallbackProvider> zuulFallbackProviders,CommandListener commandListener) {
        super(clientFactory, zuulProperties, zuulFallbackProviders);
        this.clientFactory = clientFactory;
        this.zuulProperties = zuulProperties;
        this.commandListener = commandListener;

    }



    @Override
    @SuppressWarnings("deprecation")
    public RestClientRibbonCommand create(RibbonCommandContext context) {
        String serviceId = context.getServiceId();
        ZuulFallbackProvider fallbackProvider = getFallbackProvider(serviceId);
        RestClient restClient = this.clientFactory.getClient(serviceId,
                RestClient.class);
        return new RestClientRibbonCommand(context.getServiceId(), restClient, context,
                this.zuulProperties, fallbackProvider, clientFactory.getClientConfig(serviceId));
    }

}
