package com.xie.gateway.cmd;

import com.netflix.hystrix.HystrixCommand;
import org.springframework.stereotype.Component;

/**
 * Created by xieyang on 19/11/9.
 */
@Component
public class DefaultCommandListener implements CommandListener {
    @Override
    public void onSuccess(HystrixCommand cmd) {

    }

    @Override
    public void onFailure(HystrixCommand cmd, FailureInfo failureInfo) {
        System.out.println("onFailureonFailureo  nFailureonFailureonFailureonFailureonFailureonFailure");
    }

    @Override
    public void onCircuitBreakerOpen(HystrixCommand cmd) {

    }

    @Override
    public void onCircuitBreakerClose(HystrixCommand cmd) {

    }
}
