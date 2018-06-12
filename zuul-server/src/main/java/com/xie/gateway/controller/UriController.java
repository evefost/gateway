package com.xie.gateway.controller;


import com.xie.gateway.bo.AppUriBo;
import com.xie.gateway.entity.GatewayAppNoauthUri;
import com.xie.gateway.query.AppUriQy;
import com.xie.gateway.service.GatewayAppNoauthUriService;
import com.xie.gateway.vo.AppNoauthUriVo;
import com.xie.gateway.vo.ResponseBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 应用uri管理
 */
@RestController
@RequestMapping("/uri")
public class UriController extends BaseController {

    @Resource
    GatewayAppNoauthUriService appNoauthUriService;

    @RequestMapping(value = "addNoAuthUri", method = RequestMethod.POST)
    public ResponseBean addAppNoAuthUri(@Valid AppUriBo params) {
        appNoauthUriService.addAppNoAuthUri(params);
        return ResponseBean.success();
    }

    @RequestMapping(value = "updateNoAuthUri", method = RequestMethod.POST)
    public ResponseBean updateNoAuthUri(@Valid AppUriBo params) {
        if(params.getId() == null){
            return ResponseBean.failure("uri id不能为空");
        }
        boolean b = appNoauthUriService.updateNoAuthUri(params);
        return b? ResponseBean.success():ResponseBean.failure("uri id 不存在");
    }

    @RequestMapping(value = "updateEnable", method = RequestMethod.POST)
    public ResponseBean updateEnable(Integer id,Integer enable) {
        if(id == null){
            return ResponseBean.failure("uri id不能为空");
        }
        if(enable == null){
            return ResponseBean.failure("enable 不能为空");
        }
        if(enable != 0 && enable!=1 ){
            return ResponseBean.failure("enable 非法值");
        }
        boolean b = appNoauthUriService.enableStatus(id,enable);
        if(b){
            GatewayAppNoauthUri uri = appNoauthUriService.selectById(id);
            String message = enable==0?" 未生效":" 已生效";
            return  ResponseBean.success(uri.getUrl()+message);
        }

        return b?ResponseBean.success():ResponseBean.failure("uri不存在");
    }

    @RequestMapping(value = "queryNoauthUriList", method = RequestMethod.GET)
    public ResponseBean<List<AppNoauthUriVo>> queryNoauthUriList(@Valid AppUriQy params) {
        List<AppNoauthUriVo> list = appNoauthUriService.queryNoauthUriList(params);
        return ResponseBean.success(list);
    }

}

