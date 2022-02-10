package com.mojiayi.action.designpattern.strategy;

import org.springframework.stereotype.Component;

/**
 * @author liguangri
 */
@Component
public class SendNoticeBySms implements ISendNoticeStrategy{
    @Override
    public NoticeChannelEnum gainNoticeChannel() {
        return NoticeChannelEnum.SMS;
    }

    @Override
    public NoticeChannelEnum sendNotice(NoticeBody noticeBody) {
        // 假装发了通知
        return NoticeChannelEnum.SMS;
    }
}
