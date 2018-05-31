package com.xhg.gateway.service;

import com.xhg.gateway.entity.Oauth2Client;
import com.baomidou.mybatisplus.service.IService;
import com.xhg.gateway.query.OauthQy;
import com.xhg.gateway.vo.Oauth2ClientVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 客户端认证信息(只有注册的应用才能被认证) 服务类
 * </p>
 *
 * @author K神带你飞
 * @since 2018-05-30
 */
public interface Oauth2ClientService extends IService<Oauth2Client> {

    Oauth2Client queryClientInfo(String clientId);

    List<Oauth2ClientVo> queryList(OauthQy params);
}
