package com.mc.manager.bus.deploy.info;

import lombok.Data;

/**
 * 日志的预览信息
 *
 * @author Liu Chunfu
 * @date 2018-12-14 18:01
 **/
@Data
public class LogOverviewInfo {

    private String name;

    private String ip;

    private Long size;

}
