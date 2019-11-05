package feign;

import org.springframework.beans.BeansException;
import org.springframework.cloud.netflix.feign.RefFeignClientFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * @AUTHOR XIEYANG
 */
public class FeignClientConfigRefresh implements ApplicationContextAware{

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
       this.applicationContext = applicationContext;
    }


    public void refresh(String urlKey,String url){

        Map<String, RefFeignClientFactoryBean> beansOfType = applicationContext.getBeansOfType(RefFeignClientFactoryBean.class);
        beansOfType.forEach((k,v)->{
            v.refreshConfig(urlKey,url);
        });

    }
}
