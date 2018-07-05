package com.xie.gateway.aop;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * 动态管理服务
 */
@Aspect
@Configuration
public class FeignAop {


    Logger logger = LoggerFactory.getLogger(FeignAop.class);

    @Pointcut("execution(* com.xie.gateway.remote.*.*(..)) || execution(* com.xie.gateway.xx.remote.*.*(..))")
    public void pointcutName(){}


    @Around("pointcutName()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        String s = JSON.toJSONString(args);
        logger.info(s);
        try {
           return point.proceed();
        } finally {
       }

    }


}
