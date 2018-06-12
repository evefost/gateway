package com.xie.gateway.zuul.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xie.gateway.api.AppInfo;
import com.xie.gateway.api.AppManagerService;
import com.xie.gateway.api.event.AppChangeEvent;
import com.xie.gateway.api.event.GateWayEvent;
import com.xie.gateway.api.event.RefreshEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVICE_ID_KEY;

/**
 * 服务是否被禁用过滤器
 */
@Component
public class ServiceValidFilter extends ZuulFilter implements ApplicationListener<GateWayEvent>{

    protected static final Logger logger = LoggerFactory.getLogger(ServiceValidFilter.class);

    @Resource
    private AppManagerService appManagerService;

    /**
     * 禁用的服务列表
     */
    private List<String> disableServices = new ArrayList<>();

    PathMatcher pathMatcher = new AntPathMatcher();

    @PostConstruct
    public void init() {
        refresh();
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return serviceIsDisable(ctx);
    }

    public boolean serviceIsDisable(RequestContext context) {
        String serviceId = (String) context.get(SERVICE_ID_KEY);
        if (serviceId != null) {
            for (String service : disableServices) {
                if (pathMatcher.match(service, serviceId)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Object run() {
        RequestContext.getCurrentContext().setResponseStatusCode(HttpStatus.NOT_FOUND.value());
        ReflectionUtils.rethrowRuntimeException(new ZuulException("该服务被禁用", HttpStatus.NOT_FOUND.value(), "网关后台已把该服务禁用"));
        return null;
    }

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 6;
    }


    private void refresh() {
        logger.debug("刷新禁用的服务列表");
        List<String> serviceList = appManagerService.disableServiceList();
        if (serviceList != null && !serviceList.isEmpty()) {
            disableServices = appManagerService.disableServiceList();
        }
    }

    @Override
    public void onApplicationEvent(GateWayEvent event) {
        if(event instanceof AppChangeEvent){
            AppChangeEvent changeEvent = (AppChangeEvent) event;
            AppInfo data = changeEvent.getData();
            logger.debug("收到服务信息修改:{}/operateStatus:{}",data.getServiceId(),data.getOperateStatus());
            refresh();
        }else if(event instanceof RefreshEvent){
            refresh();
        }

    }
}
