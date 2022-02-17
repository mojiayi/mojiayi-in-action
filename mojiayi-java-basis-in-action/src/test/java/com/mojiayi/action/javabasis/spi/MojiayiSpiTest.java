package com.mojiayi.action.javabasis.spi;

import org.junit.Test;

import java.util.ServiceLoader;

public class MojiayiSpiTest {
    @Test
    public void testLoadSpi() {
        ServiceLoader<MojiayiSendNoticeSpi> noticeSpis = ServiceLoader.load(MojiayiSendNoticeSpi.class);
        noticeSpis.forEach(spi -> {
            spi.sendNotice("mojiayi");
        });
    }
}
