package com.mc.manager.bus.env.service.vm;

import com.mc.manager.bus.env.info.current.VirtualMachineInfo;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.List;

/**
 * 虚拟机接口
 *
 * @author Yang jinkang
 * @date 2018/10/16.
 */
public interface VirtualMachineService {
    /**
     * 获取所有虚拟机信息
     *
     * @throws Exception
     */
    List<VirtualMachineInfo> getAllVirtualMachineInfos() throws Exception;

    /**
     * 根据主机名称获取该主机下面所以虚拟机信息
     *
     * @param hostName
     * @throws Exception
     */
    List<VirtualMachineInfo> getVirtualMachineInfosByHostName(String hostName) throws Exception;


    /**
     * 获取简单的主机信息
     *
     * @param hostName
     * @return
     * @throws Exception
     * @author Liu Chunfu
     */
    List<VirtualMachineInfo> getSimpleVmsInfoByHostName(String hostName) throws Exception;

    /**
     * 根据虚拟机名称获取该虚拟机信息
     *
     * @param vmName
     * @throws Exception
     */
    VirtualMachineInfo getVirtualMachineInfoByVMName(String vmName) throws Exception;

    /**
     * 根据虚拟机名称关闭该虚拟机电源
     *
     * @param vmName
     * @throws MalformedURLException
     * @throws RemoteException
     */
    String powerOff(String vmName) throws MalformedURLException, RemoteException;

    /**
     * 根据虚拟机名称开启虚拟机电源
     *
     * @param vmName
     * @param hostName
     * @throws MalformedURLException
     * @throws RemoteException
     */
    String powerOn(String vmName, String hostName) throws MalformedURLException, RemoteException;

    /**
     * 根据虚拟机名称重启虚拟机电源
     *
     * @param vmName
     * @throws MalformedURLException
     * @throws RemoteException
     */
    String reset(String vmName) throws MalformedURLException, RemoteException;

    /**
     * 根据虚拟机名称暂停虚拟机使用
     *
     * @param vmName
     * @throws MalformedURLException
     * @throws RemoteException
     */
    String suspend(String vmName) throws MalformedURLException, RemoteException;


}
