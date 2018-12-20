package com.mc.manager.frame.constant;

/**
 * 错误代码的枚举
 *
 * @author LiuChunfu
 * @date 2018/3/13
 */
public enum ErrorCodeEnum {

    /**
     * 参数不合法
     */
    ILLEGAL_PARAM("11000"),
    /**
     * 执行超时
     */
    TIME_OUT("12000"),
    /**
     * 无法找到实例
     */
    NO_INSTANCE("13000"),
    /**
     * 无法找到对应的执行器
     */
    NO_EXECUTOR("13500"),
    /**
     * 实例还未注册的异常
     */
    INSTANCE_NOT_REGISTERED("14500"),
    /**
     * 执行超时
     */
    FILE_NAME_ILLEGAL("15000"),
    /**
     * 执行超时
     */
    FILE_NOT_EXISTS("15100"),
    /**
     * 执行超时
     */
    FILE_ALREADY_EXISTS("15200"),
    /**
     * 未定义的异常
     */
    UN_DEFINE("88888");

    private final String errorCode;

    public String getErrorCode() {
        return this.errorCode;
    }

    ErrorCodeEnum(String errorCode) {
        this.errorCode = errorCode;
    }

}

