package com.xhg.gateway.controller;


import com.xhg.gateway.api.AppInfo;
import com.xhg.gateway.api.UriInfo;
import com.xhg.gateway.api.event.AppChangeEvent;
import com.xhg.gateway.api.event.UriChangeEvent;
import com.xhg.gateway.bo.AppBo;
import com.xhg.gateway.api.AppManagerService;
import com.xhg.gateway.entity.GatewayApp;
import com.xhg.gateway.query.AppQy;
import com.xhg.gateway.service.GatewayAppService;
import com.xhg.gateway.service.RefreshConfigService;
import com.xhg.gateway.vo.AppVo;
import com.xhg.gateway.vo.PagerResult;
import com.xhg.gateway.vo.ResponseBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * 应用管理
 */
@RestController
@RequestMapping("/app")
public class AppController extends BaseController implements ApplicationContextAware {

    protected  final Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext applicationContext;

    @Resource
    private AppManagerService appManagerService;

    @Resource
    GatewayAppService appService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @RequestMapping(value = "refresh", method = RequestMethod.POST)
    public ResponseBean refresh() {
        appManagerService.refresh();
        return ResponseBean.success();
    }

    @RequestMapping(value = "refreshUriInfo", method = RequestMethod.POST)
    public ResponseBean refreshUriInfo(@RequestBody UriInfo uriInfo) {
        UriChangeEvent event = new UriChangeEvent(this,uriInfo);
        applicationContext.publishEvent(event);
        return ResponseBean.success();
    }

    @RequestMapping(value = "refreshAppInfo", method = RequestMethod.POST)
    public ResponseBean refreshAppInfo(@RequestBody AppInfo appInfo) {
        AppChangeEvent event = new AppChangeEvent(this,appInfo);
        applicationContext.publishEvent(event);
        return ResponseBean.success();
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseBean addApp(@Valid AppBo params) {
        appService.addApp(params);
        return ResponseBean.success();
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ResponseBean updateApp(@Valid AppBo params) {
        if (params.getId() == null) {
            return ResponseBean.failure("应用id不能为空");
        }
        boolean b = appService.updateApp(params);
        return b ? ResponseBean.success() : ResponseBean.failure("应用不存在");
    }

    @RequestMapping(value = "updateEnable", method = RequestMethod.POST)
    public ResponseBean updateEnable(Integer id, Integer enable) {
        if (id == null) {
            return ResponseBean.failure("应用id不能为空");
        }
        if (enable == null) {
            return ResponseBean.failure("enable不能为空");
        }
        if (enable != 0 && enable != 1) {
            return ResponseBean.failure("enable 非法值");
        }
        boolean b = appService.enableStatus(id, enable);
        if (b) {
            GatewayApp app = appService.selectById(id);
            String message = enable == 0 ? " 已被禁用" : " 已启用";
            return ResponseBean.success(app.getServiceId() + message);
        }
        return ResponseBean.failure("应用不存在");
    }

    @RequestMapping(value = "queryPageList", method = RequestMethod.GET)
    public ResponseBean<PagerResult<AppVo>> queryPageList(AppQy params) {
        params.setCurrentPage(getCurrentPage());
        params.setPageSize(getPageSize());
        PagerResult<AppVo> appPagerResultVo = appService.queryPageList(params);
        return ResponseBean.success(appPagerResultVo);
    }


}

