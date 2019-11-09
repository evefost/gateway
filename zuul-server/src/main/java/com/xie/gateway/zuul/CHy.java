package com.xie.gateway.zuul;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixThreadPoolKey;

/**
 * Created by xieyang on 19/11/9.
 */
public class CHy  extends HystrixCommand<Object>{

    protected CHy(HystrixCommandGroupKey group) {
        super(group);
    }

    protected CHy(HystrixCommandGroupKey group, HystrixThreadPoolKey threadPool) {
        super(group, threadPool);
    }

    protected CHy(HystrixCommandGroupKey group, int executionIsolationThreadTimeoutInMilliseconds) {
        super(group, executionIsolationThreadTimeoutInMilliseconds);
    }

    protected CHy(HystrixCommandGroupKey group, HystrixThreadPoolKey threadPool, int executionIsolationThreadTimeoutInMilliseconds) {
        super(group, threadPool, executionIsolationThreadTimeoutInMilliseconds);
    }

    protected CHy(Setter setter) {
        super(setter);
    }

    @Override
    protected Object run() throws Exception {
        isCircuitBreakerOpen();
        return null;
    }

    @Override
    protected Object getFallback() {
        return super.getFallback();
    }
}
