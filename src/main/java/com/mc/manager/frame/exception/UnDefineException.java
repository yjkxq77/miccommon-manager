package com.mc.manager.frame.exception;


import com.mc.manager.frame.constant.ErrorCodeEnum;

/**
 * 未知异常
 *
 * @author LiuChunfu
 * @date 2018/1/16
 */
public class UnDefineException extends BaseException {

    private static final long serialVersionUID = 1L;

    @Override
    public String errorCode() {
        return ErrorCodeEnum.UN_DEFINE.getErrorCode();
    }

    public UnDefineException(Throwable cause) {
        super(cause);
    }

    public UnDefineException() {
    }

}
