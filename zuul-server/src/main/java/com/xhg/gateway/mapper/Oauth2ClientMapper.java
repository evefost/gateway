package com.xhg.gateway.mapper;

import com.xhg.gateway.entity.Oauth2Client;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xhg.gateway.query.AppUriQy;
import com.xhg.gateway.query.OauthQy;
import com.xhg.gateway.vo.AppNoauthUriVo;
import com.xhg.gateway.vo.Oauth2ClientVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 客户端认证信息(只有注册的应用才能被认证) Mapper 接口
 * </p>
 *
 * @author K神带你飞
 * @since 2018-05-30
 */
public interface Oauth2ClientMapper extends BaseMapper<Oauth2Client> {

    List<Oauth2ClientVo> queryList(@Param("params") OauthQy params);

}
