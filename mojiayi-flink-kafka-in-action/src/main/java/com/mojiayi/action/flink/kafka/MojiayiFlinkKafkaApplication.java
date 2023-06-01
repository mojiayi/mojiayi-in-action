package com.mojiayi.action.flink.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author mojiayi
 */
@SpringBootApplication
@EnableConfigurationProperties
public class MojiayiFlinkKafkaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MojiayiFlinkKafkaApplication.class);
    }
}
