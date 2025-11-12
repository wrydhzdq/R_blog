// src/stores/userStore.js
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  // 状态
  const user = ref(null)
  const isLoggedIn = ref(false)
  
  // 直接从 localStorage 读取初始状态
  const initFromStorage = () => {
    const saved = localStorage.getItem('rblog_user')
    if (saved) {
      try {
        user.value = JSON.parse(saved)
        isLoggedIn.value = true
        console.log('从缓存恢复用户状态:', user.value)
      } catch (error) {
        console.error('解析用户数据失败:', error)
        clearUser()
      }
    }
  }
  
  // 设置用户信息
  const setUser = (userData) => {
    user.value = userData
    isLoggedIn.value = true
    localStorage.setItem('rblog_user', JSON.stringify(userData))
    console.log('用户登录成功:', userData)
  }
  
  // 清除用户信息
  const clearUser = () => {
    user.value = null
    isLoggedIn.value = false
    localStorage.removeItem('rblog_user')
    console.log('用户已登出')
  }
  
  // 初始化
  initFromStorage()
  
  return {
    user,
    isLoggedIn,
    setUser,
    clearUser
  }
})