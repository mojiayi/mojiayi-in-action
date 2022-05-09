package com.mojiayi.action.commission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 自建项目，一步步实践搭建以SpringCloud为基础的实践工程，这是一个基础的会员服务。
 *
 * @author mojiayi
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class MojiayiCommissionServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MojiayiCommissionServiceApplication.class, args);
        System.out.println("Startup MojiayiCommissionServiceApplication in Undertow!");
    }
}
