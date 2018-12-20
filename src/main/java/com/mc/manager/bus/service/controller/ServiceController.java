package com.mc.manager.bus.service.controller;

import com.mc.manager.bus.service.info.AppInfo;
import com.mc.manager.bus.service.info.ServiceItemInfo;
import com.mc.manager.bus.service.service.ServiceService;
import com.mc.manager.frame.dto.ExecuteResult;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Appinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * 服务控制器
 *
 * @author Liu Chunfu
 * @date 2018-11-16 下午5:22
 **/
@RestController
@RequestMapping("/mic/manager/service")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    /**
     * 获取所有的apps
     *
     * @return
     */
    @GetMapping("/apps")
    public ExecuteResult<Collection<AppInfo>> getAllApps() {
        return new ExecuteResult<>(serviceService.appinfos());
    }


    /**
     * 通过ip对应主机上的应用信息
     *
     * @param ip ip地址
     * @return 应用信息
     */
    @GetMapping("/apps/ip")
    public ExecuteResult<Collection<AppInfo>> getAppsByIp(@RequestParam("ip") String ip) {
        return new ExecuteResult(serviceService.appsByIp(ip));
    }


    /**
     * 通过appName获取服务信息
     *
     * @param appName 应用名称
     * @return 服务单元
     */
    @GetMapping("/apps/appName")
    public ExecuteResult<Collection<ServiceItemInfo>> getServiceItemsByAppName(@RequestParam("appName") String appName) {
        return new ExecuteResult<>(serviceService.serviceItemInfosByAppName(appName));
    }
}
