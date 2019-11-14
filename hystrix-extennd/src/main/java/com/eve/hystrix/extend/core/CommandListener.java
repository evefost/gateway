package com.eve.hystrix.extend.core;


import org.slf4j.MDC;

/**
 * 熔断器监听器
 *
 * @author xie
 */
public class CommandListener {


    /**
     * 命令建创建
     *
     * @param commandInfo
     */
    public void onCommandCreate(CommandInfo commandInfo) {
        commandInfo.setProperty("uuid", MDC.get("uuid"));
    }


    /**
     * 成功回调
     */
    public void onSuccess(CommandInfo commandInfo) {
    }



    /**
     * 失败回调
     *
     * @param commandInfo
     */
    public void onFailure(CommandInfo commandInfo) {
    }


    /**
     * 熔断器打开（粒度为接口级）
     */
    public void onCircuitBreakerOpen(CommandInfo commandInfo) {
    }


    /**
     * 熔断器关闭 （粒度为接口级）
     */
    public void onCircuitBreakerClose(CommandInfo commandInfo) {
    }


    public void onComplete(CommandInfo commandInfo) {


    }


}
