package com.mojiayi.action.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * @author mojiayi
 */
@SpringBootApplication
@EnableKafka
public class MojiayiKafkaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MojiayiKafkaApplication.class);
    }
}
