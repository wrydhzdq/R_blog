package com.r_blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/redis")
    public String testRedis() {
        try {
            // 测试字符串存储
            redisTemplate.opsForValue().set("test:hello", "Hello Redis!");
            String result = (String) redisTemplate.opsForValue().get("test:hello");

            // 测试对象存储（模拟博客）
            redisTemplate.opsForValue().set("test:blog", "博客测试数据");

            return "Redis连接成功！测试值: " + result;
        } catch (Exception e) {
            return "Redis连接失败: " + e.getMessage();
        }
    }
}