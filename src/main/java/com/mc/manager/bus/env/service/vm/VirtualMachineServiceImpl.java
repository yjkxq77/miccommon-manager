package com.mc.manager.bus.env.service.vm;

import cn.hutool.core.util.ArrayUtil;
import com.mc.manager.bus.env.info.current.VirtualMachineInfo;
import com.mc.manager.tool.kit.CommonalityKit;
import com.mc.manager.tool.kit.VMKit;
import com.vmware.vim25.mo.*;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 虚拟机接口实现类
 *
 * @author Yang jinkang
 * @date 2018/10/16.
 */
@Service
public class VirtualMachineServiceImpl implements VirtualMachineService {
    /**
     * 将HostSystem关键字定义为常量
     */
    private static final String HOST_SYSTEM = "HostSystem";
    /**
     * 将关键字VirtualMachine定义为常量
     */
    private static final String VIRTUAL_MACHINE = "VirtualMachine";


    /**
     * 获取所有虚拟机信息
     *
     * @return
     * @throws Exception
     */
    @Override
    public List<VirtualMachineInfo> getAllVirtualMachineInfos() throws Exception {
        ServiceInstance serviceInstance = CommonalityKit.getService();
        Folder rootFolder = serviceInstance.getRootFolder();
        ManagedEntity[] hostSystems = new InventoryNavigator(rootFolder).searchManagedEntities(HOST_SYSTEM);
        if (hostSystems == null || hostSystems.length == 0) {
            serviceInstance.getServerConnection().logout();
            return null;
        }
        List<VirtualMachineInfo> list = new ArrayList<>(60);

        for (int i = 0; i < hostSystems.length; i++) {
            HostSystem hostSystem = (HostSystem) hostSystems[i];
            VirtualMachine[] virtualMachines = hostSystem.getVms();
            for (VirtualMachine virtualMachine : virtualMachines) {
                VirtualMachineInfo virtualMachineInfo = VMKit.getVMInfo(virtualMachine);
                list.add(virtualMachineInfo);
            }
        }
        return list;
    }

    /**
     * 根据 @param hostName  获取其下所有虚拟机信息
     *
     * @param hostName
     * @return
     * @throws Exception
     */
    @Override
    public List<VirtualMachineInfo> getVirtualMachineInfosByHostName(String hostName) throws Exception {
        ServiceInstance serviceInstance = CommonalityKit.getService();
        Folder rootFolder = serviceInstance.getRootFolder();
        HostSystem hostSystem = (HostSystem) new InventoryNavigator(rootFolder).searchManagedEntity(HOST_SYSTEM, hostName);
        if (hostName == null) {
            return new ArrayList<>();
        }
        VirtualMachine[] virtualMachines = hostSystem.getVms();
        if (virtualMachines == null || virtualMachines.length == 0) {
            serviceInstance.getServerConnection().logout();
            return null;
        }
        List<VirtualMachineInfo> list = new ArrayList<>(30);
        for (VirtualMachine virtualMachine : virtualMachines) {
            VirtualMachineInfo virtualMachineInfo = VMKit.getVMInfo(virtualMachine);
            list.add(virtualMachineInfo);
        }
        return list;
    }

    /**
     * 只获取主机下对应的虚拟机名称信息和ip信息
     *
     * @param hostName
     * @return
     * @throws Exception
     * @author Liu Chunfu
     */
    @Override
    public List<VirtualMachineInfo> getSimpleVmsInfoByHostName(String hostName) throws Exception {
        ServiceInstance serviceInstance = CommonalityKit.getService();
        Folder rootFolder = serviceInstance.getRootFolder();
        HostSystem hostSystem = (HostSystem) new InventoryNavigator(rootFolder).searchManagedEntity(HOST_SYSTEM, hostName);
        VirtualMachine[] virtualMachines = hostSystem.getVms();
        if (ArrayUtil.isEmpty(virtualMachines)) {
            return new ArrayList<>();
        }
        List<VirtualMachineInfo> result = Arrays.stream(virtualMachines).map(this::mapVmInfo).collect(Collectors.toList());
        return result;
    }


    private VirtualMachineInfo mapVmInfo(VirtualMachine vm) {
        VirtualMachineInfo info = new VirtualMachineInfo();
        info.setName(vm.getName());
        info.setIp(vm.getGuest().ipAddress);
        return info;
    }

    /**
     * 根据虚拟机名称获取虚拟机信息
     *
     * @param vmName
     * @throws Exception
     */
    @Override
    public VirtualMachineInfo getVirtualMachineInfoByVMName(String vmName) throws Exception {
        ServiceInstance serviceInstance = CommonalityKit.getService();
        Folder rootFolder = serviceInstance.getRootFolder();
        VirtualMachine virtualMachine = (VirtualMachine) new InventoryNavigator(rootFolder).searchManagedEntity(VIRTUAL_MACHINE, vmName);
        VirtualMachineInfo virtualMachineInfo = VMKit.getVMInfo(virtualMachine);
        return virtualMachineInfo;
    }

    /**
     * 关机
     *
     * @param vmName
     * @throws MalformedURLException
     * @throws RemoteException
     */
    @Override
    public String powerOff(String vmName) throws MalformedURLException, RemoteException {
        ServiceInstance serviceInstance = CommonalityKit.getService();
        Folder rootFolder = serviceInstance.getRootFolder();
        VirtualMachine virtualMachine = (VirtualMachine) new InventoryNavigator(rootFolder).searchManagedEntity(VIRTUAL_MACHINE, vmName);
        String result = "失败";
        if (virtualMachine != null) {
            virtualMachine.powerOffVM_Task();//关闭此虚拟机的电源
            result = ("成功");
        }

        return result;
    }

    /**
     * 开机
     *
     * @param vmName
     * @param hostName
     * @throws MalformedURLException
     * @throws RemoteException
     */
    @Override
    public String powerOn(String vmName, String hostName) throws MalformedURLException, RemoteException {
        ServiceInstance serviceInstance = CommonalityKit.getService();
        Folder rootFolder = serviceInstance.getRootFolder();
        VirtualMachine virtualMachine = (VirtualMachine) new InventoryNavigator(rootFolder).searchManagedEntity(VIRTUAL_MACHINE, vmName);
        HostSystem hostSystem = (HostSystem) new InventoryNavigator(rootFolder).searchManagedEntity(HOST_SYSTEM, hostName);
        String result = "失败";
        if (virtualMachine != null) {
            virtualMachine.powerOnVM_Task(hostSystem);
            result = ("成功");
        }

        return result;
    }

    /**
     * 重启
     *
     * @param vmName
     * @throws MalformedURLException
     * @throws RemoteException
     */
    @Override
    public String reset(String vmName) throws MalformedURLException, RemoteException {
        ServiceInstance serviceInstance = CommonalityKit.getService();
        Folder rootFolder = serviceInstance.getRootFolder();
        VirtualMachine virtualMachine = (VirtualMachine) new InventoryNavigator(rootFolder).searchManagedEntity(VIRTUAL_MACHINE, vmName);
        String result = "失败";
        if (virtualMachine != null) {
            virtualMachine.resetVM_Task();
            result = ("成功");
        }

        return result;
    }

    /**
     * 暂停
     *
     * @param vmName
     * @throws MalformedURLException
     * @throws RemoteException
     */
    @Override
    public String suspend(String vmName) throws MalformedURLException, RemoteException {
        ServiceInstance serviceInstance = CommonalityKit.getService();
        Folder rootFolder = serviceInstance.getRootFolder();
        VirtualMachine virtualMachine = (VirtualMachine) new InventoryNavigator(rootFolder).searchManagedEntity(VIRTUAL_MACHINE, vmName);
        String result = "失败";
        if (virtualMachine != null) {
            virtualMachine.suspendVM_Task();//暂停此虚拟机的执行
            result = ("成功");
        }
        return result;
    }
}
