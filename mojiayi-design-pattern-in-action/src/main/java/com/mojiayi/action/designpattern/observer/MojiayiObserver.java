package com.mojiayi.action.designpattern.observer;

import com.mojiayi.action.designpattern.strategy.NoticeChannelEnum;

/**
 * @author mojiayi
 */
public interface MojiayiObserver {
    NoticeChannelEnum doEvent();
}
