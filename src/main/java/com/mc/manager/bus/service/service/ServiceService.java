package com.mc.manager.bus.service.service;

import cn.hutool.core.util.StrUtil;
import com.mc.manager.bus.service.info.AppInfo;
import com.mc.manager.bus.service.info.ServiceItemInfo;
import com.mc.manager.frame.dto.ExecuteResult;
import com.mc.manager.tool.util.FeignUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * 服务器的服务类
 *
 * @author Liu Chunfu
 * @date 2018-11-16 下午5:47
 **/
@Service
public class ServiceService implements InitializingBean {

    /**
     * 从服务注册中心拉取服务
     */
    private ServiceFetchApi serviceFetchApi;

    /**
     * 服务注册中心地址
     */
    @Value("${registry.server.url}")
    private String registryUrl;

    /**
     * 初始化后注入
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() {
        serviceFetchApi = FeignUtil.createInstance(registryUrl, ServiceFetchApi.class);
    }

    /**
     * 通过ip地址获取应用
     *
     * @param ip ip地址
     * @return ip地址对应主机的所有应用信息
     */
    public Collection<AppInfo> appsByIp(String ip) {
        ExecuteResult<Collection<AppInfo>> result = serviceFetchApi.fetchAppInfosByIp(ip);
        if (StrUtil.isNotBlank(result.getErrorCode())) {
            throw new RuntimeException("通过Ip获取Apps时出现异常：" + result.getMessage());
        }
        return result.getResult();
    }


    /**
     * 通过appName找出对应的服务单元
     *
     * @param appName
     * @return
     */
    public Collection<ServiceItemInfo> serviceItemInfosByAppName(String appName) {
        ExecuteResult<Collection<ServiceItemInfo>> result = serviceFetchApi.fetchServiceItemByAppName(appName);
        if (StrUtil.isNotBlank(result.getErrorCode())) {
            throw new RuntimeException("通过appName:{" + appName + "}获取服务单元时出现异常：" + result.getMessage());
        }
        return result.getResult();
    }


    /**
     * 获取所有的应用
     *
     * @return 应用信息
     */
    public Collection<AppInfo> appinfos() {
        ExecuteResult<Collection<AppInfo>> result = serviceFetchApi.fetchAppInfos();
        if (StrUtil.isNotBlank(result.getErrorCode())) {
            throw new RuntimeException("获取所有App时出现异常：" + result.getMessage());
        }
        return result.getResult();
    }
}
