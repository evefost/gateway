package com.xhg.server.b;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;

@SpringBootApplication(scanBasePackages = {"com.xhg.server"})
@EnableEurekaClient
@EnableFeignClients()
@EnableAspectJAutoProxy
public class ServerB {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    final static Logger logger = LoggerFactory.getLogger(ServerB.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(ServerB.class).web(true)
                .run(args);
        logger.debug(applicationContext.getId() + "已经启动,当前host：{}",
                applicationContext.getEnvironment().getProperty("HOSTNAME"));
    }

    @Bean
    @ConditionalOnMissingBean
    public Encoder feignEncoder() {

        return new Encoder() {
            private SpringEncoder springEncoder = new SpringEncoder(messageConverters);
            private SpringFormEncoder springFormEncoder = new SpringFormEncoder();

            @Override
            public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
                if (bodyType.equals(MultipartFile.class)) {
                    springFormEncoder.encode(object, bodyType, template);
                } else {
                    springEncoder.encode(object, bodyType, template);
                }
            }
        };
    }


}
