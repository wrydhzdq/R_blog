package com.r_blog.controller;

import com.r_blog.entity.Comment;
import com.r_blog.entity.Result;
import com.r_blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 添加评论
     */
    @PostMapping
    public Result<String> addComment(@RequestBody Comment comment) {
        boolean success = commentService.addComment(comment);
        return success ? Result.success("评论发布成功") : Result.error("评论发布失败");
    }

    /**
     * 根据博客ID获取评论列表
     */
    @GetMapping("/blog/{blogId}")
    public Result<List<Comment>> getCommentsByBlogId(@PathVariable Long blogId) {
        List<Comment> comments = commentService.getCommentsByBlogId(blogId);
        return Result.success(comments);
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/{commentId}")
    public Result<String> deleteComment(@PathVariable Long commentId) {
        boolean success = commentService.deleteComment(commentId);
        return success ? Result.success("评论删除成功") : Result.error("评论删除失败");
    }

    /**
     * 获取博客评论数量
     */
    @GetMapping("/blog/{blogId}/count")
    public Result<Integer> getCommentCount(@PathVariable Long blogId) {
        int count = commentService.getCommentCountByBlogId(blogId);
        return Result.success(count);
    }
}