package com.xie.gateway.tracffic;

import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommand;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandFactory;

/**
 * Created by xieyang on 18/6/14.
 */
public interface CommanSupport<T extends RibbonCommand> {

    boolean suport(Object factory);


    void handler();




}
