package com.eve.hystrix.extend;

import com.eve.hystrix.extend.core.CommandInfo;
import com.eve.hystrix.extend.core.CommandListener;
import com.eve.hystrix.extend.core.ExecuteResultType;
import com.netflix.hystrix.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import static com.eve.hystrix.extend.core.ExecuteResultType.OTHER;

/**
 *
 * @author xieyang
 * @date 19/11/10
 */
public class CommandSupport {

    protected final static Logger logger = LoggerFactory.getLogger(CommandSupport.class);


    public static CommandInfo buildCommandInfo(HystrixCommand command,CommandListener listener){
        CommandInfo commandInfo = new CommandInfo();
        commandInfo.setExecuteResultType(ExecuteResultType.UNKNOW);
        commandInfo.setCommand(command);
        commandInfo.setListener(listener);
        if(listener != null){
            try{
                listener.onCommandCreate(commandInfo);
            }catch (Throwable throwable){
                logger.warn(" onCommandCreate call back error  ",throwable);
            }
        }
        return commandInfo;
    }

    public static void onSuccess(CommandInfo commandInfo){
        commandInfo.setExecuteResultType(ExecuteResultType.SUCCESS);
        CommandListener listener = commandInfo.getListener();
        if(listener != null){
            try{
                listener.onSuccess(commandInfo);
            }catch (Throwable throwable){
                logger.warn(" onCommandSuccess call back error  ",throwable);
            }

        }

    }

    public static void onFailure(CommandInfo commandInfo){
        HystrixCommand command = commandInfo.getCommand();
        commandInfo.setExecuteResultType(getFailureType(command));
        CommandListener listener = commandInfo.getListener();
        if(listener != null){

            try{
                listener.onFailure(commandInfo);
            }catch (Throwable throwable){
                logger.warn(" onCommandFailure call back error  ",throwable);
            }
        }

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
