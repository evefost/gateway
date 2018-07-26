package com.xhg.server.b;

import com.netflix.appinfo.ApplicationInfoManager;
import com.xhg.test.common.UserBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by xieyang on 17/11/13.
 */

@RestController
@RequestMapping("/test")
public class TestController  implements InitializingBean{
    @Autowired
    private Environment environment;

    @Autowired
    private InvokServiceA serviceA;

    
    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private EurekaInstanceConfigBean eurekaInstanceConfigBean;

    @RequestMapping(value = "queryInfo",method = RequestMethod.GET)
    public String addCompose() {
        return "serverB info";
    }


    @RequestMapping(value = "login",method = RequestMethod.GET)
    public UserBean getUser(String name) {
        List<ServiceInstance> instances = discoveryClient.getInstances("server-b");
        return serviceA.getUser(name);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String serverContextPath = environment.getProperty("server.context-path", "/");
        eurekaInstanceConfigBean.getMetadataMap().put("serverContextPath",serverContextPath);

    }
}
