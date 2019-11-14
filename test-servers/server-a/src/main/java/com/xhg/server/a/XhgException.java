package com.xhg.server.a;

public class XhgException extends RuntimeException {

    private int code;

    public XhgException(String messge) {
        super(messge);
        this.code = ResponeStatus.FAILURE.getValue();
    }

    public XhgException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
