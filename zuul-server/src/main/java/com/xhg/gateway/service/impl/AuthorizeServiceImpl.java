package com.xhg.gateway.service.impl;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.context.RequestContext;
import com.xhg.gateway.api.authorize.AuthRequest;
import com.xhg.gateway.api.authorize.AuthoInfo;
import com.xhg.gateway.api.authorize.AuthorizeService;
import com.xhg.gateway.api.authorize.ResponseType;
import com.xhg.gateway.api.event.GateWayEvent;
import com.xhg.gateway.api.event.RefreshEvent;
import com.xhg.gateway.entity.Oauth2Client;
import com.xhg.gateway.query.OauthQy;
import com.xhg.gateway.service.Oauth2ClientService;
import com.xhg.gateway.vo.Oauth2ClientVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVICE_ID_KEY;


@RestController
public class AuthorizeServiceImpl implements AuthorizeService, ApplicationListener<GateWayEvent> {

    protected  final Logger logger = LoggerFactory.getLogger(getClass());

    private ConcurrentHashMap<String, AuthoInfo> loginInfo = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, String> clientServices = new ConcurrentHashMap<>();

    @Resource
    private Oauth2ClientService oauth2ClientService;


    @PostConstruct
    public void init() {
        refresh();
    }

    @Override
    public AuthoInfo authorize(@RequestBody AuthRequest authRequest) {
        logger.info("申请受权信息:{}", JSON.toJSON(authRequest));
        AuthoInfo authoInfo = new AuthoInfo();
        if (StringUtils.isEmpty(authRequest.getClientSecret())) {
            authoInfo.setCode(-2);
            authoInfo.setMessage("安全码不能为空");
            return authoInfo;
        }
        if(ResponseType.TOKEN.value().equals(authRequest.getResponseType()) && StringUtils.isEmpty(authRequest.getUserId())){
            authoInfo.setCode(-3);
            authoInfo.setMessage("用户id不能为空");
            return authoInfo;
        }
        Oauth2Client client = oauth2ClientService.queryClientInfo(authRequest.getClientId());
        if (client == null) {
            authoInfo.setCode(-1);
            authoInfo.setMessage("客户端服务没注册");
            return authoInfo;
        }
        if (!client.getClientSecret().equals(authRequest.getClientSecret())) {
            authoInfo.setCode(-2);
            authoInfo.setMessage("安全码有误");
            return authoInfo;
        }

        String token = authRequest.getToken();
        if(StringUtils.isEmpty(token)){
            token = UUID.randomUUID().toString();
        }
        authoInfo.setCode(1);
        authoInfo.setToken(token);
        String clientId = authRequest.getClientId();
        String serviceId = clientServices.get(clientId);
        loginInfo.put(serviceId+token,authoInfo);
        return authoInfo;
    }

    @Override
    public boolean logout( String clientId,String accessToken) {
        logger.info("用户登出clientId[{}]accessToken[{}]", clientId);
        String serviceId = clientServices.get(clientId);
        AuthoInfo remove = loginInfo.remove(serviceId + accessToken);
        return remove!=null;
    }

    @Override
    public AuthoInfo check(String accessToken) {
        RequestContext ctx = RequestContext.getCurrentContext();
        String serviceId = (String) ctx.get(SERVICE_ID_KEY);
        return loginInfo.get(serviceId+accessToken);
    }


    private void refresh(){
        logger.debug("刷新受权配置信息");
        List<Oauth2ClientVo> oauth2ClientVos = oauth2ClientService.queryList(new OauthQy());
        for (Oauth2ClientVo oauth2ClientVo : oauth2ClientVos) {
            if(oauth2ClientVo.getServiceId() != null){
                clientServices.put(oauth2ClientVo.getClientId(),oauth2ClientVo.getServiceId());
            }
        };
    }

    @Override
    public void onApplicationEvent(GateWayEvent gateWayEvent) {
        if(gateWayEvent instanceof RefreshEvent){
            refresh();
        }
    }
}
