package com.mc.manager.frame.validate;

/**
 * 参数校验的验证器
 *
 * @author LiuChunfu
 * @date 2018/3/14
 */
public interface IValidator<T> {
    /**
     * 对指定的参数进行数据校验
     *
     * @param t 指定的参数
     */
    void validate(T t);

}
