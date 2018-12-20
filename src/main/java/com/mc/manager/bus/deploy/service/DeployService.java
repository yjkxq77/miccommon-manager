package com.mc.manager.bus.deploy.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.github.sd4324530.jtuple.Tuple2;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.mc.manager.bus.deploy.DeployKit;
import com.mc.manager.tool.util.JschUtil;
import com.mc.manager.tool.util.PathUtil;
import com.mc.manager.tool.util.SftpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * 资源发布服务
 *
 * @author Liu Chunfu
 * @date 2018-11-23 11:52
 **/
@Slf4j
@Service
public class DeployService {


    // environment

    /**
     * 判定 Java环境的脚本模板
     */
    private static final String CHECK_JAVA_ENVIRONMENT_SHELL_TEMPLATE = "cd {} && java -version";

    /**
     * 跳转并解压
     */
    private static final String DECOMPRESS_TAR_GZ_SHELL_TEMPLATE = "cd {} && tar -zxvf {}";

    /**
     * 创建 基础路径的命令
     */
    private static final String CREATE_SHELL_TEMPLATE = "mkdir -p {}";

    /**
     * 创建 基础路径的命令
     */
    private static final String CHECK_SHELL_TEMPLATE = "ls -l {}";

    /**
     * shell 启动脚本的名称
     */
    private static final String START_FILE_SHELL_TEMPLATE = "start-template.sh";

    /**
     * shell 关闭脚本的名称
     */
    private static final String STOP_FILE_SHELL_TEMPLATE = "stop-template.sh";

    /**
     * 状态查看的脚本的名称
     */
    private static final String STATUS_FILE_SHELL_TEMPLATE = "status-template.sh";

    /**
     * jar第一层主目录
     */
    private static final String DATE_FORMAT = "yyMMdd";

    ///**
    // * 目标服务器 fatjar 的基础路径
    // */
    //@Value("${mic.deploy.target.base.path:/deployAll-base}")
    //private String targetDeployBasePath;
    //
    ///**
    // * 目标服务器工具的基础路径
    // */
    //@Value("${mic.deploy.target.tools.base.path:/mic-core-tools}")
    //private String targetToolsBasePath;
    //
    ///**
    // * 目标环境 Jdk的 home：jdk1.8.0_171
    // */
    //@Value("${mic.deploy.target.jdk.home:jdk1.8.0_171}")
    //private String targetJdkHome;

    /**
     * JDK的名称：jdk-8u171-linux-x64.tar.gz
     */
    @Value("${mic.core.tools.jdk.full.name}")
    private String jdkFullName;

    /**
     * 本地的工具资源路径
     */
    @Value("${mic.deploy.local.tools.base.path}")
    private String localToolsBasePath;

    /**
     * 本地 fatjar 路径，如果 要部署，需要通过各种方式将资源放在该路径
     */
    @Value("${mic.deploy.local.fatjar.base.path}")
    private String localFatJarBasePath;

    @Autowired
    private DeployKit valueKit;


    /**
     * 对外的方法，统一在此处进行处理
     *
     * @param session
     * @param localJarPath
     */
    public void deployAll(Session session, String localJarPath, String startParam) {
        deployJavaEnvironment(session);
        //localJarName => hello.jar
        String localJarName = PathUtil.getName(localJarPath);
        String targetJarPath = createTargetJdkPathWithNo(localJarName, session);
        String targetJarDir = PathUtil.getParentPath(targetJarPath);
        //targetJarName => hello_01.jar
        String targetJarName = PathUtil.getName(targetJarPath);
        String localDir = generateScriptInLocal(localJarName, targetJarPath, startParam);
        deployScript(session, localDir, PathUtil.getBaseName(targetJarName), targetJarDir);
        deployJar(session, localJarPath, targetJarPath);
    }


    /**
     * 判定环境是否准备就绪
     * 找到某个固定的路径下，是否存在java环境，后续可以更严格改为执行 java -version 获取版本
     *
     * @param session
     * @return
     */
    public boolean environmentReady(Session session) {
        String path = PathUtil.combine(valueKit.targetToolsBasePath, valueKit.targetJdkHome, "bin");
        String shell = StrUtil.format(CHECK_JAVA_ENVIRONMENT_SHELL_TEMPLATE, path);
        Tuple2<String, String> tuple2 = JschUtil.execForResultAndError(session, shell);
        if (StrUtil.isNotBlank(tuple2.second)) {
            log.warn("检测 java 环境不存在，检测路径是：{}，原因是：{}", path, tuple2.second);
            return false;
        }
        return true;
    }

    /**
     * 创建工具包的基础环境
     *
     * @param session
     */
    public boolean createToolsBasePath(Session session) {
        String shell = StrUtil.format(CREATE_SHELL_TEMPLATE, valueKit.targetToolsBasePath);
        if (checkExist(session, valueKit.targetToolsBasePath)) {
            log.info("{}已存在，不需要创建！");
            return true;
        }
        return doShell(session, shell);
    }


    /**
     * 全新的部署 java 环境
     *
     * @param session
     * @return
     */
    public void deployJavaEnvironment(Session session) {
        //0.先判定下再说
        boolean javaResult = environmentReady(session);
        if (javaResult) {
            log.info("JDK环境已经部署..不需要重新部署！");
            return;
        }

        createToolsBasePath(session);
        log.info("1. 创建baseTools路径成功");

        //判定服务器 jar 包是否存在
        String targetJdkTarGzPath = PathUtil.combine(valueKit.targetToolsBasePath, jdkFullName);
        ChannelSftp channelSftp = SftpUtil.ftpChannel(session);
        boolean exists = SftpUtil.exist(channelSftp, targetJdkTarGzPath);
        if (!exists) {
            //不存在就上传
            doUploadJdk(targetJdkTarGzPath, channelSftp);
        }

        //2.通过指令解压
        String shell = StrUtil.format(DECOMPRESS_TAR_GZ_SHELL_TEMPLATE, valueKit.targetToolsBasePath, jdkFullName);
        boolean unCompressResult = doShell(session, shell);
        if (!unCompressResult) {
            throw new RuntimeException("解压目标服务器上的 jdk 失败");
        }
        log.info("3. 解压Java基础环境成功！");

        //3.通过java -version 判定是否已经部署完成
        javaResult = environmentReady(session);
        if (!javaResult) {
            throw new RuntimeException("自动部署 java环境失败！");
        }
        log.info("4. 检测环境完成");
    }


    /**
     * 创建 带有序号的 fatJar 路径
     * 老版本如下：
     * -- mic-auto-deployAll <br>
     * -- fatjar
     * -- 181009
     * -- fatjar01.jar
     * -- fatjar02.jar
     * <p>
     * 新版本如下：
     * -- mic-auto-deployAll
     * -- fatjar
     * -- fatjar_v18100901.jar
     * -- fatjar_v18100902.jar
     *
     * @param oriJarName jar 名称,例如 app.jar
     * @return jar在服务端路径路径
     */
    private String createTargetJdkPathWithNo(String oriJarName, Session session) {
        String baseName = PathUtil.getBaseName(oriJarName);
        // mic-auto-deployAll/fatjar1/181009
        String remotePath = PathUtil.combine(valueKit.targetDeployBasePath, baseName);
        ChannelSftp channelSftp = null;
        String finalName = null;
        try {
            channelSftp = SftpUtil.ftpChannel(session);
            Collection<ChannelSftp.LsEntry> fileEntry = new ArrayList<>();
            try {
                fileEntry = SftpUtil.ls(remotePath, channelSftp);
            } catch (Exception e) {
                //不处理
            }
            finalName = nameWithNoCreator(oriJarName, fileEntry);
        } catch (Exception e) {
            log.error("发生异常，信息为：{}", e.getMessage());
        } finally {
            if (channelSftp != null) {
                channelSftp.exit();
            }
        }
        String finalPath = PathUtil.combine(remotePath, finalName);
        return finalPath;
    }


    /**
     * 在本地创建jar的3类脚本文件
     * <notice>不支持多个用户同时处理</notice>
     * -- local
     * --app1
     * --app1.jar
     * --start.sh
     * --stop.sh
     * 创建脚本
     *
     * @param localJarName  本地jar的名称
     * @param targetJarPath 目标 jar的路径
     * @param javaParam     java 参数
     * @return 脚本所在的目录
     */
    private String generateScriptInLocal(String localJarName, String targetJarPath, String javaParam) {
        //java的运行路径，bin目录下的 java 这个程序
        String javaBin = PathUtil.combine(valueKit.targetToolsBasePath, valueKit.targetJdkHome, "bin", "java");
        String targetJarBaseName = PathUtil.getBaseName(targetJarPath);
        String appHome = PathUtil.getParentPath(targetJarPath);

        Map<String, String> paramMap = new HashMap<>(6);
        paramMap.put("app-home", appHome);
        paramMap.put("java-bin", javaBin);
        paramMap.put("jar-name", targetJarBaseName);
        if (StrUtil.isEmpty(javaParam)) {
            paramMap.put("java-param", "\"\"");
        } else {
            paramMap.put("java-param", javaParam);
        }
        //  本地基础路径 + appName
        String localBaseJarName = PathUtil.getBaseName(localJarName);
        String localScriptDir = PathUtil.combine(localFatJarBasePath, localBaseJarName);
        try (InputStream startIs = ClassUtils.getDefaultClassLoader().getResourceAsStream("deploy/" + START_FILE_SHELL_TEMPLATE);
             InputStream stopIs = ClassUtils.getDefaultClassLoader().getResourceAsStream("deploy/" + STOP_FILE_SHELL_TEMPLATE);
             InputStream statusIs = ClassUtils.getDefaultClassLoader().getResourceAsStream("deploy/" + STATUS_FILE_SHELL_TEMPLATE)) {
            shellToFile(startIs, paramMap, localScriptDir, targetJarBaseName, "start");
            shellToFile(stopIs, paramMap, localScriptDir, targetJarBaseName, "stop");
            shellToFile(statusIs, paramMap, localScriptDir, targetJarBaseName, "status");
        } catch (Exception e) {
            log.error("生成脚本时发生异常，异常信息为:{}", e.getMessage());
            throw new RuntimeException(e);
        }
        return localScriptDir;
    }

    /**
     * 将本地的 jar和脚本上传到服务器
     *
     * @param session
     * @param localJarDir
     * @param keyName      带有 no的关键信息
     * @param remoteJarDir
     */
    private void deployScript(Session session, String localJarDir, String keyName, String remoteJarDir) {
        ChannelSftp channelSftp = null;
        try {
            channelSftp = SftpUtil.ftpChannel(session);
            File[] ls = FileUtil.ls(localJarDir);
            for (File file : ls) {
                //是文件跳过
                if (file.isDirectory()) {
                    continue;
                }
                //不是 sh 跳过
                if (!file.getName().endsWith(".sh")) {
                    continue;
                }
                //没有包含信息跳过
                if (!file.getName().contains(keyName)) {
                    continue;
                }
                uploadFileToDir(channelSftp, file.getAbsolutePath(), remoteJarDir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channelSftp != null) {
                channelSftp.exit();
            }
        }
    }

    /**
     * 部署 jar包
     *
     * @param session
     * @param localJarPath
     * @param remoteJarPath
     */
    private void deployJar(Session session, String localJarPath, String remoteJarPath) {
        ChannelSftp channelSftp = null;
        try {
            channelSftp = SftpUtil.ftpChannel(session);
            uploadFileToFile(channelSftp, localJarPath, remoteJarPath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channelSftp != null) {
                channelSftp.exit();
            }
        }
    }

    /**
     * 将本地文件上传到目标路径，名称同本地存在相同<br>
     * 例如： (stfp,/app/123/task.jar,/target) => /target/task.jar
     *
     * @param channelSftp
     * @param localFilePath 本地文件名
     * @param remoteFileDir 目标路径名
     */
    private void uploadFileToDir(ChannelSftp channelSftp, String localFilePath, String remoteFileDir) {
        String remoteFilePath = PathUtil.combine(remoteFileDir, PathUtil.getName(localFilePath));
        boolean upload = SftpUtil.upload(localFilePath, remoteFilePath, channelSftp);
        SftpUtil.chmod(channelSftp, remoteFilePath);
        log.info("上传本地文件到服务器:{}, {} --> {}", upload, localFilePath, remoteFilePath);
    }

    /**
     * 将本地文件上传到目标的路径：
     *
     * @param channelSftp
     * @param localFilePath  本地文件名
     * @param remoteFilePath 目标文件名
     */
    private void uploadFileToFile(ChannelSftp channelSftp, String localFilePath, String remoteFilePath) {
        boolean upload = SftpUtil.upload(localFilePath, remoteFilePath, channelSftp);
        SftpUtil.chmod(channelSftp, remoteFilePath);
        log.info("上传本地文件到服务器:{}, {} --> {}", upload, localFilePath, remoteFilePath);
    }

    /**
     * 获取 session
     *
     * @param sshHost
     * @param sshPort
     * @param sshUser
     * @param sshPass
     * @return
     */
    public Session getSession(String sshHost, int sshPort, String sshUser, String sshPass) {
        return JschUtil.getSession(sshHost, sshPort, sshUser, sshPass);
    }


    /**
     * 创建路径
     *
     * @param session session
     * @param shell   命令
     * @return true:创建路径
     */
    private boolean doShell(Session session, String shell) {
        Tuple2<String, String> tuple2 = JschUtil.execForResultAndError(session, shell);
        if (StrUtil.isNotBlank(tuple2.second)) {
            log.error("{} 存在问题，原因为：{}", shell, tuple2.second);
            return false;
        }
        return true;
    }

    /**
     * 将 shell 脚本内容写入文件
     *
     * @param is            input流
     * @param paramMap      参数
     * @param appParentPath 第一层目录
     * @param baseName      基础名称
     * @param keyName       核心名称
     */
    private void shellToFile(InputStream is, Map<String, String> paramMap, String appParentPath, String baseName, String keyName) {
        String content = IoUtil.read(is, CharsetUtil.UTF_8);
        String result = StrUtil.format(content, paramMap);
        File file = FileUtil.writeString(result, PathUtil.combine(appParentPath, baseName + "_" + keyName + ".sh"), CharsetUtil.UTF_8);
        log.info("{} 脚本写入本地成功，路径为：{}", keyName, file.getAbsolutePath());
    }

    /**
     * 带有自增 No 的文件名
     * <p>
     * * 新版本如下：
     * * -- mic-auto-deployAll
     * * -- fatjar
     * * -- fatjar_v18100901.jar
     * * -- fatjar_v18100902.jar
     * <p>
     * @param oriName   原始文件名
     * @param fileEntry 文件集合
     * @return 文件名
     */
    private String nameWithNoCreator(String oriName, Collection<ChannelSftp.LsEntry> fileEntry) {
        String baseName = PathUtil.getBaseName(oriName);
        String extension = PathUtil.getExtension(oriName);
        //处理
        Optional<Integer> maxOpt = fileEntry.stream()
                .filter(entry -> entry.getFilename().endsWith(".jar"))
                .filter(entry -> entry.getFilename().contains("_v"))
                .filter(entry -> entry.getFilename().startsWith(baseName))
                .map(entry -> PathUtil.getBaseName(entry.getFilename()))
                .map(str -> str.substring(str.length()-2))
                .map(str -> Convert.toInt(str))
                .max(Integer::compareTo);

        String no = replenishLeft(maxOpt.orElse(0) + 1);
        String finalName = baseName + "_v"+DateUtil.format(new Date(),DATE_FORMAT) + no + "." + extension;
        return finalName;
    }

    /**
     * 在个位数的左边补0转为字符串
     *
     * @param number 数字
     * @return
     */
    private String replenishLeft(int number) {
        if (number < 10) {
            return "0" + Convert.toStr(number);
        }
        return Convert.toStr(number);
    }

    /**
     * 检测文件是否存在
     *
     * @param session
     * @param remotePath
     * @return
     */
    private boolean checkExist(Session session, String remotePath) {
        //执行成功表示存在
        return doShell(session, StrUtil.format(CHECK_SHELL_TEMPLATE, remotePath));
    }


    /**
     * 上传 jar的安装包
     *
     * @param targetJdkTarGzPath 目标 jdk 的位置
     * @param channelSftp        sftp 的传输通道
     */
    private void doUploadJdk(String targetJdkTarGzPath, ChannelSftp channelSftp) {
        //1.通过 SFTP上传
        String localFilePath = PathUtil.combine(localToolsBasePath, jdkFullName);
        boolean uploadResult;
        try {
            uploadResult = SftpUtil.upload(localFilePath, targetJdkTarGzPath, channelSftp);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传 jdk 失败,本地路径是{}，目标路径是：{},原因为:{}", localFilePath, targetJdkTarGzPath, e.getMessage());
            throw new RuntimeException(e);
        } finally {
            channelSftp.exit();
        }
        if (!uploadResult) {
            log.error("上传 jdk失败");
            throw new RuntimeException("上传 jdk 失败");
        }
        log.info("2. 上传jar包成功！");
    }

}
