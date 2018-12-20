package com.mc.manager.bus.server.web.controller;

import cn.hutool.db.PageResult;
import com.github.sd4324530.jtuple.Tuple2;
import com.mc.manager.bus.server.info.PageResultInfo;
import com.mc.manager.bus.server.info.ServerOverviewInfo;
import com.mc.manager.bus.server.service.ServerOverviewService;
import com.mc.manager.frame.dto.ExecuteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Yang jinkang
 * @date 2018/11/18 22:58
 */
@RestController
@RequestMapping("/mic/manager/server/overview")
public class ServerOverviewController {


    /**
     * 注入serverOverviewService
     */
    @Autowired
    private ServerOverviewService serverOverviewService;

    /**
     * 获取服务器概要信息
     *
     * @return
     * @throws Exception
     */
    @GetMapping
    public ExecuteResult<List<ServerOverviewInfo>> getServerOverview() throws Exception {
        return new ExecuteResult<>(serverOverviewService.getServerOverview());
    }

    /**
     * 分页获取结果集
     *
     * @param pageSize
     * @param pageNo
     * @return
     */
    @GetMapping("/page")
    public ExecuteResult<PageResultInfo> getServiceOverview(@RequestParam("pageSize") Integer pageSize, @RequestParam("pageNo") Integer pageNo) {
        return new ExecuteResult<>(serverOverviewService.getServerOverview(pageSize,pageNo));
    }
}