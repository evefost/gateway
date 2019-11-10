package com.xie.gateway.cmd.cmd;

import com.netflix.client.config.IClientConfig;
import com.xie.gateway.cmd.CommandInfo;
import com.xie.gateway.cmd.CommandListener;
import com.xie.gateway.cmd.ExecuteResultType;
import org.springframework.cloud.netflix.ribbon.apache.RibbonLoadBalancingHttpClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.cloud.netflix.zuul.filters.route.apache.HttpClientRibbonCommand;
import org.springframework.http.client.ClientHttpResponse;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xieyang on 19/11/9.
 */
public class ReHttpClientRibbonCommand extends HttpClientRibbonCommand {

    final static ConcurrentHashMap<String, Boolean> serverStatus = new ConcurrentHashMap<String, Boolean>();

    private CommandListener commandListener;


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
