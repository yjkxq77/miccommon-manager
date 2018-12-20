package com.mc.manager.frame.exception;


import cn.hutool.core.util.StrUtil;
import com.mc.manager.frame.constant.ErrorCodeEnum;

/**
 * 参数异常错误
 *
 * @author LiuChunfu
 * @date 2018/1/16
 */
public class IllegalParamException extends BaseException {

    @Override
    public String errorCode() {
        return ErrorCodeEnum.ILLEGAL_PARAM.getErrorCode();
    }

    public IllegalParamException(String message, Object... args) {
        super(StrUtil.format(message, args));
    }
}
