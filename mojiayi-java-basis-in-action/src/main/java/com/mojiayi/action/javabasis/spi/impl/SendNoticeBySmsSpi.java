package com.mojiayi.action.javabasis.spi.impl;

import com.mojiayi.action.javabasis.spi.MojiayiSendNoticeSpi;

public class SendNoticeBySmsSpi implements MojiayiSendNoticeSpi {
    @Override
    public void sendNotice(String receiver) {
        System.out.println("send notice by sms,receiver=" + receiver);
    }
}
