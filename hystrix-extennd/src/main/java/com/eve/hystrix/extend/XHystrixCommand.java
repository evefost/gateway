package com.eve.hystrix.extend;

import com.eve.hystrix.extend.core.CommandInfo;
import com.eve.hystrix.extend.core.CommandListener;
import com.netflix.hystrix.*;
import com.netflix.hystrix.HystrixCommandGroupKey.Factory;
import com.netflix.hystrix.strategy.properties.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import other.BusinessException;

import java.lang.reflect.Method;

/**
 * @author xie
 */
public class XHystrixCommand extends HystrixCommand<Object> {


    private Logger logger = LoggerFactory.getLogger(XHystrixCommand.class);



    private RequestMappingInfo mappingInfo;

    private CommandListener listener;

    private CommandInfo commandInfo;

    private Object target;

    private Object[] args;


    public XHystrixCommand(RequestMappingInfo mappingInfo, Object target, Object[] args,
                           CommandListener listener) {
        super(createSetter(mappingInfo));
        this.mappingInfo = mappingInfo;
        this.target = target;
        this.args = args;
        this.listener = listener;
        this.commandInfo = CommandSupport.buildCommandInfo(this, this.listener);

    }

    public static Setter createSetter(RequestMappingInfo mappingInfo) {
        String groupKey = mappingInfo.getAppName();
        String commandKey = mappingInfo.getUrl();
        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter();
        if (mappingInfo.getExecutionTimeoutInMilliseconds() != null) {
            commandProperties
                .withExecutionTimeoutInMilliseconds(mappingInfo.getExecutionTimeoutInMilliseconds());
        }
        if (mappingInfo.getCircuitBreakerErrorThresholdPercentage() > 0) {
            commandProperties
                .withCircuitBreakerErrorThresholdPercentage(mappingInfo.getCircuitBreakerErrorThresholdPercentage());
        }
        //线程池配置
        HystrixThreadPoolProperties.Setter threadPoolProperties = HystrixThreadPoolProperties.Setter();
        if (mappingInfo.getCoreSize() > 0) {
            threadPoolProperties.withCoreSize(mappingInfo.getCoreSize());
        }
        if (mappingInfo.getMaximumSize() > 0) {
            threadPoolProperties.withMaximumSize(mappingInfo.getMaximumSize());
        }
        if (mappingInfo.getQueueSizeRejectionThreshold() > 0) {
            threadPoolProperties.withQueueSizeRejectionThreshold(mappingInfo.getQueueSizeRejectionThreshold());
        }

        Setter setter = Setter.withGroupKey(Factory.asKey(groupKey))
            .andCommandKey(HystrixCommandKey.Factory
                .asKey(commandKey))
            .andCommandPropertiesDefaults(commandProperties)
            .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(groupKey))
            .andThreadPoolPropertiesDefaults(threadPoolProperties);
        return setter;
    }


    @Override
    protected Object run() throws Exception {
       try {

           Object result = mappingInfo.getMethod().invoke(target, args);
           CommandSupport.onSuccess(commandInfo);
           return result;
       }finally {
           if(listener != null){
               listener.onComplete(commandInfo);
           }
       }
    }




    @Override
    protected Object getFallback() {
        Object result = null;
        Method fallbackMethod = mappingInfo.getFallbackMethod();
        if (fallbackMethod != null) {
            try {
                if (fallbackMethod.getParameters().length == 0) {
                    //没配置参数的fallback
                    result = mappingInfo.getFallbackMethod().invoke(target, new Object[]{});
                } else {
                    result = mappingInfo.getFallbackMethod().invoke(target, args);
                }
            } catch (Throwable e) {
                logger.error("用户自定义fallback:{}失败:", mappingInfo.getMethod().toString(), e);
            }
        }
        if(result == null){
            return defaultFallback();
        }
        CommandSupport.onFailure(commandInfo);
        return result;
    }


    private Object defaultFallback() {
       return null;
    }


}
