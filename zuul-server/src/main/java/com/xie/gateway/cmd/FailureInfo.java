package com.xie.gateway.cmd;

/**
 * Created by xieyang on 19/11/9.
 */

public class FailureInfo {

    private Throwable cause;

    private FailureType failureType;

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public FailureType getFailureType() {
        return failureType;
    }

    public void setFailureType(FailureType failureType) {
        this.failureType = failureType;
    }
}
