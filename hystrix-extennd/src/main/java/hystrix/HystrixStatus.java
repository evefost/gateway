package hystrix;

public enum HystrixStatus {

    CIRCUIT_BREAKER_OPEN,
    RESPONSE_REJECTED,
    RESPONSE_TIMEDOUT,
    EXECUTED_FAILURE,
    EXECUTED_SUCCEED;

}
