package com.xie.gateway.tracffic;

import com.xie.gateway.tracffic.wraper.CommanFactoryWraper;
import com.xie.gateway.tracffic.wraper.HttpCleintCommanFacotoryWraper;
import com.xie.gateway.tracffic.wraper.OkHttpCommanFacotoryWraper;
import com.xie.gateway.tracffic.wraper.RestClientCommanFacotoryWraper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;


@Configuration
public class TracfficCommanFactoryConfiguration {

    @Bean
    @ConditionalOnProperty(name = "ribbon.httpclient.enabled", matchIfMissing = true)
    public CommanFactoryWraper httpCommanFactoryWapter(SpringClientFactory clientFactory, ZuulProperties zuulProperties,Set<FallbackProvider> zuulFallbackProviders ){
        return  new HttpCleintCommanFacotoryWraper(clientFactory, zuulProperties,
                zuulFallbackProviders);
    }

    @Bean
    @ConditionalOnProperty(name = "ribbon.okhttp.enabled",matchIfMissing = false)
    public CommanFactoryWraper okttpFactoryWraper(SpringClientFactory clientFactory, ZuulProperties zuulProperties,Set<FallbackProvider> zuulFallbackProviders ){
        return  new OkHttpCommanFacotoryWraper(clientFactory, zuulProperties,
                zuulFallbackProviders);
    }
    @Bean
    @ConditionalOnProperty(name = "ribbon.restclient.enabled",matchIfMissing = false)
    public CommanFactoryWraper restClientFactoryWraper(SpringClientFactory clientFactory, ZuulProperties zuulProperties,Set<FallbackProvider> zuulFallbackProviders ){
        return  new RestClientCommanFacotoryWraper(clientFactory, zuulProperties,
                zuulFallbackProviders);
    }



}
