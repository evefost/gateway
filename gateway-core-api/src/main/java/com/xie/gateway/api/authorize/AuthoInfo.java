package com.xie.gateway.api.authorize;

import java.io.Serializable;

public class AuthoInfo implements Serializable, Cloneable {

    /**
     * 1,证认成功，-1客户端未注册，-2 安全码不对,-3 用户id为空
     */
    private  int code=0;

    /**
     * token
     */
    private String token;

    /**
     * 有效期
     */
    private Long expireIn;

    private String scope;

    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(Long expireIn) {
        this.expireIn = expireIn;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}