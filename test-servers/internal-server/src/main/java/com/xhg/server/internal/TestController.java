package com.xhg.server.internal;

import com.xhg.test.common.UserBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xieyang on 17/11/13.
 */

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping(value = "queryInfo",method = RequestMethod.GET)
    public String queryInfo() {
        return "内部服务 info";
    }

    @RequestMapping(value = "getUser",method = RequestMethod.GET)
    public UserBean getUser() {
        return new UserBean();
    }

}
