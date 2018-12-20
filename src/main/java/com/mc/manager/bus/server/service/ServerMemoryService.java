package com.mc.manager.bus.server.service;

import com.mc.manager.bus.server.info.ServerMemoryInfo;
import com.mc.manager.tool.kit.CommonalityKit;
import com.mc.manager.tool.kit.ServerKit;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;

import static com.mc.manager.bus.common.BusConstant.VIRTUAL_MACHINE;

/**
 * 获取服务器内存使用率接口实现类
 *
 * @author Yang Jinkang
 * @date 2018/11/19 10:56
 */
@Service
public class ServerMemoryService {

    /**
     * 获取服务器内存信息
     *
     * @param vmName
     * @return
     * @throws RemoteException
     */
    public ServerMemoryInfo getServerMemoryinfo(String vmName) throws RemoteException {
        ServiceInstance serviceInstance = CommonalityKit.getService();
        Folder rootFolder = serviceInstance.getRootFolder();
        VirtualMachine virtualMachine = (VirtualMachine) new InventoryNavigator(rootFolder).searchManagedEntity(VIRTUAL_MACHINE, vmName);
        ServerMemoryInfo serverMemoryInfo = ServerKit.getServerMemoryInfo(virtualMachine);
        return serverMemoryInfo;
    }
}
