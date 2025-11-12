package com.r_blog.service;

import com.r_blog.entity.AdminUser;

public interface AdminUserService {
    // 用户登录验证
    public AdminUser login(String username, String password);

    // 更新用户信息
    public boolean updateUserInfo(AdminUser adminUser);

    // 根据ID查询用户
    public AdminUser getUserDetailById(Long userId);
}
