package com.eve.hystrix.extend.core;

/**
 * hystrix fallback interface
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/11/14
 */
public interface HystrixFallback<R> {

    R getFallbackData();

}
