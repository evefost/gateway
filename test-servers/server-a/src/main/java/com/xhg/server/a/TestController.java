package com.xhg.server.a;

import com.xhg.test.common.UserBean;
import com.xie.gateway.api.authorize.AuthorizeService;
import com.xie.gateway.api.authorize.AuthorizeService2;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired(required = false)
    private AuthorizeService authorizeService;

    @Autowired(required = false)
    @Resource
    private AuthorizeService2 authorizeService2;

    @RequestMapping(value = "queryInfo",method = RequestMethod.GET)
    public String queryInfo() {
        if(true){
            throw new RuntimeException("xxxxxxx");
        }
        return "serverA info:"+port;
    }

    @RequestMapping(value = "getUser",method = RequestMethod.GET)
    public UserBean getUser() {
        return new UserBean();
    }


    @RequestMapping(value = "login",method = RequestMethod.POST)
    public UserBean getUser(String name) {
        UserBean userBean =  new UserBean();
        userBean.setName(name);
//        AuthRequest authRequest = new AuthRequest("server-a","123456","1233333");
//        AuthoInfo authorize = authorizeService.authorize(authRequest);
//        if(authorize.getCode()==1){
//            return "v0"+port+ "网关认证成功token:"+authorize.getToken();
//        }
//        return "网关认证的失败:"+authorize.getMessage();
        return userBean;
    }

}
