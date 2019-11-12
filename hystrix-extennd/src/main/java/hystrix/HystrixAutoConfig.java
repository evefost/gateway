package hystrix;

import hystrix.feign.FeignClientBeanProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "xhg.hystrix.enable")
public class HystrixAutoConfig {

    @Bean
    FeignClientBeanProcessor getFeignClientBeanProcessor(){
        return  new FeignClientBeanProcessor();
    }

    @Bean
    HystrixControllerAspect getHystrixControllerAspect(){
        return new HystrixControllerAspect();
    }

    @Bean
    MethodScanner getFeignClientScanner(){
        return  new MethodScanner();
    }

}
