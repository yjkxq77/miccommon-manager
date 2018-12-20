package com.mc.jvm.connect;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.zeromq.ZMQ;

/**
 * 模拟动态绑定IP和端口
 * <p>
 * 结论:可以通过使用多线程动态connect()动态绑定IP端口,必须设置订阅消息(""即可)
 *
 * @author FengJie
 * @date 2018/6/12
 */
@RunWith(SpringRunner.class)
public class Client1 {
    @Test
    @SuppressWarnings("InfiniteLoopStatement")
    public void recv() {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket socket = context.socket(ZMQ.SUB);
        socket.connect("tcp://localhost:28887");
        socket.subscribe("");
        while (true) {
            String recv = socket.recvStr();
            System.out.println(recv);
        }
    }
}
