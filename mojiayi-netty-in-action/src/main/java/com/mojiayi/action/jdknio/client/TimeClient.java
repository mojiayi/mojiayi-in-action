package com.mojiayi.action.jdknio.client;

public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        TimeClientHandle timeClientHandle = new TimeClientHandle("127.0.0.1", port);
        new Thread(timeClientHandle, "nio-TimeClientHandle-001").start();
    }
}
