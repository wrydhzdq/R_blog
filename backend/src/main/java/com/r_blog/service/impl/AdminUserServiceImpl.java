package com.r_blog.service.impl;

import com.r_blog.config.RedisConfig;
import com.r_blog.entity.AdminUser;
import com.r_blog.mapper.AdminUserMapper;
import com.r_blog.service.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
//@Slf4j
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 用户缓存键前缀
    private static final String USER_CACHE_KEY = "user:";
    private static final long USER_CACHE_EXPIRE = 2;

    @Override
    public AdminUser login(String username, String password) {
        AdminUser user = adminUserMapper.login(username, password);

        if (user != null) {
            try {
                // 登录成功，缓存到Redis
                String cacheKey = USER_CACHE_KEY + user.getId();
                redisTemplate.opsForValue().set(cacheKey, user, USER_CACHE_EXPIRE, TimeUnit.HOURS);
            } catch (Exception e) {
                // Redis异常不应该影响登录，只记录日志
                System.out.println("Redis缓存失败，但不影响登录: " + e.getMessage());
            }
        }

        return user;  // 必须返回user！
    }

    @Override
    public AdminUser getUserDetailById(Long userId) {
        try {
            String cacheKey = USER_CACHE_KEY + userId;
            AdminUser cachuser = (AdminUser) redisTemplate.opsForValue().get(cacheKey);
            if (cachuser != null) {
                //缓存命中 直接返回
                return cachuser;
            }
            else {
                //缓存未命中
                AdminUser user = adminUserMapper.selectByPrimaryKey(userId);
                if (user != null) {
                    redisTemplate.opsForValue().set(cacheKey, user,USER_CACHE_EXPIRE, TimeUnit.HOURS);
                }
                return user;
            }
        } catch (Exception e) {
            return adminUserMapper.selectByPrimaryKey(userId);
        }
    }

    @Override
    public boolean updateUserInfo(AdminUser adminUser) {
        try {
            //返回受影响行数
            int result = adminUserMapper.update(adminUser);
            if(result >= 1)
            {
                String cacheKey = USER_CACHE_KEY + adminUser.getId();
                redisTemplate.delete(cacheKey);
            }
            return true;
        } catch (Exception e) {
            //log.error("更新用户信息失败: {}", e.getMessage());
            return false;
        }
    }
}