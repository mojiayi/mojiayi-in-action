package com.mojiayi.action.designpattern.strategy;

import com.mojiayi.action.designpattern.Application;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class SendNoticeTest {
    @Autowired
    private SendNoticeService sendNoticeService;

    @Test
    public void testSendNotice() {
        NoticeBody noticeBody = new NoticeBody();
        NoticeChannelEnum noticeChannel = sendNoticeService.sendNotice(noticeBody, NoticeChannelEnum.SMS);
        Assert.assertEquals(NoticeChannelEnum.SMS.getCode(), noticeChannel.getCode());

        noticeChannel = sendNoticeService.sendNotice(noticeBody, NoticeChannelEnum.APP_PUSH);
        Assert.assertEquals(NoticeChannelEnum.APP_PUSH.getCode(), noticeChannel.getCode());

        noticeChannel = sendNoticeService.sendNotice(noticeBody, NoticeChannelEnum.WECHAT);
        Assert.assertEquals(NoticeChannelEnum.WECHAT.getCode(), noticeChannel.getCode());

        noticeChannel = sendNoticeService.sendNotice(noticeBody, NoticeChannelEnum.MOBILE_CALL);
        Assert.assertNull(noticeChannel);
    }
}
