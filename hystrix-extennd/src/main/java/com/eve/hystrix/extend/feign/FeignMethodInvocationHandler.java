package com.eve.hystrix.extend.feign;


import com.eve.hystrix.extend.RequestMappingInfo;
import com.eve.hystrix.extend.XHystrixCommand;
import com.eve.hystrix.extend.core.CommandListener;
import com.eve.hystrix.extend.core.HystrixFallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author xie
 */
public class FeignMethodInvocationHandler implements InvocationHandler, MethodInterceptor {

    protected Logger logger = LoggerFactory.getLogger(FeignMethodInvocationHandler.class);

    private Object target;


    private String currentAppName;

    private HystrixFallback hystrixFallback;

    private CommandListener commandListener;


    public FeignMethodInvocationHandler(Object target,
                                        String currentAppName, HystrixFallback hystrixFallback, CommandListener listener) {
        this.target = target;
        this.currentAppName = currentAppName;
        this.hystrixFallback = hystrixFallback;
        this.commandListener = listener;

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RequestMappingInfo requestMappingInfo = RequestMappingInfo.methodMappings.get(method);
        if (requestMappingInfo != null && requestMappingInfo.isHystrix()) {
            return new XHystrixCommand(requestMappingInfo, target, args, hystrixFallback, commandListener).execute();
        }
        return method.invoke(target, args);
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        return invoke(null, method, args);
    }


}