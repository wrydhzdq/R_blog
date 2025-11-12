package com.r_blog.controller;

import com.aliyun.oss.OSS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class OssTestController {

    @Autowired
    private OSS ossClient;

    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;

    @GetMapping("/oss")
    public String testOssConnection() {
        try {
            // 测试OSS连接是否正常
            boolean exists = ossClient.doesBucketExist(bucketName);
            return "OSS连接成功！Bucket存在: " + exists;
        } catch (Exception e) {
            return "OSS连接失败: " + e.getMessage();
        }
    }
}

