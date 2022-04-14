package com.mojiayi.action.kafka.consumer;

import com.mojiayi.action.kafka.constants.MojiayiConsumerGroups;
import com.mojiayi.action.kafka.constants.MojiayiTopics;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author liguangri
 */
@Component
public class MojiayiKafkaConsumer {
    @KafkaListener(topics = MojiayiTopics.MOJIAYI_EVENTS, groupId = MojiayiConsumerGroups.MOJIAYI_TEST)
    public void receiveMojiayiEvents(String rawMessage) {
        System.out.println("receive message=" + rawMessage);
    }
}
