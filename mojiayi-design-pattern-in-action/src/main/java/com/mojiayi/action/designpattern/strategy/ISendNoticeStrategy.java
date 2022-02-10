package com.mojiayi.action.designpattern.strategy;

/**
 * @author liguangri
 */
public interface ISendNoticeStrategy {
    NoticeChannelEnum gainNoticeChannel();

    NoticeChannelEnum sendNotice(NoticeBody noticeBody);
}
