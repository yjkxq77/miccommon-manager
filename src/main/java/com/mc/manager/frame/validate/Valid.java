package com.mc.manager.frame.validate;

import java.lang.annotation.*;

/**
 * 哥,写点什么吧！！
 *
 * @author LiuChunfu
 * @date 2018/3/14
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Valid {

    /**
     * 具体的执行类
     *
     * @return
     */
    Class<? extends IValidator> value();

    /**
     * 验证第几个参数
     *
     * @return 参数的下标
     */
    int index() default 0;
}
