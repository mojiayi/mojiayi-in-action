package com.mojiayi.action.designpattern.strategy;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liguangri
 */
@Component
public class SendNoticeService implements ApplicationContextAware {
    private Map<NoticeChannelEnum, ISendNoticeStrategy> strategyMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, ISendNoticeStrategy> tempMap = applicationContext.getBeansOfType(ISendNoticeStrategy.class);
        tempMap.values().forEach(strategy -> strategyMap.put(strategy.gainNoticeChannel(), strategy));
    }

    public NoticeChannelEnum sendNotice(NoticeBody noticeBody, NoticeChannelEnum noticeChannel) {
        ISendNoticeStrategy sendNoticeStrategy = strategyMap.get(noticeChannel);
        if (sendNoticeStrategy == null) {
            return null;
        }
        return sendNoticeStrategy.sendNotice(noticeBody);
    }
}
