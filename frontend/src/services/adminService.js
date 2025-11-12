// src/services/adminService.js
import request from '@/utils/request'

class AdminService {
  // 登录
  async login(username, password) {
    try {
      const response = await request.post('/admin/login', { username, password })
      console.log('登录响应:', response)
      return response
    } catch (error) {
      console.error('登录失败:', error)
      throw error
    }
  }

  // 获取用户详情
  async getUserDetail(id) {
    try {
      const response = await request.get(`/admin/user/${id}`)
      return response
    } catch (error) {
      console.error('获取用户详情失败:', error)
      throw error
    }
  }

  // 更新用户信息
  async updateUser(userData) {
    try {
      const response = await request.put('/admin/user', userData)
      return response
    } catch (error) {
      console.error('更新用户信息失败:', error)
      throw error
    }
  }
}

export const adminService = new AdminService()