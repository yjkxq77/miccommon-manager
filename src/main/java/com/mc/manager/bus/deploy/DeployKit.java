package com.mc.manager.bus.deploy;

import cn.hutool.core.util.StrUtil;
import com.github.sd4324530.jtuple.Tuple2;
import com.jcraft.jsch.Session;
import com.mc.manager.bus.common.entity.ServerInfo;
import com.mc.manager.bus.server.service.ServerRepositoryService;
import com.mc.manager.tool.util.JschUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 应用部署共用的Value
 *
 * @author Liu Chunfu
 * @date 2018-12-13 14:03
 **/
@Component
@Slf4j
public class DeployKit {

    /**
     * 目标服务器 fatjar 的基础路径
     */
    @Value("${mic.deploy.target.base.path:/deployAll-base}")
    public String targetDeployBasePath;

    /**
     * 目标服务器工具的基础路径
     */
    @Value("${mic.deploy.target.tools.base.path:/mic-core-tools}")
    public String targetToolsBasePath;

    /**
     * 目标环境 Jdk的 home：jdk1.8.0_171
     */
    @Value("${mic.deploy.target.jdk.home:jdk1.8.0_171}")
    public String targetJdkHome;


    @Autowired
    private ServerRepositoryService service;


    /**
     * 获取session
     *
     * @param ip
     * @return
     */
    public Session session(String ip) {
        ServerInfo serverInfo = service.findByIp(ip);
        if (serverInfo == null) {
            log.warn("无法获取 ip 对应的服务器信息");
            serverInfo = service.defaultServerInfo(ip);
        }
        return JschUtil.getSession(serverInfo.getIp(), serverInfo.getPort(), serverInfo.getUserName(), serverInfo.getPwd());
    }


    /**
     * 将形如:<br>
     * hello-8090_v18121301.jar => hello-8090 <br>
     * hello-8090_v18121301-20181213.log => hello-8090  <br>
     *
     * @param appFullName app的完全名称
     * @return
     */
    public String parseAppName(String appFullName) {
        //todo 通过正则判定格式
        int index = appFullName.lastIndexOf("_v");
        String appName = appFullName.substring(0, index);
        return appName;
    }

    /**
     * 获取版本号<br>
     * 将形如 hello-8090_v18121301.jar => 18121301
     *
     * @param appFullName app的完全名称
     * @return
     */
    public String parseAppVersion(String appFullName) {
        //todo 通过正则判定格式
        int index = appFullName.lastIndexOf("_v");
        String version = appFullName.substring(index + 2);
        return version;
    }

    /**
     * 执行脚本
     *
     * @param ip
     * @param scriptPath
     * @return
     */
    public String executePath(String ip, String scriptPath) {
        Session session = session(ip);
        Tuple2<String, String> tuple2 = JschUtil.execForResultAndError(session, scriptPath);
        if (StrUtil.isNotBlank(tuple2.second)) {
            String template = "执行脚本{} 出现错误，错误原因是：{}";
            String msg = StrUtil.format(template, scriptPath, tuple2.second);
            throw new RuntimeException(msg);
        }
        log.info("执行脚本{} 的结果是{}", scriptPath, tuple2.first);
        return tuple2.first;
    }


}
