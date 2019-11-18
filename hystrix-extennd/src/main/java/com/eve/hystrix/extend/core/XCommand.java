package com.eve.hystrix.extend.core;

import java.lang.annotation.*;

/**
 * 熔断接口标识
 * 注意，被标识接口线程会被切换，跟threadlocal相关的变量数据会失效
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public  @interface XCommand {

    /**
     * 请求超时间
     *
     * @return
     */
    int timeoutInMilliseconds() default 0;

    /**
     * 用户自定回调接口
     *
     * @return
     */
    String fallbackMethod() default "";

    /**
     * 熔断消息提示
     *
     * @return
     */
    String circuitBreakMessage() default "";

    /**
     * 超时提示
     */
    String timeOutMessage() default "";

    /**
     * 失败提示
     */
    String failureMessage() default "";

    /**
     * 拒绝提示
     */
    String rejectMessage() default "";

    /**
     * 错误熔断阀值50
     *
     * @return
     */
    int circuitBreakerErrorThresholdPercentage() default 0;

    /**********************线程池的配置**********************/

    String coreSize() default "0";

    /**
     * 线程池最大值10
     *
     * @return
     */
    int maximumSize() default 0;

    /**
     * 队列拒绝阀值 5
     *
     * @return
     */
    int queueSizeRejectionThreshold() default 0;

}