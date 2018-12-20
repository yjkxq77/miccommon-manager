package com.mc.manager.bus.server.info;

import lombok.Data;

/**
 * 服务器内存信息实体
 *
 * @author Yang jinkang
 * @date 2018/11/19 10:43
 */
@Data
public class ServerMemoryInfo {
    /**
     * 服务器名称
     */
    private String serverName;

    /**
     * 服务器内存使用率
     */
    private String serverMemoryUsageRate;

    /**
     * 服务器内存未使用率
     */
    private String serverMemoryUnusedRate;
}
