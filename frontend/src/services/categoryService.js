// src/services/categoryService.js
import request from '@/utils/request'

class CategoryService {
  // 根据分类获取博客
  async getBlogsByCategory(categoryId, page = 1, pageSize = 10) {
    try {
      console.log(`调用分类博客接口: /api/blog/category/${categoryId}`)
      const response = await request.get(`/api/blog/category/${categoryId}?page=${page}&pageSize=${pageSize}`)
      console.log('分类博客接口返回:', response)
      return response
    } catch (error) {
      console.error('获取分类博客失败:', error)
      if (error.message.includes('Network') || error.message.includes('Failed to fetch')) {
        return this.getMockCategoryBlogs(categoryId)
      }
      throw error
    }
  }

  // 模拟分类博客数据
  getMockCategoryBlogs(categoryId) {
    const categoryMap = {
      1: '技术文章',
      2: '生活随笔', 
      3: '学习笔记'
    }
    
    return {
      code: 200,
      data: {
        list: [
          {
            id: Date.now(),
            title: `${categoryMap[categoryId]}示例 (模拟数据)`,
            content: '这是分类博客内容...',
            summary: `${categoryMap[categoryId]}的示例博客`,
            categoryId: categoryId,
            status: 1,
            viewCount: 50,
            createTime: new Date().toISOString()
          }
        ]
      }
    }
  }
}

export const categoryService = new CategoryService()