package com.r_blog.util;

import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.UUID;

public class OssUploadUtil {

    /**
     * 生成唯一的文件名
     */
    public static String generateFileName(String originalFileName) {
        // 获取文件后缀
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        // 生成UUID
        String uuid = UUID.randomUUID().toString().replace("-", "");
        // 格式：uuid + 后缀
        return uuid + suffix;
    }

    /**
     * 生成OSS存储路径
     * 格式：blog/年/月/日/文件名
     */
    public static String generateObjectName(String originalFileName) {
        LocalDate now = LocalDate.now();
        String fileName = generateFileName(originalFileName);
        //字符串拼接，但用的是更高级的 String.format() 方法。
        return String.format("blog/%d/%02d/%02d/%s",
                now.getYear(), now.getMonthValue(), now.getDayOfMonth(), fileName);
    }

    /**
     * 验证是否是图片文件
     */
    public static boolean isImageFile(String fileName) {
        String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"};
        //把".jpg"这部分单独取出来
        String extension = fileName.toLowerCase().substring(fileName.lastIndexOf("."));
        for (String allowed : allowedExtensions) {
            if (allowed.equals(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证文件大小（最大5MB）
     */
    public static boolean isFileSizeValid(long fileSize) {
        return fileSize <= 5 * 1024 * 1024; // 5MB
    }

    public static String extractObjectNameFromUrl(String url) {
        // 从完整URL中提取OSS objectName
        //这里写自己的
        String domain = "https://r-blog-images.oss-cn-beijing.aliyuncs.com/";
        if (url.startsWith(domain)) {
            return url.substring(domain.length());
        }
        return url; // 如果格式不匹配，返回原URL
    }
}
