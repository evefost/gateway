package com.xie.gateway.cmd.factory;

import com.xie.gateway.cmd.CommandListener;
import com.xie.gateway.cmd.ReHttpClientRibbonCommand;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.ribbon.apache.RibbonLoadBalancingHttpClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.cloud.netflix.zuul.filters.route.apache.HttpClientRibbonCommand;
import org.springframework.cloud.netflix.zuul.filters.route.apache.HttpClientRibbonCommandFactory;

import java.util.Collections;
import java.util.Set;

/**
 * Created by xieyang on 19/11/9.
 */
public class ReHttpClientRibbonCommandFactory extends HttpClientRibbonCommandFactory {

    private final SpringClientFactory clientFactory;

    private final ZuulProperties zuulProperties;

    private  CommandListener commandListener;

    public ReHttpClientRibbonCommandFactory(SpringClientFactory clientFactory, ZuulProperties zuulProperties) {
        this(clientFactory, zuulProperties, Collections.<ZuulFallbackProvider>emptySet(),null);

    }

    public ReHttpClientRibbonCommandFactory(SpringClientFactory clientFactory, ZuulProperties zuulProperties, Set<ZuulFallbackProvider> fallbackProviders, CommandListener commandListener) {
        super(clientFactory, zuulProperties, fallbackProviders);
        this.clientFactory = clientFactory;
        this.zuulProperties = zuulProperties;
        this.commandListener = commandListener;
    }

    @Override
    public HttpClientRibbonCommand create(final RibbonCommandContext context) {
        ZuulFallbackProvider zuulFallbackProvider = getFallbackProvider(context.getServiceId());
        final String serviceId = context.getServiceId();
        final RibbonLoadBalancingHttpClient client = this.clientFactory.getClient(
                serviceId, RibbonLoadBalancingHttpClient.class);
        client.setLoadBalancer(this.clientFactory.getLoadBalancer(serviceId));

        return new ReHttpClientRibbonCommand(serviceId, client, context, zuulProperties, zuulFallbackProvider,
                clientFactory.getClientConfig(serviceId),commandListener);
    }
}
