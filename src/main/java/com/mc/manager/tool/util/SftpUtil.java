package com.mc.manager.tool.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import sun.net.ftp.FtpDirEntry;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * SFTP相关工具类，主要包括:
 * <li>文件上传</li>
 * <li>文件展示</li>
 * <li>文件展示</li>
 * <li>文件判定</li>
 *
 * @author Liu Chunfu
 * @date 2018-11-05 下午2:47
 **/
@Slf4j
public class SftpUtil {

    private SftpUtil() {
    }

    /**
     * 获取服务器上指定路径的列表
     *
     * @param remotePath 远程路径
     * @param sftp       通过SftpUtil.ftpChannel()创建
     * @return 文件视图集合
     * @throws SftpException
     */
    public static Collection<ChannelSftp.LsEntry> ls(String remotePath, ChannelSftp sftp) throws SftpException {
        Vector vector = sftp.ls(remotePath);
        return (Collection<ChannelSftp.LsEntry>) vector.stream().collect(Collectors.toCollection(ArrayList::new));
    }


    /**
     * 上传文件
     *
     * @param localFile  本地文件
     * @param remoteFile 远程文件,全路径
     * @param sftp       通过SftpUtil.ftpChannel()创建
     * @return
     */
    public static boolean upload(String localFile, String remoteFile, ChannelSftp sftp) {
        try {
            if (!FileUtil.exist(localFile)) {
                throw new RuntimeException("待上传的本地文件不存在：" + localFile);
            }
            if (FileUtil.isDirectory(localFile)) {
                throw new RuntimeException("带上传的路径是一个目录,本API只支持单文件上传：" + localFile);
            }
            String parentPath = PathUtil.getParentPath(remoteFile);
            createParent(parentPath, sftp);
            sftp.put(localFile, remoteFile, ChannelSftp.OVERWRITE);
            Vector result = sftp.ls(remoteFile);
            return !result.isEmpty();
        } catch (Exception e) {
            log.error("文件从{} => {} 上传失败，原始是：{}",  localFile, remoteFile, e.getMessage());
            return false;
        }
    }

    /**
     * 判定文件是否下载成功
     *
     * @param remoteFile 远程服务器上的文件
     * @param localFile  本地资源路径的文件
     * @param sftp       通过SftpUtil.ftpChannel()创建
     * @return true:下载成功  false:下载失败
     * @throws Exception
     */
    public static boolean downLoad(String remoteFile, String localFile, ChannelSftp sftp) throws Exception {
        Vector lsResult = sftp.ls(remoteFile);
        if (lsResult.isEmpty()) {
            throw new RuntimeException("服务器上文件不存在：" + remoteFile);
        }
        //如果存在，则需要保证1不是目录，2如果有文件先进行删除
        if (FileUtil.exist(localFile)) {
            if (FileUtil.isDirectory(localFile)) {
                throw new RuntimeException("当前路径是一个目录路径，不允许创建文件：" + localFile);
            }
            boolean result = FileUtil.del(localFile);
            log.warn("从SFTP下载文件的时候，需要覆盖文件，所以将其删除，路径为：{},结果为{}", localFile, result);
        }

        //保证parent存在
        FileUtil.mkParentDirs(localFile);
        //自己保证本地文件流的关闭
        try (FileOutputStream fos = new FileOutputStream(localFile)) {
            sftp.get(remoteFile, fos);
        }
        return FileUtil.exist(localFile);
    }


    /**
     * 连接sftp服务器
     *
     * @param host     远程主机ip地址
     * @param port     sftp连接端口，null 时为默认端口
     * @param user     用户名
     * @param password 密码
     * @return
     * @throws JSchException
     */
    public static Session connect(String host, Integer port, String user, String password) {
        //使用 hutool的带有自己连接池更优美
        return cn.hutool.extra.ssh.JschUtil.getSession(host, port, user, password);
    }

    /**
     * 获取FTP操作通道
     *
     * @param session
     * @return
     * @throws JSchException
     */
    public static ChannelSftp ftpChannel(Session session) {
        return cn.hutool.extra.ssh.JschUtil.openSftp(session);
    }

    /**
     * 创建parent路径，支持级联创建
     *
     * @param remoteFilePath 远程路径：/dev/test 不应该是包括文件的路径: /dev/test/app.jar
     * @param sftp
     * @throws SftpException
     */
    public static void createParent(String remoteFilePath, ChannelSftp sftp) throws SftpException {
        List<String> splitStrs = StrUtil.splitTrim(remoteFilePath, "/");
        StringBuilder builder = new StringBuilder();
        for (String splitStr : splitStrs) {
            builder.append("/").append(splitStr);
            if (exist(sftp, builder.toString())) {
                continue;
            }
            sftp.mkdir(builder.toString());
        }
    }

    /**
     * 通过抛出异常的方式来判定远程文件是否存在
     *
     * @param sftp       sftp通道
     * @param remotePath 远程路径
     * @return true:存在 false:不存在
     */
    public static boolean exist(ChannelSftp sftp, String remotePath) {
        try {
            sftp.ls(remotePath);
        } catch (SftpException e) {
            return false;
        }
        return true;
    }


    /**
     * 对目标文件授权
     *
     * @param sftp       sftp通道
     * @param targetFile 目标文件
     * @return true:成功 false:失败
     */
    public static boolean chmod(ChannelSftp sftp, String targetFile) {
        try {
            sftp.chmod(FtpDirEntry.Permission.USER.ordinal(), targetFile);
        } catch (SftpException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}