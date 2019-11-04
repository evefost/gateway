package com.xie.gateway.zuul.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 改写请求path，配置到不同环境服务
 *
 * @author xie
 */
@Component
public class RewritePathFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RewritePathFilter.class);


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }


    @Override
    public  void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestContextPath = request.getContextPath();
        if (requestContextPath.startsWith("/admin")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String envServiceId = "server-a";
        ServletRequest newRequest;
        if (StringUtils.isEmpty(envServiceId)) {
            log.warn("据contextPath[{}]没匹配到相应的服务", requestContextPath);
            newRequest = servletRequest;
        }else {
            newRequest = new HttpServletRequestWrapper(request, envServiceId);
        }
        filterChain.doFilter(newRequest, servletResponse);
    }
    Pattern pattern = Pattern.compile("[0-9.]*");
    public boolean serverNameIsIP(String str){
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    @Override
    public void destroy() {

    }
}