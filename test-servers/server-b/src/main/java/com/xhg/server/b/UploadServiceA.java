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
@FeignClient(name = "server-abbbbb",configuration = {UploadServiceA.MultipartSupportConfig.class})
@RequestMapping(value = "${server-a-context-path:/}/a")
public interface UploadServiceA {
    @PostMapping(value = "/test/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String handleFileUpload(@RequestPart(value = "file") MultipartFile file);



    @Configuration
    class MultipartSupportConfig {
        @Bean()
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder();
        }
    }

}
