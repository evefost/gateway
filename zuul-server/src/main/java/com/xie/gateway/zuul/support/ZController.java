package com.xie.gateway.zuul.support;

import com.netflix.zuul.context.RequestContext;
import org.springframework.cloud.netflix.zuul.web.ZuulController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xie yang
 * @date 2018/11/6-10:30
 */
public class ZController extends ZuulController {

    public ZController() {
        setServletClass(ZServlet.class);
        setServletName("zuul");
        setSupportedMethods((String[]) null);
    }

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // We don't care about the other features of the base class, just want to
            // handle the request
            return super.handleRequestInternal(request, response);
        }
        finally {
            // @see com.netflix.zuul.context.ContextLifecycleFilter.doFilter
            RequestContext.getCurrentContext().unset();
        }
    }

}
