// src/main/java/com/r_blog/dto/response/LoginResponse.java
package com.r_blog.dto.response;

import com.r_blog.entity.AdminUser;
import lombok.Data;


public class LoginResponse {
    private String token;
    private AdminUser userInfo;

    public LoginResponse(String token, AdminUser userInfo) {
        this.token = token;
        this.userInfo = userInfo;
    }

    public LoginResponse() {
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                ", userInfo=" + userInfo +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AdminUser getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(AdminUser userInfo) {
        this.userInfo = userInfo;
    }
}