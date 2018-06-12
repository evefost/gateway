package com.xie.gateway.service;

import com.baomidou.mybatisplus.service.IService;
import com.xie.gateway.bo.AppBo;
import com.xie.gateway.entity.GatewayApp;
import com.xie.gateway.query.AppQy;
import com.xie.gateway.vo.AppVo;
import com.xie.gateway.vo.PagerResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author K神带你飞
 * @since 2018-05-29
 */
public interface GatewayAppService extends IService<GatewayApp> {

    boolean addApp(AppBo params);

    boolean updateApp(AppBo params);

    PagerResult<AppVo> queryPageList(AppQy params);

    List<AppVo> queryList(AppQy params);

    boolean enableStatus(Integer id, Integer enable);
}
