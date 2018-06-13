package com.xie.gateway.tracffic;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.netflix.zuul.filters.route.apache.HttpClientRibbonCommandFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Created by xieyang on 18/6/14.
 */
@Configuration
@Order
public class CommanFactoryCheck {

    @ConditionalOnBean(HttpClientRibbonCommandFactory.class)
    void test1(){
        System.out.println("HttpClientRibbonCommandFactory 存在");
    }

    @ConditionalOnBean(HttpClientRibbonCommandFactory.class)
    void test2(){
        System.out.println("OkHttpRibbonCommandFactory 存在");
    }
    @ConditionalOnBean(HttpClientRibbonCommandFactory.class)
    void test3(){
        System.out.println("RestClientRibbonCommandFactory 存在");
    }
}
