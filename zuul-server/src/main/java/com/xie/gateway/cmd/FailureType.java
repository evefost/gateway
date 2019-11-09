package com.xie.gateway.cmd;

/**
 *
 *
 * @author xieyang
 * @date 19/11/9
 */
public enum FailureType {
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
     *
     */
    OTHER

}
