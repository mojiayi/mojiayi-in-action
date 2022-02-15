package com.mojiayi.action.designpattern.observer;

import com.mojiayi.action.designpattern.strategy.NoticeChannelEnum;

public class SendNoticeByAppPushObserver implements MojiayiObserver {
    @Override
    public NoticeChannelEnum doEvent() {
        // 假装发了通知
        return NoticeChannelEnum.APP_PUSH;
    }
}
