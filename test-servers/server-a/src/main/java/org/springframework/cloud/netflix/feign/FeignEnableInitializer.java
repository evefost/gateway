package org.springframework.cloud.netflix.feign;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;

/**
 * change the config of {@link EnableFeignClients}
 * use ReFeignClientsRegistrar instead  of {@link FeignClientsRegistrar} background
 * and user no need to change anything
 *
 * @author xieyang
 * @date 19/11/5
 */
public class FeignEnableInitializer implements ApplicationContextInitializer {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Import importAnnotation = AnnotationUtils.getAnnotation(EnableFeignClients.class, Import.class);
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(importAnnotation);
        Field memberValues = null;
        try {
            memberValues = invocationHandler.getClass().getDeclaredField("memberValues");
            memberValues.setAccessible(true);
            LinkedHashMap<Object, Object> linkedHashMap = (LinkedHashMap<Object, Object>) memberValues.get(invocationHandler);
            Class[] classes = new Class[]{ReFeignClientsRegistrar.class};
            linkedHashMap.put("value", classes);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
