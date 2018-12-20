package com.mc.manager.bus.env.service.runtime;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.mc.manager.bus.env.info.current.ServiceEnvInfo;
import com.mc.manager.bus.env.info.ori.EnvInfo;
import com.mc.manager.bus.env.info.ori.RuntimeInfo;
import com.mc.manager.frame.dto.ExecuteResult;
import com.mc.manager.tool.info.MultiSetValueMap;
import com.mc.manager.tool.util.FeignUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 获取服务信息的接口，从服务注册中心获取
 *
 * @author Liu Chunfu
 * @date 2018-09-26 下午2:50
 **/
@Slf4j
@Service
public class RuntimeService implements InitializingBean {


    @Value("${registry.server.url}")
    public String registryServerUri;

    /**
     * Feign的实例
     */
    private RuntimeFetchApi api;


    /**
     * 返回服务运行信息
     *
     * @return map，其中key为instanceId，value环境信息
     */
    public MultiSetValueMap<String, ServiceEnvInfo> obtainAllRuntimes() {
        Map<String, EnvInfo> oriMap = obtainOriSource();
        return obtainServiceInfos(oriMap);
    }

    /**
     * 1.获取原始数据
     *
     * @return
     */
    private Map<String, EnvInfo> obtainOriSource() {
        ExecuteResult<Map<String, EnvInfo>> result = api.fetchEnv();
        if (StrUtil.isNotBlank(result.getErrorCode())) {
            log.error("从服务注册中心{}获取 环境信息失败，原因为代码为:{},错误原因为:{}", registryServerUri, result.getErrorCode(), result.getMessage());
            return null;
        }
        return result.getResult();
    }

    /**
     * 将从RegistryServer中获取的数据填充成RuntimeInfo
     *
     * @param oriSource 原始数据
     * @return runtime结果集合，其中key为ip,value为具体的运行信息。
     */
    public MultiSetValueMap<String, ServiceEnvInfo> obtainServiceInfos(Map<String, EnvInfo> oriSource) {
        MultiSetValueMap<String, ServiceEnvInfo> map = new MultiSetValueMap<>();
        oriSource.forEach((id, envInfo) -> {
            RuntimeInfo runtimeInfo = envInfo.getRuntimeInfo();
            ServiceEnvInfo serviceEnvInfo = fillServiceEnv(id, runtimeInfo);
            //赋值新的名称
            serviceEnvInfo.setAppName(envInfo.getAppName());
            map.put(analysisIp(id), serviceEnvInfo);
        });
        return map;
    }


    /**
     * EnvInfo  --> ServiceEnvInfo
     *
     * @param instanceId  实例id
     * @param runtimeInfo 运行实例
     * @return 一个RuntimeInfo
     */
    public ServiceEnvInfo fillServiceEnv(String instanceId, RuntimeInfo runtimeInfo) {
        ServiceEnvInfo serviceEnvInfo = new ServiceEnvInfo();
        serviceEnvInfo.setClassLoaded(Convert.toStr(runtimeInfo.getClassLoaded(), "null"));
        //此处临时处理ip可能有问题
        serviceEnvInfo.setInstanceId(instanceId);
        serviceEnvInfo.setIp(analysisIp(instanceId));
        serviceEnvInfo.setCpuPercent(runtimeInfo.getCpuPercent());
        serviceEnvInfo.setMemMax(Convert.toStr(runtimeInfo.getMemMax(), "null"));
        serviceEnvInfo.setMemUsed(Convert.toStr(runtimeInfo.getMemUsed(), "null"));
        serviceEnvInfo.setMemPercent(runtimeInfo.getMemPercent());
        serviceEnvInfo.setHeapInit(Convert.toStr(runtimeInfo.getHeapInit(), "null"));
        serviceEnvInfo.setHeapCommitted(Convert.toStr(runtimeInfo.getNonHeapCommitted(), "null"));
        serviceEnvInfo.setHeapMax(Convert.toStr(runtimeInfo.getHeapMax(), "null"));
        serviceEnvInfo.setHeapUsed(Convert.toStr(runtimeInfo.getHeapUsed(), "null"));
        serviceEnvInfo.setHeapPercent(runtimeInfo.getHeapPercent());
        serviceEnvInfo.setNonHeapInit(Convert.toStr(runtimeInfo.getNonHeapInit(), "null"));
        serviceEnvInfo.setNonHeapCommitted(Convert.toStr(runtimeInfo.getNonHeapCommitted(), "null"));
        serviceEnvInfo.setNonHeapMax(serviceEnvInfo.getHeapMax());
        serviceEnvInfo.setNonHeapPercent(runtimeInfo.getNonHeapPercent());
        serviceEnvInfo.setNonHeapUsed(Convert.toStr(runtimeInfo.getNonHeapUsed(), "null"));
        serviceEnvInfo.setThreadCount(Convert.toStr(runtimeInfo.getThreadCount(), "null"));
        serviceEnvInfo.setDaemonThreadCount(Convert.toStr(runtimeInfo.getDaemonThreadCount(), "null"));
        serviceEnvInfo.setPeakThreadCount(Convert.toStr(runtimeInfo.getPeakThreadCount(), "null"));
        serviceEnvInfo.setThreadCpuTime(Convert.toStr(runtimeInfo.getThreadCpuTime(), "null"));
        serviceEnvInfo.setThreadCpuUserTime(Convert.toStr(runtimeInfo.getThreadCpuUserTime(), "null"));
        serviceEnvInfo.setClassLoaded(Convert.toStr(runtimeInfo.getClassLoaded(), "null"));
        serviceEnvInfo.setClassTotalLoaded(Convert.toStr(runtimeInfo.getClassTotalLoaded(), "null"));
        serviceEnvInfo.setClassUnloaded(Convert.toStr(runtimeInfo.getClassUnloaded(), "null"));
        serviceEnvInfo.setPid(Convert.toStr(runtimeInfo.getPid(), "null"));
        return serviceEnvInfo;
    }

    /**
     * 封装获取ip的方法
     *
     * @param instanceId 实例id
     * @return ip地址
     */
    private String analysisIp(String instanceId) {
        return StrUtil.split(instanceId, ":")[0];
    }

    @Override
    public void afterPropertiesSet() throws Exception {
       api= FeignUtil.createInstance(registryServerUri, RuntimeFetchApi.class);
    }
}
