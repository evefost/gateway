package com.xie.gateway.zuul.support;

import com.xie.gateway.controller.PageErrorController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.netflix.zuul.ZuulServerAutoConfiguration;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.web.ZuulController;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

//@Configuration
public class CustZuulConfig extends ZuulServerAutoConfiguration {

    @Autowired
    private PageErrorController errorController;

    @Autowired
    private ZuulController zuulController;

    @Bean
    @Primary
    @Override
    public ZuulHandlerMapping zuulHandlerMapping(RouteLocator routes) {
        ZuulHandlerMapping mapping = new CustZuulHandlerMapper(routes, zuulController);
        mapping.setErrorController(this.errorController);
        return mapping;
    }


    /**
     * 重写自定zuulservlet
     * @return
     */
    @Bean
    @Primary
    @Override
    public ServletRegistrationBean zuulServlet() {
        ServletRegistrationBean servlet = new ServletRegistrationBean(new ZServlet(),
            this.zuulProperties.getServletPattern());
        // The whole point of exposing this servlet is to provide a route that doesn't
        // buffer requests.
        servlet.addInitParameter("buffer-requests", "false");
        return servlet;
    }


    /**
     * 配合重写zuulservlet
     * @return
     */
    @Bean
    @Primary
    @Override
    public ZuulController zuulController() {
        return new ZController();
    }

}
