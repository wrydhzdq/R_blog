
package com.r_blog.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    /**
     * 上传图片到阿里云OSS
     * @param file 上传的文件
     * @return 文件的访问URL
     */
    String uploadImage(MultipartFile file);

    /**
     * 删除OSS上的文件
     * @param fileUrl 文件的完整URL
     * @return 是否删除成功
     */
    boolean deleteFile(String fileUrl);
}
