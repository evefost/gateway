package com.xhg.server.b.fegin;

import com.alibaba.fastjson.JSON;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.cloud.client.loadbalancer.LoadBalancedBackOffPolicyFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryListenerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryPolicyFactory;
import org.springframework.cloud.netflix.feign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.netflix.feign.ribbon.FeignLoadBalancer;
import org.springframework.cloud.netflix.feign.ribbon.RetryableFeignLoadBalancer;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancedRetryPolicyFactory;
import org.springframework.cloud.netflix.ribbon.ServerIntrospector;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.Map;

/**
 * 动态管理服务
 */

@Aspect
public class TracfficAop {

    private final SpringClientFactory factory;
    private final LoadBalancedRetryPolicyFactory loadBalancedRetryPolicyFactory;
    private final LoadBalancedBackOffPolicyFactory loadBalancedBackOffPolicyFactory;
    private final LoadBalancedRetryListenerFactory loadBalancedRetryListenerFactory;
    private boolean enableRetry = false;
    private volatile Map<String, FeignLoadBalancer> cache = new ConcurrentReferenceHashMap<>();

    public TracfficAop(SpringClientFactory factory) {
        // super(factory);
        this.factory = factory;
        this.loadBalancedRetryPolicyFactory = new RibbonLoadBalancedRetryPolicyFactory(factory);
        this.loadBalancedBackOffPolicyFactory = null;
        this.loadBalancedRetryListenerFactory = null;
    }


    @Pointcut("execution(* org.springframework.cloud.netflix.feign.ribbon.CachingSpringLoadBalancerFactory.create(..))")
    public void pointcutName() {

    }


    @Around("pointcutName()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        //处理线程池或信号量
        Object target = point.getTarget();
        String serviceId = (String) point.getArgs()[0];
        return create(serviceId);
    }


    public FeignLoadBalancer create(String clientName) {
        if (this.cache.containsKey(clientName)) {
            return this.cache.get(clientName);
        }
        IClientConfig config = this.factory.getClientConfig(clientName);
        ILoadBalancer lb = this.factory.getLoadBalancer(clientName);
        ServerIntrospector serverIntrospector = this.factory.getInstance(clientName, ServerIntrospector.class);
        FeignLoadBalancer client = enableRetry ? new MyRetryableFeignLoadBalancer(lb, config, serverIntrospector,
                loadBalancedRetryPolicyFactory, loadBalancedBackOffPolicyFactory, loadBalancedRetryListenerFactory) : new MyFeignLoadBalancer(lb, config, serverIntrospector);
        this.cache.put(clientName, client);
        return client;
    }

}
