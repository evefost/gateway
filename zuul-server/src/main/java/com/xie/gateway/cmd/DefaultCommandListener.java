package com.xie.gateway.cmd;

import com.netflix.hystrix.HystrixCommand;
import com.xie.gateway.zuul.DefaultFallbackProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by xieyang on 19/11/9.
 */
@Component
public class DefaultCommandListener implements CommandListener {


    protected final static Logger logger = LoggerFactory.getLogger(DefaultCommandListener.class);


    @Override
    public void onSuccess(CommandInfo commandInfo) {

    }

    @Override
    public void onFailure(CommandInfo commandInfo) {
        logger.warn("on failure:{}",commandInfo.getExecuteResultType());
    }

    @Override
    public void onCircuitBreakerOpen(CommandInfo commandInfo) {

    }

    @Override
    public void onCircuitBreakerClose(CommandInfo commandInfo) {

    }
}
