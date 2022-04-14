package com.mojiayi.action.designpattern.observer;

import com.mojiayi.action.designpattern.strategy.NoticeChannelEnum;

/**
 * @author liguangri
 */
public class SendNoticeByWechatObserver implements MojiayiObserver {
    @Override
    public NoticeChannelEnum doEvent() {
        // 假装发了通知
        return NoticeChannelEnum.WECHAT;
    }
}
