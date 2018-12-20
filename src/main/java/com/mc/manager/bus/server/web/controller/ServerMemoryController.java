package com.mc.manager.bus.server.web.controller;

import com.mc.manager.bus.server.service.ServerMemoryService;
import com.mc.manager.frame.dto.ExecuteResult;
import com.mc.manager.bus.server.info.ServerMemoryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.RemoteException;

/**
 * @author Yang jinkang
 * @date 2018/11/19 10:51
 */
@RestController
@RequestMapping("/mic/manager/server/memory")
public class ServerMemoryController {
    /**
     * 注入serverMemoryService
     */
    @Autowired
    private ServerMemoryService serverMemoryService;

    /**
     * 获取服务器内存信息
     *
     * @param vmName
     * @return
     * @throws RemoteException
     */
    @GetMapping
    public ExecuteResult<ServerMemoryInfo> getServerMemoryInfo(@RequestParam("vmName") String vmName) throws RemoteException {
        return new ExecuteResult<>(serverMemoryService.getServerMemoryinfo(vmName));
    }
}
