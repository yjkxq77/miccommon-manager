package com.mc.manager.bus.event;

import lombok.Data;

import java.util.Date;

/**
 * 服务器事件信息实体
 *
 * @author Yang jinkang
 * @date 2018/11/19 13:58
 */
@Data
public class EventInfo {
    /**
     * 服务器名称
     */
    private String name;

    /**
     * 服务器ip
     */
    private String ip;

    /**
     * 事件
     */
    private String event;

    /**
     * 触发时间
     */
    private Date time;
}
