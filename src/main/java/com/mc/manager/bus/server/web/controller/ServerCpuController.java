package com.mc.manager.bus.server.web.controller;

import com.mc.manager.bus.server.info.ServerCpuInfo;
import com.mc.manager.bus.server.service.ServerCpuService;
import com.mc.manager.frame.dto.ExecuteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yang jinkang
 * @Description:
 * @date 2018/11/19 10:50
 */
@RestController
@RequestMapping("/mic/manager/server/cpu")
public class ServerCpuController {

    /**
     * 注入ServerCpuService
     */
    @Autowired
    private ServerCpuService serverCpuService;

    /**
     * 通过服务器名称获取服务器cpu使用率
     *
     * @param vmName
     * @return
     * @throws Exception
     */
    @GetMapping
    public ExecuteResult<ServerCpuInfo> getServerCpuInfo(@RequestParam("vmName") String vmName) throws Exception {
        return new ExecuteResult<>(serverCpuService.getServerCpuInfo(vmName));
    }
}
