package com.mojiayi.action.designpattern.strategy;

import org.springframework.stereotype.Component;

/**
 * @author liguangri
 */
@Component
public class SendNoticeByWechat implements ISendNoticeStrategy {
    @Override
    public NoticeChannelEnum gainNoticeChannel() {
        return NoticeChannelEnum.WECHAT;
    }

    @Override
    public NoticeChannelEnum sendNotice(NoticeBody noticeBody) {
        // 假装发了通知
        return NoticeChannelEnum.WECHAT;
    }
}
