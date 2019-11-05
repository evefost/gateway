package com.xhg.server.a;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.ReFeignClientsRegistrar;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;

@SpringBootApplication(scanBasePackages = {"com.xhg.server"})
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.xhg"})
public class ServerA {
	final static Logger logger = LoggerFactory.getLogger(ServerA.class);

	public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
//		replaceConfiguration();
		ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(ServerA.class).web(true)
				.run(args);
		logger.debug(applicationContext.getId() + "已经启动,当前host：{}",
				applicationContext.getEnvironment().getProperty("HOSTNAME"));
	}

	private static void replaceConfiguration() throws NoSuchFieldException, IllegalAccessException {
		EnableFeignClients annotation = AnnotationUtils.getAnnotation(ServerA.class, EnableFeignClients.class);
		Import importAnnotation = AnnotationUtils.getAnnotation(annotation, Import.class);
		InvocationHandler invocationHandler = Proxy.getInvocationHandler(importAnnotation);
		Field memberValues = invocationHandler.getClass().getDeclaredField("memberValues");
		memberValues.setAccessible(true);
		LinkedHashMap<Object, Object> linkedHashMap = (LinkedHashMap<Object, Object>) memberValues.get(invocationHandler);
		Class[] classes = new Class[]{ReFeignClientsRegistrar.class};
		linkedHashMap.put("value", classes);
		System.out.println(invocationHandler);

	}
}
