package com.mc.manager.bus.env.web.controller;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.mc.manager.frame.dto.ExecuteResult;
import com.mc.manager.frame.exception.BaseException;
import com.mc.manager.frame.exception.UnDefineException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * 全局的异常处理增强
 *
 * @author LiuChunfu
 * @date 2018/3/13
 */
@Slf4j
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ExecuteResult handlerException(Exception exception) {
        String msg = ExceptionUtil.stacktraceToString(exception, 100);
        log.error("发生异常！，信息信息：{}", msg);
        //打印出异常栈
        exception.printStackTrace();
        //没有定义的异常
        ExecuteResult result = new ExecuteResult();
        result.setResult("失败");
        result.setMessage(exception.getLocalizedMessage());
        result.setErrorCode("8888");
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }
}
