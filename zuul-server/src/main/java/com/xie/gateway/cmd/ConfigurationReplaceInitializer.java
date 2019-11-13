//package com.xie.gateway.cmd;
//
//import org.springframework.cloud.commons.httpclient.HttpClientConfiguration;
//import org.springframework.cloud.netflix.feign.EnableFeignClients;
//import org.springframework.cloud.netflix.zuul.ZuulProxyAutoConfiguration;
//import org.springframework.context.ApplicationContextInitializer;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.context.annotation.Import;
//import org.springframework.core.annotation.AnnotationUtils;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.Proxy;
//import java.util.LinkedHashMap;
//
///**
// * and user no need to change anything
// *
// * @author xieyang
// * @date 19/11/5
// */
//public class ConfigurationReplaceInitializer implements ApplicationContextInitializer {
//
//    @Override
//    public void initialize(ConfigurableApplicationContext applicationContext) {
//        Import importAnnotation = AnnotationUtils.getAnnotation(ZuulProxyAutoConfiguration.class, Import.class);
//        InvocationHandler invocationHandler = Proxy.getInvocationHandler(importAnnotation);
//        Field memberValues = null;
//        try {
//            memberValues = invocationHandler.getClass().getDeclaredField("memberValues");
//            memberValues.setAccessible(true);
//            LinkedHashMap<Object, Object> linkedHashMap = (LinkedHashMap<Object, Object>) memberValues.get(invocationHandler);
//            Class[] classes =
//                    new Class[]{RibbonCommandFactoryConfiguration.HttpClientRibbonConfiguration.class
//                            ,RibbonCommandFactoryConfiguration.OkHttpRibbonConfiguration.class,
//                            RibbonCommandFactoryConfiguration.RestClientRibbonConfiguration.class,
//                            HttpClientConfiguration.class
//            };
//            linkedHashMap.put("value", classes);
//            System.out.println("xxx");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//}
