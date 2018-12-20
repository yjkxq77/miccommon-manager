package com.mc.vm;

import com.mc.manager.tool.kit.CommonalityKit;
import com.mc.manager.tool.kit.VMKit;
import com.vmware.vim25.PerfMetricSeriesCSV;
import com.vmware.vim25.mo.*;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2018/9/19.
 */
public class VirtualMachineCpu {
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

                            List cpuPerList = null;
                            cpuPerList = VMKit.getVMCPUPerByTimeRange(virtualMachines[i].getName(), CommonalityKit.getService(), 20, "");

                            System.out.println("numCpu:" + numCPU);
                            for (int s = 0; s < cpuPerList.size(); s++) {
                                Map m = (Map) cpuPerList.get(s);
                                String point = (String) m.get("point");
                                PerfMetricSeriesCSV[] csvs = (PerfMetricSeriesCSV[]) m.get("csvs");
                                if (point.equals("usage")) {
                                    PerfMetricSeriesCSV _value = csvs[0];
                                    Float f = Float.valueOf(_value.getValue()) / 100f;
                                    System.out.println("usage rate:" + f + "\n");
                                } else if (point.equals("usagemhz")) {
                                    PerfMetricSeriesCSV _value = csvs[0];
                                    System.out.println("usagemhz:" + _value.getValue() + " MHz");
                                }
                            }
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
