package com.mc.manager.bus.server.info;

import lombok.Data;

/**
 * 服务器概述信息实体类
 *
 * @author Yang jinkang
 * @date 2018/11/19 10:29
 */
@Data
public class ServerOverviewInfo {
    /**
     * 服务器名称
     */
    private String serverName;

    /**
     * 服务器ip
     */
    private String serverIp;

    /**
     * 服务器状态
     */
    private String serverStatus;

    /**
     * 服务器cpu使用率
     */
    private String serverCpuUsageRate;

    /**
     * 服务器内存使用率
     */
    private String serverMemoryUsageRate;
}
