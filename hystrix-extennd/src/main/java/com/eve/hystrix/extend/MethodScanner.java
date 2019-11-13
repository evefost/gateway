package com.eve.hystrix.extend;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author xie yang
 * @date 2018/10/4-19:25
 */

public class MethodScanner implements BeanClassLoaderAware, EnvironmentAware, ResourceLoaderAware {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected Environment environment;

    private ResourceLoader resourceLoader;


    protected ClassLoader classLoader;

    public MethodScanner(Environment environment){
        this.environment = environment;
    }


    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {

            @Override
            protected boolean isCandidateComponent(
                AnnotatedBeanDefinition beanDefinition) {
                if (beanDefinition.getMetadata().isIndependent()) {
                    // TODO until SPR-11711 will be resolved
                    if (beanDefinition.getMetadata().isInterface()
                        && beanDefinition.getMetadata()
                        .getInterfaceNames().length == 1
                        && Annotation.class.getName().equals(beanDefinition
                        .getMetadata().getInterfaceNames()[0])) {
                        try {
                            Class<?> target = ClassUtils.forName(
                                beanDefinition.getMetadata().getClassName(),
                                MethodScanner.class.getClassLoader());
                            return !target.isAnnotation();
                        } catch (Exception ex) {
                            ex.printStackTrace();

                        }
                    }
                    return true;
                }
                return false;

            }
        };
    }

    private String resolve(String value) {
        if (StringUtils.hasText(value)) {
            return this.environment.resolvePlaceholders(value);
        }
        return value;
    }

    public Map<Method, RequestMappingInfo> scanRequestMapping(Set<String> basePackages)
        throws ClassNotFoundException {

        Map<Method, RequestMappingInfo> requestMappings = new HashMap<Method, RequestMappingInfo>();
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);
        AnnotationTypeFilter feignFilter = new AnnotationTypeFilter(FeignClient.class);
        AnnotationTypeFilter controllerFilter = new AnnotationTypeFilter(Controller.class);
        scanner.addIncludeFilter(feignFilter);
        scanner.addIncludeFilter(controllerFilter);
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner
                .findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {
                    AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                    parseMappings(beanDefinition.getBeanClassName(), requestMappings);
                }
            }
        }
        return requestMappings;

    }

    private void parseMappings(String beanClassName, Map<Method, RequestMappingInfo> requestMappings)
        throws ClassNotFoundException {

        Class<?> targetClass = classLoader.loadClass(beanClassName);
        FeignClient feignClient = targetClass.getAnnotation(FeignClient.class);
        String applicationName = resolve("${spring.application.name}");
        if (feignClient != null) {
            applicationName = resolve(feignClient.name());
        }
        Method[] methods = null;
        if (targetClass.isInterface()) {
            methods = targetClass.getMethods();
        } else {
            methods = targetClass.getDeclaredMethods();
        }
        Map<String,Method> tempMethods = new HashMap<>();
        for (Method method : methods) {
            tempMethods.put(method.getName(),method);
        }

        String url = "";
        if (targetClass.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping classRequestMapping = targetClass.getAnnotation(RequestMapping.class);
            String[] value = classRequestMapping.value();
            // value 有可能为空 com.xhg.customer.remote.gateway.RemoteSiteService
            if (!StringUtils.isEmpty(value) && value.length > 0) {
                url = resolve(value[0]);
            }
        }
        RequestMappingInfo clientMapping = new RequestMappingInfo();
        clientMapping.setUrl(url);
        if (targetClass.isAnnotationPresent(XCommand.class)) {
            parseXhgCommand(targetClass.getAnnotation(XCommand.class),clientMapping,tempMethods);
        }
        for (Method method : methods) {
            RequestMappingInfo mappingInfo = new RequestMappingInfo();
            mappingInfo.setClazz(targetClass);
            mappingInfo.setAppName(applicationName);
            mappingInfo.setMethod(method);
            if (method.isAnnotationPresent(XCommand.class)) {
                parseXhgCommand(method.getAnnotation(XCommand.class), mappingInfo, tempMethods);
                parseRequestMapping(method, mappingInfo);
                mergeInfo(clientMapping, mappingInfo);
                requestMappings.put(method, mappingInfo);
            }
        }
    }


    private void parseXhgCommand(XCommand cmd , RequestMappingInfo mappingInfo, Map<String,Method> tempMethods){
        if (cmd.timeoutInMilliseconds() > 0) {
            mappingInfo.setExecutionTimeoutInMilliseconds(cmd.timeoutInMilliseconds());
        }

        if (cmd.circuitBreakerErrorThresholdPercentage() > 0) {
            mappingInfo.setCircuitBreakerErrorThresholdPercentage(cmd.circuitBreakerErrorThresholdPercentage());
        }


        if(StringUtils.isEmpty(cmd.fallbackMethod())){
            mappingInfo.setCircuitBreakMessage(cmd.fallbackMethod());
        }
        if (!StringUtils.isEmpty(cmd.circuitBreakMessage())) {
            mappingInfo.setCircuitBreakMessage(cmd.circuitBreakMessage());
        }
        if (!StringUtils.isEmpty(cmd.timeOutMessage())) {
            mappingInfo.setTimeOutMessage(cmd.timeOutMessage());
        }
        if (!StringUtils.isEmpty(cmd.rejectMessage())) {
            mappingInfo.setRejectMessage(cmd.rejectMessage());
        }
        if (!StringUtils.isEmpty(cmd.failureMessage())) {
            mappingInfo.setFailureMessage(cmd.failureMessage());
        }
        if(!StringUtils.isEmpty(cmd.fallbackMethod())){
            Method fallback = tempMethods.get(cmd.fallbackMethod());
            if(fallback == null){
                logger.warn(mappingInfo.getMethod().toString()+" set fallback but not found "+cmd.fallbackMethod());
            }else {
                mappingInfo.setFallbackMethod(fallback);
                checkFallbackMethod(mappingInfo);
            }
        }

        if (cmd.maximumSize() > 0) {
            mappingInfo.setMaximumSize(cmd.maximumSize());
        }
        Integer coreSize = Integer.parseInt(resolve(cmd.coreSize()));
        if (coreSize > 0) {
            mappingInfo.setCoreSize(coreSize);
        }
        if (cmd.queueSizeRejectionThreshold() > 0) {
            mappingInfo.setQueueSizeRejectionThreshold(cmd.queueSizeRejectionThreshold());
        }
    }




    private void checkFallbackMethod(RequestMappingInfo mappingInfo){
        Method method = mappingInfo.getMethod();
        Method fallbackMethod = mappingInfo.getFallbackMethod();
        if(!method.getReturnType().equals(fallbackMethod.getReturnType())){
            throw new HystrixException("fallback接口返回类型与原接口类型必须相同"+method.toString());
        }
        if(fallbackMethod.getParameterCount()==0){
            return;
        }
        if(method.getParameterCount() !=fallbackMethod.getParameterCount()){
            throw new HystrixException("fallback接口参数长度与原接口参数长度必须相同"+method.toString());
        }
        Class<?>[] srcTypes = method.getParameterTypes();
        Class<?>[] fallbackTypes = fallbackMethod.getParameterTypes();
        for(int i=0;i<srcTypes.length;i++){
           if(!srcTypes[i].equals(fallbackTypes[i])){
               throw new HystrixException("fallback接口参数类型与原接口参数类型必须一致"+method.toString());
           }
        }
    }

    private void parseRequestMapping(Method method,RequestMappingInfo mappingInfo){

        String[] value = null;
        String reMethod = null;
        String methodUrl = null;
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            value = mapping.value();
            RequestMethod[] requestMethods = mapping.method();
            if (requestMethods.length > 0) {
                for (RequestMethod m : requestMethods) {
                    reMethod = m.name() + ",";
                }
                reMethod = reMethod.substring(0, reMethod.length() - 1);
            } else {
                reMethod = "GET,POST,DELETE,OPTIONS";
            }
        } else if (method.isAnnotationPresent(GetMapping.class)) {
            GetMapping mapping = method.getAnnotation(GetMapping.class);
            value = mapping.value();
            reMethod = "GET";
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            PostMapping mapping = method.getAnnotation(PostMapping.class);
            value = mapping.value();
            reMethod = "POST";
        }
        if (value != null && value.length == 1) {
            methodUrl = value[0];
        }
        mappingInfo.setUrl(methodUrl);
        mappingInfo.setMethod(method);
        mappingInfo.setRequestMethod(reMethod);
        if (method.isAnnotationPresent(NoHystrix.class)||method.toString().contains("default")) {
            mappingInfo.setHystrix(false);
        }
    }


    private void mergeInfo(RequestMappingInfo clientMapping,RequestMappingInfo mappingInfo){
        if(!StringUtils.isEmpty(clientMapping.getUrl())){
            mappingInfo.setUrl(clientMapping.getUrl()+mappingInfo.getUrl());
        }
        String uri = "->" + mappingInfo.getAppName() + "]" + mappingInfo.getUrl() + "[" + mappingInfo.getRequestMethod() + "]";
        mappingInfo.setUri(uri);

        if(mappingInfo.getExecutionTimeoutInMilliseconds() == null){
            mappingInfo.setExecutionTimeoutInMilliseconds(clientMapping.getExecutionTimeoutInMilliseconds());
        }
        String commandPrefix = "hystrix.command.";
        if (mappingInfo.getExecutionTimeoutInMilliseconds() == null) {

            String property = ".execution.isolation.thread.timeoutInMilliseconds";
            String key = commandPrefix + mappingInfo.getClazz().getName() + "." + mappingInfo.getMethod().getName() + property;
            String timeoutInMilliseconds = this.environment.getProperty(key);
            if (!StringUtils.isEmpty(timeoutInMilliseconds)) {
                mappingInfo.setExecutionTimeoutInMilliseconds(Integer.parseInt(timeoutInMilliseconds));
            }
        }
        if(mappingInfo.getCircuitBreakerErrorThresholdPercentage()==0){
            mappingInfo.setCircuitBreakerErrorThresholdPercentage(clientMapping.getCircuitBreakerErrorThresholdPercentage());
        }
        if(mappingInfo.getCircuitBreakerErrorThresholdPercentage()==0){
            String key = commandPrefix + "circuitBreakerErrorThresholdPercentage";
            mappingInfo.setCircuitBreakerErrorThresholdPercentage(Integer.parseInt(environment.getProperty(key,"50")));
        }

        //
        if(StringUtils.isEmpty(mappingInfo.getCircuitBreakMessage())){
            mappingInfo.setCircuitBreakMessage(clientMapping.getCircuitBreakMessage());
        }
        if(StringUtils.isEmpty(mappingInfo.getCircuitBreakMessage())){
            String key = commandPrefix + "default.circuitBreakMessage";
            mappingInfo.setCircuitBreakMessage(environment.getProperty(key,"服务暂时不可用，请稍后再试!"));
        }
        //
        if(StringUtils.isEmpty(mappingInfo.getFailureMessage())){
            mappingInfo.setFailureMessage(clientMapping.getFailureMessage());
        }
        if(StringUtils.isEmpty(mappingInfo.getFailureMessage())){
            String key = commandPrefix + "default.failureMessage";
            mappingInfo.setFailureMessage(environment.getProperty(key,"调用服务失败，请稍后再试!"));
        }
        //
        if(StringUtils.isEmpty(mappingInfo.getTimeOutMessage())){
            mappingInfo.setTimeOutMessage(clientMapping.getTimeOutMessage());
        }
        if(StringUtils.isEmpty(mappingInfo.getTimeOutMessage())){
            String key = commandPrefix + "default.timeOutMessage";
            mappingInfo.setTimeOutMessage(environment.getProperty(key,"服务响应超时"));
        }
        //
        if(StringUtils.isEmpty(mappingInfo.getRejectMessage())){
            mappingInfo.setRejectMessage(clientMapping.getRejectMessage());
        }
        if(StringUtils.isEmpty(mappingInfo.getRejectMessage())){
            String key = commandPrefix + "default.rejectMessage";
            mappingInfo.setRejectMessage(environment.getProperty(key,"系统繁忙，请稍后再试!"));
        }


        //thread
        String threadPrefix = "hystrix.threadpool.default.";
        //线程池coreSize
        if(mappingInfo.getCoreSize()==0){
            mappingInfo.setCoreSize(clientMapping.getCoreSize());
        }
        if(mappingInfo.getCoreSize()==0){
            String key = threadPrefix + "coreSize";
            mappingInfo.setCoreSize(Integer.parseInt(environment.getProperty(key,"10")));
        }

        //线程池最大值

        if(mappingInfo.getMaximumSize()==0){
            mappingInfo.setMaximumSize(clientMapping.getMaximumSize());
        }
        if(mappingInfo.getMaximumSize()==0){
            String key = threadPrefix + "maximumSize";
            mappingInfo.setMaximumSize(Integer.parseInt(environment.getProperty(key,"10")));
        }
        //队列拒绝阀值
        if(mappingInfo.getQueueSizeRejectionThreshold()==0){
            mappingInfo.setQueueSizeRejectionThreshold(clientMapping.getQueueSizeRejectionThreshold());
        }
        if(mappingInfo.getQueueSizeRejectionThreshold()==0){
            String key = threadPrefix + "queueSizeRejectionThreshold";
            mappingInfo.setQueueSizeRejectionThreshold(Integer.parseInt(environment.getProperty(key,"5")));
        }


    }

}
