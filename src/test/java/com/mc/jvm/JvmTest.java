package com.mc.jvm;

import cn.hutool.json.JSONUtil;
import cn.hutool.system.JavaRuntimeInfo;
import cn.hutool.system.JvmInfo;
import cn.hutool.system.RuntimeInfo;
import cn.hutool.system.SystemUtil;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JVM测试
 *
 * @author Liu Chunfu
 * @date 2018-09-26 上午10:22
 **/
public class JvmTest {


    @Test
    public void test1() {
        JvmInfo jvmInfo = SystemUtil.getJvmInfo();

        JavaRuntimeInfo javaRuntimeInfo = SystemUtil.getJavaRuntimeInfo();

        RuntimeInfo runtimeInfo = SystemUtil.getRuntimeInfo();

        System.out.println(JSONUtil.toJsonStr(jvmInfo));

        //System.out.println(123);
    }

    @Test
    public void test2() {
    }
}
