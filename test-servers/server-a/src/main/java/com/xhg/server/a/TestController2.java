package com.xhg.server.a;

import com.xhg.test.common.UserBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xieyang on 17/11/13.
 */

@RestController
@RequestMapping("/test2")
public class TestController2 {

    @RequestMapping(value = "queryInfo",method = RequestMethod.GET)
    public String queryInfo() {
        return "serverA info";
    }

    @RequestMapping(value = "getUser",method = RequestMethod.GET)
    public UserBean getUser() {
        return new UserBean();
    }


    @RequestMapping(value = "login",method = RequestMethod.POST)
    public UserBean getUser(String name) {
        UserBean userBean =  new UserBean();
        userBean.setName(name);
        return userBean;
    }

}
