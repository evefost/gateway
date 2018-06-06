package com.xhg.server.a;

import com.xhg.gateway.api.authorize.AuthRequest;
import com.xhg.gateway.api.authorize.AuthoInfo;
import com.xhg.gateway.api.authorize.AuthorizeService;
import com.xhg.test.common.UserBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by xieyang on 17/11/13.
 */

@RestController
@RequestMapping("/test")
public class TestController {

    @Value("${server.port}")
    private String port;

    @Resource
    private AuthorizeService authorizeService;

    @RequestMapping(value = "queryInfo",method = RequestMethod.GET)
    public String queryInfo() {
        return "serverA info:"+port;
    }

    @RequestMapping(value = "getUser",method = RequestMethod.GET)
    public UserBean getUser() {
        return new UserBean();
    }


    @RequestMapping(value = "login",method = RequestMethod.POST)
    public String getUser(String name) {
        UserBean userBean =  new UserBean();
        userBean.setName(name);
        AuthRequest authRequest = new AuthRequest("server-a","123456","1233333");
        AuthoInfo authorize = authorizeService.authorize(authRequest);
        if(authorize.getCode()==1){
            return "v0"+port+ "网关认证成功token:"+authorize.getToken();
        }
        return "网关认证的失败:"+authorize.getMessage();
    }

}
