package com.mc.manager.frame.aspect;

import cn.hutool.core.lang.Singleton;
import com.mc.manager.frame.exception.BaseException;
import com.mc.manager.frame.exception.UnDefineException;
import com.mc.manager.frame.validate.IValidator;
import com.mc.manager.frame.validate.Valid;
import com.mc.manager.tool.util.JoinPointUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 对参数进行校验的切面
 *
 * @author LiuChunfu
 * @date 2018/3/14
 */
@Aspect
@Component
@Slf4j
@Order(0)
public class ValidateAspect {


    @Pointcut("@annotation(com.mc.manager.frame.validate.Valid)")
    public void pointCut() {
    }

    /**
     * 对服务实例传递过来的的参数进行校验
     *
     * @param joinPoint 切点
     * @throws Throwable 异常
     */
    @Before(value = "pointCut()")
    public void valid(JoinPoint joinPoint) throws Throwable {
        try {
            Object[] args = JoinPointUtil.getMethodArgs(joinPoint);
            Method method = JoinPointUtil.getMethod(joinPoint);
            Valid valid = method.getAnnotation(Valid.class);
            Class<? extends IValidator> validatorClass = valid.value();
            //内置缓存
            IValidator validator = Singleton.get(validatorClass);
            int index = valid.index();
            validator.validate(args[index]);
        } catch (Exception e) {
            log.error("[验证参数] 通过切面验证参数的时候发生异常，具体信息：{}", e.getMessage());
            //我们自己定义的异常，可以不在转化
            if (e instanceof BaseException) {
                throw e;
            }
            throw new UnDefineException(e);
        }
    }

}


