package com.eve.hystrix.extend.core;

import com.netflix.hystrix.HystrixCommand;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author xieyang
 * @date 19/11/10
 */

public class CommandInfo {

    private Map<String,Object> properties = new HashMap<>();

    private CommandListener listener;

    private HystrixCommand command;

    private ExecuteResultType executeResultType;

    private Throwable cause;

    private String serviceId;

    private String uri;


    public void setProperty(String key,Object value){
        properties.put(key,value);
    }

    public Object getProperty(String key){
       return properties.get(key);
    }

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

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public CommandListener getListener() {
        return listener;
    }

    public void setListener(CommandListener listener) {
        this.listener = listener;
    }
}
