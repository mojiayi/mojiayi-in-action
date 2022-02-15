package com.mojiayi.action.designpattern.observer;

import com.google.common.eventbus.EventBus;
import org.junit.Test;

public class ObserverSendNoticeTest {
    @Test
    public void testSendNotice() {
        MojiayiObserverable observerable = new MojiayiObserverable();

        observerable.addObserver(new SendNoticeBySmsObserver());
        observerable.addObserver(new SendNoticeByAppPushObserver());
        observerable.addObserver(new SendNoticeByWechatObserver());

        observerable.setState(1);
        observerable.notifyAllObservers();

//        MojiayiEventListener eventListener = new MojiayiEventListener();
//        EventBus eventBus = new EventBus();
//        eventBus.register(eventListener);
//        eventBus.post(new MojiayiNotifyEvent("159", "我的微信", "我的appid"));
    }
}
