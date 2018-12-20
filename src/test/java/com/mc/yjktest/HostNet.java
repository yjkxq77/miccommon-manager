package com.mc.yjktest;


import com.mc.manager.tool.kit.HostKit;
import com.vmware.vim25.PerfMetricSeriesCSV;
import com.vmware.vim25.mo.*;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2018/10/11.
 */
public class HostNet {
    private static final String URL = "https://192.168.10.100/sdk";
    private static final String USER_NAME = "administrator@vsphere.local";
    private static final String PASSWORD = "!QAZ2wsx";


    public static void main(String[] args) {
        try {
            HostKit hostKit = new HostKit();
            ServiceInstance serviceInstance = new ServiceInstance(new URL(URL), USER_NAME, PASSWORD, true);
            Folder rootFolder = serviceInstance.getRootFolder();
            ManagedEntity[] mes = new InventoryNavigator(rootFolder).searchManagedEntities("HostSystem");

            if (mes.length > 0) {
                for (int j = 0; j < mes.length; j++) {
                    HostSystem systems = (HostSystem) mes[j];
                    System.out.println("主机名称：" + mes[j].getName());
                    //物理服务器所关联的虚拟机
                    List hostNetlist = null;
                    hostNetlist = hostKit.getHostNetTimeRange(mes[j].getName(), serviceInstance, 20, "");


                    for (int s = 0; s < hostNetlist.size(); s++) {
                        Map m = (Map) hostNetlist.get(s);
                        String point = (String) m.get("point");
                        PerfMetricSeriesCSV[] csvs = (PerfMetricSeriesCSV[]) m.get("csvs");
                        if (point.equals("bytesRx")) {
                            PerfMetricSeriesCSV _value = csvs[0];
                            System.out.println("接收速率：" + _value.getValue());
                        } else if (point.equals("bytesTx")) {
                            PerfMetricSeriesCSV _value = csvs[0];
                            System.out.println("传输速率:" + _value.getValue() + " KB/S");

                        }


                        System.out.println("______________________----------------");


                    }
                }
            }


            serviceInstance.getServerConnection().logout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
