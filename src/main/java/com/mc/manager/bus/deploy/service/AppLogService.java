package com.mc.manager.bus.deploy.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.mc.manager.bus.deploy.DeployKit;
import com.mc.manager.bus.deploy.info.AppDetailLogInfo;
import com.mc.manager.bus.deploy.info.LogOverviewInfo;
import com.mc.manager.tool.util.PathUtil;
import com.mc.manager.tool.util.SftpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 应用日志信息
 * <li>查看应用的控制台日志信息</li>
 * <li>根据指定行数截取日志内容</li>
 *
 * @author Liu Chunfu
 * @date 2018-12-14 17:34
 **/
@Slf4j
@Service
public class AppLogService {

    /**
     * 统计文件行数的指令
     */
    private static final String TOTAL_LINE_SHELL_TEMPLATE = "awk 'END{print NR}' {}";

    /**
     * 按照指定行数 输出日志内容
     */
    private static final String LOG_CONTENT_SHELL_TEMPLATE = "sed -n '{},{}p' {}";

    /**
     * 按照指定行数 输出日志内容
     */
    private static final String LOG_FLAG = ".log";


    @Autowired
    private DeployKit kit;


    /**
     * 找出 app对应的所有日志
     *
     * @param ip
     * @param appName
     * @return
     */
    public List<AppDetailLogInfo> appLogs(String ip, String appName) throws SftpException {
        Session session = kit.session(ip);
        ChannelSftp sftp = SftpUtil.ftpChannel(session);
        String remotePath = PathUtil.combine(kit.targetDeployBasePath, appName);
        Collection<ChannelSftp.LsEntry> entries = SftpUtil.ls(remotePath, sftp);
        //entries.stream().
        return null;
    }

    /**
     * 获取日志行数
     *
     * @param ip
     * @param logFullPath hello-8090_v18121301-20181213.log
     * @return
     */
    public int logLines(String ip, String logFullPath) {
        //找到对应的目录
        String fullPath = absolutePath(logFullPath);
        String shell = StrUtil.format(TOTAL_LINE_SHELL_TEMPLATE, fullPath);
        String lineStr = kit.executePath(ip, shell);
        return Convert.toInt(lineStr);
    }

    /**
     * 获取日志内容
     *
     * @param ip
     * @param logFullPath
     * @param start
     * @param end
     * @return
     */
    public String logContent(String ip, String logFullPath, int start, int end) {
        String fullPath = absolutePath(logFullPath);
        String shell = StrUtil.format(LOG_CONTENT_SHELL_TEMPLATE, start, end, fullPath);
        String lineStr = kit.executePath(ip, shell);
        return lineStr;
    }

    /**
     * 日志的全路径
     *
     * @param logFullPath
     * @return
     */
    private String absolutePath(String logFullPath) {
        String appName = kit.parseAppName(logFullPath);
        return PathUtil.combine(kit.targetDeployBasePath, appName, logFullPath);
    }

    /**
     * 判定是否是日志
     *
     * @param entry
     * @return
     */
    private boolean isLog(ChannelSftp.LsEntry entry) {
        return entry.getFilename().endsWith(LOG_FLAG);
    }

    private LogOverviewInfo entryToLog(ChannelSftp.LsEntry entry, String ip) {
        String filename = entry.getFilename();
        long size = entry.getAttrs().getSize();
        LogOverviewInfo logInfo = new LogOverviewInfo();
        logInfo.setIp(ip);
        logInfo.setName(filename);
        logInfo.setSize(size);
        return logInfo;
    }
}
