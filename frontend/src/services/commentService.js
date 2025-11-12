// src/services/commentService.js
import request from '@/utils/request'

class CommentService {
  // 获取博客评论
  async getCommentsByBlogId(blogId) {
    try {
      const response = await request.get(`/api/comment/blog/${blogId}`)
      return response
    } catch (error) {
      console.error('获取评论失败:', error)
      if (error.message.includes('Network') || error.message.includes('Failed to fetch')) {
        return this.getMockComments()
      }
      throw error
    }
  }

  // 添加评论
  async addComment(commentData) {
    try {
      const response = await request.post('/api/comment', commentData)
      return response
    } catch (error) {
      console.error('添加评论失败:', error)
      throw error
    }
  }

  // 删除评论
  async deleteComment(commentId) {
    try {
      const response = await request.delete(`/api/comment/${commentId}`)
      return response
    } catch (error) {
      console.error('删除评论失败:', error)
      throw error
    }
  }

  // 获取评论数量
  async getCommentCount(blogId) {
    try {
      const response = await request.get(`/api/comment/blog/${blogId}/count`)
      return response
    } catch (error) {
      console.error('获取评论数量失败:', error)
      return { code: 200, data: 0 }
    }
  }

  // 模拟评论数据
  getMockComments() {
    return {
      code: 200,
      data: [
        {
          id: 1,
          userName: '热心读者',
          content: '这篇文章写得真好！学到了很多新知识。',
          createTime: new Date().toISOString()
        },
        {
          id: 2, 
          userName: '技术小白',
          content: '讲解得很清晰，谢谢作者的分享！',
          createTime: new Date(Date.now() - 2 * 60 * 60 * 1000).toISOString()
        }
      ]
    }
  }
}

export const commentService = new CommentService()