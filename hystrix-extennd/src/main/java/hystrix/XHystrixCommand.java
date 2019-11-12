package hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey.Factory;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xie
 */
@RequestMapping
public class XHystrixCommand extends HystrixCommand<Object> {

    final static ConcurrentHashMap<String, Boolean> serverStatus = new ConcurrentHashMap<String, Boolean>();

    private Logger logger = LoggerFactory.getLogger(XHystrixCommand.class);

    private HttpServletRequest request;

    private RequestMappingInfo mappingInfo;

    private HystrixStatusListener listener;

    private Object target;

    private Object[] args;


    public XHystrixCommand(RequestMappingInfo mappingInfo, Object target, Object[] args,
                           HystrixStatusListener listener) {
        super(createSetter(mappingInfo));
        this.mappingInfo = mappingInfo;
        this.target = target;
        this.args = args;
        this.listener = listener;
        this.request = getRequest();
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
        //DataHolder.setRequest(request);
        Object invoke = mappingInfo.getMethod().invoke(target, args);
        doAfterSuccess();
        //成功移除
        //DataHolder.remove();
        return invoke;
    }

    private void doAfterSuccess() {
        if (listener != null) {
            listener.onSuccess(mappingInfo, this);
            if (isCircuitReClose()) {
                try {
                    listener.onCircuitBreakerClose(mappingInfo, this);
                } catch (Throwable e) {
                    logger.error("熔断监听接口出错", e);
                }
            }
        }

    }


    @Override
    protected Object getFallback() {
        Object result = null;
        if (listener != null) {
            listener.onFailure(mappingInfo, this);
        }
        Method fallbackMethod = mappingInfo.getFallbackMethod();
        if (fallbackMethod != null) {
            try {
                if (fallbackMethod.getParameters().length == 0) {
                    result = mappingInfo.getFallbackMethod().invoke(target, new Object[]{});
                } else {
                    result = mappingInfo.getFallbackMethod().invoke(target, args);
                }
            } catch (Throwable e) {
                logger.error("用户自定义fallback:{}失败:", mappingInfo.getMethod().toString(), e);
            }
        }
        boolean circuitReOpen = isCircuitReOpen();
        Throwable ex = getExecutionException();
        //BusinessException businessException = findBusinessException(getExecutionException());
        if (result == null) {
            result = defaultFallback(businessException);
        }
        loggerFallback(ex, businessException, circuitReOpen);
        if (listener != null && circuitReOpen) {
            try {
                listener.onCircuitBreakerOpen(mappingInfo, this);
            } catch (Throwable e) {
                logger.error("熔断监听接口出错", e);
            }
        }
        //保存证上面代码不出错
        //DataHolder.remove();
        return result;

    }


    private Object defaultFallback(BusinessException businessException) {
//        Class<?> returnType = mappingInfo.getMethod().getReturnType();
//        boolean isResponseBeanType = returnType.isAssignableFrom(ResponseBean.class);
//        ResponseBean<?> responseBean = null;
//        if (isCircuitBreakerOpen()) {
//            logger.warn("{} is circuit breaker open", mappingInfo.getUri());
//            if (isResponseBeanType) {
//                responseBean = ResponseBean
//                    .errorOfSystem(mappingInfo.getCircuitBreakMessage(), Status.SYSTEM_BUSY.getCode());
//            }
//        } else if (isResponseRejected()) {
//            logger.warn("{} is response rejected ", mappingInfo.getUri());
//            if (isResponseBeanType) {
//                responseBean = ResponseBean.errorOfSystem(mappingInfo.getRejectMessage(), Status.SYSTEM_BUSY.getCode());
//            }
//        } else if (isResponseTimedOut()) {
//            HystrixCommandProperties properties = getProperties();
//            HystrixProperty<Integer> integerHystrixProperty = properties.executionTimeoutInMilliseconds();
//            Integer timeout = integerHystrixProperty.get();
//            logger.warn("{} is response timed out :{} setting time out:{}", mappingInfo.getUri(),
//                getExecutionTimeInMilliseconds(),
//                timeout);
//            if (isResponseBeanType) {
//                responseBean = ResponseBean
//                    .errorOfSystem(mappingInfo.getTimeOutMessage(), Status.SYSTEM_BUSY.getCode());
//            }
//        } else {
//            logger.warn("{} is executed failure", mappingInfo.getUri());
//            if (businessException != null) {
//                responseBean = ResponseBean
//                    .failure(businessException.getErrorMessage(), businessException.getErrorCode());
//            } else {
//                if (isResponseBeanType) {
//                    responseBean = ResponseBean
//                        .errorOfSystem(mappingInfo.getFailureMessage(), Status.HYSTRIX_ERROR.getCode());
//                }
//            }
//        }
//        if (isResponseBeanType) {
//            return responseBean;
//        } else {
//            return super.getFallback();
//        }

        return super.getFallback();
    }

    private BusinessException loggerFallback(Throwable ex, BusinessException businessException, boolean circuitReOpen) {
        List<Object> params = new ArrayList(args.length);
        if (args.length > 0) {
            //request参数异步序例化有问
            for (Object arg : args) {
                if (arg instanceof ServletRequest) {
                    params.add("servletRequest");
                } else if (arg instanceof ServletResponse) {
                    params.add("servletResponse");
                } else {
                    params.add(arg);
                }
            }
        }
        String traceId = getTraceId();
        loggerHeaders(traceId);
        if (businessException == null) {
            Throwable targetException;
            if (ex instanceof InvocationTargetException) {
                InvocationTargetException inEx = (InvocationTargetException) ex;
                targetException = inEx.getTargetException();
            } else {
                targetException = ex;
            }
            if (isCircuitBreakerOpen()) {
                if (circuitReOpen) {
                    logger
                        .error("traceId[{}]系统异常:请求path[{}:{}],请求参数[{}],异常原因[熔断打开],出错机器[{}]",
                            traceId, mappingInfo.getUrl(), mappingInfo.getRequestMethod(), JSON.toJSONString(params),
                            AppStatic.localhost);
                } else {
                    logger
                        .warn("traceId[{}]系统异常:请求path[{}:{}],请求参数[{}],异常原因[熔断打开],出错机器[{}]",
                            traceId, mappingInfo.getUrl(), mappingInfo.getRequestMethod(), JSON.toJSONString(params),
                            AppStatic.localhost);
                }
            } else {
                logger
                    .error("traceId[{}]系统异常:请求path[{}:{}],请求参数[{}],异常原因[{}],出错机器[{}]，\n异常详情 : ",
                        traceId, mappingInfo.getUrl(), mappingInfo.getRequestMethod(), JSON.toJSONString(params),
                        targetException.getMessage(), AppStatic.localhost, targetException);
            }
        } else if (businessException instanceof VitalBusinessException) {
            logger.error("traceId[{}]系统异常:请求path[{}:{}],请求参数[{}],异常原因[{}],出错机器[{}]",
                traceId, mappingInfo.getUrl(), mappingInfo.getRequestMethod(), JSON.toJSONString(params),
                businessException.getDetailMessage(), AppStatic.localhost);
        } else {
            logger.warn("traceId[{}]业务异常:请求path[{}:{}],请求参数[{}],异常原因[{}],出错机器[{}],",
                traceId, mappingInfo.getUrl(), mappingInfo.getRequestMethod(), JSON.toJSONString(params),
                businessException.getMessage(), AppStatic.localhost);
        }

        return businessException;
    }

    private void loggerHeaders(String traceId) {
        HttpServletRequest request = DataHolder.getRequest();
        if (request == null) {
            logger.warn("traceId[" + traceId + "]request is null");
            return;
        }
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder sb = new StringBuilder();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            sb.append(name).append(":").append(value).append(",");
        }
        String headers = sb.toString();
        if (headers.length() > 1) {
            headers = headers.substring(0, headers.length() - 1);
        }
        logger.info("traceId[{}]请示头参数:[{}]", traceId, headers);
    }

    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) DataHolder.getData("httpServletRequest");
            if (httpServletRequest != null) {
                return httpServletRequest;
            }
        }
        if (requestAttributes instanceof ServletRequestAttributes) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
            return attributes.getRequest();
        }
        return null;
    }

    public String getTraceId() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        String traceId = request.getHeader("traceId");
        if (StringUtils.isEmpty(traceId)) {
            return (String) request.getAttribute("traceId");
        } else {
            return traceId;
        }
    }

    protected BusinessException findBusinessException(Throwable t) {
        if (t == null) {
            return null;
        }
        if (t instanceof BusinessException) {
            return (BusinessException) t;
        }
        return findBusinessException(t.getCause());
    }

    private boolean isCircuitReOpen() {
        if (!isCircuitBreakerOpen()) {
            return false;
        }
        String appName = mappingInfo.getAppName();
        Boolean status = serverStatus.get(appName);
        if (status == null) {
            serverStatus.put(appName, true);
            return true;
        } else if (!status) {
            serverStatus.put(appName, true);
            return true;
        }
        return false;
    }

    private boolean isCircuitReClose() {
        String appName = mappingInfo.getAppName();
        Boolean status = serverStatus.get(appName);
        if (status != null && status) {
            serverStatus.put(appName, false);
            return true;
        }
        return false;
    }
}
