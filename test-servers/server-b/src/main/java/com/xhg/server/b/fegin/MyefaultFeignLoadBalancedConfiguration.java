package com.xhg.server.b.fegin;

import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class MyefaultFeignLoadBalancedConfiguration {

    @Bean
    public TracfficAop cachingLBClientFactory2(
            SpringClientFactory factory) {
        return new TracfficAop(factory);
    }


}