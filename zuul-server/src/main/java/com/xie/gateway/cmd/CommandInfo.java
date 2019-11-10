package com.xie.gateway.cmd;

import com.netflix.hystrix.HystrixCommand;

/**
 * Created by xieyang on 19/11/10.
 */
public class CommandInfo {

    private HystrixCommand command;


    private ExecuteResultType executeResultType;

    private Throwable cause;

    public HystrixCommand getCommand() {
        return command;
    }

    public void setCommand(HystrixCommand command) {
        this.command = command;
    }

    public ExecuteResultType getExecuteResultType() {
        return executeResultType;
    }

    public void setExecuteResultType(ExecuteResultType executeResultType) {
        this.executeResultType = executeResultType;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }
}
