package com.mojiayi.action.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author mojiayi
 */
@SpringBootApplication
@MapperScan("com.mojiayi.action.mybatis.dao")
public class MojiayiMybatisBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(MojiayiMybatisBootstrap.class);
    }
}
