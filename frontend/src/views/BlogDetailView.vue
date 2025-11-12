<!-- src/views/BlogDetailView.vue -->
<template>
  <div class="blog-detail-page">
    <!-- 返回按钮 -->
    <div class="nav-bar">
      <button @click="goBack" class="back-btn">← 返回列表</button>
      <h1>博客详情</h1>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>⏳ 正在加载博客内容...</p>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error" class="error">
      <p>❌ {{ error }}</p>
      <button @click="loadBlogDetail" class="retry-btn">重新加载</button>
    </div>

    <!-- 博客详情内容 -->
    <div v-else-if="blog" class="blog-content">
      <!-- 封面图片 -->
      <div v-if="blog.coverImage" class="cover-image">
        <img :src="blog.coverImage" :alt="blog.title" />
      </div>

      <!-- 博客标题和元信息 -->
      <header class="blog-header">
        <h1 class="title">{{ blog.title }}</h1>
        <div class="meta-info">
          <span class="view-count">👁️ {{ blog.viewCount }} 阅读</span>
          <span class="create-time">📅 {{ formatDate(blog.createTime) }}</span>
          <span class="status" :class="getStatusClass(blog.status)">
            {{ getStatusText(blog.status) }}
          </span>
        </div>
      </header>

      <!-- 博客摘要 -->
      <div v-if="blog.summary" class="summary">
        <h3>📝 摘要</h3>
        <p>{{ blog.summary }}</p>
      </div>

      <!-- 博客正文 -->
      <article class="content">
        <h3>📖 正文内容</h3>
        <div class="content-text">{{ blog.content }}</div>
      </article>

      <!-- 操作按钮 -->
      <div class="actions">
        <button @click="increaseViewCount" class="action-btn" :disabled="viewLoading">
          {{ viewLoading ? '⏳ 增加中...' : '👁️ 增加阅读量' }}
        </button>
        <button @click="toggleBlogStatus" class="action-btn" :disabled="statusLoading">
          {{ statusLoading ? '⏳ 更新中...' : (blog.status === 1 ? '📝 设为草稿' : '🚀 发布博客') }}
        </button>
      </div>
    </div>

    <!-- 博客不存在 -->
    <div v-else class="not-found">
      <p>📭 博客不存在</p>
      <button @click="goBack" class="back-btn">返回列表</button>
    </div>

    <!-- 操作反馈弹窗 -->
    <div v-if="showToast" class="toast" :class="toastType">
      {{ toastMessage }}
    </div>

    <!-- 评论组件 -->
    <CommentSection :blog-id="blogId" />
  </div>
</template>

<script setup>
// 导入评论组件
import CommentSection from '@/components/CommentSection.vue'
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { blogService } from '@/services/blogService'

const route = useRoute()
const router = useRouter()

// 响应式数据
const blog = ref(null)
const loading = ref(false)
const error = ref('')
const viewLoading = ref(false)
const statusLoading = ref(false)

// 弹窗状态
const showToast = ref(false)
const toastMessage = ref('')
const toastType = ref('success')

// 获取路由参数中的博客ID
const blogId = ref(route.params.id)
console.log('当前博客ID:', blogId.value)

// 🔹 显示操作反馈
const showToastMessage = (message, type = 'success') => {
  toastMessage.value = message
  toastType.value = type
  showToast.value = true
  
  setTimeout(() => {
    showToast.value = false
  }, 3000)
}

// 🔹 加载博客详情 - 使用服务层
const loadBlogDetail = async () => {
  try {
    loading.value = true
    error.value = ''
    
    console.log(`正在加载博客 ${blogId.value} 的详情...`)
    
    const result = await blogService.getBlogDetail(blogId.value)
    console.log('博客详情数据:', result)
    
    if (result.code === 200) {
      blog.value = result.data
      console.log('✅ 博客详情加载成功')
      showToastMessage('博客加载成功', 'success')
    } else {
      throw new Error(result.message || '获取博客详情失败')
    }
    
  } catch (err) {
    console.error('加载博客详情失败:', err)
    error.value = err.message
    showToastMessage('加载失败: ' + err.message, 'error')
    
    // 临时模拟数据（用于测试）
    if (err.message.includes('Failed to fetch')) {
      blog.value = {
        id: blogId.value,
        title: 'SpringBoot入门教程 (模拟数据)',
        content: '这是博客的详细内容...SpringBoot是一个很好的框架...',
        summary: 'SpringBoot学习指南',
        categoryId: 1,
        coverImage: '/images/1.jpg',
        status: 1,
        viewCount: 100,
        createTime: '2024-01-15T10:30:00',
        updateTime: '2024-01-15T10:30:00'
      }
      showToastMessage('使用模拟数据（实际连接失败）', 'error')
    }
  } finally {
    loading.value = false
  }
}

// 🔹 增加阅读量 - 真实API调用
const increaseViewCount = async () => {
  try {
    viewLoading.value = true
    console.log(`增加博客 ${blogId.value} 的阅读量...`)
    
    const result = await blogService.increaseViewCount(blogId.value)
    
    if (result.code === 200) {
      // 更新本地数据
      if (blog.value) {
        blog.value.viewCount += 1
      }
      console.log('✅ 阅读量增加成功')
      showToastMessage('阅读量+1', 'success')
    }
  } catch (err) {
    console.error('增加阅读量失败:', err)
    showToastMessage('增加阅读量失败: ' + err.message, 'error')
  } finally {
    viewLoading.value = false
  }
}

// 🔹 切换博客状态 - 真实API调用
const toggleBlogStatus = async () => {
  if (!blog.value) return
  
  const newStatus = blog.value.status === 1 ? 0 : 1
  
  try {
    statusLoading.value = true
    
    const result = await blogService.updateBlogStatus(blogId.value, newStatus)
    
    if (result.code === 200) {
      // 更新本地状态
      blog.value.status = newStatus
      const statusText = newStatus === 1 ? '已发布' : '草稿'
      console.log(`✅ 博客状态更新为: ${statusText}`)
      showToastMessage(`博客已${statusText}`, 'success')
    }
  } catch (err) {
    console.error('更新状态失败:', err)
    showToastMessage('更新状态失败: ' + err.message, 'error')
  } finally {
    statusLoading.value = false
  }
}

// 🔹 返回列表
const goBack = () => {
  router.push('/')
}

// 🔹 状态显示辅助函数
const getStatusClass = (status) => {
  return status === 1 ? 'published' : 'draft'
}

const getStatusText = (status) => {
  return status === 1 ? '已发布' : '草稿'
}

// 🔹 格式化日期
const formatDate = (dateString) => {
  try {
    return new Date(dateString).toLocaleDateString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    })
  } catch {
    return '未知日期'
  }
}

// 🔹 生命周期 - 组件加载时获取数据
onMounted(() => {
  console.log('博客详情组件已挂载')
  loadBlogDetail()
})
</script>

<style scoped>
.blog-detail-page {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
  min-height: 100vh;
}

/* 导航栏 */
.nav-bar {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 2rem;
  padding: 1rem;
  background: #2c3e50;
  border-radius: 12px;
  border: 4px solid #34495e;
}

.nav-bar h1 {
  color: #ecf0f1;
  margin: 0;
  font-size: 1.2rem;
}

.back-btn {
  background: #3498db;
  color: white;
  border: none;
  padding: 10px 16px;
  border-radius: 6px;
  cursor: pointer;
  font-family: 'Press Start 2P', cursive;
  font-size: 0.7rem;
  border: 2px solid #2980b9;
  transition: all 0.3s ease;
}

.back-btn:hover {
  background: #2980b9;
  transform: translateY(-1px);
}

/* 加载状态 */
.loading {
  text-align: center;
  padding: 60px 20px;
  color: #ecf0f1;
  font-size: 1rem;
  background: #34495e;
  border-radius: 12px;
  border: 4px solid #2c3e50;
  margin: 20px 0;
}

.spinner {
  border: 4px solid #f3f3f3;
  border-top: 4px solid #e74c3c;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 错误状态 */
.error {
  text-align: center;
  padding: 40px 20px;
  background: #e74c3c;
  border-radius: 12px;
  color: white;
  margin: 20px 0;
  border: 4px solid #c0392b;
}

.retry-btn {
  margin-top: 15px;
  padding: 12px 24px;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 0.9rem;
  font-family: 'Press Start 2P', cursive;
  transition: all 0.3s ease;
  border: 3px solid #2980b9;
}

.retry-btn:hover {
  background: #2980b9;
  transform: translateY(-2px);
}

/* 博客内容 */
.blog-content {
  background: #2c3e50;
  border-radius: 16px;
  border: 6px solid #34495e;
  overflow: hidden;
  margin-bottom: 2rem;
}

/* 封面图片 */
.cover-image {
  width: 100%;
  height: 300px;
  background: #34495e;
  overflow: hidden;
}

.cover-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 博客头部 */
.blog-header {
  padding: 2rem;
  background: #34495e;
  border-bottom: 4px solid #2c3e50;
}

.title {
  color: #ecf0f1;
  font-size: 1.8rem;
  margin: 0 0 1rem 0;
  line-height: 1.3;
  text-shadow: 2px 2px 0 #2c3e50;
}

.meta-info {
  display: flex;
  gap: 1.5rem;
  color: #bdc3c7;
  font-size: 0.8rem;
  flex-wrap: wrap;
}

.view-count, .create-time {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.status {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.7rem;
  font-weight: bold;
  border: 2px solid;
}

.status.published {
  background: #27ae60;
  color: white;
  border-color: #219653;
}

.status.draft {
  background: #f39c12;
  color: white;
  border-color: #e67e22;
}

/* 摘要 */
.summary {
  padding: 1.5rem 2rem;
  background: #34495e;
  border-bottom: 2px solid #2c3e50;
}

.summary h3 {
  color: #3498db;
  margin: 0 0 1rem 0;
  font-size: 1rem;
}

.summary p {
  color: #bdc3c7;
  line-height: 1.6;
  margin: 0;
  font-size: 0.9rem;
}

/* 正文内容 */
.content {
  padding: 2rem;
}

.content h3 {
  color: #3498db;
  margin: 0 0 1.5rem 0;
  font-size: 1rem;
}

.content-text {
  color: #ecf0f1;
  line-height: 1.8;
  font-size: 0.9rem;
  white-space: pre-wrap;
}

/* 操作按钮 */
.actions {
  padding: 1.5rem 2rem;
  background: #34495e;
  border-top: 2px solid #2c3e50;
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.action-btn {
  padding: 12px 20px;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 0.8rem;
  font-family: 'Press Start 2P', cursive;
  border: 3px solid #2980b9;
  transition: all 0.3s ease;
}

.action-btn:hover:not(:disabled) {
  background: #2980b9;
  transform: translateY(-2px);
}

.action-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.action-btn:last-child {
  background: #9b59b6;
  border-color: #8e44ad;
}

.action-btn:last-child:hover:not(:disabled) {
  background: #8e44ad;
}

/* 博客不存在 */
.not-found {
  text-align: center;
  padding: 60px 20px;
  color: #bdc3c7;
  background: #34495e;
  border-radius: 12px;
  margin-top: 20px;
  border: 4px solid #2c3e50;
}

/* 弹窗样式 */
.toast {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 16px 24px;
  border-radius: 8px;
  color: white;
  font-weight: bold;
  z-index: 1000;
  animation: slideIn 0.3s ease;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  font-family: 'Press Start 2P', cursive;
  font-size: 0.7rem;
  border: 3px solid;
}

.toast.success {
  background: #27ae60;
  border-color: #219653;
}

.toast.error {
  background: #e74c3c;
  border-color: #c0392b;
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .blog-detail-page {
    padding: 10px;
  }
  
  .nav-bar {
    flex-direction: column;
    text-align: center;
    gap: 0.5rem;
  }
  
  .title {
    font-size: 1.4rem;
  }
  
  .meta-info {
    flex-direction: column;
    gap: 0.5rem;
    align-items: flex-start;
  }
  
  .actions {
    flex-direction: column;
  }
  
  .cover-image {
    height: 200px;
  }
}
</style>