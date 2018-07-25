package com.xhg.server.b;

import com.xhg.test.common.UserBean;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by xieyang on 18/7/25.
 */
@FeignClient(name = "server-a")
public interface InvokServiceA {

    @RequestMapping(value = "/aaa/test/login",method = RequestMethod.GET)
    UserBean getUser(@RequestParam("name") String name);
}
