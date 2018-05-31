package com.xhg.gateway.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xhg.gateway.api.AppManagerService;
import com.xhg.gateway.api.event.RefreshEvent;
import com.xhg.gateway.entity.GatewayApp;
import com.xhg.gateway.query.AppUriQy;
import com.xhg.gateway.service.GatewayAppNoauthUriService;
import com.xhg.gateway.service.GatewayAppService;
import com.xhg.gateway.vo.AppNoauthUriVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppManagerServiceImpl implements AppManagerService , ApplicationContextAware {
    protected  final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private GatewayAppService gatewayAppService;

    @Resource
    private GatewayAppNoauthUriService noauthUriService;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @Override
    public List<String> disableServiceList() {
        EntityWrapper<GatewayApp> ew = new EntityWrapper<>();
        ew.eq("enable", 0);
        List<GatewayApp> gatewayApps = gatewayAppService.selectList(ew);
        List<String> services = new ArrayList<>(gatewayApps.size());
        for (GatewayApp gatewayApp : gatewayApps) {
            services.add(gatewayApp.getServiceId());
        }
        return services;
    }

    @Override
    public Map<String, List<String>> noAuthUriList() {
        AppUriQy uriQy = new AppUriQy();
        uriQy.setEnable(1);
        List<AppNoauthUriVo> uris = noauthUriService.queryNoauthUriList(uriQy);
        Map<String, List<String>> uriMapList = new HashMap<>();
        for (AppNoauthUriVo uri : uris) {
            List<String> serviceNoAuthUris = uriMapList.get(uri.getServiceId());
            if (serviceNoAuthUris == null) {
                serviceNoAuthUris = new ArrayList<>();
                uriMapList.put(uri.getServiceId(), serviceNoAuthUris);
            }
            if(StringUtils.isEmpty(uri.getContextPath())){
                serviceNoAuthUris.add(uri.getUrl());
            }else {
                serviceNoAuthUris.add(uri.getContextPath()+uri.getUrl());
            }

        }
        return uriMapList;
    }

    @Override
    public void refresh() {
        logger.info("发送刷新网置设置事件");
        RefreshEvent event = new RefreshEvent(this);
        applicationContext.publishEvent(event);
        //todo 集群应同步事件到其它网关节点
    }


}
