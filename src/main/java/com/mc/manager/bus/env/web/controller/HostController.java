package com.mc.manager.bus.env.web.controller;

import com.mc.manager.bus.env.info.current.HostInfo;
import com.mc.manager.bus.env.info.current.HostNameInfo;
import com.mc.manager.bus.env.info.current.VirtualMachineInfo;
import com.mc.manager.bus.env.service.host.HostService;
import com.mc.manager.bus.env.service.vm.VirtualMachineService;
import com.mc.manager.frame.dto.ExecuteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.RemoteException;
import java.util.List;

/**
 * 主机相关的接口
 *
 * @author Liu Chunfu
 * @date 2018-10-16 下午2:34
 **/
@RequestMapping("/mic/manager/host")
@RestController
public class HostController {
    /**
     * 注入HostService
     */
    @Autowired
    private HostService hostService;

    @Autowired
    private VirtualMachineService virtualMachineService;


    /**
     * 获取所有的主机信息
     *
     * @throws Exception
     */
    @GetMapping("/all")
    public ExecuteResult<List<HostInfo>> allHostinfos() throws Exception {
        ExecuteResult<List<HostInfo>> result = new ExecuteResult<>(hostService.getAllHostInfos());
        return result;
    }

    /**
     * 根据主机名称获取主机信息
     *
     * @param hostName
     * @throws Exception
     */
    @GetMapping
    public ExecuteResult<HostInfo> hostInfoByHostName(@RequestParam("hostName") String hostName) throws Exception {
        ExecuteResult<HostInfo> result = new ExecuteResult<>(hostService.getHostInfoByHostName(hostName));
        return result;
    }

    /**
     * 根据{@arg hostName} 获取该主机对应的所有虚拟机信息
     *
     * @param hostName 主机名称
     * @throws Exception
     */
    @GetMapping("vm")
    public ExecuteResult<List<VirtualMachineInfo>> virtualMachineInfosOfHost(@RequestParam("hostName") String hostName) throws Exception {
        long t1 = System.currentTimeMillis();
        ExecuteResult<List<VirtualMachineInfo>> result = new ExecuteResult<>(virtualMachineService.getVirtualMachineInfosByHostName(hostName));
        long t2 = System.currentTimeMillis();
        System.err.println("复杂模式耗时："+(t2-t1));
        return result;
    }

    @GetMapping("vm-simple")
    public ExecuteResult<List<VirtualMachineInfo>> virtualMachineInfosOfHostSimple(@RequestParam("hostName") String hostName) throws Exception {
        long t1 = System.currentTimeMillis();
        ExecuteResult<List<VirtualMachineInfo>> result = new ExecuteResult<>(virtualMachineService.getSimpleVmsInfoByHostName(hostName));
        long t2 = System.currentTimeMillis();
        System.err.println("简单模式耗时："+(t2-t1));
        return result;
    }


    /**
     * 获取主机名称
     *
     * @return
     * @throws RemoteException
     */
    @GetMapping("hostName")
    public ExecuteResult<List<HostNameInfo>> allHostName() throws RemoteException {
        return new ExecuteResult<>(hostService.getHostNameInfo());
    }

}
