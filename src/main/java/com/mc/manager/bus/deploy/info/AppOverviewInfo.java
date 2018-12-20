package com.mc.manager.bus.deploy.info;

import lombok.Data;

/**
 * App预览时的信息
 *
 * @author Liu Chunfu
 * @date 2018-12-14 17:29
 **/
@Data
public class AppOverviewInfo {

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 一个多少个版本
     */
    private int versionCount;

    /**
     * 最新的版本
     */
    private String newestVersion;
}
