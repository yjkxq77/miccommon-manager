package com.mc.manager.frame.exception;


import cn.hutool.core.util.StrUtil;
import com.mc.manager.frame.constant.ErrorCodeEnum;

/**
 * 权限异常
 *
 * @author LiuChunfu
 * @date 2018年05月09日 15:42:38
 */
public class AuthorityException extends BaseException {

    @Override
    public String errorCode() {
        return ErrorCodeEnum.ILLEGAL_PARAM.getErrorCode();
    }

    public AuthorityException(String message, Object... args) {
        super(StrUtil.format(message, args));
    }
}
