package com.mc.manager.tool.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * 协助AOP的切点获取各类信息<br>
 *
 * @author LiuChunfu
 * @date 2018/2/8
 */
public final class JoinPointUtil {

    private JoinPointUtil() {
    }

    /**
     * 获取当前方法签名（多类具体信息）
     *
     * @param joinPoint 连接点
     * @return 方法签名
     */
    public static MethodSignature getMethodSignature(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();

        if (signature instanceof MethodSignature) {
            return (MethodSignature) signature;
        }
        throw new RuntimeException("Current pointCut is method!");
    }

    /**
     * 获取当前切的方法
     *
     * @param joinPoint 连接点
     * @return 方法
     */
    public static Method getMethod(JoinPoint joinPoint) {
        //方法1:直接通过 "MethodSignature" 获取 ,也可以再次通过反射获取，但是不推荐
        return getMethodSignature(joinPoint).getMethod();
    }

    /**
     * 获取当前切的方法的名称
     *
     * @param joinPoint 连接点
     * @return 方法
     */
    public static String getMethodName(JoinPoint joinPoint) {
        return getMethod(joinPoint).getName();
    }

    /**
     * 获取当前切的方法的返回值
     *
     * @param joinPoint
     * @return
     */
    public static Class<?> getReturnType(JoinPoint joinPoint) {
        return getMethodSignature(joinPoint).getReturnType();
    }

    /**
     * 获取切面目标类的Class对象
     *
     * @param joinPoint 连接点
     * @return 目标类
     */
    public static Class getTargetClass(JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass();
    }

    /**
     * 获取切的目标类的Class名称
     *
     * @param joinPoint 连接点
     * @return
     */
    public static String getTargetClassName(JoinPoint joinPoint) {
        return getTargetClass(joinPoint).getName();
    }

    /**
     * 获取切的目标类的Class名称
     *
     * @param joinPoint 连接点
     * @return
     */
    public static String getTargetClassSimpleName(JoinPoint joinPoint) {
        return getTargetClass(joinPoint).getSimpleName();
    }

    /**
     * 获取切面所切方法的参数
     *
     * @param joinPoint 连接点
     * @return 参数类型数组
     */
    public static Class<?>[] getParamterTypes(JoinPoint joinPoint) {
        return getMethodSignature(joinPoint).getMethod().getParameterTypes();
    }

    /**
     * 获取切面所切方法的参数
     *
     * @param joinPoint 连接点
     * @return 具体的参数
     */
    public static Object[] getMethodArgs(JoinPoint joinPoint) {
        return joinPoint.getArgs();
    }
}
