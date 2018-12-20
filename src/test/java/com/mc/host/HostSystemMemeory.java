package com.mc.host;

import com.vmware.vim25.HostListSummary;
import com.vmware.vim25.mo.*;

import java.net.URL;

/**
 */
public class HostSystemMemeory {
    private static final String URL = "https://192.168.10.100/sdk";
    private static final String USER_NAME = "administrator@vsphere.local";
    private static final String PASSWORD = "!QAZ2wsx";

    public static void main(String[] args){

        try{
            //获取 HostSystem
            ServiceInstance serviceInstance = new ServiceInstance(new URL(URL), USER_NAME, PASSWORD, true);
            Folder rootFolder = serviceInstance.getRootFolder();
            ManagedEntity[] mes = new InventoryNavigator(rootFolder).searchManagedEntities("HostSystem");
            //逻辑判断
            if(mes==null || mes.length==0){
                serviceInstance.getServerConnection().logout();
            }else{
                for(int i=0;i<mes.length;i++){
                    HostSystem hostSystem=(HostSystem)mes[i];
                    System.out.println("HostSystem name:"+hostSystem.getName());
                    double memorySize = (double) hostSystem.getHardware().memorySize / 1024 / 1024; //内存总容量
                    double overallMemoryUsage =hostSystem.getSummary().quickStats.overallMemoryUsage; //内存使用容量(MB)
                    HostListSummary summary = hostSystem.getSummary();
                    float rate = (float) (overallMemoryUsage/memorySize);
                    System.out.println("memry rate:"+rate);
                    System.out.println("memery total:"+memorySize+" MB");
                    System.out.println("memery used:"+overallMemoryUsage+" MB\n");
                }
            }
            serviceInstance.getServerConnection().logout();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
