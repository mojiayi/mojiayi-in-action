package com.mojiayi.action.jdkaio.server;

public class AsyncTimeServer {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        AsyncTimeServerHandler asyncTimeServer = new AsyncTimeServerHandler(port);
        new Thread(asyncTimeServer, "aio-async-time-server-handler-001").start();
    }
}
