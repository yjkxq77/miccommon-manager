package com.mc.manager.bus.server.info;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页信息
 *
 * @author Yang jinkang
 * @date 2018/11/22 13:36
 */
@Data
public class PageResultInfo {

    /**
     * 请求页的数据大小
     */
    private int pageSize;

    /**
     * 请求页
     */
    private int pageNo;

    /**
     * 数据总量
     */
    private int total;

    /**
     * 服务器概述
     */
    private List<ServerOverviewInfo> serverOverviewInfos;
}
