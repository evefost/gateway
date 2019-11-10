package com.xie.gateway.cmd.cmd;

import com.netflix.hystrix.HystrixCommand;
import com.xie.gateway.cmd.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import static com.xie.gateway.cmd.ExecuteResultType.OTHER;

/**
 *
 * @author xieyang
 * @date 19/11/10
 */
public class CommandSupport {

    protected final static Logger logger = LoggerFactory.getLogger(DefaultCommandListener.class);


    public static CommandInfo buildFailureInfo(HystrixCommand command){
        CommandInfo commandInfo = new CommandInfo();
        ExecuteResultType failureType = getFailureType(command);
        commandInfo.setExecuteResultType(failureType);
        return commandInfo;
    }


    public static ExecuteResultType getFailureType(HystrixCommand command) {
        ExecuteResultType failureType;
        if (command.isResponseTimedOut()) {
            failureType= ExecuteResultType.RESPONSE_TIMEDOUT;
        } else if (command.isResponseRejected()) {
            failureType= ExecuteResultType.RESPONSERE_JECTED;
        } else if (command.isCircuitBreakerOpen()) {
            failureType= ExecuteResultType.CIRCUITBREAK_EROPEN;
        } else {
            Throwable ex = command.getFailedExecutionException();
            if(ex != null){
                Throwable cause = findCause(ex);
                if (cause instanceof SocketTimeoutException) {
                    logger.debug(" is ribbon  time out");
                    failureType= ExecuteResultType.RESPONSE_TIMEDOUT;
                } else if (cause instanceof ConnectException) {
                    failureType= ExecuteResultType.CONNECT_FAILURE;
                }else {
                    failureType = OTHER;
                }
            }else {
                failureType = OTHER;
            }
        }

        return failureType;
    }


    private static Throwable findCause(Throwable root) {
        Throwable cause = root.getCause();
        if (cause == null) {
            return root;
        }
        return findCause(cause);
    }


    /**
     * 据统计维度确定是熔断器否为重新打弄
     *
     * @return
     */
    private static boolean isCircuitReOpen(HystrixCommand command) {
        if (!command.isCircuitBreakerOpen()) {
            return false;
        }
        //todo

        return false;
    }

    private boolean isCircuitReClose() {
//        String appName = mappingInfo.getAppName();
//        Boolean status = serverStatus.get(appName);
//        if (status != null && status) {
//            return true;
//        }
        return false;
    }

}
