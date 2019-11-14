package com.xhg.server.a;

import com.eve.hystrix.extend.core.CommandInfo;
import com.eve.hystrix.extend.core.HystrixFallback;
import org.springframework.stereotype.Component;

/**
 * Created by xieyang on 19/11/15.
 */
@Component
public class DefaultFallback implements HystrixFallback<ResponseBean,CommandInfo> {
    @Override
    public ResponseBean getFallbackData(CommandInfo commandInfo) {
        return ResponseBean.failure("服务调用失败");
    }
}
