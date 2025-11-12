package com.r_blog.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.r_blog.service.FileService;
import com.r_blog.util.OssUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static com.r_blog.util.OssUploadUtil.extractObjectNameFromUrl;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private OSS ossClient;  // 注入OSS客户端

    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;

    @Value("${aliyun.oss.domain}")
    private String domain;

    @Override
    public String uploadImage(MultipartFile file) {
        // 1. 检查文件是否为空
        if (file.isEmpty()) {
            return null;
        }

        // 2. 验证文件类型
        String originalFileName = file.getOriginalFilename();
        if (!OssUploadUtil.isImageFile(originalFileName)) {
            return null;
        }

        // 3. 验证文件大小
        if (!OssUploadUtil.isFileSizeValid(file.getSize())) {
            return null;
        }

        try {
            // 4. 生成OSS对象名称
            String objectName = OssUploadUtil.generateObjectName(originalFileName);

            // 5. 执行上传到OSS 三个参数
            ossClient.putObject(bucketName, objectName, file.getInputStream());

            // 6. 返回完整的访问URL
            String url = domain + "/" + objectName;

            return url;

        } catch (Exception e) {
            System.out.println("上传失败: " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean deleteFile(String fileUrl) {
        try {
            String objectName = extractObjectNameFromUrl(fileUrl);
            ossClient.deleteObject(bucketName, objectName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}