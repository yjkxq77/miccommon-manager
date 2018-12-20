package com.mc.script;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ssh.JschUtil;
import com.github.sd4324530.jtuple.Tuple2;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.mc.manager.tool.util.PathUtil;
import com.mc.manager.tool.util.SftpUtil;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 脚本执行
 *
 * @author Liu Chunfu
 * @date 2018-11-23 17:13
 **/
public class ScriptExecutor {


    @Test
    public void skipBigData() throws IOException {
        //可能最优的方案
        String bigFilePath = "/Users/liuchunfu/Desktop/10.5.21.28_hello-8090_start.sh";
        File file = new File(bigFilePath);
        Stream<String> lines = Files.lines(file.toPath());
        lines.skip(4).forEach(System.out::println);
    }


    @Test
    public void testSubStringPath() {
        String filename = "hello-8090_v18121301.jar";
        String baseName = PathUtil.getBaseName(filename);
        int index = baseName.lastIndexOf("_v");
        String appName = baseName.substring(0, index);
        String version = baseName.substring(index + 2);
        System.out.println(appName);
        System.out.println(version);
    }


    /**
     * jar第一层主目录
     */
    private static final String DATE_FORMAT = "yyMMdd";

    /**
     * jar第二层目录
     */
    private static final String TIME_FORMAT = "HHmmss";

    @Test
    public void testHutoolJsch() {
        Session session = JschUtil.getSession("10.5.21.2", 22, "root", "1qaz2wsx");
        String echo_hello = cn.hutool.extra.ssh.JschUtil.exec(session, "cd /mic-project && sh hello.sh ", CharsetUtil.CHARSET_UTF_8);
        System.out.println(echo_hello);
    }

    @Test
    public void testHutoolJschShell() {
        Session session = cn.hutool.extra.ssh.JschUtil.getSession("10.5.21.2", 22, "root", "1qaz2wsx");
        ChannelSftp channelSftp = JschUtil.openSftp(session);

    }

    @Test
    public void testMap() {
        Map<String, List<String>> map = new HashMap<>();

        Stream<Map.Entry<String, List<String>>> stream = map.entrySet().stream();
        Map<String, String> collect = stream.collect(Collectors.toMap(entry -> entry.getKey(), entry -> CollectionUtil.getFirst(entry.getValue().iterator())));
    }

    @Test
    public void testFileExist() {
        Session session = JschUtil.getSession("10.5.21.2", 22, "root", "1qaz2wsx");
        Tuple2<String, String> tuple2 = com.mc.manager.tool.util.JschUtil.execForResultAndError(session, "ls - l / mic - project22");
        System.err.println(tuple2.second);
    }

    @Test
    public void pathTest1() {
        System.out.println(createJarPath("register-server12.jar"));
    }

    private String createJarPath(String jarName) {
        String jarDirMainName = DateUtil.format(new Date(), DATE_FORMAT);
        String jarSuffix = DateUtil.format(new Date(), TIME_FORMAT);
        String baseName = PathUtil.getBaseName(jarName);
        String finalJarName = baseName + jarSuffix + "." + PathUtil.getExtension(jarName);
        String combine = PathUtil.combine("/mic-deplop-path", baseName, jarDirMainName, finalJarName);
        return combine;
    }

    @Test
    public void testJarNameGenerate() {
        String oriName = "123.jar";
        String localPath = "/Volumes/Repository/temp/test";
        String baseName = PathUtil.getBaseName(oriName);
        String extension = PathUtil.getExtension(oriName);
        File[] ls = FileUtil.ls(localPath);
        //处理
        Optional<Integer> maxOpt = Arrays.stream(ls)
                .filter(File::isFile)
                .filter(file -> file.getName().contains("_"))
                .filter(file -> file.getName().startsWith(baseName))
                .map(file -> PathUtil.getBaseName(file.getAbsolutePath()))
                .map(str -> str.substring(StrUtil.lastIndexOfIgnoreCase(str, "_") + 1))
                .map(str -> Convert.toInt(str))
                .max(Integer::compareTo);

        String no = replenishLeft(maxOpt.orElse(0) + 1);
        String finalName = baseName + "_" + no + "." + extension;
        System.out.println(finalName);
    }

    private String replenishLeft(int number) {
        if (number < 10) {
            return "0" + Convert.toStr(number);
        }
        return Convert.toStr(number);
    }

    @Test
    public void testStr() {
        String str = "12_3_123";
        int i = StrUtil.lastIndexOfIgnoreCase(str, "_");
        String substring = str.substring(i + 1);
        System.out.println(substring);
    }

    @Test
    public void testCreateParent() throws SftpException {
        ChannelSftp channelSftp = SftpUtil.ftpChannel(getSession());

        String path = "/1/2/3/4/5/8/9/";
        List<String> splitStrs = StrUtil.splitTrim(path, "/");
        String currentPath = "";
        for (String splitStr : splitStrs) {
            currentPath = currentPath + "/" + splitStr;
            System.out.println(currentPath);
            if (remotePathExist(channelSftp, currentPath)) {
                continue;
            }
            channelSftp.mkdir(currentPath);
            System.out.println(currentPath);
        }
    }


    @Test
    public void testCreateParentPath1() {
        ChannelSftp channelSftp = SftpUtil.ftpChannel(getSession());
        try {
            createParent("/l1/2/3/4", channelSftp);
        } catch (SftpException e) {
            e.printStackTrace();
        }

    }

    private void createParent(String remoteFilePath, ChannelSftp sftp) throws SftpException {
        List<String> splitStrs = StrUtil.splitTrim(remoteFilePath, "/");
        StringBuilder builder = new StringBuilder();
        for (String splitStr : splitStrs) {
            builder.append("/").append(splitStr);
            System.out.println(builder.toString());
            if (remotePathExist(sftp, builder.toString())) {
                continue;
            }
            sftp.mkdir(builder.toString());
        }
    }

    private Session getSession() {
        return com.mc.manager.tool.util.JschUtil.getSession("10.5.21.27", 22, "root", "1qaz2wsx");
    }

    private boolean remotePathExist(ChannelSftp sftp, String remotePath) {
        try {
            sftp.ls(remotePath);
        } catch (SftpException e) {
            return false;
        }
        return true;
    }
}
