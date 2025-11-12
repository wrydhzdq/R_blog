package com.r_blog.controller;

import com.r_blog.dto.request.LoginRequest;
import com.r_blog.dto.response.LoginResponse;
import com.r_blog.entity.AdminUser;
import com.r_blog.entity.Result;
import com.r_blog.service.AdminUserService;
import com.r_blog.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminUserController {
    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private JwtUtil jwtUtil;

    //登录
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        AdminUser adminUser = adminUserService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (adminUser != null) {
            String token = jwtUtil.generateToken(adminUser.getId(), adminUser.getUsername());
            LoginResponse response = new LoginResponse(token, adminUser);
            return Result.success("登录成功", response);
        } else {
            return Result.error("用户名或密码错误");
        }
    }

    //根据id查用户
    @GetMapping("/user/{id}")
    public Result<AdminUser> getUserDetail(@PathVariable Long id) {
        AdminUser adminUser = adminUserService.getUserDetailById(id);
        if (adminUser != null) {
            return Result.success(adminUser);
        } else {
            return Result.error("用户不存在");
        }
    }

    @PutMapping("/user")
    public Result<String> updateUser(@RequestBody AdminUser adminUser) {
        boolean result = adminUserService.updateUserInfo(adminUser);
        return result ? Result.success("更新成功") : Result.error("更新失败");
    }
}
