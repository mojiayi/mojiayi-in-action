package com.mojiayi.action.kafka;

import com.mojiayi.action.kafka.producer.MojiayiKafkaProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MojiayiKafkaApplication.class)
public class SendMessageTest {
    @Autowired
    private MojiayiKafkaProducer kafkaProducer;

    @Test
    public void testSendMessage() {
        kafkaProducer.sendMojiayiEvents("this is bob and tom");
    }
}
