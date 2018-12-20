package com.mc.manager.tool.util;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ssh.JschRuntimeException;
import com.github.sd4324530.jtuple.Tuple2;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;

/**
 * 自定义的 JschUtil
 *
 * @author Liu Chunfu
 * @date 2018-12-08 15:29
 **/
public class JschUtil extends cn.hutool.extra.ssh.JschUtil {


    /**
     * 执行shell 命令，并返回结果
     *
     * @param session
     * @param shell
     * @return
     */
    public static Tuple2<String, String> execForResultAndError(Session session, String shell) {
        ChannelExec channel;
        try {
            channel = (ChannelExec) session.openChannel("exec");
        } catch (JSchException e) {
            throw new JschRuntimeException(e);
        }

        channel.setCommand(StrUtil.bytes(shell, CharsetUtil.CHARSET_UTF_8));
        channel.setInputStream(null);
        InputStream resultIn = null;
        InputStream errIn = null;
        try {
            channel.connect();// 执行命令 等待执行结束
            resultIn = channel.getInputStream();
            errIn = channel.getErrStream();
            String result = IoUtil.read(resultIn, CharsetUtil.CHARSET_UTF_8);
            String error = IoUtil.read(errIn, CharsetUtil.CHARSET_UTF_8);
            return Tuple2.with(result, error);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } catch (JSchException e) {
            throw new JschRuntimeException(e);
        } finally {
            IoUtil.close(errIn);
            IoUtil.close(resultIn);
            close(channel);
        }
    }
}
