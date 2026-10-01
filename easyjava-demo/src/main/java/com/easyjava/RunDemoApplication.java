package com.easyjava;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author yingmingfeng
 * @classname RunDemoApplication
 * @description 启动测试类
 * @date 2026/10/1 23:38
 */
@SpringBootApplication
@MapperScan(basePackages = "com.easyjava.mappers")
public class RunDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(RunDemoApplication.class, args);
    }
}
