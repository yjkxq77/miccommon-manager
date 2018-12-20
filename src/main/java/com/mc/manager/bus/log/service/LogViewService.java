package com.mc.manager.bus.log.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.jcraft.jsch.*;
import com.mc.manager.bus.log.info.FileDownWrapper;
import com.mc.manager.bus.log.info.LogViewVo;
import com.mc.manager.tool.util.SftpUtil;
import com.mc.manager.tool.util.PathUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 日志视图的服务
 *
 * @author Liu Chunfu
 * @date 2018-11-05 下午4:38
 **/
@Service
public class LogViewService {

    @Value("${mic.log.base.path:/}")
    private String logBasePath;

    public LogViewService() {

    }

    /**
     * SFTP的缓存。
     * todo 测试并发下的安全性
     */
    private Map<String, ChannelSftp> cache = new ConcurrentHashMap<>(30);

    /**
     * 用户名
     */
    private static final String USER_NAME = "root";
    /**
     * 密码
     * todo 后续加密
     */
    private static final String PWD = "1qaz2wsx";

    /**
     * 连接端口
     */
    private static final Integer PORT = 22;

    /**
     * 远程文件的基础路径
     */
    private static final String REMOTE_LOG_BASE_PATH = "/mic-project-log";


    /**
     * 获取基础目录下的文件名：当前是以ip分类的
     *
     * @return ip信息
     */
    public Set<String> localBasePathLs() {
        File[] files = FileUtil.ls(logBasePath);
        return Arrays.stream(files).map(File::getName).collect(Collectors.toSet());
    }

    /**
     * 通过第一层获取第二层的信息,应该是 info,warn,error,debug
     *
     * @param oneDepthName 第一层的名称,当前是ip
     * @return
     */
    public Set<String> localBaseType(String oneDepthName) {
        if (StrUtil.isBlank(oneDepthName)) {
            throw new RuntimeException("必须指定第一层文件的名称，不可以为null或者空字符串！");
        }
        String secondPath = PathUtil.combine(logBasePath, oneDepthName);
        return Arrays.stream(FileUtil.ls(secondPath)).map(File::getName).collect(Collectors.toSet());
    }

    /**
     * 通过第一层和第二层拼接后获取到最终的日志视图类
     *
     * @param oneDepthName    第一层路径
     * @param secondDepthName 第二层路径
     * @return 日志视图
     */
    public Set<LogViewVo> localLogViews(String oneDepthName, String secondDepthName) {
        if (StrUtil.isBlank(oneDepthName)) {
            throw new RuntimeException("必须指定第一层文件的名称，不可以为null或者空字符串！");
        }
        if (StrUtil.isBlank(secondDepthName)) {
            throw new RuntimeException("必须指定第二层文件的名称，不可以为null或者空字符串！");
        }
        String logPath = PathUtil.combine(logBasePath, oneDepthName, secondDepthName);
        return Arrays.stream(FileUtil.ls(logPath)).map(this::localFileMap).collect(Collectors.toSet());
    }

    /**
     * 获取指定的日志目录下的文件
     *
     * @param ip ip路径
     * @return 文件集合
     * @throws JSchException
     * @throws SftpException
     */
    public Collection<LogViewVo> remoteLs(String ip) throws JSchException, SftpException {
        ChannelSftp channel = createOrGet(ip);
        Collection<ChannelSftp.LsEntry> entries = SftpUtil.ls(REMOTE_LOG_BASE_PATH, channel);
        return entries.stream().map(this::ftpEntryMap).collect(Collectors.toList());
    }


    /**
     * 包裹文件信息
     *
     * @param oneDepthName
     * @param secondDepthName
     * @param logName
     * @return
     */
    public FileDownWrapper wrapperFile(String oneDepthName, String secondDepthName, String logName) {
        String logPath = PathUtil.combine(logBasePath, oneDepthName, secondDepthName, logName);
        File file = new File(logPath);
        if (!file.exists()) {
            throw new RuntimeException("未找到对应的文件：" + logPath);
        }
        return new FileDownWrapper(file.getName(), file);
    }

    /**
     * todo 未完工，此处暂时停止
     *
     * @param ip
     * @param fileName
     * @return
     * @throws JSchException
     */
    public String remoteDownload(String ip, String fileName) throws JSchException {
        ChannelSftp channelSftp = createOrGet(ip);
        return null;
    }

    /**
     * 从缓存中获取或者创建一个同远程服务器的链接
     *
     * @param ip ip地址
     * @return FTP操作通道
     * @throws JSchException
     */
    private ChannelSftp createOrGet(String ip) throws JSchException {
        ChannelSftp channel = cache.get(ip);
        if (null != channel) {
            if (channel.isClosed() || !channel.isConnected()) {
                channel.connect();
            }
            return channel;
        }
        Session session = SftpUtil.connect(ip, PORT, USER_NAME, PWD);
        channel = SftpUtil.ftpChannel(session);
        cache.put(ip, channel);
        return channel;
    }

    /**
     * 映射一个文件LogView的视图
     *
     * @param ftpEntry SFTP的对象
     * @return LogView
     */
    private LogViewVo ftpEntryMap(ChannelSftp.LsEntry ftpEntry) {
        String filename = ftpEntry.getFilename();
        String longName = ftpEntry.getLongname();
        SftpATTRS attrs = ftpEntry.getAttrs();
        long size = attrs.getSize();
        return new LogViewVo(filename, longName, size);
    }

    /**
     * 本地文件转为logView
     *
     * @param file 本地文件
     * @return LogView
     */
    private LogViewVo localFileMap(File file) {
        String name = file.getName();
        String absolutePath = file.getAbsolutePath();
        long totalSpace = file.getTotalSpace();
        return new LogViewVo(name, absolutePath, totalSpace);
    }


}
