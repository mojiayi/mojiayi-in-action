package com.mojiayi.action.designpattern.strategy;

/**
 * @author mojiayi
 */
public interface ISendNoticeStrategy {
    NoticeChannelEnum gainNoticeChannel();

    NoticeChannelEnum sendNotice(NoticeBody noticeBody);
}
