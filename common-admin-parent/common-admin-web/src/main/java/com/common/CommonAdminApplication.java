package com.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 通用管理系统启动类
 * 
 * @author common-admin
 * @since 2024-01-01
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
@MapperScan("com.common.mapper")
public class CommonAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonAdminApplication.class, args);
        System.out.println("========================================");
        System.out.println("    通用管理系统启动成功");
        System.out.println("    接口文档地址: http://localhost:8080/doc.html");
        System.out.println("========================================");
    }
}