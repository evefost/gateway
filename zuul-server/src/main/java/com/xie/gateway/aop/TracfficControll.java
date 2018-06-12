package com.xie.gateway.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态管理服务测试
 */
@Aspect
@Configuration
public class TracfficControll {

    @Resource
   private ZuulProperties defaultProperties;

    private ConcurrentHashMap<String,SingleServiceProperties> servicesProperties = new ConcurrentHashMap<>();

    @Pointcut("execution(* org.springframework.cloud.netflix.zuul.filters.route..RibbonCommandFactory.create(..))")
    public void pointcutName() {
    }




    @Around("pointcutName()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        //处理线程池或信号量
        RibbonCommandFactory factory = (RibbonCommandFactory) point.getTarget();
        RibbonCommandContext context = (RibbonCommandContext) point.getArgs()[0];
        return factory.create(context);
    }

}
