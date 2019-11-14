package com.eve.hystrix.extend;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xie
 */
public class RequestMappingInfo {

    public static Map<Method, RequestMappingInfo> methodMappings = new HashMap<>(0);

    private Class clazz;
    private Method method;
    private String currentServiceId;
    private String serviceId;
    private String url;
    private String uri;
    private String requestMethod;

    private Method fallbackMethod;

    private boolean isHystrix = true;

    private Integer executionTimeoutInMilliseconds;

    /**
     * 熔断消息提示
     *
     * @return
     */
    private String circuitBreakMessage;

    /**
     * 超时提示
     */
    String timeOutMessage;

    /**
     * 失败提示
     */
    String failureMessage;

    /**
     * 拒绝提示
     */
    String rejectMessage;


    /**
     * 错误熔断阀值(50)
     */
    int circuitBreakerErrorThresholdPercentage;

    /**********************线程池的配置**********************/

    int coreSize;

    /**
     * 线程池最大值(50)
     */
    int maximumSize;

    /**
     * 队列拒绝阀值 5
     */
    int queueSizeRejectionThreshold;

    public int getCoreSize() {
        return coreSize;
    }

    public void setCoreSize(int coreSize) {
        this.coreSize = coreSize;
    }

    public static Map<Method, RequestMappingInfo> getMethodMappings() {
        return methodMappings;
    }

    public int getCircuitBreakerErrorThresholdPercentage() {
        return circuitBreakerErrorThresholdPercentage;
    }

    public void setCircuitBreakerErrorThresholdPercentage(int circuitBreakerErrorThresholdPercentage) {
        this.circuitBreakerErrorThresholdPercentage = circuitBreakerErrorThresholdPercentage;
    }

    public int getMaximumSize() {
        return maximumSize;
    }

    public void setMaximumSize(int maximumSize) {
        this.maximumSize = maximumSize;
    }

    public int getQueueSizeRejectionThreshold() {
        return queueSizeRejectionThreshold;
    }

    public void setQueueSizeRejectionThreshold(int queueSizeRejectionThreshold) {
        this.queueSizeRejectionThreshold = queueSizeRejectionThreshold;
    }

    public static void setMethodMappings(
        Map<Method, RequestMappingInfo> methodMappings) {
        RequestMappingInfo.methodMappings = methodMappings;
    }

    public String getTimeOutMessage() {
        return timeOutMessage;
    }

    public void setTimeOutMessage(String timeOutMessage) {
        this.timeOutMessage = timeOutMessage;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public String getRejectMessage() {
        return rejectMessage;
    }

    public void setRejectMessage(String rejectMessage) {
        this.rejectMessage = rejectMessage;
    }

    public Method getFallbackMethod() {
        return fallbackMethod;
    }

    public void setFallbackMethod(Method fallbackMethod) {
        this.fallbackMethod = fallbackMethod;
    }

    public Integer getExecutionTimeoutInMilliseconds() {
        return executionTimeoutInMilliseconds;
    }

    public void setExecutionTimeoutInMilliseconds(Integer executionTimeoutInMilliseconds) {
        this.executionTimeoutInMilliseconds = executionTimeoutInMilliseconds;
    }

    public String getCircuitBreakMessage() {
        return circuitBreakMessage;
    }

    public void setCircuitBreakMessage(String circuitBreakMessage) {
        this.circuitBreakMessage = circuitBreakMessage;
    }


    public boolean isHystrix() {
        return isHystrix;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public void setHystrix(boolean hystrix) {
        isHystrix = hystrix;
    }



    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }



    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getCurrentServiceId() {
        return currentServiceId;
    }

    public void setCurrentServiceId(String currentServiceId) {
        this.currentServiceId = currentServiceId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}