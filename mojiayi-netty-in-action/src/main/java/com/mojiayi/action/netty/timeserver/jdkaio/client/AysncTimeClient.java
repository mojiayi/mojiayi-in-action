package com.mojiayi.action.netty.timeserver.jdkaio.client;

public class AysncTimeClient {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        AysncTimeClientHandler timeClientHandle = new AysncTimeClientHandler("127.0.0.1", port);
        new Thread(timeClientHandle, "aio-TimeClientHandle-001").start();
    }
}
