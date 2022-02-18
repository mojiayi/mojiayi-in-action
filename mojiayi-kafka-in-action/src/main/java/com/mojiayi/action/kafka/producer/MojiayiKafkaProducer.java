package com.mojiayi.action.kafka.producer;

import com.mojiayi.action.kafka.constants.MojiayiTopics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author liguangri
 */
@Component
public class MojiayiKafkaProducer {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendMojiayiEvents(String msg) {
        kafkaTemplate.send(MojiayiTopics.MOJIAYI_EVENTS, msg);
    }
}
