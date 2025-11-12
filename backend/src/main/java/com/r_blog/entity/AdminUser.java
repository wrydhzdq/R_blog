package com.r_blog.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

//@Data
//管理员实体类
public class AdminUser {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public AdminUser(LocalDateTime createTime, Long id, String nickname, String password, LocalDateTime updateTime, String username) {
        this.createTime = createTime;
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.updateTime = updateTime;
        this.username = username;
    }

    public AdminUser() {
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "AdminUser{" +
                "createTime=" + createTime +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
