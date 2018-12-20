package com.mc.manager.bus.server.info;

import lombok.Data;

/**
 * 服务器Cpu信息实体
 *
 * @author Yang jinkang
 * @date 2018/11/19 10:48
 */
@Data
public class ServerCpuInfo {
    /**
     * 服务器名称
     */
    private String serverName;

    /**
     * 服务器Cpu使用率
     */
    private String cpuUsageRate;
}
