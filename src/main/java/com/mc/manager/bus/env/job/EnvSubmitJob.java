package com.mc.manager.bus.env.job;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mc.manager.bus.env.info.current.EnvWrapper;
import com.mc.manager.bus.env.info.current.HostInfo;
import com.mc.manager.bus.env.info.current.ServiceEnvInfo;
import com.mc.manager.bus.env.info.current.VirtualMachineInfo;
import com.mc.manager.bus.env.service.runtime.RuntimeService;
import com.mc.manager.tool.info.MultiSetValueMap;
import com.mc.manager.tool.kit.CommonalityKit;
import com.mc.manager.tool.kit.EnvSendKit;
import com.mc.manager.tool.kit.HostKit;
import com.mc.manager.tool.kit.VMKit;
import com.vmware.vim25.mo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 环境变量上报任务
 *
 * @author Liu Chunfu
 * @date 2018-09-26 下午4:02
 **/
@Slf4j
@Component
public class EnvSubmitJob {

    private int port = 28899;

    /**
     * 主题
     */
    private static final String topic = "vm_env_service";

    /**
     * 运行信息
     */
    @Autowired
    private RuntimeService runtimeService;

    /**
     * 定时发送消息
     */
    @Scheduled(fixedDelay = 10000, initialDelay = 5000)
    public void envSubmit() throws Exception {
        doEnvSubmit();
    }

    private void doEnvSubmit() {
        EnvWrapper wrapper = new EnvWrapper();
        try {
            fillHostAndVm(wrapper);
        } catch (Exception e) {
            log.error("获取虚拟机时候发生异常，信息为：{}", e.getMessage());
            e.printStackTrace();
        }
        Optional.ofNullable(wrapper.getHostInfo()).orElse(new ArrayList<>()).stream().forEach(hostInfo -> {
            Collection<VirtualMachineInfo> virList = hostInfo.getVirtualMachineInfos();
            fillServiceEnv(virList);
        });
        wrapper.setCurTimestamp(System.currentTimeMillis());
        EnvSendKit.send(topic, port, wrapper);
    }


    /**
     * 填充host参数
     *
     * @param envWrapper
     */
    private void fillHostAndVm(EnvWrapper envWrapper) throws Exception {
        List<HostInfo> hostInfoList = analysisHostInfoList();
        envWrapper.setHostInfo(hostInfoList);
    }

    /**
     * 获取hostInfo的list信息，同时解析虚拟机信息
     *
     * @return
     * @throws Exception
     */
    private List<HostInfo> analysisHostInfoList() throws Exception {
        ManagedEntity[] hosts = connection();
        if (ArrayUtil.isEmpty(hosts)) {
            return new ArrayList<>();
        }
        List<HostInfo> hostInfoList = new ArrayList<>(20);
        for (ManagedEntity host : hosts) {
            HostSystem hostSystem = (HostSystem) host;
            HostInfo hostInfo = fillHostInfo(hostSystem);
            hostInfoList.add(hostInfo);
        }
        return hostInfoList;
    }

    /**
     * 填充一个hostInfo信息
     *
     * @param hostSystem 原始的hostSytem
     * @return hostInfo信息
     */
    private HostInfo fillHostInfo(HostSystem hostSystem) throws Exception {
        HostKit hostKit = new HostKit();
        HostInfo hostInfo = hostKit.getHostInfo(hostSystem);
        //lcf add 解析虚拟机
        List<VirtualMachineInfo> virtualMachineInfos = analysisVirtualMachines(hostSystem);
        hostInfo.setVirtualMachineInfos(virtualMachineInfos);
        return hostInfo;
    }


    /**
     * 解析host下的虚拟机信息
     *
     * @param hostSystem host主机
     * @return host下的虚拟机
     * @throws Exception 异常信息
     */
    private List<VirtualMachineInfo> analysisVirtualMachines(HostSystem hostSystem) throws Exception {
        VirtualMachine[] virtualMachines = hostSystem.getVms();
        if (virtualMachines == null || virtualMachines.length == 0) {
            return null;
        }
        List<VirtualMachineInfo> list = new ArrayList<>(30);

        for (VirtualMachine virtualMachine : virtualMachines) {
            VirtualMachineInfo virtualMachineInfo = VMKit.getVMInfo(virtualMachine);
            if (virtualMachine.getResourcePool() == null) {
                continue;
            }
            list.add(virtualMachineInfo);
        }
        return list;
    }

    /**
     * connection
     */
    private ManagedEntity[] connection() throws Exception {
        ServiceInstance serviceInstance = CommonalityKit.getService();

        Folder rootFolder = serviceInstance.getRootFolder();
        ManagedEntity[] mes = new InventoryNavigator(rootFolder).searchManagedEntities("HostSystem");
        if (mes == null || mes.length == 0) {
            serviceInstance.getServerConnection().logout();
            return null;
        }
        return mes;
    }


    /**
     * 填充环境参数
     *
     * @param vmInfos 虚拟机信息
     */
    private void fillServiceEnv(Collection<VirtualMachineInfo> vmInfos) {
        if (CollectionUtil.isEmpty(vmInfos)) {
            return;
        }
        MultiSetValueMap<String, ServiceEnvInfo> envInfoMap = runtimeService.obtainAllRuntimes();
        for (VirtualMachineInfo vmInfo : vmInfos) {
            String ip = vmInfo.getIp();
            if (StrUtil.isBlank(ip)) {
                //暂时如此处理
                log.warn("当前ip为null,电源状态为：" + vmInfo.getStatus() + " 内容：" + JSONUtil.toJsonStr(vmInfo));
                continue;
            }

            Set<ServiceEnvInfo> serviceEnvInfos = envInfoMap.valueSet(ip);
            if (serviceEnvInfos != null) {
                List<ServiceEnvInfo> serviceEnvInfos1 = new ArrayList<>(serviceEnvInfos);
                vmInfo.setEnvInfos(serviceEnvInfos1);
            }

        }
    }

}
