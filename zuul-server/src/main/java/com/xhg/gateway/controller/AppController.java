package com.xhg.gateway.controller;


import com.xhg.gateway.bo.AppBo;
import com.xhg.gateway.api.AppManagerService;
import com.xhg.gateway.entity.GatewayApp;
import com.xhg.gateway.query.AppQy;
import com.xhg.gateway.service.GatewayAppService;
import com.xhg.gateway.vo.AppVo;
import com.xhg.gateway.vo.PagerResult;
import com.xhg.gateway.vo.ResponseBean;
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
public class AppController extends BaseController {

    @Resource
    private AppManagerService appManagerService;

    @Resource
    GatewayAppService appService;


    @RequestMapping(value = "refresh", method = RequestMethod.POST)
    public ResponseBean refresh() {
        appManagerService.refresh();
        return ResponseBean.success();
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseBean addApp(@Valid AppBo params) {
        appService.addApp(params);
        return ResponseBean.success();
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ResponseBean updateApp(@Valid AppBo params) {
        if(params.getId() == null){
            return ResponseBean.failure("应用id不能为空");
        }
        boolean b = appService.updateApp(params);
        return b?ResponseBean.success():ResponseBean.failure("应用不存在");
    }

    @RequestMapping(value = "updateEnable", method = RequestMethod.POST)
    public ResponseBean updateEnable(Integer id,Integer enable) {
        if(id == null){
            return ResponseBean.failure("应用id不能为空");
        }
        if(enable == null){
            return ResponseBean.failure("enable不能为空");
        }
        if(enable != 0 && enable!=1 ){
            return ResponseBean.failure("enable 非法值");
        }
        boolean b = appService.enableStatus(id,enable);
        if(b){
            GatewayApp app = appService.selectById(id);
            String message = enable==0?" 已被禁用":" 已启用";
            return  ResponseBean.success(app.getServiceId()+message);
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

