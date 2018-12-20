package com.mc.manager.bus.env.info.ori;

import lombok.Data;

import java.util.List;

/**
 * 环境信息：包括硬盘信息，运行时信息，系统信息
 *
 * @author LiuChunfu
 * @date 2018/3/23
 */
@Data
public class EnvInfo {

    /**
     * 当前服务的名称(应黄总要求添加)
     *
     * @since 3.4.5
     */
    private String appName;

    /**
     * 当前服务实例的ID(ip:port)
     */
    private String instanceId;

    /**
     * 运行信息
     */
    private RuntimeInfo runtimeInfo;

    /**
     * 系统信息
     */
    private SystemInfo systemInfo;

    /**
     * 硬盘信息
     */
    private List<DiskInfo> diskInfoList;
}
