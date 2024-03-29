package com.xie.gateway.zuul.filters;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.context.RequestContext;
import com.xie.gateway.vo.ResponseBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.ERROR_TYPE;

// 默认springcloud有一个errorfilter，会重定向到一个/error的路径
// 如果要是自定义的errorfilter生效，关掉springcloud提供的这个errorfilter即可。
@Component
public class SendErrorRestFilter extends SendErrorFilter {
	protected static final Logger logger = LoggerFactory.getLogger(SendErrorRestFilter.class);

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		RequestContext context = RequestContext.getCurrentContext();
		Throwable throwable = findCauseException(context.getThrowable());
		// 获取response状态码
		int status = context.getResponseStatusCode();
		// 转成json格式输出
		ResponseBean<Object> failure = ResponseBean.failure(status, throwable.getMessage());
		// 记录日志
		logger.warn("zuul后台有个异常", context.getThrowable());
		context.setResponseBody(JSON.toJSONString(failure));
		context.getResponse().setContentType("application/json;charset=UTF-8");
		// 处理了异常以后，就清空
		context.remove("throwable");
		return null;
	}

	@Override
	public String filterType() {
		return ERROR_TYPE;
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	// 找出最初始的异常
	Throwable findCauseException(Throwable throwable) {
		while (throwable.getCause() != null) {
			throwable = throwable.getCause();
		}
		return throwable;
	}

}
