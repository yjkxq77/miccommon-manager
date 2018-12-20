package com.mc.manager.bus.env.service.host;

import com.mc.manager.bus.env.info.current.HostInfo;
import com.mc.manager.bus.env.info.current.HostNameInfo;
import com.mc.manager.tool.kit.CommonalityKit;
import com.mc.manager.tool.kit.HostKit;
import com.vmware.vim25.mo.*;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * 物理接口实现类
 *
 * @author Yang jinkang
 * @date 2018/10/16.
 */
@Service
public class HostServiceImpl implements HostService {
    /**
     * 将HostSystem关键字定义为常量
     */
    private static final String HOST_SYSTEM = "HostSystem";


    /**
     * 获取所有主机信息
     *
     * @throws Exception
     */
    @Override
    public List<HostInfo> getAllHostInfos() throws Exception {
        ServiceInstance serviceInstance = CommonalityKit.getService();
        Folder rootFolder = serviceInstance.getRootFolder();
        ManagedEntity[] hostSystems = new InventoryNavigator(rootFolder).searchManagedEntities(HOST_SYSTEM);
        if (hostSystems == null || hostSystems.length == 0) {
            serviceInstance.getServerConnection().logout();
            return null;
        }
        List<HostInfo> list = new ArrayList<>(10);

        for (ManagedEntity hostSystem : hostSystems) {
            HostSystem hostSystem1 = (HostSystem) hostSystem;
            HostInfo hostInfo = HostKit.getHostInfo(hostSystem1);
            list.add(hostInfo);
        }

        return list;
    }

    /**
     * 根据主机名获取主机信息
     *
     * @param hostName
     * @throws Exception
     */
    @Override
    public HostInfo getHostInfoByHostName(String hostName) throws Exception {

        ServiceInstance serviceInstance = CommonalityKit.getService();
        Folder rootFolder = serviceInstance.getRootFolder();
        HostSystem hostSystem = (HostSystem) new InventoryNavigator(rootFolder).searchManagedEntity(HOST_SYSTEM, hostName);
        HostKit hostKit = new HostKit();
        HostInfo hostInfo = hostKit.getHostInfo(hostSystem);
        return hostInfo;

    }

    /**
     * 获取主机名称
     *
     * @return
     * @throws RemoteException
     */
    @Override
    public List<HostNameInfo> getHostNameInfo() throws RemoteException {
        ServiceInstance serviceInstance = CommonalityKit.getService();
        Folder rootFolder = serviceInstance.getRootFolder();
        ManagedEntity[] hostSystems = new InventoryNavigator(rootFolder).searchManagedEntities(HOST_SYSTEM);
        if (hostSystems == null || hostSystems.length == 0) {
            serviceInstance.getServerConnection().logout();
            return null;
        }
        List<HostNameInfo> list = new ArrayList<>(10);
        for (ManagedEntity hostSystem : hostSystems) {
            HostSystem hostSystem1 = (HostSystem) hostSystem;
            HostNameInfo hostNameInfo = HostKit.getHostNameInfo(hostSystem1);
            list.add(hostNameInfo);
        }
        return list;
    }
}
