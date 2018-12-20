package com.mc.manager.bus.deploy.info;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 部署的 jar 的信息
 *
 * @author Liu Chunfu
 * @date 2018-12-13 16:24
 **/
@Data
@AllArgsConstructor
public class DeployAppInfo {

    /**
     * 完全的名称
     */
    private String fullName;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 比如应用为：/mic-auto-deploy/hello-8090/181212/hello-8090_01.jar
     * 格式为：日期_no => 181212_01
     */
    private String version;

    /**
     * 判定是否已经运行
     */
    private boolean asActive = false;

}
