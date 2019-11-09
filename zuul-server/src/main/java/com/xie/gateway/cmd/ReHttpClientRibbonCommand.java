package com.xie.gateway.cmd;

import com.netflix.client.config.IClientConfig;
import org.springframework.cloud.netflix.ribbon.apache.RibbonLoadBalancingHttpClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.cloud.netflix.zuul.filters.route.apache.HttpClientRibbonCommand;
import org.springframework.http.client.ClientHttpResponse;

import java.util.concurrent.ConcurrentHashMap;

import static com.xie.gateway.cmd.FailureType.OTHER;

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
            commandListener.onSuccess(this);
        }
        return response;
    }

    @Override
    protected ClientHttpResponse getFallbackResponse() {
        boolean circuitBreakerOpen = isCircuitBreakerOpen();
        FailureInfo failureInfo = new FailureInfo();
        failureInfo.setFailureType(getFailureType());
        if (commandListener != null) {
            commandListener.onFailure(this,failureInfo);
        }

        return super.getFallbackResponse();
    }



    private FailureType getFailureType() {
        FailureType failureType;
        if (isCircuitBreakerOpen()) {
            failureType= FailureType.CIRCUITBREAK_EROPEN;
        } else if (isResponseRejected()) {
            failureType= FailureType.CIRCUITBREAK_EROPEN;
        } else if (isResponseTimedOut()) {
            failureType= FailureType.CIRCUITBREAK_EROPEN;
        } else {
           failureType = OTHER;
        }

        return failureType;

    }


    /**
     * 据统计维度确定是熔断器否为重新打弄
     *
     * @return
     */
    private boolean isCircuitReOpen() {
        if (!isCircuitBreakerOpen()) {
            return false;
        }
        //todo

        return false;
    }

    private boolean isCircuitReClose() {
//        String appName = mappingInfo.getAppName();
//        Boolean status = serverStatus.get(appName);
//        if (status != null && status) {
//            return true;
//        }
        return false;
    }
}
