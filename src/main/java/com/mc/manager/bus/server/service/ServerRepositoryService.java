package com.mc.manager.bus.server.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.mc.manager.bus.common.entity.ServerInfo;
import com.mc.manager.bus.common.mapper.ServerInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务器操作 DB的服务
 * <li>根据 ip获取服务信息</li>
 *
 * @author Liu Chunfu
 * @date 2018-12-13 14:25
 **/
@Service
@Slf4j
public class ServerRepositoryService {


    /**
     * 默认的用户名
     */
    @Value("${mic.manager.default.user:root}")
    private String defaultUserName;

    /**
     * 默认的密码
     */
    @Value("${mic.manager.default.pwd:1qaz2wsx}")
    private String defaultPwd;

    /**
     * 默认的端口
     */
    @Value("${mic.manager.default.pwd:22}")
    private Integer defaultPort;


    @Autowired
    private ServerInfoMapper mapper;

    /**
     * 通过 ip获取具体信息
     *
     * @param ip
     * @return
     */
    public ServerInfo findByIp(String ip) {
        if (StrUtil.isBlank(ip)) {
            return null;
        }
        List<ServerInfo> serverInfos = findServersByIp(ip);
        if (CollectionUtil.isEmpty(serverInfos)) {
            return null;
        }
        if (serverInfos.size() > 1) {
            log.warn("通过 ip:{} 查到多个匹配项，同项目不符合，请管理员核查！", ip);
        }
        return serverInfos.get(0);
    }

    /**
     * 保存用户
     *
     * @param serverInfo
     * @return
     */
    public boolean saveUser(ServerInfo serverInfo) {
        //判定 ip 是否存在
        if (!CollectionUtil.isEmpty(findServersByIp(serverInfo.getIp()))) {
            log.warn("已存在ip {},所以不能保存只能更新！ ", serverInfo.getIp());
            return false;
        }
        int lines = mapper.insert(serverInfo);
        return lines > 0;
    }

    /**
     * 更新记录
     *
     * @param serverInfo
     * @return
     */
    public boolean updateUser(ServerInfo serverInfo) {
        return mapper.updateByPrimaryKeySelective(serverInfo) > 0;
    }


    /**
     * 获取默认的服务器信息
     *
     * @param ip ip信息
     * @return
     */
    public ServerInfo defaultServerInfo(String ip) {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setIp(ip);
        serverInfo.setDes("默认的服务信息");
        serverInfo.setUserName(defaultUserName);
        serverInfo.setPwd(defaultPwd);
        serverInfo.setPort(defaultPort);
        return serverInfo;
    }

    private List<ServerInfo> findServersByIp(String ip) {
        if (StrUtil.isEmpty(ip)) {
            return new ArrayList<>();
        }
        Weekend<ServerInfo> weekend = Weekend.of(ServerInfo.class);
        WeekendCriteria<ServerInfo, Object> criteria = weekend.weekendCriteria();
        criteria.andEqualTo(ServerInfo::getIp, ip);
        List<ServerInfo> serverInfos = mapper.selectByExample(weekend);
        return serverInfos;
    }


}
