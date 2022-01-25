package com.mojiayi.action.jdkaio.server;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeServerHandler implements Runnable {
    private int port;

    CountDownLatch countDownLatch;

    AsynchronousServerSocketChannel serverSocketChannel;

    public AsyncTimeServerHandler(int port) {
        this.port = port;
        try {
            serverSocketChannel = AsynchronousServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("the aio timer server is start in port:" + port);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    @Override
    public void run() {
        countDownLatch = new CountDownLatch(1);

        doAccept();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void doAccept() {
        serverSocketChannel.accept(this, new AcceptCompletiondHandler());
    }
}
