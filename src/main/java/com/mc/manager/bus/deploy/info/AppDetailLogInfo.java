package com.mc.manager.bus.deploy.info;

import lombok.Data;

/**
 * 日志信息
 *
 * @author Liu Chunfu
 * @date 2018-12-14 17:31
 **/
@Data
public class AppDetailLogInfo {

    /**
     * 日志名称
     */
    private String logName;

    /**
     * 日志总行数
     */
    private Integer totalLines;

    /**
     * 其实 No
     */
    private Integer startLineNo;

    /**
     * 结束 No
     */
    private Integer endLineNo;

    /**
     * 内容
     */
    private String content;

    /**
     * 所属的服务器
     */
    private String ip;
}
