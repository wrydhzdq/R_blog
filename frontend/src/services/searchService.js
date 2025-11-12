// src/services/searchService.js
import request from '@/utils/request'

class SearchService {
  // 搜索博客
  async searchBlogs(keyword, page = 1, pageSize = 10) {
    try {
      const params = {
        page: page,
        pageSize: pageSize
      }
      
      if (keyword && keyword.trim()) {
        params.title = keyword.trim()
      }
      
      console.log('搜索请求参数:', params)
      const response = await request.get('/api/blog/list', { params })
      console.log('搜索结果:', response)
      return response
    } catch (error) {
      console.error('搜索博客失败:', error)
      if (error.message.includes('Network') || error.message.includes('Failed to fetch')) {
        return this.getMockSearchResults(keyword)
      }
      throw error
    }
  }

  // 高级搜索
  async advancedSearch(searchParams) {
    try {
      const params = {
        page: searchParams.page || 1,
        pageSize: searchParams.pageSize || 10
      }
      
      if (searchParams.title) {
        params.title = searchParams.title
      }
      if (searchParams.categoryId) {
        params.categoryId = searchParams.categoryId
      }
      
      console.log('高级搜索参数:', params)
      const response = await request.get('/api/blog/list', { params })
      return response
    } catch (error) {
      console.error('高级搜索失败:', error)
      throw error
    }
  }

  // 模拟搜索结果
  getMockSearchResults(keyword) {
    return {
      code: 200,
      data: {
        list: [
          {
            id: 1,
            title: `包含"${keyword}"的搜索结果 (模拟数据)`,
            content: `这是关于${keyword}的内容...`,
            summary: `搜索关键词: ${keyword}`,
            categoryId: 1,
            status: 1,
            viewCount: 30,
            createTime: new Date().toISOString()
          }
        ]
      }
    }
  }
}

export const searchService = new SearchService()