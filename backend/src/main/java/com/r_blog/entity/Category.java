package com.r_blog.entity;

import lombok.Data;

import java.time.LocalDateTime;

//@Data
//类别类
public class Category {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Category(LocalDateTime createTime, String description, Long id, String name, LocalDateTime updateTime) {
        this.createTime = createTime;
        this.description = description;
        this.id = id;
        this.name = name;
        this.updateTime = updateTime;
    }

    public Category() {
    }

    @Override
    public String toString() {
        return "Category{" +
                "createTime=" + createTime +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
