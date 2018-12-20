package com.mc.manager.frame.dto;

import com.mc.manager.frame.exception.BaseException;
import lombok.Data;

/**
 * 执行结果
 *
 * @author LiuChunfu
 * @date 2018/3/13
 */
@Data
public class ExecuteResult<T> {

    /**
     * 结果
     */
    private T result;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 自定义的异常，如果为null则获取gerErrorCode
     */
    private String message;

    /**
     * 异常错误的执行结果构造
     *
     * @param exception
     */
    public ExecuteResult(BaseException exception) {
        this.errorCode = exception.errorCode();
        this.message = exception.getMessage();
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 正确结果的构造
     *
     * @param result 结果
     */
    public ExecuteResult(T result) {
        this.result = result;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 默认的构造函数，方便后续序列化
     */
    public ExecuteResult() {
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
