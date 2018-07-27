package com.xhg.server.b;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.ribbon.RequestTemplate;
import com.xhg.test.common.UserBean;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
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
    private UploadServiceA uploadService;

    @Autowired
    private InvokServiceA serviceA;


    @Autowired
    LoadBalancerClient loadBalancerClient;

    
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

    @RequestMapping(value = "login2",method = RequestMethod.GET)
    public UserBean getUser2(String name) {
        List<ServiceInstance> instances = discoveryClient.getInstances("server-b");
        UserBean userBean = new UserBean();
        userBean.setName(name);
        userBean.setAge(66);
        return serviceA.getUser2(userBean);
    }

    @RequestMapping(value = "upload",method = RequestMethod.GET)
    public String getUser() {

        File file = new File("C:\\Users\\xie\\Desktop\\111.java");
        DiskFileItem fileItem = (DiskFileItem) new DiskFileItemFactory().createItem("file",
                MediaType.TEXT_PLAIN_VALUE, true, file.getName());

        try (InputStream input = new FileInputStream(file); OutputStream os = fileItem.getOutputStream()) {
            IOUtils.copy(input, os);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid file: " + e, e);
        }

        MultipartFile multi = new CommonsMultipartFile(fileItem);
        String s = uploadService.handleFileUpload(multi);
        //loadBalancerClient.choose("")
        return "上传成功："+s;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String serverContextPath = environment.getProperty("server.context-path", "/");
        eurekaInstanceConfigBean.getMetadataMap().put("serverContextPath",serverContextPath);

    }
}
