package com.xhg.server.a;

import com.eve.hystrix.extend.XCommand;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 类说明
 * <p>
 *
 * @author 谢洋
 * @version 1.0.0
 * @date 2019/11/5
 */
@XCommand
@FeignClient(name = "server-c")
public interface FeignServerC {

    @XCommand
    @RequestMapping(value = "/test/queryInfo", method = RequestMethod.GET)
    ResponseBean addCompose();
}
