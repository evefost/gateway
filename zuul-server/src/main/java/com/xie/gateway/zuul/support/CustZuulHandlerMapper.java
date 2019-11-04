package com.xie.gateway.zuul.support;

import com.netflix.zuul.context.RequestContext;
import com.xie.gateway.exception.XhgException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.web.ZuulController;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVICE_ID_KEY;


/**
 * 简复覆盖，观察路由结果
 */
public class CustZuulHandlerMapper extends ZuulHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(CustZuulHandlerMapper.class);


    public final static String[] noIntercepterUrls = {
            "/info",
            "/env",
            "/admin/login",
            "/admin/app/refresh",
            "/admin/app/refreshAppInfo",
            "/admin/app/refreshUriInfo",
            "/swagger-resources/**",
            "/webjars/**", "/v2/**",
            "/swagger-ui.html/**"

    };

    private ErrorController errorController;


    @Value("${zuul.prefix:}")
    private String zuulPrefix;

    @Value("${eureka.client.serviceUrl.defaultZone:}")
    private String eurekaUrls;

    public CustZuulHandlerMapper(RouteLocator routeLocator, ZuulController zuul) {
        super(routeLocator, zuul);

    }

    @Override
    public void setErrorController(ErrorController errorController) {
        super.setErrorController(errorController);
        this.errorController = errorController;
    }

    PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected Object lookupHandler(String urlPath, HttpServletRequest request) throws Exception {
        if (this.errorController != null && urlPath.equals(this.errorController.getErrorPath())) {
            return null;
        }
        log.debug("请求url:{}", urlPath);
        if (pathMatcher.match("/admin/**", urlPath)) {
            log.debug("网关 amdin url");
            return null;
        }
        for (String paterm : noIntercepterUrls) {
            if (pathMatcher.match(paterm, urlPath)) {
                log.debug("网关  url");
                return null;
            }
        }

        Object handler = super.lookupHandler(urlPath, request);

        if (handler == null) {
            RequestContext currentContext = RequestContext.getCurrentContext();
            String serviceId = (String) currentContext.get(SERVICE_ID_KEY);
            String detailMessage = "网关没匹配到对应的服务["+serviceId+"],服务可能没有启动的实例,注册中心:"+eurekaUrls;
            log.warn(detailMessage);
            throw new XhgException("网关没匹配到对应的服务");
        }
        return handler;
    }

    private String parseServiceId(String urlPath) {
        try {
            String[] split = urlPath.split("/");
            return split[1];
        } catch (Exception e) {
            logger.warn("parseServiceId ex:{}", e);
        }
        return "--";
    }
}
