//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.xie.gateway.vo;


import java.io.Serializable;

public class RsBody<T> implements Serializable, Cloneable {
    public static final long serialVersionUID = 6334971409410300128L;

    private int code = 1;

    private String message = "成功";

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
