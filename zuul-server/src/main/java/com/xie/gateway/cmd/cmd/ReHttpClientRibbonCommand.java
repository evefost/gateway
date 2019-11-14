package com.xie.gateway.cmd.cmd;

import com.eve.hystrix.extend.CommandSupport;
import com.eve.hystrix.extend.core.CommandInfo;
import com.eve.hystrix.extend.core.CommandListener;
import com.netflix.client.config.IClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.ribbon.apache.RibbonLoadBalancingHttpClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.cloud.netflix.zuul.filters.route.apache.HttpClientRibbonCommand;
import org.springframework.http.client.ClientHttpResponse;

/**
 * Created by xieyang on 19/11/9.
 */
public class ReHttpClientRibbonCommand extends HttpClientRibbonCommand {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    private CommandListener commandListener;

    private CommandInfo commandInfo;


    public ReHttpClientRibbonCommand(String commandKey, RibbonLoadBalancingHttpClient client, RibbonCommandContext context, ZuulProperties zuulProperties) {
        this(commandKey, client, context, zuulProperties,null);
    }

    public ReHttpClientRibbonCommand(String commandKey, RibbonLoadBalancingHttpClient client, RibbonCommandContext context, ZuulProperties zuulProperties, ZuulFallbackProvider zuulFallbackProvider) {
        this(commandKey, client, context, zuulProperties, zuulFallbackProvider,null);
    }

    public ReHttpClientRibbonCommand(String commandKey, RibbonLoadBalancingHttpClient client, RibbonCommandContext context, ZuulProperties zuulProperties, ZuulFallbackProvider zuulFallbackProvider, IClientConfig config) {
        this(commandKey, client, context, zuulProperties, zuulFallbackProvider, config,null);
    }

    public ReHttpClientRibbonCommand(String commandKey, RibbonLoadBalancingHttpClient client, RibbonCommandContext context, ZuulProperties zuulProperties, ZuulFallbackProvider zuulFallbackProvider, IClientConfig config,CommandListener commandListener) {
        super(commandKey, client, context, zuulProperties, zuulFallbackProvider, config);
        this.commandListener = commandListener;
        this.commandInfo = CommandSupport.buildCommandInfo(this,commandListener);
        commandInfo.setServiceId(commandKey);
        commandInfo.setUri(context.getUri());
    }




    @Override
    protected ClientHttpResponse run() throws Exception {
        try {
            ClientHttpResponse response = super.run();
            CommandSupport.onSuccess(commandInfo);
            return response;
        }finally {
            if (commandListener != null) {
                try {
                    commandListener.onComplete(commandInfo);
                } catch (Throwable throwable) {
                    logger.warn("command onComplete callback error ", throwable);
                }
            }
        }
    }

    @Override
    protected ClientHttpResponse getFallbackResponse() {
        try {
            return super.getFallbackResponse();
        }finally {
            CommandSupport.onFailure(commandInfo);
        }

    }



}
