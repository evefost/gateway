package com.eve.hystrix.extend.config;

import com.eve.hystrix.extend.CommandMethodScanner;
import com.eve.hystrix.extend.RequestMappingInfo;
import com.eve.hystrix.extend.feign.FeignClientBeanProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author xieyang
 */
@Component
@ConditionalOnProperty(name = "hystrix.enable", matchIfMissing = true)
public class HystrixAutoConfig implements SmartInitializingSingleton {

    protected final Logger logger = LoggerFactory.getLogger(getClass());


    @Bean
    HystrixAspect getHystrixControllerAspect(){
        return new HystrixAspect();
    }

    @Bean
    FeignClientBeanProcessor feignClientBeanProcessor() {
        return new FeignClientBeanProcessor();
    }

    @Bean
    CommandMethodScanner getFeignClientScanner(Environment environment){
        return  new CommandMethodScanner(environment);
    }

    @Autowired
    CommandMethodScanner methodScanner;

    @Override
    public void afterSingletonsInstantiated() {
        Set<String> packages = new HashSet<>();
        packages.add("com.xhg");
        try {
            RequestMappingInfo.methodMappings = methodScanner.scanRequestMapping(packages);
        }catch (Throwable throwable){
            logger.warn("hystrix 熔断接口扫描失败:",throwable);
        }
    }
}
