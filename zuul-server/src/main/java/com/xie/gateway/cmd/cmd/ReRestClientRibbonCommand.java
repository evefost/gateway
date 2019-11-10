package com.xie.gateway.cmd.cmd;

import com.netflix.client.config.IClientConfig;
import com.netflix.client.http.HttpRequest;
import com.netflix.niws.client.http.RestClient;
import com.xie.gateway.cmd.CommandInfo;
import com.xie.gateway.cmd.CommandListener;
import com.xie.gateway.cmd.ExecuteResultType;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.RestClientRibbonCommand;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.MultiValueMap;

import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xieyang on 19/11/10.
 */
public class ReRestClientRibbonCommand extends RestClientRibbonCommand {
    final static ConcurrentHashMap<String, Boolean> serverStatus = new ConcurrentHashMap<String, Boolean>();

    private CommandListener commandListener;

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
    }

    public ReRestClientRibbonCommand(String commandKey, RestClient restClient, HttpRequest.Verb verb, String uri, Boolean retryable, MultiValueMap<String, String> headers, MultiValueMap<String, String> params, InputStream requestEntity) {
        super(commandKey, restClient, verb, uri, retryable, headers, params, requestEntity);
    }



    @Override
    protected ClientHttpResponse run() throws Exception {
        ClientHttpResponse response = super.run();
        if (commandListener != null) {
            CommandInfo commandInfo = new CommandInfo();
            commandInfo.setExecuteResultType(ExecuteResultType.SUCCESS);
            commandListener.onSuccess(commandInfo);
        }
        return response;
    }

    @Override
    protected ClientHttpResponse getFallbackResponse() {
        CommandInfo commandInfo = CommandSupport.buildFailureInfo(this);
        if(commandListener != null){
            commandListener.onFailure(commandInfo);
        }
        return super.getFallbackResponse();
    }
}
