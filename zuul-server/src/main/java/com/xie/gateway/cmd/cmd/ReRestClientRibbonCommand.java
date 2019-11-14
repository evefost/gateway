package com.xie.gateway.cmd.cmd;

import com.eve.hystrix.extend.CommandSupport;
import com.eve.hystrix.extend.core.CommandInfo;
import com.eve.hystrix.extend.core.CommandListener;
import com.netflix.client.config.IClientConfig;
import com.netflix.client.http.HttpRequest;
import com.netflix.niws.client.http.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.RestClientRibbonCommand;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.MultiValueMap;

import java.io.InputStream;


/**
 * Created by xieyang on 19/11/10.
 */
public class ReRestClientRibbonCommand extends RestClientRibbonCommand {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private CommandListener commandListener;

    private CommandInfo commandInfo;

    public ReRestClientRibbonCommand(String commandKey, RestClient client, RibbonCommandContext context, ZuulProperties zuulProperties) {
        this(commandKey, client, context, zuulProperties,null);
    }

    public ReRestClientRibbonCommand(String commandKey, RestClient client, RibbonCommandContext context, ZuulProperties zuulProperties, ZuulFallbackProvider zuulFallbackProvider) {
        this(commandKey, client, context, zuulProperties, zuulFallbackProvider,null);
    }

    public ReRestClientRibbonCommand(String commandKey, RestClient client, RibbonCommandContext context, ZuulProperties zuulProperties, ZuulFallbackProvider zuulFallbackProvider, IClientConfig config) {
        this(commandKey, client, context, zuulProperties, zuulFallbackProvider, config,null);
    }

    public ReRestClientRibbonCommand(String commandKey, RestClient client, RibbonCommandContext context, ZuulProperties zuulProperties, ZuulFallbackProvider zuulFallbackProvider, IClientConfig config,CommandListener listener) {
        super(commandKey, client, context, zuulProperties, zuulFallbackProvider, config);
        this.commandListener = listener;
        this.commandInfo = CommandSupport.buildCommandInfo(this,listener);
        commandInfo.setServiceId(commandKey);
        commandInfo.setUri(context.getUri());
    }

    public ReRestClientRibbonCommand(String commandKey, RestClient restClient, HttpRequest.Verb verb, String uri, Boolean retryable, MultiValueMap<String, String> headers, MultiValueMap<String, String> params, InputStream requestEntity) {
        super(commandKey, restClient, verb, uri, retryable, headers, params, requestEntity);
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
