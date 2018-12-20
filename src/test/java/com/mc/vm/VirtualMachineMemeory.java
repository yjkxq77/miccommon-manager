package com.mc.vm;

import com.vmware.vim25.mo.*;

import java.net.URL;

/**
 * Created by user on 2018/9/20.
 */
public class VirtualMachineMemeory {
    private static final String URL = "https://192.168.10.100/sdk";
    private static final String USER_NAME = "administrator@vsphere.local";
    private static final String PASSWORD = "!QAZ2wsx";
    public static  void main(String[] args){
        try {
            ServiceInstance serviceInstance = new ServiceInstance(new URL(URL), USER_NAME, PASSWORD, true);
            Folder rootFolder = serviceInstance.getRootFolder();
            ManagedEntity[] mes = new InventoryNavigator(rootFolder).searchManagedEntities("HostSystem");

            if(mes.length>0){
                for (int j=0;j<mes.length;j++) {
                    HostSystem systems = (HostSystem) mes[j];
                    //物理服务器所关联的虚拟机
                    VirtualMachine[] virtualMachines = systems.getVms();

                    if (virtualMachines.length > 0) {
                        for (int i = 0; i < virtualMachines.length; i++) {
                            System.out.println("virtualMacheines name is:" + virtualMachines[i].getName());
                            VirtualMachine virtualMachine = virtualMachines[i];

                            String ip = virtualMachine.getGuest().getIpAddress();
                            double memorySize = (double) virtualMachine.getConfig().getHardware().getMemoryMB(); //内存总容量
                            double overallMemoryUsage = (double)virtualMachine.getSummary().quickStats.guestMemoryUsage; //内存使用容量(MB)
                            float per = (float) (overallMemoryUsage / memorySize);

                            System.out.println("IP:"+ip);
                            System.out.println(virtualMachines[i].getName() + "Memory total:" + memorySize + " MB");
                            System.out.println(virtualMachines[i].getName() + "Memory usage:" + overallMemoryUsage + " MB");
                            System.out.println(virtualMachines[i].getName() + "Usage rate:" + per + "\n");


                        }
                    }
                }
            }
            serviceInstance.getServerConnection().logout();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
