package com.mc.manager.bus.server.service;

import com.mc.manager.bus.env.service.vm.VirtualMachineService;
import com.mc.manager.bus.server.info.PageResultInfo;
import com.mc.manager.bus.server.info.ServerOverviewInfo;
import com.mc.manager.tool.kit.CommonalityKit;
import com.mc.manager.tool.kit.ServerKit;
import com.mc.manager.tool.kit.VMKit;
import com.vmware.vim25.mo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.mc.manager.bus.common.BusConstant.HOST_SYSTEM;

/**
 * 获取服务器概要信息接口实现类
 *
 * @author Yang jinkang
 * @date 2018/11/19 9:40
 */
@Service
public class ServerOverviewService {

    @Autowired
    private VirtualMachineService virtualMachineService;

    /**
     * 通过分页方式返回服务器概要信息
     *
     * @param pageSize
     * @param pageNo
     * @return
     */
    public PageResultInfo getServerOverview(Integer pageSize, Integer pageNo) {
        int offset = pageSize * (pageNo - 1);
        List<VirtualMachine> virtualMachines = null;
        Integer total = null;
        try {
            virtualMachines = VMKit.getAllVM();
            total = virtualMachines.size();
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new RuntimeException("获取虚拟机时发生异常，可能是远程连接异常！", e);
        }
        List<ServerOverviewInfo> result = virtualMachines.stream().map(this::overviewInfo).filter(Objects::nonNull).skip(offset).limit(pageSize).collect(Collectors.toList());
        PageResultInfo pageResultInfo = new PageResultInfo();
        pageResultInfo.setServerOverviewInfos(result);
        pageResultInfo.setTotal(total);
        pageResultInfo.setPageNo(pageNo);
        pageResultInfo.setPageSize(pageSize);
        return pageResultInfo;
    }

    /**
     * 获取所有概要信息的方法
     *
     * @return
     * @throws Exception
     */
    public List<ServerOverviewInfo> getServerOverview() throws Exception {
        ServiceInstance serviceInstance = CommonalityKit.getService();
        Folder rootFolder = serviceInstance.getRootFolder();
        ManagedEntity[] hostSystems = new InventoryNavigator(rootFolder).searchManagedEntities(HOST_SYSTEM);
        if (hostSystems == null || hostSystems.length == 0) {
            serviceInstance.getServerConnection().logout();
            return null;
        }
        List<ServerOverviewInfo> list = new ArrayList<>(60);

        for (int i = 0; i < hostSystems.length; i++) {
            HostSystem hostSystem = (HostSystem) hostSystems[i];
            VirtualMachine[] virtualMachines = hostSystem.getVms();
            for (VirtualMachine virtualMachine : virtualMachines) {
                ServerOverviewInfo serverOverviewInfo = ServerKit.getServerOverview(virtualMachine);
                list.add(serverOverviewInfo);
            }
        }
        return list;
    }


    /**
     * 概述的视图概览
     *
     * @param virtualMachine
     * @return
     */
    private ServerOverviewInfo overviewInfo(VirtualMachine virtualMachine) {
        try {
            return ServerKit.getServerOverview(virtualMachine);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
