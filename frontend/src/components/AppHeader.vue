<!-- src/components/AppHeader.vue -->
<template>
  <header class="app-header" v-if="userStore.isLoggedIn">
    <div class="nav-content">
      <h1 class="logo">✍️ R-Blog</h1>
      <nav class="nav-links">
        <router-link to="/" class="nav-link">🏠 首页</router-link>
        
        <!-- 添加管理链接 -->
        <router-link to="/manage" class="nav-link">🎮 管理</router-link>
        
        <!-- 静态分类下拉菜单 -->
        <div class="dropdown">
          <button class="nav-link dropdown-btn">📂 分类</button>
          <div class="dropdown-content">
            <a @click="goToCategory(1)" class="dropdown-item">技术文章</a>
            <a @click="goToCategory(2)" class="dropdown-item">生活随笔</a>
            <a @click="goToCategory(3)" class="dropdown-item">学习笔记</a>
          </div>
        </div>

        <!-- 登录状态显示 -->
        <div class="user-section">
          <div class="user-info">
            <span class="welcome-text">🎮 欢迎, {{ userStore.user?.nickname || userStore.user?.username }}</span>
            <button @click="handleLogout" class="logout-btn">🚪 退出</button>
          </div>
        </div>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/userStore'

const router = useRouter()
const userStore = useUserStore()

const goToCategory = (categoryId) => {
  console.log('跳转到分类:', categoryId)
  router.push(`/category/${categoryId}`)
}

const handleLogout = () => {
  if (confirm('确定要退出登录吗？')) {
    userStore.clearUser()
    router.push('/login')
  }
}
</script>

<style scoped>
/* 保持原有样式不变 */
.app-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 0;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.nav-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
}

.logo {
  color: white;
  margin: 0;
  font-size: 1.5rem;
}

.nav-links {
  display: flex;
  gap: 2rem;
  align-items: center;
}

.nav-link {
  color: white;
  text-decoration: none;
  font-weight: 500;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  transition: all 0.3s ease;
  cursor: pointer;
}

.nav-link:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

.nav-link.router-link-active {
  background: rgba(255, 255, 255, 0.3);
  font-weight: bold;
}

.user-section {
  margin-left: auto;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.welcome-text {
  color: white;
  font-size: 14px;
}

.logout-btn {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.3s ease;
}

.logout-btn:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-1px);
}

/* 下拉菜单样式 */
.dropdown {
  position: relative;
  display: inline-block;
}

.dropdown-btn {
  background: none;
  border: none;
  font: inherit;
  color: inherit;
}

.dropdown-content {
  display: none;
  position: absolute;
  top: 100%;
  left: 0;
  background: white;
  min-width: 160px;
  box-shadow: 0 8px 16px rgba(0,0,0,0.1);
  border-radius: 6px;
  z-index: 1000;
  overflow: hidden;
}

.dropdown:hover .dropdown-content {
  display: block;
}

.dropdown-item {
  display: block;
  padding: 12px 16px;
  color: #333;
  text-decoration: none;
  border-bottom: 1px solid #f0f0f0;
  transition: background 0.3s;
  cursor: pointer;
}

.dropdown-item:hover {
  background: #f8f9fa;
  color: #667eea;
}

.dropdown-item:last-child {
  border-bottom: none;
}
</style>