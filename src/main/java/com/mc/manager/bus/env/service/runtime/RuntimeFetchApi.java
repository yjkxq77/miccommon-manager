package com.mc.manager.bus.env.service.runtime;

import com.mc.manager.bus.env.info.ori.EnvInfo;
import com.mc.manager.frame.dto.ExecuteResult;
import feign.Headers;
import feign.RequestLine;

import java.util.Map;

/**
 * 获取Runtime信息
 *
 * @author Liu Chunfu
 * @date 2018-09-26 下午4:53
 **/
public interface RuntimeFetchApi {

    /**
     * 获取服务端的环境信息
     *
     * @return 环境信息
     */
    @RequestLine("GET /mic/env/all")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    ExecuteResult<Map<String, EnvInfo>> fetchEnv();

}
