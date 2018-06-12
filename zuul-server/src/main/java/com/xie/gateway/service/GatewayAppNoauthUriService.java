package com.xie.gateway.service;

import com.xie.gateway.bo.AppUriBo;
import com.xie.gateway.entity.GatewayAppNoauthUri;
import com.baomidou.mybatisplus.service.IService;
import com.xie.gateway.query.AppUriQy;
import com.xie.gateway.vo.AppNoauthUriVo;
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
public interface GatewayAppNoauthUriService extends IService<GatewayAppNoauthUri> {


    List<AppNoauthUriVo> queryNoauthUriList(AppUriQy params);

    PagerResult<AppNoauthUriVo> queryPageList(AppUriQy params);


    boolean addAppNoAuthUri(AppUriBo params);

    boolean updateNoAuthUri(AppUriBo params);

    boolean enableStatus(Integer id, Integer enable);
}
