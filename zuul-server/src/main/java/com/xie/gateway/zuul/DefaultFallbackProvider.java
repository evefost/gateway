
package com.xie.gateway.zuul;

import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.zuul.context.RequestContext;
import com.xie.gateway.vo.ResponseBean;
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
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;

/**
 * @author xieyang
 */
@Component
@ConditionalOnProperty(value = "gateway.zuul.defaultFallback.enabled", matchIfMissing = true)
public class DefaultFallbackProvider implements FallbackProvider {

    protected final static Logger logger = LoggerFactory.getLogger(DefaultFallbackProvider.class);

    @Override
    public String getRoute() {
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse() {
        return new FallbackResponse();
    }

    @Override
    public ClientHttpResponse fallbackResponse(Throwable cause) {
        Throwable rootCause = findCause(cause);
        RequestContext currentContext = RequestContext.getCurrentContext();
        ResponseBean<Object> response;
        int status = 500;
        String message;
        if (rootCause instanceof SocketTimeoutException) {
            message = "请求服务超时";
        } else if (rootCause instanceof ConnectException) {
            message = "连接服务失败";
        } else {
            message = "网关调用服务出错";

        }
        response = ResponseBean.failure(status,message);
        String fallbackBody  = JSON.toJSONString(response);
        logger.error("{}: code[{}] forword [{}->{}] cause [{}]", message,status,rootCause.getMessage());
        return new FallbackResponse(fallbackBody,status);
    }



    private Throwable findCause(Throwable root) {
        Throwable cause = root.getCause();
        if (cause == null) {
            return root;
        }
        return findCause(cause);
    }

    class FallbackResponse extends AbstractClientHttpResponse {

        private int status = 500;

        private String fallbackBody;

        private InputStream inputStream;

        public FallbackResponse() {
        }

        public FallbackResponse(String fallbackBody,int status) {
            this.fallbackBody = fallbackBody;
            this.status = status;
        }

        @Override
        public HttpHeaders getHeaders() {
            HttpHeaders headers = new HttpHeaders();
            return headers;
        }

        @Override
        public InputStream getBody() throws IOException {
            inputStream = new ByteArrayInputStream(fallbackBody.getBytes(Charset.forName("UTF-8")));
            return inputStream;
        }


        @Override
        public String getStatusText() throws IOException {
            return "服务有问题啦";
        }

        @Override
        public HttpStatus getStatusCode() throws IOException {
            return HttpStatus.valueOf(status);
        }

        @Override
        public int getRawStatusCode() throws IOException {
            return status;
        }

        @Override
        public void close() {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
