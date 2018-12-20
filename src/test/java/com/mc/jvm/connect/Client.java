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
public class Client {
    private ZMQ.Socket getSocket() {
        ZMQ.Context context = ZMQ.context(1);
        return context.socket(ZMQ.SUB);
    }

    @Test
    @SuppressWarnings("InfiniteLoopStatement")
    public void recv() {
        ZMQ.Socket socket = getSocket();
        //必须设置主题,如果服务端未定义,设置为  ""   ,否则无法接收消息
        socket.subscribe("");

        //new Connect(socket).start();
        new Recv(socket).start();
        //new Recv1(socket).start();

        while (true) {
            //为了不停止程序
        }
    }

    private class Connect extends Thread {
        ZMQ.Socket socket;

        private Connect(ZMQ.Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            super.run();
            //for (int i = 7; i < 8; i++) {
            socket.connect("tcp://localhost:28887");
            //sleep();
            //}
        }

        private void sleep() {
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //只需要关系这个线程
    private class Recv extends Thread {
        ZMQ.Socket socket;

        private Recv(ZMQ.Socket socket) {
            this.socket = socket;
            this.socket.connect("tcp://localhost:28887");
            this.socket.subscribe("");
        }

        @Override
        @SuppressWarnings("InfiniteLoopStatement")
        public void run() {
            while (true) {
                try {
                    String recv = socket.recvStr();
                    //System.out.println(recv);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class Recv1 extends Thread {
        ZMQ.Socket socket;

        private Recv1(ZMQ.Socket socket) {
            this.socket = socket;
            this.socket.connect("tcp://localhost:28881");
            this.socket.subscribe("test");
        }

        @Override
        @SuppressWarnings("InfiniteLoopStatement")
        public void run() {
            while (true) {
                try {
                    String recv = socket.recvStr();
                    System.out.println("方法1:  " + recv);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
