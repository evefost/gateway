package com.xie.gateway.controller;


import com.xie.gateway.api.AppInfo;
import com.xie.gateway.api.AppManagerService;
import com.xie.gateway.api.UriInfo;
import com.xie.gateway.api.event.AppChangeEvent;
import com.xie.gateway.api.event.UriChangeEvent;
import com.xie.gateway.bo.AppBo;
import com.xie.gateway.entity.GatewayApp;
import com.xie.gateway.query.AppQy;
import com.xie.gateway.remote.SitePriceFeignService;
import com.xie.gateway.service.GatewayAppService;
import com.xie.gateway.vo.AppVo;
import com.xie.gateway.vo.PagerResult;
import com.xie.gateway.vo.ResponseBean;
import com.xie.gateway.xx.remote.TestFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

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


    @Resource
    private SitePriceFeignService feignService;

    @Resource
    private TestFeign feignService2;

    @RequestMapping(value = "test", method = RequestMethod.POST)
    public ResponseBean test() {
        return ResponseBean.success(feignService.queryInfo(new AppBo()));
    }

    @RequestMapping(value = "test2", method = RequestMethod.POST)
    public ResponseBean test2() {
        return ResponseBean.success(feignService2.queryInfo(new AppBo()));
    }

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

