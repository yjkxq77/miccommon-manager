package com.mc.manager.bus.common.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "app_history")
public class AppHistory {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 应用名
     */
    @Column(name = "app_name")
    private String appName;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 0:下线 1:上线
     */
    @Column(name = "event_type")
    private Short eventType;

    /**
     * 描述
     */
    private String description;

    /**
     * 事件时间
     */
    @Column(name = "event_time")
    private Date eventTime;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取应用名
     *
     * @return app_name - 应用名
     */
    public String getAppName() {
        return appName;
    }

    /**
     * 设置应用名
     *
     * @param appName 应用名
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * 获取ip地址
     *
     * @return ip - ip地址
     */
    public String getIp() {
        return ip;
    }

    /**
     * 设置ip地址
     *
     * @param ip ip地址
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 获取端口
     *
     * @return port - 端口
     */
    public Integer getPort() {
        return port;
    }

    /**
     * 设置端口
     *
     * @param port 端口
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * 获取0:下线 1:上线
     *
     * @return event_type - 0:下线 1:上线
     */
    public Short getEventType() {
        return eventType;
    }

    /**
     * 设置0:下线 1:上线
     *
     * @param eventType 0:下线 1:上线
     */
    public void setEventType(Short eventType) {
        this.eventType = eventType;
    }

    /**
     * 获取描述
     *
     * @return description - 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取事件时间
     *
     * @return event_time - 事件时间
     */
    public Date getEventTime() {
        return eventTime;
    }

    /**
     * 设置事件时间
     *
     * @param eventTime 事件时间
     */
    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }
}