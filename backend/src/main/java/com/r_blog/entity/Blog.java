package com.r_blog.entity;

import lombok.Data;

import java.time.LocalDateTime;

//@Data
public class Blog {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private Long categoryId;
    private String coverImage;
    private Integer status;
    private Long viewCount;
    private Integer isDeleted;
    // 直接改成String！ mybatis会自己转换给数据库,这样解决了redis的序列器不给datatime格式转给json的问题
    private String createTime;
    private String updateTime;

    public Blog() {
    }

    public Blog(Long categoryId, String content, String coverImage, String createTime, Long id, Integer isDeleted, Integer status, String summary, String title, String updateTime, Long viewCount) {
        this.categoryId = categoryId;
        this.content = content;
        this.coverImage = coverImage;
        this.createTime = createTime;
        this.id = id;
        this.isDeleted = isDeleted;
        this.status = status;
        this.summary = summary;
        this.title = title;
        this.updateTime = updateTime;
        this.viewCount = viewCount;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "categoryId=" + categoryId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", summary='" + summary + '\'' +
                ", coverImage='" + coverImage + '\'' +
                ", status=" + status +
                ", viewCount=" + viewCount +
                ", isDeleted=" + isDeleted +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }
}