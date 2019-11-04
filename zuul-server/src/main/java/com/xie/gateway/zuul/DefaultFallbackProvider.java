
package com.xie.gateway.zuul;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.context.RequestContext;
import com.xie.gateway.vo.ResponseBean;
import org.apache.http.conn.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.AbstractClientHttpResponse;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVICE_ID_KEY;

// 写一个默认的hystrix降级策略
@Component
// 如果没有这个配置项或者配置为false，就不实例化
@ConditionalOnProperty(value = "gateway.zuul.defaultFallback.enabled", matchIfMissing = true)
public class DefaultFallbackProvider implements FallbackProvider {
    protected final static Logger logger = LoggerFactory.getLogger(DefaultFallbackProvider.class);

    @Override
    public String getRoute() {
        // null 或者 *  代表为默认的fallback
        // route对应eureka中的服务名 或者 你自己在配置文件中 配置的serviceId
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse() {
        return new FallbackRespone();
    }

    @Override
    public ClientHttpResponse fallbackResponse(Throwable cause) {
        return new FallbackRespone(cause);
    }

    class FallbackRespone extends AbstractClientHttpResponse {
        private Throwable cause;

        public FallbackRespone() {
        }

        public FallbackRespone(Throwable cause) {
            this.cause = cause;
        }

        @Override
        public HttpHeaders getHeaders() {
            HttpHeaders headers = new HttpHeaders();
            return headers;
        }

        @Override
        public InputStream getBody() throws IOException {
            RequestContext currentContext = RequestContext.getCurrentContext();
            String serviceId = (String) currentContext.get(SERVICE_ID_KEY);
            ResponseBean<Object> respone = null;
            Throwable rootCause = findCause(this.cause);
            logger.error("调用异常信息 [{}]",serviceId, rootCause);
            if (rootCause instanceof SocketTimeoutException) {
                respone = ResponseBean.failure("请求服务超时");
            } else if (rootCause instanceof HttpHostConnectException) {
                respone = ResponseBean.failure("连接服务失败");
            } else {
                respone = ResponseBean.failure("网关调用服务出错");
            }


            String result = JSON.toJSONString(respone);
            return new ByteArrayInputStream(result.getBytes());
        }

        private Throwable findCause(Throwable root) {
            Throwable cause = root.getCause();
            if (cause == null) {
                return root;
            }
            return findCause(cause);
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
    }
}
