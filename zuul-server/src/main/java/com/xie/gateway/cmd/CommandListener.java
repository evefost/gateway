package com.xie.gateway.cmd;


import com.netflix.hystrix.HystrixCommand;

/**
 * 熔断器监听器
 * @author xie
 */
public interface CommandListener {


    /**
     *  成功回调
     */
    void onSuccess( CommandInfo commandInfo);


    /**
     * 失败回调
     * @param commandInfo
     */
    void onFailure(CommandInfo commandInfo);


    /**
     * 熔断器打开（粒度为接口级）
     */
    void onCircuitBreakerOpen(CommandInfo commandInfo);

    /**
     * 熔断器关闭 （粒度为接口级）
     */
    void onCircuitBreakerClose(CommandInfo commandInfo);



}
