package com.mc.manager.frame.exception;

/**
 * 定义的抽象
 *
 * @author LiuChunfu
 * @date 2018/3/13
 */
public abstract class BaseException extends RuntimeException {

    /**
     * 定义错误码
     *
     * @return 错误码定义
     */
    public abstract String errorCode();

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException() {
    }
}

