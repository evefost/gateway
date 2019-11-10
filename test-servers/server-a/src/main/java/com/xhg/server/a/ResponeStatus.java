package com.xhg.server.a;

/**
 * 定义返回状态
 */
public enum ResponeStatus {
    /**
     * 停机维护
     */
    MAINTENANCE(-300, "停机维护"),
    /**
     * 成功
     */
    SUCCESS(1, "服务器连接成功"),

    FAILURE(-1, "失败"),
    /**
     * 系统错误
     */
    ERROR(500, "系统错误"),
    /**
     * 系统错误
     */
    INVALID(400, "请求参数出错"),
    /**
     * 退出登录
     */
    LOGOUT(-1, "退出登录"),

    /**
     * 未登录
     */
    LOGIN(-2, "登录名或者密码错误"),
    /**
     * login time out
     */
    LOGIN_TIMEOUT(-4, "登录超时"),
    /**
     * 没有权限
     */
    MANAGERUSER_UNAUTHORIZED(-3, "没有权限"),
    /**
     * 请求过于频繁，请稍后再试
     */
    FREQUENT(-3, "请求过于频繁，请稍后再试"),

    PARAM_FAIL_CODE(-5, "参数错误"),
    /**
     * 今天提现次数已达上限，请明天再试吧
     */
    PUT_FORWARD_FREQUENCY(-6, "今天提现次数已达上限，请明天再试吧");
    private final int value;

    private final String reasonPhrase;

    ResponeStatus(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int getValue() {
        return value;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }
}
