<!-- src/views/CategoryView.vue -->
<template>
  <div class="category-page">
    <h1>📂 博客分类</h1>
    
    <!-- 加载状态 -->
    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>⏳ 正在加载分类...</p>
    </div>
    
    <!-- 错误状态 -->
    <div v-else-if="error" class="error">
      <p>❌ {{ error }}</p>
      <button @click="loadCategories" class="retry-btn">重新加载</button>
    </div>
    
    <!-- 分类列表 -->
    <div v-else class="category-list">
      <div class="category-count">共 {{ categories.length }} 个分类</div>
      
      <div 
        v-for="category in categories" 
        :key="category.id" 
        class="category-card"
        @click="viewCategoryBlogs(category.id)"
      >
        <div class="category-header">
          <h3 class="category-name">{{ category.name }}</h3>
          <span class="blog-count">{{ category.blogCount }} 篇</span>
        </div>
        
        <p class="category-desc">{{ category.description }}</p>
        
        <div class="category-footer">
          <span class="create-time">创建时间: {{ formatDate(category.createTime) }}</span>
          <button @click.stop="editCategory(category)" class="edit-btn">编辑</button>
        </div>
      </div>
      
      <!-- 空状态 -->
      <div v-if="categories.length === 0" class="empty">
        <p>📝 还没有分类，快去创建吧！</p>
        <button @click="createCategory" class="action-btn">创建分类</button>
      </div>
    </div>

    <!-- 操作反馈弹窗 -->
    <div v-if="showToast" class="toast" :class="toastType">
      {{ toastMessage }}
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { categoryService } from '@/services/categoryService'

const router = useRouter()

// 响应式数据
const categories = ref([])
const loading = ref(false)
const error = ref('')

// 弹窗状态
const showToast = ref(false)
const toastMessage = ref('')
const toastType = ref('success')

// 🔹 显示操作反馈
const showToastMessage = (message, type = 'success') => {
  toastMessage.value = message
  toastType.value = type
  showToast.value = true
  
  setTimeout(() => {
    showToast.value = false
  }, 3000)
}

// 🔹 加载分类列表
const loadCategories = async () => {
  try {
    loading.value = true
    error.value = ''
    
    console.log('正在加载分类数据...')
    
    const result = await categoryService.getCategories()
    console.log('分类数据:', result)
    
    if (result.code === 200) {
      categories.value = result.data
      console.log('✅ 分类加载成功')
      showToastMessage('分类加载成功', 'success')
    } else {
      throw new Error(result.message || '获取分类失败')
    }
    
  } catch (err) {
    console.error('加载分类失败:', err)
    error.value = err.message
    showToastMessage('加载失败: ' + err.message, 'error')
  } finally {
    loading.value = false
  }
}

// 🔹 查看分类下的博客
const viewCategoryBlogs = (categoryId) => {
  console.log('查看分类博客:', categoryId)
  router.push(`/category/${categoryId}/blogs`)
}

// 🔹 编辑分类
const editCategory = (category) => {
  console.log('编辑分类:', category)
  showToastMessage(`编辑分类: ${category.name}`, 'success')
  // 后续会实现编辑功能
}

// 🔹 创建分类
const createCategory = () => {
  console.log('创建分类')
  showToastMessage('创建分类功能开发中...', 'success')
}

// 🔹 格式化日期
const formatDate = (dateString) => {
  try {
    return new Date(dateString).toLocaleDateString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    })
  } catch {
    return '未知日期'
  }
}

onMounted(() => {
  console.log('CategoryView 组件已挂载')
  loadCategories()
})
</script>

<style scoped>
.category-page {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
  min-height: 100vh;
}

h1 {
  text-align: center;
  color: #2c3e50;
  margin-bottom: 30px;
}

/* 加载状态 */
.loading {
  text-align: center;
  padding: 60px 20px;
  color: #7f8c8d;
  font-size: 18px;
}

.spinner {
  border: 3px solid #f3f3f3;
  border-top: 3px solid #3498db;
  border-radius: 50%;
  width: 40px;
  height: 40px;
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
  background: #ffeaea;
  border-radius: 10px;
  color: #e74c3c;
  margin: 20px 0;
}

.retry-btn {
  margin-top: 15px;
  padding: 10px 20px;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
}

.retry-btn:hover {
  background: #2980b9;
}

/* 分类列表 */
.category-count {
  color: #7f8c8d;
  margin-bottom: 20px;
  font-size: 14px;
  padding: 10px 0;
  border-bottom: 1px solid #e1e1e1;
}

.category-card {
  background: white;
  border: 2px solid #e1e1e1;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

.category-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0,0,0,0.15);
  border-color: #3498db;
}

.category-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.category-name {
  margin: 0;
  color: #2c3e50;
  font-size: 18px;
}

.blog-count {
  background: #3498db;
  color: white;
  padding: 4px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: bold;
}

.category-desc {
  color: #5d6d7e;
  line-height: 1.6;
  margin-bottom: 15px;
}

.category-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
  color: #95a5a6;
}

.edit-btn {
  padding: 6px 12px;
  background: #f8f9fa;
  border: 1px solid #dee2e6;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.edit-btn:hover {
  background: #e9ecef;
  border-color: #adb5bd;
}

/* 空状态 */
.empty {
  text-align: center;
  padding: 60px 20px;
  color: #7f8c8d;
  background: #f8f9fa;
  border-radius: 10px;
  margin-top: 20px;
}

.action-btn {
  margin-top: 15px;
  padding: 10px 20px;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.action-btn:hover {
  background: #2980b9;
}

/* 弹窗样式 */
.toast {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 12px 20px;
  border-radius: 8px;
  color: white;
  font-weight: bold;
  z-index: 1000;
  animation: slideIn 0.3s ease;
}

.toast.success {
  background: #27ae60;
}

.toast.error {
  background: #e74c3c;
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
</style>