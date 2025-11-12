package com.r_blog.mapper;

import com.r_blog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    // 添加评论
    int insertComment(Comment comment);

    // 根据博客ID查询评论列表
    List<Comment> selectCommentByBlogId(@Param("blogId") Long blogId);

    // 根据ID查询评论
    Comment selectCommentById(@Param("commentId") Long commentId);

    // 删除评论（逻辑删除）
    int deleteComment(@Param("commentId") Long commentId);

    // 根据博客ID统计评论数
    int countCommentById(@Param("blogId") Long blogId);
}