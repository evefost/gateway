package com.xhg.gateway.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xhg.gateway.bo.AppBo;
import com.xhg.gateway.api.AppInfo;
import com.xhg.gateway.api.OperateStatus;
import com.xhg.gateway.api.event.AppChangeEvent;
import com.xhg.gateway.entity.GatewayApp;
import com.xhg.gateway.exception.XhgException;
import com.xhg.gateway.mapper.GatewayAppMapper;
import com.xhg.gateway.query.AppQy;
import com.xhg.gateway.service.GatewayAppService;
import com.xhg.gateway.service.RefreshConfigService;
import com.xhg.gateway.util.BeanUtils;
import com.xhg.gateway.vo.AppVo;
import com.xhg.gateway.vo.PagerResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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
public class GatewayAppServiceImpl extends ServiceImpl<GatewayAppMapper, GatewayApp> implements GatewayAppService  , ApplicationContextAware {

    protected static final Logger logger = LoggerFactory.getLogger(GatewayAppServiceImpl.class);

    private ApplicationContext applicationContext;

    @Resource
    private RefreshConfigService refreshConfigService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @Override
    @Transactional
    public boolean addApp(AppBo params) {
        insert(BeanUtils.copy(params, GatewayApp.class));
        checkAppModify(params);
        //发送更新事件
        publishChangeEvent(params.getServiceId(),OperateStatus.ADD);
        return true;
    }

    @Override
    public boolean enableStatus(Integer id, Integer enable) {
        GatewayApp app = new GatewayApp();
        app.setId(id);
        app.setEnable(enable);
        boolean result = updateById(app);
        if(result){
            GatewayApp app1 = selectById(id);
            publishChangeEvent(app1.getServiceId(),OperateStatus.ENABLE_CHANGE);
        }
        return result;

    }

    @Override
    public boolean updateApp(AppBo params) {
        boolean result = updateById(BeanUtils.copy(params, GatewayApp.class));
        checkAppModify(params);
        if(result){
            GatewayApp app1 = selectById(params.getId());
            publishChangeEvent(app1.getServiceId(),OperateStatus.UPDATE);
        }
        return result;
    }

    private void publishChangeEvent(String serviceId,OperateStatus status){
        AppInfo appInfo = new AppInfo();
        appInfo.setServiceId(serviceId);
        appInfo.setOperateStatus(status);
        AppChangeEvent event = new AppChangeEvent(this,appInfo);
        applicationContext.publishEvent(event);
        //同步通知其它gateway-service
        refreshConfigService.syncRefresh(event);
    }


    private void checkAppModify(AppBo params) {
        EntityWrapper<GatewayApp> ew = new EntityWrapper<>();
        ew.eq("service_id", params.getServiceId());
        //暂时不支持多版本管理
        //ew.eq("version", params.getVersion());
        int count = selectCount(ew);
        if (count > 1) {
            throw new XhgException("当前服务id已存在");
        }
    }

    @Override
    public PagerResult<AppVo> queryPageList(AppQy params) {
        Page<GatewayApp> page = new Page<>();
        page.setCurrent(params.getCurrentPage());
        page.setSize(params.getPageSize());
        Page<GatewayApp> appPage = selectPage(page);

        PagerResult<AppVo> pageResultVo = new PagerResult();
        pageResultVo.setCurrentPage(page.getCurrent());
        pageResultVo.setTotal(page.getTotal());
        pageResultVo.setTotalPages((int) page.getPages());
        List<AppVo> list = new ArrayList<>(appPage.getRecords().size());
        AppVo appVo = null;
        for (GatewayApp app : appPage.getRecords()) {
            appVo = BeanUtils.copy(app, AppVo.class);
            list.add(appVo);
        }
        pageResultVo.setList(list);
        return pageResultVo;
    }

    @Override
    public List<AppVo> queryList(AppQy params) {
        EntityWrapper<GatewayApp> ew = new EntityWrapper<>();
        if(params.getEnable() != null){
            ew.eq("enable",params.getEnable());
        }
        List<GatewayApp> gatewayApps = selectList(ew);
        List<AppVo> list = new ArrayList<>();
        AppVo appVo = null;
        for (GatewayApp app : gatewayApps) {
            appVo = BeanUtils.copy(app, AppVo.class);
            list.add(appVo);
        }
        return list;
    }
}
