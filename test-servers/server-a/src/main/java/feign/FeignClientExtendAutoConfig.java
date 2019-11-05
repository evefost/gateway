package feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author xieyang
 * @date 19/11/5
 */
@Configuration
public class FeignClientExtendAutoConfig {

    @Bean
    FeignClientConfigRefresh feignClientConfigRefresh(){
        return new FeignClientConfigRefresh();
    }
}
