package com.mc.manager.bus.server.service;

import com.mc.manager.bus.server.info.ServerCpuInfo;
import com.mc.manager.tool.kit.CommonalityKit;
import com.mc.manager.tool.kit.ServerKit;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;
import org.springframework.stereotype.Service;

import static com.mc.manager.bus.common.BusConstant.VIRTUAL_MACHINE;

/**
 * 获取服务器cpu使用率接口实现类
 *
 * @author Yang jinkang
 * @date 2018/11/19 11:21
 */
@Service
public class ServerCpuService {


    /**
     * 获取Cpu使用率
     *
     * @param vmName
     * @return
     * @throws Exception
     */
    public ServerCpuInfo getServerCpuInfo(String vmName) throws Exception {
        ServiceInstance serviceInstance = CommonalityKit.getService();
        Folder rootFolder = serviceInstance.getRootFolder();
        VirtualMachine virtualMachine = (VirtualMachine) new InventoryNavigator(rootFolder).searchManagedEntity(VIRTUAL_MACHINE, vmName);
        ServerCpuInfo serverCpuInfo = ServerKit.getServerCpuInfo(virtualMachine);
        return serverCpuInfo;
    }
}
