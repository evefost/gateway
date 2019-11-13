package com.eve.hystrix.extend.core;

/**
 *
 *
 * @author xieyang
 * @date 19/11/9
 */
public enum ExecuteResultType {

    /**
     *成功
     */
    SUCCESS,
    /**
     * 熔断打开
     */
    CIRCUITBREAK_EROPEN,

    /**
     * 拒绝响应
     */
    RESPONSERE_JECTED,

    /**
     * 响应超时
     */
    RESPONSE_TIMEDOUT,


    /**
     * 连接失败
     */
    CONNECT_FAILURE,

    /**
     *
     */
    OTHER,

    /**
     *
     */
    UNKNOW

}
