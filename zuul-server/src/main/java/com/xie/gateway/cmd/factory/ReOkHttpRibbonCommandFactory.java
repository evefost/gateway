package com.xie.gateway.cmd.factory;

import com.xie.gateway.cmd.CommandListener;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.ribbon.okhttp.OkHttpLoadBalancingClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.cloud.netflix.zuul.filters.route.okhttp.OkHttpRibbonCommand;
import org.springframework.cloud.netflix.zuul.filters.route.okhttp.OkHttpRibbonCommandFactory;

import java.util.Collections;
import java.util.Set;

/**
 * Created by xieyang on 19/11/9.
 */
public class ReOkHttpRibbonCommandFactory extends OkHttpRibbonCommandFactory {

    private final SpringClientFactory clientFactory;

    private final ZuulProperties zuulProperties;
    private CommandListener commandListener;

    public ReOkHttpRibbonCommandFactory(SpringClientFactory clientFactory, ZuulProperties zuulProperties) {
        this(clientFactory, zuulProperties, Collections.<ZuulFallbackProvider>emptySet(),null);
    }

    public ReOkHttpRibbonCommandFactory(SpringClientFactory clientFactory, ZuulProperties zuulProperties, Set<ZuulFallbackProvider> zuulFallbackProviders,CommandListener commandListener) {
        super(clientFactory, zuulProperties, zuulFallbackProviders);
        this.clientFactory = clientFactory;
        this.zuulProperties = zuulProperties;
        this.commandListener = commandListener;
    }


    @Override
    public OkHttpRibbonCommand create(final RibbonCommandContext context) {
        final String serviceId = context.getServiceId();
        ZuulFallbackProvider fallbackProvider = getFallbackProvider(serviceId);
        final OkHttpLoadBalancingClient client = this.clientFactory.getClient(
                serviceId, OkHttpLoadBalancingClient.class);
        client.setLoadBalancer(this.clientFactory.getLoadBalancer(serviceId));

        return new OkHttpRibbonCommand(serviceId, client, context, zuulProperties, fallbackProvider,
                clientFactory.getClientConfig(serviceId));
    }
}
