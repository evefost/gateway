package com.xie.gateway.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xie.gateway.entity.GatewayAppNoauthUri;
import com.xie.gateway.query.AppUriQy;
import com.xie.gateway.vo.AppNoauthUriVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author K神带你飞
 * @since 2018-05-29
 */
public interface GatewayAppNoauthUriMapper extends BaseMapper<GatewayAppNoauthUri> {

    List<AppNoauthUriVo> queryList(@Param("params") AppUriQy params);

    List<AppNoauthUriVo> queryList(@Param("page")Page<GatewayAppNoauthUri> page, @Param("params") AppUriQy params);

}
