package com.r_blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // 启用定时任务
public class RBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(RBlogApplication.class, args);
    }

}
