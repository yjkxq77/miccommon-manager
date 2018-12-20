package com.mc.manager.tool.kit;

import cn.hutool.json.JSONUtil;
import org.zeromq.ZMQ;

/**
 * 环境信息发送
 *
 * @author Liu Chunfu
 * @date 2018-09-26 下午4:10
 **/
public class EnvSendKit {

    /**
     * 发布信息
     */
    private static ZMQ.Socket publisher;

    /**
     * 静态
     */
    static {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket socket = context.socket(ZMQ.PUB);
        socket.bind("tcp://*:28899");
        publisher = socket;
    }


    /**
     * 将body序列化后发送
     *
     * @param topic 主题
     * @param body  消息体
     */
    public static void send(String topic, int port, Object body) {
      /*  publisher.bind("tcp:/*//*:" + 28887);*/
        publisher.sendMore(topic);
        publisher.send(JSONUtil.toJsonStr(body));
    }

}
