package com.mc.host;


import com.mc.manager.bus.env.info.current.HostInfo;
import com.vmware.vim25.mo.*;

import java.net.URL;

/**
 * 获取足迹CPU的
 *
 * @author Yang Jinkang
 * @date 2018-09-26 下午4:53
 **/
public class HostSystemCpu {
    private static final String URL = "https://192.168.10.100/sdk";
    private static final String USER_NAME = "administrator@vsphere.local";
    private static final String PASSWORD = "!QAZ2wsx";

    public static void main(String[] args) {
        try {
            ServiceInstance serviceInstance = new ServiceInstance(new URL(URL), USER_NAME, PASSWORD, true);
            Folder rootFolder = serviceInstance.getRootFolder();
            ManagedEntity[] mes = new InventoryNavigator(rootFolder).searchManagedEntities("HostSystem");

            HostInfo[] hostInfos = new HostInfo[mes.length];

            //逻辑判断
            if (mes == null || mes.length == 0) {
                serviceInstance.getServerConnection().logout();
            }

            for (int i = 0; i < mes.length; i++) {
                HostSystem hostSystem = (HostSystem) mes[i];
                System.out.println("HostSystem name:" + hostSystem.getName());
                Integer overallCpuUsage = hostSystem.getSummary().getQuickStats().overallCpuUsage;//使用量
                System.out.println("usage：" + overallCpuUsage + " Mhz");
                //hostSystem.getHardware().getCpuInfo().getNumCpuCores();
                long hz = hostSystem.getHardware().getCpuInfo().getHz();
                int numCpuCores = hostSystem.getHardware().getCpuInfo().getNumCpuCores();
                System.out.println("numOfCores:" + numCpuCores);
                float cpurate = ((float) overallCpuUsage * 1024 * 1024 / hz * numCpuCores) * 100;
                System.out.println("cpu usage rate:" + cpurate + "%\n");
                String cpuRate = String.valueOf(cpurate);


                hostInfos[i].setName(hostSystem.getName());
                hostInfos[i].setCpuUsageRate(cpuRate);
                //hostInfos[i].setCpuNums(numCpuCores);


            }
            serviceInstance.getServerConnection().logout();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
