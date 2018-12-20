package com.mc.jvm.connect;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.zeromq.ZMQ;

/**
 * 客服端模拟多个实例发送
 *
 * @author FengJie
 * @date 2018/6/12
 */
@RunWith(SpringRunner.class)
public class Server {
    private ZMQ.Socket getSocket() {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket socket = context.socket(ZMQ.PUB);
        return socket;
    }

    @Test
    @SuppressWarnings("InfiniteLoopStatement")
    public void send() {
        ZMQ.Socket socket = getSocket();
        socket.bind("tcp://*:28887");

        ZeroMqServer zeroMqServer = new ZeroMqServer(socket);
        zeroMqServer.setName("线程:");
        zeroMqServer.start();
        while (true) {
            //为了不停止程序
        }
    }

    private class ZeroMqServer extends Thread {
        ZMQ.Socket socket;

        private ZeroMqServer(ZMQ.Socket socket) {
            this.socket = socket;
        }

        @Override
        @SuppressWarnings("InfiniteLoopStatement")
        public void run() {
            int i = 0;
            while (true) {
                //第一次发送用 sendMore() 方法,相当于发送的是主题
                socket.sendMore("");
                //第二次发送用 send() 方法,是发送的主要内容
                socket.send("空主题" + i);
                sleep();
                socket.sendMore("test");
                socket.send("teest" + i);
                i++;

            }
        }

        private void sleep() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
