package com.xhg.server.a;

import com.eve.hystrix.extend.core.CommandInfo;
import com.eve.hystrix.extend.core.CommandListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by xieyang on 19/11/9.
 */
@Component
public class DefaultCommandListener extends CommandListener {


    protected final static Logger logger = LoggerFactory.getLogger(DefaultCommandListener.class);


    @Override
    public void onCommandCreate(CommandInfo commandInfo) {
        String s = UUID.randomUUID().toString();
        MDC.put("uuid",s);
        super.onCommandCreate(commandInfo);
    }

    @Override
    public void onSuccess(CommandInfo commandInfo) {
        logger.info("onSuccess uuid [{}] ",commandInfo.getProperty("uuid"));
    }

    @Override
    public void onFailure(CommandInfo commandInfo) {
        logger.warn(" [{}] on failure:{}:",commandInfo.getProperty("uuid"),commandInfo.getExecuteResultType(),commandInfo.getCause());
    }

    @Override
    public void onCircuitBreakerOpen(CommandInfo commandInfo) {

    }

    @Override
    public void onCircuitBreakerClose(CommandInfo commandInfo) {

    }
}
