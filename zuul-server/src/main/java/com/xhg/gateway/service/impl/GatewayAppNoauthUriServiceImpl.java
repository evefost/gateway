package com.xhg.gateway.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xhg.gateway.bo.AppUriBo;
import com.xhg.gateway.api.OperateStatus;
import com.xhg.gateway.api.UriInfo;
import com.xhg.gateway.api.event.UriChangeEvent;
import com.xhg.gateway.entity.GatewayApp;
import com.xhg.gateway.entity.GatewayAppNoauthUri;
import com.xhg.gateway.exception.XhgException;
import com.xhg.gateway.mapper.GatewayAppNoauthUriMapper;
import com.xhg.gateway.query.AppUriQy;
import com.xhg.gateway.service.GatewayAppNoauthUriService;
import com.xhg.gateway.service.GatewayAppService;
import com.xhg.gateway.util.BeanUtils;
import com.xhg.gateway.vo.AppNoauthUriVo;
import com.xhg.gateway.vo.PagerResult;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author K神带你飞
 * @since 2018-05-29
 */
@Service
public class GatewayAppNoauthUriServiceImpl extends ServiceImpl<GatewayAppNoauthUriMapper, GatewayAppNoauthUri> implements GatewayAppNoauthUriService,ApplicationContextAware {

    @Resource
    private GatewayAppNoauthUriMapper appNoauthUriMapper;

    @Resource
    private GatewayAppService appService;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    @Transactional
    public boolean addAppNoAuthUri(AppUriBo params) {
        GatewayApp app = appService.selectById(params.getAppId());
        if (app == null) {
            throw new XhgException("应用id不存在");
        }
        GatewayAppNoauthUri copy = BeanUtils.copy(params, GatewayAppNoauthUri.class);
        insert(copy);
        checkUri(params);
        publishChangeEvent(copy.getId(),OperateStatus.ADD);
        return true;
    }

    @Override
    public boolean updateNoAuthUri(AppUriBo params) {
        boolean result = updateById(BeanUtils.copy(params, GatewayAppNoauthUri.class));
        checkUri(params);
        if(result){
            UriInfo uriInfo = new UriInfo();
            GatewayAppNoauthUri uri = selectById(params.getId());
            uriInfo.setAppId(uri.getAppId());
            uriInfo.setUrl(uri.getUrl());
            uriInfo.setOperateStatus(OperateStatus.UPDATE);
            publishChangeEvent(params.getId(),OperateStatus.UPDATE);
        }
        return result;
    }

    @Override
    public boolean enableStatus(Integer id, Integer enable) {
        GatewayAppNoauthUri app = new GatewayAppNoauthUri();
        app.setId(id);
        app.setEnable(enable);
        boolean result =  updateById(app);
        if(result){
            publishChangeEvent(id,OperateStatus.ENABLE_CHANGE);
        }
        return result;
    }

    private void publishChangeEvent(Integer id,OperateStatus operateStatus){
        GatewayAppNoauthUri uri = selectById(id);
        UriInfo uriInfo = new UriInfo();
        uriInfo.setAppId(uri.getAppId());
        uriInfo.setUrl(uri.getUrl());
        uriInfo.setOperateStatus(operateStatus);
        UriChangeEvent event = new UriChangeEvent(this,uriInfo);
        applicationContext.publishEvent(event);
    }

    private void checkUri(AppUriBo params) {
        EntityWrapper<GatewayAppNoauthUri> ew = new EntityWrapper<>();
        ew.eq("app_id", params.getAppId());
        ew.eq("url", params.getUrl());
        int count = selectCount(ew);
        if (count > 1) {
            throw new XhgException("当前应用 uri已存在");
        }
    }

    @Override
    public List<AppNoauthUriVo> queryNoauthUriList(AppUriQy params) {
        return appNoauthUriMapper.queryList(params);
    }

    @Override
    public PagerResult<AppNoauthUriVo> queryPageList(AppUriQy params) {
        Page<GatewayAppNoauthUri> page = new Page<>(params.getCurrentPage(), params.getPageSize());
        List<AppNoauthUriVo> list = appNoauthUriMapper.queryList(page, params);
        PagerResult result = new PagerResult();
        result.setList(list);
        result.setTotal(page.getTotal());
        result.setCurrentPage(page.getCurrent());
        result.setPageSize(page.getSize());
        return result;
    }


}