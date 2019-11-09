package com.xie.gateway.cmd;


import com.netflix.hystrix.HystrixCommand;

/**
 * 熔断器监听器
 * @author xie
 */
public interface CommandListener {


    /**
     *  成功回调
     * @param cmd
     */
    void onSuccess( HystrixCommand cmd);


    /**
     * 失败回调
     * @param cmd
     */
    void onFailure(HystrixCommand cmd,FailureInfo failureInfo);


    /**
     * 熔断器打开（粒度为接口级）
     */
    void onCircuitBreakerOpen(HystrixCommand cmd);

    /**
     * 熔断器关闭 （粒度为接口级）
     */
    void onCircuitBreakerClose(HystrixCommand cmd);



}
