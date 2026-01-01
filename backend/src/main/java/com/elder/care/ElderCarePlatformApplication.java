package com.elder.care;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.elder.care.mapper")
public class ElderCarePlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElderCarePlatformApplication.class, args);
    }
}
