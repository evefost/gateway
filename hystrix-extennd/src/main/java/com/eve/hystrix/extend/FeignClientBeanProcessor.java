package com.eve.hystrix.extend;


import com.eve.hystrix.extend.core.CommandListener;
import com.eve.hystrix.extend.core.HystrixFallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;


/**
 * feign client 代理处理
 *
 * @author xie
 */
public class FeignClientBeanProcessor implements BeanPostProcessor {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${spring.application.name:}")
    private String appName;

    @Value("${server.port:}")
    private String port;

    @Autowired(required = false)
    private CommandListener listener;

    @Autowired(required = false)
    private HystrixFallback hystrixFallback;

    private Enhancer enhancer = new Enhancer();


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        Class<?> clazz = bean.getClass();
        if (isCreateControllerProxy(clazz)) {
            Object proxy = cglibProxy(clazz.getSuperclass(), new FeignMethodInvocationHandler(bean, hystrixFallback, listener));
            if (proxy != null) {
                return proxy;
            }
            return bean;
        } else if (isCreateFeignProxy(clazz)) {
            Object proxy = Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(),
                    new FeignMethodInvocationHandler(bean,hystrixFallback, listener));
            return proxy;
        }
        return bean;
    }



    private boolean isCreateControllerProxy(Class<?> clazz) {

//        Controller annotation = AnnotationUtils.findAnnotation(clazz, Controller.class);
//        if (annotation != null) {
//            if(clazz.getPackage().getName().startsWith("com.xhg")){
//                return true;
//            }
//        }
        return false;
    }

    private boolean isCreateFeignProxy(Class<?> claz) {
        Class<?>[] interfaces = claz.getInterfaces();
        for (Class clazz : interfaces) {
            FeignClient annotation = AnnotationUtils.findAnnotation(clazz, FeignClient.class);
            if (annotation != null) {
                NoHystrix noHystrix = AnnotationUtils.findAnnotation(clazz, NoHystrix.class);
                if (noHystrix != null) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * final 方法没法创建代理(cglib代理为实现灰的子类，故无法覆盖其final方法)
     *
     * @param clazz
     * @return
     */
    private Object cglibProxy(Class clazz, MethodInterceptor callback) {
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(callback);
        Constructor<?>[] constructors = clazz.getConstructors();
        Parameter[] parameters = constructors[0].getParameters();
        if (parameters.length > 0) {
            return null;
        }
        Object[] arguments = new Object[parameters.length];
        Class[] paramsTypes = new Class[parameters.length];
        return enhancer.create(paramsTypes, arguments);
    }


}
