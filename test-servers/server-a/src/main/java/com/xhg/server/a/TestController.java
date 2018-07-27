package com.xhg.server.a;


import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.ribbon.RequestTemplate;
import com.netflix.ribbon.proxy.annotation.TemplateName;
import com.xie.gateway.api.ClientProperties;
import com.xie.gateway.api.authorize.AuthorizeService;
import com.xhg.test.common.UserBean;
import com.xie.gateway.api.authorize.AuthorizeService2;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;

/**
 * Created by xieyang on 17/11/13.
 */

@RestController
@RequestMapping("/a/test")
public class TestController  implements InitializingBean {


    @Autowired
    private Environment environment;

    @Autowired
    private EurekaInstanceConfigBean eurekaInstanceConfigBean;

    @Value("${server.port}")
    private String port;

    @Resource
    private AuthorizeService authorizeService;

    @Resource
    private AuthorizeService2 authorizeService2;

    @RequestMapping(value = "queryInfo",method = RequestMethod.GET)
    public String queryInfo() {
        return "serverA info:"+port;
    }

    @RequestMapping(value = "getUser",method = RequestMethod.GET)
    public UserBean getUser() {
        return new UserBean();
    }


    @Autowired
    private ClientProperties clientProperties;

    @RequestMapping(value = "login",method = RequestMethod.GET)
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

    @RequestMapping(value = "login2",method = RequestMethod.POST)
    public UserBean getUser(@RequestBody UserBean user) {
        UserBean userBean =  new UserBean();
        userBean.setName(user.getName());
//        AuthRequest authRequest = new AuthRequest("server-a","123456","1233333");
//        AuthoInfo authorize = authorizeService.authorize(authRequest);
//        if(authorize.getCode()==1){
//            return "v0"+port+ "网关认证成功token:"+authorize.getToken();
//        }
//        return "网关认证的失败:"+authorize.getMessage();
        return userBean;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String serverContextPath = environment.getProperty("server.context-path", "/");
        eurekaInstanceConfigBean.getMetadataMap().put("serverContextPath",serverContextPath);

    }
    int i =0;

    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String handleFileUpload(@RequestPart(value = "file") MultipartFile file) throws IOException {
        i++;
        String originalFilename = file.getOriginalFilename();
        File newFile = new File("/Users/xieyang/Downloads/陈志辉简历.docx"+i);
        InputStream inputStream = file.getInputStream();
        OutputStream ous = new FileOutputStream(newFile);
        IOUtils.copy(inputStream,ous);
        return originalFilename;
    }
}
