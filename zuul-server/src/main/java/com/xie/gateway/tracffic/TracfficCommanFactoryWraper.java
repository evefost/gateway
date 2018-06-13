package com.xie.gateway.tracffic;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommand;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by xieyang on 18/6/14.
 */
@Component("commanfactoryWraper")
public class TracfficCommanFactoryWraper implements ApplicationContextAware,InitializingBean{

    private ApplicationContext applicationContext;

    @Resource
    private RibbonCommandFactory factory;
    //ribbon.httpclient.enabled"
    //ribbon.okhttp.enabled
    //ribbon.restclient.enabled

    @Autowired
    private Environment environment;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

    }

//   public TracfficCommanFactoryWraper(RibbonCommandFactory factory){
//        this.factory=factory;
//    }


    public RibbonCommand create(RibbonCommandContext context) {
        return factory.create(context);
    }


    @Override
    public void afterPropertiesSet() throws Exception {

        AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
        RibbonCommandFactory bean = autowireCapableBeanFactory.getBean(RibbonCommandFactory.class);
        System.out.println(bean);
    }
}
