package com.xhg.server.a;

import com.xhg.test.common.UserBean;
import feign.FeignClientConfigRefresh;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xieyang on 17/11/13.
 */

@RestController
@RequestMapping("/test2")
public class TestController2 implements ApplicationContextAware {

    @Autowired
    private FeignServerB serverB;

    @Autowired
    private FeignServerC serverC;

    @RequestMapping(value = "queryInfo",method = RequestMethod.GET)
    public String queryInfo() throws NoSuchFieldException, IllegalAccessException {
        Object client = applicationContext.getBean("com.xhg.server.a.FeignServerB");
        String s = serverB.addCompose();
//        InvocationHandler invocationHandler = Proxy.getInvocationHandler(client);
//        Class<? extends InvocationHandler> invokeClass = invocationHandler.getClass();
//        Field targetField = invokeClass.getDeclaredField("target");
//        targetField.setAccessible(true);
//        Target.HardCodedTarget target = (Target.HardCodedTarget) targetField.get(invocationHandler);
//        String srcUrl = target.url();
//        Field urlField = Target.HardCodedTarget.class.getDeclaredField("url");
//        urlField.setAccessible(true);
//        urlField.set(target, "http://localhost:9003");
//        String newUrl = target.url();
//
//        String s1 = serverB.addCompose();
//        urlField.set(target, "http://server-b");
        return s;
    }

    @RequestMapping(value = "getUser",method = RequestMethod.GET)
    public String getUser() {
        return serverC.addCompose();
    }

    @Autowired
    private FeignClientConfigRefresh refresh;

    @RequestMapping(value = "refresh",method = RequestMethod.GET)
    public void refresh(String url) {
         refresh.refresh(  url);
    }


    @RequestMapping(value = "login",method = RequestMethod.POST)
    public UserBean getUser(String name) {
        UserBean userBean =  new UserBean();
        userBean.setName(name);
        return userBean;
    }

    ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
