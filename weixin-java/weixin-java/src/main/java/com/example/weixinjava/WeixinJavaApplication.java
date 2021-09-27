package com.example.weixinjava;

import com.example.weixinjava.service.CreateMenu;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.sql.SQLOutput;

@SpringBootApplication
@EnableScheduling // 这里，启用定时任务
public class WeixinJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeixinJavaApplication.class, args);
    }

}
