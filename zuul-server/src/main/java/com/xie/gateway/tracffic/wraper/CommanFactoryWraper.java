package com.xie.gateway.tracffic.wraper;

import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommand;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;

/**
 * 用于替换zuul原有commanfactory
 */
public interface CommanFactoryWraper <T extends RibbonCommand> {

    T create(RibbonCommandContext context);
}
