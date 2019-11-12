package hystrix.feign;



import hystrix.HystrixStatusListener;
import hystrix.MethodScanner;
import hystrix.RequestMappingInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;



/**
 * feign client 代理处理
 * @author xie
 */
public class FeignClientBeanProcessor implements BeanPostProcessor, SmartInitializingSingleton {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${spring.application.name:}")
    private String appName;

    @Value("${server.port:}")
    private String port;

    @Autowired(required = false)
    private HystrixStatusListener listener;

    private Enhancer enhancer = new Enhancer();



    @Autowired
    private MethodScanner scanRemoteRequest;



    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
        throws BeansException {
        Class<?> clazz = bean.getClass();
        if(isCreateControllerProxy(clazz)){
            Object proxy =  cglibProxy(clazz.getSuperclass(),new FeignMethodInvocationHandler(bean,appName,listener));
            if(proxy != null){
                return proxy;
            }
            return bean;
        }else if(isCreateFeignProxy(clazz)){
            Object proxy = Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(),
                new FeignMethodInvocationHandler(bean, appName,listener));
            return proxy;
        }
        return bean;
    }

    private boolean isCreateProxy(Class<?> clazz) {
        if (isCreateControllerProxy(clazz)) {
            return true;
        }
        return isCreateFeignProxy(clazz);
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
            if (clazz.isAnnotationPresent(FeignClient.class)) {
                if (clazz.isAnnotationPresent(NoHystrix.class)) {
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
        if(parameters.length>0){
            return null;
        }
        Object[] arguments = new Object[parameters.length];
        Class[] paramsTypes = new Class[parameters.length];
        return enhancer.create(paramsTypes, arguments);
    }





    @Override
    public void afterSingletonsInstantiated() {
        Set<String> packages = new HashSet<>();
        packages.add("com.xhg");
        try {
            RequestMappingInfo.methodMappings = scanRemoteRequest.scanRequestMapping(packages);
        }catch (Throwable throwable){
            logger.warn("hystrix 熔断接口扫描失败:",throwable);
        }

    }
}
