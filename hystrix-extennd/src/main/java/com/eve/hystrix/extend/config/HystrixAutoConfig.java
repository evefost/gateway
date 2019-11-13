package com.eve.hystrix.extend.config;

import com.eve.hystrix.extend.MethodScanner;
import com.eve.hystrix.extend.RequestMappingInfo;
import com.eve.hystrix.extend.core.HystrixAspect;
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

@Component
@ConditionalOnProperty(name = "xhg.hystrix.enable",matchIfMissing = true)
public class HystrixAutoConfig implements SmartInitializingSingleton {
    protected final Logger logger = LoggerFactory.getLogger(getClass());


    @Bean
    HystrixAspect getHystrixControllerAspect(){
        return new HystrixAspect();
    }

    @Bean
    MethodScanner getFeignClientScanner(Environment environment){
        return  new MethodScanner(environment);
    }

    @Autowired
    MethodScanner methodScanner;

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
