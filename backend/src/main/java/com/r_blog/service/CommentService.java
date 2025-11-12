package com.r_blog.service;

import com.r_blog.entity.Comment;

import java.util.List;

public interface CommentService {

    /**
     * 添加评论
     */
    boolean addComment(Comment comment);

    /**
     * 根据博客ID查询评论列表
     */
    List<Comment> getCommentsByBlogId(Long blogId);

    /**
     * 删除评论
     */
    boolean deleteComment(Long commentId);

    /**
     * 根据博客ID统计评论数
     */
    int getCommentCountByBlogId(Long blogId);
}
