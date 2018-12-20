package com.mc.vm;


import com.vmware.vim25.VirtualMachineStorageSummary;
import com.vmware.vim25.VirtualMachineSummary;
import com.vmware.vim25.mo.*;

import java.net.URL;

/**
 * Created by user on 2018/9/20.
 */
public class VirtualMachineDisk {
    private static final String URL = "https://192.168.10.100/sdk";
    private static final String USER_NAME = "administrator@vsphere.local";
    private static final String PASSWORD = "!QAZ2wsx";
    public static void main(String[] args){
        try{

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
                                if (virtualMachine != null) {
                                    VirtualMachineSummary virtualMachineSummary = virtualMachine.getSummary();
                                    VirtualMachineStorageSummary virtualMachineStorageSummary = virtualMachineSummary.storage;

                                    long commit = (long) virtualMachineStorageSummary.committed/1024;//已用容量
                                    System.out.println("virtual disk committed:" + commit + " byte");
                                    long unshared = virtualMachineStorageSummary.unshared/1024;
                                    System.out.println("virtual disk unshared:" + unshared + " byte");
                                    double uncommit = (double) virtualMachineStorageSummary.uncommitted/1024;//空闲容量
                                    System.out.println("virtual disk uncommit:" + uncommit + " byte" + "\n");

                                    double disktotal = commit+uncommit;
                                    double diskRate = (double)commit/disktotal;

                                    System.out.println("total:"+disktotal);
                                    System.out.println("rate:"+diskRate);

                                } else {
                                    serviceInstance.getServerConnection().logout();
                                }

                            }
                        }
                    }
                }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
