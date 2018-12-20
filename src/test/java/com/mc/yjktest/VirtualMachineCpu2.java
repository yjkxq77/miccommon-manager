package com.mc.yjktest;


import com.mc.manager.tool.kit.VMKit;
import com.vmware.vim25.PerfMetricSeriesCSV;
import com.vmware.vim25.mo.*;

import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2018/9/19.
 */
public class VirtualMachineCpu2 {
    private static final String URL = "https://192.168.10.100/sdk";
    private static final String USER_NAME = "administrator@vsphere.local";
    private static final String PASSWORD = "!QAZ2wsx";


    public static void main(String[] args) {
        try {
            ServiceInstance serviceInstance = new ServiceInstance(new URL(URL), USER_NAME, PASSWORD, true);
            Folder rootFolder = serviceInstance.getRootFolder();
            ManagedEntity[] mes = new InventoryNavigator(rootFolder).searchManagedEntities("HostSystem");

            if (mes.length > 0) {
                for (int j = 0; j < mes.length; j++) {
                    HostSystem systems = (HostSystem) mes[j];
                    //物理服务器所关联的虚拟机
                    VirtualMachine[] virtualMachines = systems.getVms();

                    if (virtualMachines.length > 0) {
                        for (int i = 0; i < virtualMachines.length; i++) {
                            System.out.println("virtualMacheines name is:" + virtualMachines[i].getName());
                            VirtualMachine virtualMachine = virtualMachines[i];
                            int numCPU = virtualMachine.getConfig().getHardware().numCPU;

                            int bootTime = virtualMachine.getRuntime().bootTime.get(Calendar.SECOND);
                            System.out.println("bootTime:" + bootTime);
                            List netPerList = null;
                            netPerList = VMKit.getVMNet(virtualMachines[i].getName(), 20, "");
                            for (int k = 0; k < netPerList.size(); k++) {
                                Map m = (Map) netPerList.get(k);
                                String point = (String) m.get("point");
                                PerfMetricSeriesCSV[] csvs = (PerfMetricSeriesCSV[]) m.get("csvs");
                                System.out.println("+++++++++++++++++++++++++");
                                if (point.equals("bytesRx")) {
                                    PerfMetricSeriesCSV _value = csvs[0];
                                    int rxper = Integer.parseInt(_value.getValue());
                                    int rxtotal = rxper * bootTime;
                                    System.out.println("接收速率：" + _value.getValue() + " kiloBytesPerSecond" + " KB/S");
                                    System.out.println("接收总量：" + rxtotal + " KB");
                                } else if (point.equals("bytesTx")) {
                                    PerfMetricSeriesCSV _value = csvs[0];
                                    System.out.println("传输速率：" + _value.getValue() + " kiloBytesPerSecond" + " KB/S");
                                } else if (point.equals("received")) {
                                    PerfMetricSeriesCSV _value = csvs[0];
                                    System.out.println("带宽：" + _value.getValue() + " kiloBytesPerSecond" + " KB/S");
                                } else if (point.equals("usage")) {
                                    PerfMetricSeriesCSV _value = csvs[0];
                                    System.out.println("网络利用率：" + _value.getValue() + " kiloBytesPerSecond");
                                }
                            }
                            System.out.println("______________________----------------");
                        }
                    }
                }

            }
            serviceInstance.getServerConnection().logout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
