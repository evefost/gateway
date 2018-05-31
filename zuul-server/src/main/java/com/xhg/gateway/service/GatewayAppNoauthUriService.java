package com.xhg.gateway.service;

import com.xhg.gateway.bo.AppUriBo;
import com.xhg.gateway.entity.GatewayAppNoauthUri;
import com.baomidou.mybatisplus.service.IService;
import com.xhg.gateway.query.AppUriQy;
import com.xhg.gateway.vo.AppNoauthUriVo;
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
public interface GatewayAppNoauthUriService extends IService<GatewayAppNoauthUri> {


    List<AppNoauthUriVo> queryNoauthUriList(AppUriQy params);

    PagerResult<AppNoauthUriVo> queryPageList(AppUriQy params);


    boolean addAppNoAuthUri(AppUriBo params);

    boolean updateNoAuthUri(AppUriBo params);

    boolean enableStatus(Integer id, Integer enable);
}
