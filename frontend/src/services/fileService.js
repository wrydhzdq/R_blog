// src/services/fileService.js
import request from '@/utils/request'

class FileService {
  // 上传图片到通用接口
  async uploadImage(file) {
    const formData = new FormData()
    formData.append('file', file)
    
    try {
      console.log('开始上传图片到通用接口:', file.name, '大小:', file.size)
      
      const response = await request({
        url: '/api/upload/image',
        method: 'POST',
        data: formData,
        headers: {
          'Content-Type': 'multipart/form-data'
        },
        timeout: 30000
      })
      
      console.log('通用图片上传响应:', response)
      
      if (response.code === 200 && response.data) {
        console.log('上传成功，图片URL:', response.data)
        return response.data
      } else {
        throw new Error(response.message || '上传失败')
      }
    } catch (error) {
      console.error('图片上传失败:', error)
      throw new Error(error.message || '上传失败，请检查网络连接')
    }
  }

  // 上传博客封面图片
  async uploadBlogCover(blogId, file) {
    const formData = new FormData()
    formData.append('file', file)
    
    try {
      console.log('上传博客封面，博客ID:', blogId, '文件名:', file.name)
      
      const response = await request({
        url: `/api/blog/${blogId}/cover`,
        method: 'POST',
        data: formData,
        headers: {
          'Content-Type': 'multipart/form-data'
        },
        timeout: 30000
      })
      
      console.log('博客封面上传响应:', response)
      
      if (response.code === 200 && response.data) {
        console.log('封面上传成功，URL:', response.data)
        return response.data
      } else {
        throw new Error(response.message || '封面上传失败')
      }
    } catch (error) {
      console.error('博客封面上传失败:', error)
      throw new Error(error.message || '封面上传失败，请检查网络连接')
    }
  }

  // 删除图片
  async deleteImage(fileUrl) {
    try {
      console.log('删除图片:', fileUrl)
      
      const response = await request({
        url: '/api/upload/delete',
        method: 'DELETE',
        data: { fileUrl }
      })
      
      console.log('图片删除响应:', response)
      return response
    } catch (error) {
      console.error('图片删除失败:', error)
      throw error
    }
  }
}

export const fileService = new FileService()