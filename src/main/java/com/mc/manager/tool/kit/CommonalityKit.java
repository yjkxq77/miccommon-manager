package com.mc.manager.tool.kit;

import com.vmware.vim25.mo.ServiceInstance;

import java.net.URL;

/**
 * Created by user on 2018/10/16.
 */
public class CommonalityKit {

    private static final String URL = "https://192.168.10.100/sdk";
    private static final String USER_NAME = "administrator@vsphere.local";
    private static final String PASSWORD = "!QAZ2wsx";
    private static ServiceInstance serviceInstance = null;

    public static ServiceInstance getService() {
        if (serviceInstance==null){
            try {
                serviceInstance = new ServiceInstance(new URL(URL), USER_NAME, PASSWORD, true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return serviceInstance;
    }



}
