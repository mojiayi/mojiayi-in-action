package com.mojiayi.action.designpattern.strategy;

import org.springframework.stereotype.Component;

/**
 * @author mojiayi
 */
@Component
public class SendNoticeByAppPush implements ISendNoticeStrategy{
    @Override
    public NoticeChannelEnum gainNoticeChannel() {
        return NoticeChannelEnum.APP_PUSH;
    }

    @Override
    public NoticeChannelEnum sendNotice(NoticeBody noticeBody) {
        // 假装发了通知
        return NoticeChannelEnum.APP_PUSH;
    }
}
