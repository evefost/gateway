package com.xie.gateway.api.authorize;

public class AuthRequest {

    /**
     * 用户id {@link AuthRequest#responseType}等于token是不能为空
     */
    private String userId;

    /**
     * 客户端id(相对于网关)
     */
    private String clientId;

    /**
     * 客户端 安全码
     */
    private String clientSecret;

    /**
     * 认证类型{@link ResponseType}
     */
    private String responseType = ResponseType.TOKEN.value();

    /**
     * 如里客户端传入token，服务端则不再生成token
     */
    private String token;

    /**
     * token 有效期,如果客户端没传入，
     */
    private Long expireIn;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public AuthRequest(){
    }
    public AuthRequest(String clientId,String clientSecret){
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public AuthRequest(String clientId,String clientSecret,String userId){
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClientId() {
        return clientId;
    }

    public Long getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(Long expireIn) {
        this.expireIn = expireIn;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }
}
