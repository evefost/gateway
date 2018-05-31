package com.xhg.gateway.service;

import com.baomidou.mybatisplus.service.IService;
import com.xhg.gateway.bo.AppBo;
import com.xhg.gateway.entity.GatewayApp;
import com.xhg.gateway.query.AppQy;
import com.xhg.gateway.vo.AppVo;
import com.xhg.gateway.vo.PagerResult;

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
