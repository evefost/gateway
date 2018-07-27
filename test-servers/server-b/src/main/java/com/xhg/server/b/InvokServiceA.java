package com.xhg.server.b;

import com.xhg.test.common.UserBean;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by xieyang on 18/7/25.
 */
@FeignClient(name = "server-a")
@RequestMapping(value = "${server-a-context-path:/}/a")
public interface InvokServiceA {

    @RequestMapping(value = "/test/login",method = RequestMethod.GET)
    UserBean getUser(@RequestParam("name") String name);

    @RequestMapping(value = "/test/login2",method = RequestMethod.POST)
    UserBean getUser2(@RequestBody UserBean name);


}
