// src/services/blogService.js
import request from '@/utils/request'

class BlogService {
  // 获取所有博客列表
  async getBlogList(params = {}) {
    try {
      const queryParams = new URLSearchParams({
        page: params.page || 1,
        pageSize: params.pageSize || 10,
        ...params
      })
      
      const response = await request.get(`/api/blog/all?${queryParams}`)
      console.log('博客列表响应:', response)
      return response
    } catch (error) {
      console.error('获取博客列表失败:', error)
      if (error.message.includes('Network') || error.message.includes('Failed to fetch')) {
        return this.getMockBlogs()
      }
      throw error
    }
  }

  // 获取博客详情
  async getBlogDetail(id) {
    try {
      const response = await request.get(`/api/blog/${id}`)
      console.log('博客详情响应:', response)
      return response
    } catch (error) {
      console.error('获取博客详情失败:', error)
      if (error.message.includes('Network') || error.message.includes('Failed to fetch')) {
        return this.getMockBlogDetail(id)
      }
      throw error
    }
  }

  // 增加阅读量
  async increaseViewCount(id) {
    try {
      const response = await request.put(`/api/blog/${id}/view`)
      return response
    } catch (error) {
      console.error('增加阅读量失败:', error)
      return { code: 200, message: 'success' }
    }
  }

  // 更新博客状态
  async updateBlogStatus(id, status) {
    try {
      const response = await request.put(`/api/blog/${id}/status?status=${status}`)
      return response
    } catch (error) {
      console.error('更新博客状态失败:', error)
      throw error
    }
  }

  // 创建博客
  async saveBlog(blogData) {
    try {
      const response = await request.post('/api/blog', blogData)
      return response
    } catch (error) {
      console.error('创建博客失败:', error)
      throw error
    }
  }

  // 删除博客
  async deleteBlog(id) {
    try {
      const response = await request.delete(`/api/blog/${id}`)
      return response
    } catch (error) {
      console.error('删除博客失败:', error)
      throw error
    }
  }

  // 更新博客
  async updateBlog(blogData) {
    try {
      const response = await request.put('/api/blog', blogData)
      return response
    } catch (error) {
      console.error('更新博客失败:', error)
      throw error
    }
  }

  // 点赞博客（模拟）
  async likeBlog(id) {
    try {
      await new Promise(resolve => setTimeout(resolve, 500))
      const isSuccess = Math.random() > 0.2
      if (isSuccess) {
        return { code: 200, message: '点赞成功' }
      } else {
        throw new Error('点赞失败，请重试')
      }
    } catch (error) {
      console.error('点赞失败:', error)
      throw error
    }
  }

  // 模拟数据（备用）
  getMockBlogs() {
    return {
      code: 200,
      data: {
        list: [
          {
            id: 1,
            title: 'SpringBoot入门教程 (模拟数据)',
            content: 'SpringBoot详细内容...',
            summary: 'SpringBoot学习指南',
            categoryId: 1,
            coverImage: '/images/1.jpg',
            status: 1,
            viewCount: 100,
            createTime: '2024-01-15T10:30:00'
          },
          {
            id: 2,
            title: 'Redis缓存实战 (模拟数据)',
            content: 'Redis使用教程...',
            summary: 'Redis入门到实践', 
            categoryId: 1,
            coverImage: '/images/2.jpg',
            status: 1,
            viewCount: 85,
            createTime: '2024-01-14T15:20:00'
          }
        ]
      }
    }
  }

  getMockBlogDetail(id) {
    return {
      code: 200,
      data: {
        id: id,
        title: '博客详情 (模拟数据)',
        content: '这是博客的详细内容...',
        summary: '博客摘要',
        categoryId: 1,
        coverImage: '/images/1.jpg',
        status: 1,
        viewCount: 100,
        createTime: '2024-01-15T10:30:00',
        updateTime: '2024-01-15T10:30:00'
      }
    }
  }
}

export const blogService = new BlogService()