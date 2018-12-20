package com.mc.manager.bus.service.service;

import com.mc.manager.bus.service.info.AppInfo;
import com.mc.manager.bus.service.info.ServiceItemInfo;
import com.mc.manager.frame.dto.ExecuteResult;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.Collection;

/**
 * 服务方法的Feign接口
 *
 * @author Liu Chunfu
 * @create 2018-11-16 下午5:28
 **/
public interface ServiceFetchApi {


    /**
     * 第一层入口，获取服务注册中心的所有应用
     *
     * @return 服务注册中心的应用
     */
    @RequestLine("GET /mic/third-party/apps")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    ExecuteResult<Collection<AppInfo>> fetchAppInfos();


    /**
     * 根据Ip获取服务注册中心的应用
     *
     * @param ip ip地址
     * @return ip对应服务器所在的应用
     */
    @RequestLine("GET /mic/third-party/apps/ip?ip={ip}")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    ExecuteResult<Collection<AppInfo>> fetchAppInfosByIp(@Param("ip") String ip);


    /**
     * 通过应用名称找到所属的服务单元
     *
     * @param appName 应用名称
     * @return 服务单元集合
     */
    @RequestLine("GET /mic/third-party/service?appName={appName}")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    ExecuteResult<Collection<ServiceItemInfo>> fetchServiceItemByAppName(@Param("appName") String appName);

}
