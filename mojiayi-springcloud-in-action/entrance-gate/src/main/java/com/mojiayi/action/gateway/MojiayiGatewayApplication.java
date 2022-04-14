package com.mojiayi.action.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author liguangri
 */
@EnableDiscoveryClient
@SpringBootApplication
@Slf4j
public class MojiayiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(MojiayiGatewayApplication.class, args);
        log.info("Startup MojiayiGatewayApplication");
    }
}
