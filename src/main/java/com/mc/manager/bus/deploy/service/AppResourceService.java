package com.mc.manager.bus.deploy.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.mc.manager.bus.deploy.info.DeployAppInfo;
import com.mc.manager.bus.deploy.DeployKit;
import com.mc.manager.tool.util.PathUtil;
import com.mc.manager.tool.util.SftpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 资源服务，主要作用
 * <li>罗列所有 最新 jar</li>
 * <li>罗列所有的 jar以及状态</li>
 * <li>查看对应的日志信息，行数和分页内容</li>
 *
 * @author Liu Chunfu
 * @date 2018-12-13 13:51
 **/
@Service
@Slf4j
public class AppResourceService {

    private static final String JAR_EXTENSION = "jar";


    @Autowired
    private DeployKit kit;

    @Autowired
    private ManagerService managerService;

    /**
     * 获取部署目录下的应用
     *
     * @param ip ip信息
     * @return
     */
    public List<String> appInfoList(String ip) {
        String targetDeployBasePath = kit.targetDeployBasePath;
        try {
            Collection<ChannelSftp.LsEntry> entries = SftpUtil.ls(targetDeployBasePath, SftpUtil.ftpChannel(kit.session(ip)));
            List<String> result = Optional.ofNullable(entries).orElse(new ArrayList<>()).stream().map(entry -> entry.getFilename()).collect(Collectors.toList());
            return result;
        } catch (SftpException e) {
            throw new RuntimeException("[获取jar包] 获取服务器上的信息出错，异常信息为:" + e.getMessage(), e);
        }
    }

    /**
     * 获取目录下的某一个应用的所有版本,针对的是一个
     *
     * @param ip      ip 信息
     * @param appName 应用信息
     * @return
     */
    public List<DeployAppInfo> parseAppInfos(String ip, String appName) {
        String targetAppPath = PathUtil.combine(kit.targetDeployBasePath, appName);
        Session session = kit.session(ip);
        ChannelSftp sftp = null;
        try {
            sftp = SftpUtil.ftpChannel(session);
            Collection<ChannelSftp.LsEntry> entries = SftpUtil.ls(targetAppPath, sftp);
            //core:convert entry to app
            //todo 正则表示式进行格式判定
            List<DeployAppInfo> result = entries.stream().filter(this::isJar)
                    .map(entry -> entryToApp(entry, ip))
                    .collect(Collectors.toList());
            return result;
        } catch (Exception e) {
            log.error("解析服务器:{} 的应用:{} 时发生异常，信息为：{}", ip, appName, e.getMessage());
            throw new RuntimeException(e);
        } finally {
            if (sftp != null) {
                sftp.exit();
            }
        }
    }

    /**
     * hello-8090_v18121301.jar => appName:hello-8090 version:18121301
     *
     * @param entry sftp 的 文件信息
     * @return 部署的 app信息
     */
    private DeployAppInfo entryToApp(ChannelSftp.LsEntry entry, String ip) {
        String filename = entry.getFilename();
        String baseName = PathUtil.getBaseName(filename);
        int index = baseName.lastIndexOf("_v");
        String appName = baseName.substring(0, index);
        String version = baseName.substring(index + 2);
        boolean alive = managerService.asActive(ip, filename);
        return new DeployAppInfo(filename, appName, version, alive);
    }

    /**
     * 判定是否是 jar
     *
     * @param entry
     * @return
     */
    private boolean isJar(ChannelSftp.LsEntry entry) {
        return entry.getFilename().endsWith(JAR_EXTENSION);
    }


}
