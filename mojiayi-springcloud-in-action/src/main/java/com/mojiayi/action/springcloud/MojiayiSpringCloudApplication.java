package com.mojiayi.action.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 自建项目，一步步实践搭建以SpringCloud为基础的实践工程，
 *
 * @author mojiayi
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class MojiayiSpringCloudApplication {
    public static void main(String[] args) {
        SpringApplication.run(MojiayiSpringCloudApplication.class, args);
        System.out.println("Startup MojiayiSpringCloudApplication in Undertow!");
    }
}
