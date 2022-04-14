package com.mojiayi.action.designpattern.observer;

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
    }
}
