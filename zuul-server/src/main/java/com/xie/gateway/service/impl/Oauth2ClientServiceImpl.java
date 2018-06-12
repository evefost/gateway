package com.xie.gateway.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xie.gateway.entity.Oauth2Client;
import com.xie.gateway.mapper.Oauth2ClientMapper;
import com.xie.gateway.query.OauthQy;
import com.xie.gateway.service.Oauth2ClientService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xie.gateway.vo.Oauth2ClientVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 客户端认证信息(只有注册的应用才能被认证) 服务实现类
 * </p>
 *
 * @author K神带你飞
 * @since 2018-05-30
 */
@Service
public class Oauth2ClientServiceImpl extends ServiceImpl<Oauth2ClientMapper, Oauth2Client> implements Oauth2ClientService {

    @Resource
    private  Oauth2ClientMapper clientMapper;

    @Override
    public Oauth2Client queryClientInfo(String clientId) {
        EntityWrapper<Oauth2Client> ew = new EntityWrapper<>();
        ew.eq("client_id",clientId);
        List<Oauth2Client> oauth2Clients = selectList(ew);
        if(oauth2Clients.isEmpty()){
            return null;
        }
        return oauth2Clients.get(0);
    }

    @Override
    public List<Oauth2ClientVo> queryList(OauthQy params) {
        return clientMapper.queryList(params);
    }
}
