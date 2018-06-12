
package com.xie.gateway.zuul;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.context.RequestContext;
import com.xie.gateway.vo.ResponseBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.AbstractClientHttpResponse;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

// 写一个默认的hystrix降级策略
@Component
// 如果没有这个配置项或者配置为false，就不实例化
@ConditionalOnProperty(value = "tony.zuul.defaultFallback.enabled", matchIfMissing = true)
public class DefaultFallbackProvider implements ZuulFallbackProvider{
	@Override
	public String getRoute() {
		// null 或者 *  代表为默认的fallback
		// route对应eureka中的服务名 或者 你自己在配置文件中 配置的serviceId
		return "*";
	}

	@Override
	public ClientHttpResponse fallbackResponse() {
		return new AbstractClientHttpResponse() {
			
			@Override
			public HttpHeaders getHeaders() {
				HttpHeaders headers = new HttpHeaders();
                return headers;
			}
			
			@Override
			public InputStream getBody() throws IOException {
				RequestContext currentContext = RequestContext.getCurrentContext();
				Throwable throwable = currentContext.getThrowable();
				ResponseBean<Object> respone = ResponseBean.failure("网关调用服务出错");
				String result = JSON.toJSONString(respone);
				return new ByteArrayInputStream(result.getBytes());
			}
			
			@Override
			public String getStatusText() throws IOException {
				return "服务有问题啦";
			}
			
			@Override
			public HttpStatus getStatusCode() throws IOException {
				return HttpStatus.BAD_GATEWAY;
			}
			
			@Override
			public int getRawStatusCode() throws IOException {
				return 502;
			}
			
			@Override
			public void close() {
				
			}
		};
	}

}
