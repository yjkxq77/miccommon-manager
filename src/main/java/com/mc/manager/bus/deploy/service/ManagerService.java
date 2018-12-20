package com.mc.manager.bus.deploy.service;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.github.sd4324530.jtuple.Tuple2;
import com.jcraft.jsch.Session;
import com.mc.manager.bus.deploy.DeployKit;
import com.mc.manager.tool.util.JschUtil;
import com.mc.manager.tool.util.PathUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 脚本操作服务,主要作用
 * <li>启动 jar包</li>
 * <li>停止 jar包</li>
 * <li>查看 jar包状态</li>
 *
 * @author Liu Chunfu
 * @date 2018-12-13 13:56
 **/
@Service
@Slf4j
public class ManagerService {

    /**
     * 存活与否的标志
     */
    private static final String ALIVE_FLAG = "alvie";

    /**
     * deploy 的工具
     */
    @Autowired
    private DeployKit kit;

    /**
     * 判定应用是否激活<br>
     * 核心是通过指定服务器上的定制化脚本文件，通过输出进行判定
     *
     * @param ip          ip信息
     * @param appFullName 类似:hello-8090_v18121301.jar
     * @return true:存活 false:关闭
     */
    public boolean asActive(String ip, String appFullName) {
        String statusShellPath = parseScriptPath(appFullName, "status");
        String result = kit.executePath(ip, statusShellPath);
        if (StrUtil.isBlank(result)) {
            log.warn("执行ip 为:{} 上的脚本：{} 输出为空！", ip, statusShellPath);
            return false;
        }
        log.debug("执行ip 为:{} 上的脚本：{} 输出为{}！", ip, statusShellPath, result);
        return result.contains(ALIVE_FLAG);
    }

    /**
     * 执行启动指令
     *
     * @param ip
     * @param appFullName 类似:hello-8090_v18121301.jar
     * @return true:启动成功 false:启动失败
     */
    public boolean startApp(String ip, String appFullName) {
        String start = parseScriptPath(appFullName, "start");
        kit.executePath(ip, start);
        ThreadUtil.sleep(1, TimeUnit.SECONDS);
        return asActive(ip, appFullName);
    }

    /**
     * 执行关闭指令
     *
     * @param ip
     * @param appFullName 类似:hello-8090_v18121301.jar
     * @return true:关闭成功 false:关闭失败
     */
    public boolean stopApp(String ip, String appFullName) {
        String start = parseScriptPath(appFullName, "stop");
        kit.executePath(ip, start);
        ThreadUtil.sleep(1, TimeUnit.SECONDS);
        return !asActive(ip, appFullName);
    }

    /**
     * 按照规则解析出 script的地址<br/>
     * 比如：hello-8090_v18121301.jar => hello-8090_v18121302_${type}.sh
     *
     * @param appFullName app 脚本全名称
     * @param type        脚本类型
     * @return 执行成功后的结果
     */
    private String parseScriptPath(String appFullName, String type) {
        //hello-8090_v18121301
        String baseName = PathUtil.getBaseName(appFullName);
        // hello-8090
        String appOriName = appFullName.substring(0, appFullName.indexOf("_v"));
        return PathUtil.combine(kit.targetDeployBasePath, appOriName, baseName + "_" + type + ".sh");
    }
}
