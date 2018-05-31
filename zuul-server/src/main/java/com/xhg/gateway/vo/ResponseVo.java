package com.xhg.gateway.vo;

import java.io.Serializable;

/**
 * 用于适配置老接口
 */
public class ResponseVo implements Serializable, Cloneable {

    public static final long serialVersionUID = 6334971409410600125L;

    private RsBody responseBody;

    private int code = 1;

    private String message = "成功";

    public RsBody getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(RsBody responseBody) {
        this.responseBody = responseBody;
    }

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
}
