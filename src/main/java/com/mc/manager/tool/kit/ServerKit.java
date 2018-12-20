package com.mc.manager.tool.kit;

import cn.hutool.core.convert.Convert;
import com.mc.manager.bus.server.info.ServerCpuInfo;
import com.mc.manager.bus.server.info.ServerMemoryInfo;
import com.mc.manager.bus.server.info.ServerOverviewInfo;
import com.vmware.vim25.PerfMetricSeriesCSV;
import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

import java.util.List;
import java.util.Map;

/**
 * @author Yang jinkang
 * @date 2018/11/19 10:34
 */
public class ServerKit {


    private ServerKit() {
    }

    /**
     * 获取服务器概述信息
     *
     * @param virtualMachine
     * @return
     * @throws Exception
     */
    public static ServerOverviewInfo getServerOverview(VirtualMachine virtualMachine) throws Exception {
        ServerOverviewInfo serverOverviewInfo = new ServerOverviewInfo();
        String name = virtualMachine.getName();
        List cpuPerList = VMKit.getVMCPUPerByTimeRange(virtualMachine.getName(), CommonalityKit.getService(),20, "");
        for (int s = 0; s < cpuPerList.size(); s++) {
            Map m = (Map) cpuPerList.get(s);
            String point = (String) m.get("point");
            PerfMetricSeriesCSV[] csvs = (PerfMetricSeriesCSV[]) m.get("csvs");
            Float f = 0f;
            if (point.equals("usage")) {
                PerfMetricSeriesCSV _value = csvs[0];
                int mid1 = Integer.parseInt(_value.getValue()) + 1;
                String value1 = String.valueOf(mid1);
                f = Float.valueOf(value1) / 100f;
                String cpuRate = Convert.toStr(f);
                serverOverviewInfo.setServerCpuUsageRate(cpuRate);
            }
            VirtualMachinePowerState statusEnum = virtualMachine.getRuntime().powerState;
            String statusStr = "正常";
            if (statusEnum.equals(VirtualMachinePowerState.poweredOff)
                    || statusEnum.equals(VirtualMachinePowerState.suspended)) {
                statusStr = "离线";
            } else if (f > 70F) {
                statusStr = "告警";
            }
            serverOverviewInfo.setServerStatus(statusStr);
        }

        double memorySize = (double) virtualMachine.getConfig().getHardware().getMemoryMB(); //内存总容量（MB）
        double overallMemoryUsage = (double) virtualMachine.getSummary().quickStats.guestMemoryUsage; //内存使用容量(MB)
        float memoryrate = (float) (overallMemoryUsage / memorySize) * 100;
        float memoryrate1 = (float) (Math.round(memoryrate * 100)) / 100;

        String ip = virtualMachine.getGuest().getIpAddress();
        String memoryRate = Convert.toStr(memoryrate1);

        serverOverviewInfo.setServerName(name);
        serverOverviewInfo.setServerIp(ip);
        serverOverviewInfo.setServerMemoryUsageRate(memoryRate);

        return serverOverviewInfo;
    }

    /**
     * 获取服务器内存使用率
     *
     * @param virtualMachine
     * @return
     */
    public static ServerMemoryInfo getServerMemoryInfo(VirtualMachine virtualMachine) {
        ServiceInstance serviceInstance = CommonalityKit.getService();
        ServerMemoryInfo serverMemoryInfo = new ServerMemoryInfo();
        String name = virtualMachine.getName();

        double memorySize = (double) virtualMachine.getConfig().getHardware().getMemoryMB(); //内存总容量（MB）
        double overallMemoryUsage = (double) virtualMachine.getSummary().quickStats.guestMemoryUsage; //内存使用容量(MB)
        float memoryrate = (float) (overallMemoryUsage / memorySize) * 100;
        float memoryrate1 = (float) (Math.round(memoryrate * 100)) / 100;
        float memoryUnusedRate1 = 100f - memoryrate1;

        String memoryRate = Convert.toStr(memoryrate1);
        String memoryUnysedRate = Convert.toStr(memoryUnusedRate1);

        serverMemoryInfo.setServerName(name);
        serverMemoryInfo.setServerMemoryUsageRate(memoryRate);
        serverMemoryInfo.setServerMemoryUnusedRate(memoryUnysedRate);

        return serverMemoryInfo;
    }

    /**
     * 获取服务器Cpu信息
     *
     * @param virtualMachine
     * @return
     * @throws Exception
     */
    public static ServerCpuInfo getServerCpuInfo(VirtualMachine virtualMachine) {
        ServerCpuInfo serverCpuInfo = new ServerCpuInfo();
        String name = virtualMachine.getName();

        List cpuPerList = null;
        try {
            cpuPerList = VMKit.getVMCPUPerByTimeRange(virtualMachine.getName(), CommonalityKit.getService(),20, "");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        for (int s = 0; s < cpuPerList.size(); s++) {
            Map m = (Map) cpuPerList.get(s);
            String point = (String) m.get("point");
            PerfMetricSeriesCSV[] csvs = (PerfMetricSeriesCSV[]) m.get("csvs");
            if (point.equals("usage")) {
                PerfMetricSeriesCSV _value = csvs[0];
                int mid1 = Integer.parseInt(_value.getValue()) + 1;
                String value1 = String.valueOf(mid1);
                Float f = Float.valueOf(value1) / 100f;
                String cpuRate = Convert.toStr(f);
                serverCpuInfo.setCpuUsageRate(cpuRate);
            } else if (point.equals("usagemhz")) {
                PerfMetricSeriesCSV _value = csvs[0];
            }
        }
        serverCpuInfo.setServerName(name);


        return serverCpuInfo;
    }
}
