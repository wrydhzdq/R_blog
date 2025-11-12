package com.r_blog.dto.request;

import lombok.Data;

//@Data
public class LoginRequest {
    private String username;
    private String password;

    // 修复构造函数参数顺序
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginRequest() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
