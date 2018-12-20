package com.mc.manager.bus.env.info.ori;

import lombok.Data;

/**
 * 系统基本信息
 *
 * @author LiuChunfu
 * @date 2018/3/23
 */
@Data
public class SystemInfo {

    /**
     * 系统名称:windows/centos/ubuntu
     */
    private String system;

    /**
     * 系统版本:10/7/xp/14.1
     */
    private String version;

    /**
     * 系统架构 (X86/X64)
     */
    private String arch;

    /**
     * 运行环境<br>
     * 如果是Java: Java-8<br>
     * 如果是C#: .NET Frame 4.0<br>
     */
    private String runtime;

    /**
     * 主机名<br>
     */
    private String hostName;
}
