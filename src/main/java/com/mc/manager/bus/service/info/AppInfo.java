package com.mc.manager.bus.service.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 应用信息
 *
 * @author Liu Chunfu
 * @date 2018-11-15 下午2:36
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppInfo {

    /**
     * 应用名称<br>
     * 同一类型的应用只会有1个名称
     */
    private String appName;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 服务应用描述
     */
    private String description;

    /**
     * @sine 3.0.1
     * 运行语言: 0-java 1-C#。 默认是Java
     */
    private String runLanguage = "Java";

    /**
     * ip地址
     */
    private String ip;

    /**
     * 服务注册时间或者下线时间
     */
    private Long registerTime;

    /**
     * 是否下线
     */
    private boolean asAlive;

}
