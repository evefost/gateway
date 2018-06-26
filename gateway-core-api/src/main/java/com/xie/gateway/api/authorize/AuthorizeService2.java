package com.xie.gateway.api.authorize;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 受权服务
 */
@FeignClient(name="xhg-gateway-server")
public interface AuthorizeService2 {

    /**
     * 认证
     * @param authRequest
     * @return
     */
    @PostMapping(value = "/oauth2/authorize")
    AuthoInfo authorize(@RequestBody AuthRequest authRequest);

    /**
     * 登出
     * @param accessToken
     * @param clientId
     * @return
     */
    @PostMapping(value = "/oauth2/logout")
    boolean logout(@RequestParam("clientId") String clientId, @RequestParam("accessToken") String accessToken);


    /**
     * 校验token是否有效
     * @param accessToken
     * @return
     */
    @PostMapping(value="/oauth2/check")
    AuthoInfo check(@RequestParam("accessToken") String accessToken);




}
