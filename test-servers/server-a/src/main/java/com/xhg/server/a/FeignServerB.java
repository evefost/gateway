package com.xhg.server.a;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/11/5
 */
@FeignClient(name = "server-b", url = "${ms-url.server-b:}")
public interface FeignServerB {

    @RequestMapping(value = "/test/queryInfo", method = RequestMethod.GET)
    String addCompose();
}
