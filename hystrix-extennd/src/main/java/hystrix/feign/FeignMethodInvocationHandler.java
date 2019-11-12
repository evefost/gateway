package hystrix.feign;



import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import hystrix.HystrixStatusListener;
import hystrix.RequestMappingInfo;
import hystrix.XHystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * @author xie
 */
public class FeignMethodInvocationHandler implements InvocationHandler ,MethodInterceptor {

    protected Logger logger = LoggerFactory.getLogger(FeignMethodInvocationHandler.class);

    private Object target;




    private String currentAppName;

    private HystrixStatusListener listener;


    public FeignMethodInvocationHandler(Object target,
        String currentAppName,HystrixStatusListener listener) {
        this.target = target;
        this.currentAppName = currentAppName;
        this.listener = listener;

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object otherHandler = doBefore(method, args);
        if (otherHandler != null){
            return otherHandler;
        }
        RequestMappingInfo requestMappingInfo = RequestMappingInfo.methodMappings.get(method);
        if (requestMappingInfo != null && requestMappingInfo.isHystrix()) {
            return new XHystrixCommand(requestMappingInfo, target, args,listener).execute();
        }
        return method.invoke(target, args);
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        return doBefore(method, args);
    }

    private Object doBefore(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        if ("equals".equals(method.getName())) {
            try {
                Object
                    otherHandler =
                    args.length > 0 && args[0] != null ? Proxy.getInvocationHandler(args[0]) : null;
                return equals(otherHandler);
            } catch (IllegalArgumentException e) {
                return false;
            }
        } else if ("hashCode".equals(method.getName())) {
            return method.hashCode();
        } else if ("toString".equals(method.getName())) {
            return method.toString();
        }
        RequestMappingInfo requestMappingInfo = RequestMappingInfo.methodMappings.get(method);
        if (requestMappingInfo != null && requestMappingInfo.isHystrix()) {
            return new XHystrixCommand(requestMappingInfo, target, args,listener).execute();
        }
        return method.invoke(target, args);
    }

}