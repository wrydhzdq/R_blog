package com.r_blog.dto.request;

import jdk.jshell.Snippet;
import lombok.Data;

//@Data
public class BlogQueryDTO {
    private String title;
    private Long categoryId; //分类ID
    private Integer status;
    private Integer page = 1;
    private Integer pageSize = 10;


    public BlogQueryDTO(Long categoryId, Integer page, Integer pageSize, Integer status, String title) {
        this.categoryId = categoryId;
        this.page = page;
        this.pageSize = pageSize;
        this.status = status;
        this.title = title;
    }

    public BlogQueryDTO() {
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "BlogQueryDTO{" +
                "categoryId=" + categoryId +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", page=" + page +
                ", pageSize=" + pageSize +
                '}';
    }
}
