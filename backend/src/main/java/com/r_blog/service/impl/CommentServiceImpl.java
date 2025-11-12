package com.r_blog.service.impl;

import com.r_blog.entity.Comment;
import com.r_blog.mapper.CommentMapper;
import com.r_blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public boolean addComment(Comment comment) {
        try {
            // 设置父评论ID，如果没有父评论则设为0
            if (comment.getParentId() == null) {
                comment.setParentId(0L);
            }
            int result = commentMapper.insertComment(comment);
            return result > 0;
        } catch (Exception e) {
            System.out.println("添加评论失败: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Comment> getCommentsByBlogId(Long blogId) {
        return commentMapper.selectCommentByBlogId(blogId);
    }

    @Override
    public boolean deleteComment(Long commentId) {
        try {
            int result = commentMapper.deleteComment(commentId);
            return result > 0;
        } catch (Exception e) {
            System.out.println("删除评论失败: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int getCommentCountByBlogId(Long blogId) {
        return commentMapper.countCommentById(blogId);
    }
}
