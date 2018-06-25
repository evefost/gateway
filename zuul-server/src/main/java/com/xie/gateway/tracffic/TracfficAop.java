package com.xie.gateway.tracffic;

import com.xie.gateway.tracffic.wraper.CommanFactoryWraper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 动态管理服务
 */
@Aspect
@Configuration
public class TracfficAop {

    @Autowired
    private ZuulProperties zuulProperties;

    @Resource()
    private CommanFactoryWraper factoryWraper;

    @Pointcut("execution(* org.springframework.cloud.netflix.zuul.filters.route..RibbonCommandFactory.create(..))")
    public void pointcutName() {

    }


    @Around("pointcutName()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        //处理线程池或信号量
        RibbonCommandContext context = (RibbonCommandContext) point.getArgs()[0];
        return factoryWraper.create(context);
    }

}
