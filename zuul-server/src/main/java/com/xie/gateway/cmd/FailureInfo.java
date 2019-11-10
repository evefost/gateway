package com.xie.gateway.cmd;

/**
 * Created by xieyang on 19/11/9.
 */

public class FailureInfo {

    private Throwable cause;

    private ExecuteResultType failureType;

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public ExecuteResultType getFailureType() {
        return failureType;
    }

    public void setFailureType(ExecuteResultType failureType) {
        this.failureType = failureType;
    }
}
