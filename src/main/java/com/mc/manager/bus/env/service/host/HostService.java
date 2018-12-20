package com.mc.manager.bus.env.service.host;

import com.mc.manager.bus.env.info.current.HostInfo;
import com.mc.manager.bus.env.info.current.HostNameInfo;
import org.springframework.web.bind.annotation.RequestParam;

import java.rmi.RemoteException;
import java.util.List;

/**
 * 物理机接口
 *
 * @author Yang jinkang
 * @date 2018/10/16.
 */
public interface HostService {

    /**
     * 获取所有主机信息
     *
     * @throws Exception
     */
    List<HostInfo> getAllHostInfos() throws Exception;

    /**
     * 根据主机名称获取主机信息
     *
     * @param hostName
     * @throws Exception
     */
    HostInfo getHostInfoByHostName(@RequestParam("hostName") String hostName) throws Exception;

    /**
     * 获取所有主机名称
     *
     * @return
     */
    List<HostNameInfo> getHostNameInfo() throws RemoteException;
}
