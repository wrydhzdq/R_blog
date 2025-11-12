package com.r_blog.entity;

import lombok.Data;

import java.time.LocalDateTime;

//@Data
public class Comment {
    private Long id;
    private Long blogId;
    private Long userId;
    private String userName;
    private String content;
    private Long parentId;  // 0表示顶级评论
    private Integer status; // 0-删除，1-正常
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Comment() {
    }

    public Comment(Long blogId, String content, LocalDateTime createTime, Long id, Long parentId, Integer status, LocalDateTime updateTime, Long userId, String userName) {
        this.blogId = blogId;
        this.content = content;
        this.createTime = createTime;
        this.id = id;
        this.parentId = parentId;
        this.status = status;
        this.updateTime = updateTime;
        this.userId = userId;
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "blogId=" + blogId +
                ", id=" + id +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", content='" + content + '\'' +
                ", parentId=" + parentId +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
