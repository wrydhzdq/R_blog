package com.r_blog.mapper;

import com.r_blog.entity.AdminUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminUserMapper {
    // 根据用户名查询用户
    AdminUser selectByUsername(String username);

    // 用户登录验证
    AdminUser login(@Param("username") String username, @Param("password") String password);

    // 更新用户信息
    int update(AdminUser adminUser);

    // 根据ID查询用户
    AdminUser selectByPrimaryKey(Long adminUserId);

}
