<!-- src/views/BlogListView.vue -->
<template>
  <div class="blog-list-page">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>📚 {{ currentCategoryName }}</h1>
      <div class="breadcrumb">
        <router-link to="/">首页</router-link>
        <span v-if="categoryId && !currentSearchKeyword"> / {{ currentCategoryName }}</span>
        <span v-if="currentSearchKeyword"> / 搜索"{{ currentSearchKeyword }}"</span>
      </div>
    </div>

    <!-- 搜索组件 -->
    <BlogSearch 
      @search="handleSearch"
      @clear="handleClearSearch"
    />
    
    <!-- 加载状态 -->
    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>🎮 正在加载博客...</p>
    </div>
    
    <!-- 错误状态 -->
    <div v-else-if="error" class="error">
      <p>💥 {{ error }}</p>
      <button @click="loadBlogs" class="retry-btn">🔄 重新加载</button>
    </div>
    
    <!-- 博客列表 -->
    <div v-else class="blog-list">
      <div class="blog-count">
        <span v-if="currentSearchKeyword">
          🔍 搜索"{{ currentSearchKeyword }}" 共找到 {{ blogs.length }} 篇博客
        </span>
        <span v-else>
          🎯 共找到 {{ blogs.length }} 篇博客
        </span>
      </div>
      
      <!-- 使用博客卡片组件 -->
      <BlogCard 
        v-for="blog in blogs" 
        :key="blog.id"
        :blog="blog"
        @view-detail="handleViewDetail"
        @like="handleLike"
        @share="handleShare"
      />
      
      <!-- 空状态 -->
      <div v-if="blogs.length === 0" class="empty">
        <p v-if="currentSearchKeyword">🔍 没有找到包含"{{ currentSearchKeyword }}"的博客</p>
        <p v-else-if="categoryId">📝 这个分类还没有博客文章</p>
        <p v-else>📝 还没有博客文章，快去创建一篇吧！</p>
      </div>
    </div>

    <!-- 操作反馈弹窗 -->
    <div v-if="showToast" class="toast" :class="toastType">
      {{ toastMessage }}
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { useRouter } from 'vue-router'
import BlogCard from '@/components/BlogCard.vue'
import BlogSearch from '@/components/BlogSearch.vue'
import { blogService } from '@/services/blogService'
import { categoryService } from '@/services/categoryService'
import { searchService } from '@/services/searchService'

const router = useRouter()

// 🔹 接收分类ID参数
const props = defineProps({
  categoryId: {
    type: Number,
    default: null
  }
})

// 响应式数据
const blogs = ref([])
const loading = ref(false)
const error = ref('')
const currentSearchKeyword = ref('')

// 弹窗状态
const showToast = ref(false)
const toastMessage = ref('')
const toastType = ref('success')

// 分类名称映射
const categoryMap = {
  1: '技术文章',
  2: '生活随笔', 
  3: '学习笔记'
}

// 计算当前分类名称
const currentCategoryName = computed(() => {
  if (currentSearchKeyword.value) {
    return `搜索"${currentSearchKeyword.value}"`
  }
  return props.categoryId ? categoryMap[props.categoryId] : '所有博客'
})

// 🔹 显示操作反馈
const showToastMessage = (message, type = 'success') => {
  toastMessage.value = message
  toastType.value = type
  showToast.value = true
  
  setTimeout(() => {
    showToast.value = false
  }, 3000)
}

// 🔹 加载博客 - 根据是否有分类ID调用不同接口
const loadBlogs = async () => {
  try {
    loading.value = true
    error.value = ''
    
    console.log('加载博客，分类ID:', props.categoryId)
    
    let result
    
    if (props.categoryId) {
      // 调用分类博客接口：GET /api/blog/category/{categoryId}
      result = await categoryService.getBlogsByCategory(props.categoryId, 1, 20)
      console.log('分类博客结果:', result)
    } else {
      // 调用全部博客接口：GET /api/blog/all
      result = await blogService.getBlogList({ page: 1, pageSize: 20 })
    }
    
    if (result.code === 200) {
      blogs.value = result.data?.list || result.data || []
      console.log(`✅ 加载成功: ${blogs.value.length} 篇博客`)
      showToastMessage(`🎉 ${currentCategoryName.value}加载成功`, 'success')
    } else {
      throw new Error(result.message || '获取博客失败')
    }
    
  } catch (err) {
    console.error('加载博客失败:', err)
    error.value = err.message
    showToastMessage('💥 加载失败: ' + err.message, 'error')
  } finally {
    loading.value = false
  }
}

// 🔹 处理搜索
const handleSearch = async (keyword) => {
  try {
    console.log('开始搜索:', keyword)
    loading.value = true
    currentSearchKeyword.value = keyword
    
    // 直接调用搜索服务
    const result = await searchService.searchBlogs(keyword)
    
    if (result.code === 200) {
      blogs.value = result.data?.list || []
      console.log(`🔍 搜索成功: ${blogs.value.length} 条结果`)
      
      if (blogs.value.length === 0) {
        showToastMessage(`📭 没有找到包含"${keyword}"的博客`, 'info')
      } else {
        showToastMessage(`🎯 找到 ${blogs.value.length} 篇相关博客`, 'success')
      }
    } else {
      throw new Error(result.message || '搜索失败')
    }
  } catch (err) {
    console.error('搜索失败:', err)
    blogs.value = []
    showToastMessage('💥 搜索失败: ' + err.message, 'error')
  } finally {
    loading.value = false
  }
}

// 🔹 处理清空搜索
const handleClearSearch = () => {
  console.log('清空搜索')
  currentSearchKeyword.value = ''
  // 重新加载正常列表
  loadBlogs()
}

// 🔹 关键：监听分类ID变化
watch(() => props.categoryId, (newCategoryId, oldCategoryId) => {
  console.log('分类ID变化:', oldCategoryId, '->', newCategoryId)
  if (newCategoryId !== oldCategoryId) {
    // 切换分类时清空搜索
    if (currentSearchKeyword.value) {
      handleClearSearch()
    } else {
      loadBlogs()
    }
  }
})

// 🔹 处理查看详情
const handleViewDetail = async (blogId) => {
  try {
    console.log('查看博客详情:', blogId)
    
    // 先增加阅读量
    await blogService.increaseViewCount(blogId)
    console.log('阅读量增加成功')
    
    // 再跳转页面
    router.push(`/blog/${blogId}`)
    
  } catch (err) {
    console.error('增加阅读量失败:', err)
    // 即使增加阅读量失败，也允许跳转
    router.push(`/blog/${blogId}`)
  }
}

// 🔹 处理点赞
const handleLike = async (blogId) => {
  try {
    console.log('点赞博客:', blogId)
    const result = await blogService.likeBlog(blogId)
    
    if (result.code === 200) {
      showToastMessage('👍 点赞成功！', 'success')
    }
  } catch (err) {
    console.error('点赞失败:', err)
    showToastMessage('💥 ' + err.message, 'error')
  }
}

// 🔹 处理分享
const handleShare = (blog) => {
  console.log('分享博客:', blog)
  
  // 模拟分享功能
  if (navigator.share) {
    navigator.share({
      title: blog.title,
      text: blog.summary,
      url: window.location.origin + '/blog/' + blog.id
    })
  } else {
    // fallback
    const shareUrl = window.location.origin + '/blog/' + blog.id
    navigator.clipboard.writeText(shareUrl)
    showToastMessage('📋 链接已复制到剪贴板', 'success')
  }
}

onMounted(() => {
  console.log('BlogListView 组件已挂载，分类ID:', props.categoryId)
  loadBlogs()
})
</script>

<style scoped>
.blog-list-page {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
  min-height: 100vh;
}

.page-header {
  text-align: center;
  margin-bottom: 30px;
  background: #2c3e50;
  border: 6px solid #34495e;
  border-radius: 16px;
  padding: 2rem;
  color: white;
}

.page-header h1 {
  color: #ecf0f1;
  margin-bottom: 10px;
  font-size: 1.8rem;
  text-shadow: 2px 2px 0 #34495e;
}

.breadcrumb {
  color: #bdc3c7;
  font-size: 0.8rem;
}

.breadcrumb a {
  color: #3498db;
  text-decoration: none;
  transition: color 0.3s;
}

.breadcrumb a:hover {
  text-decoration: underline;
  color: #2980b9;
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

/* 博客列表 */
.blog-count {
  color: #ecf0f1;
  margin-bottom: 20px;
  font-size: 0.9rem;
  padding: 12px 20px;
  background: #34495e;
  border-radius: 8px;
  border: 3px solid #2c3e50;
}

.empty {
  text-align: center;
  padding: 60px 20px;
  color: #bdc3c7;
  background: #34495e;
  border-radius: 12px;
  margin-top: 20px;
  border: 4px solid #2c3e50;
}

.empty p {
  margin: 0;
  font-size: 1rem;
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
  font-size: 0.8rem;
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

.toast.info {
  background: #3498db;
  border-color: #2980b9;
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

/* 确保组件间的间距 */
.blog-list > * {
  margin-bottom: 16px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .blog-list-page {
    padding: 10px;
  }
  
  .page-header h1 {
    font-size: 1.4rem;
  }
  
  .page-header {
    padding: 1.5rem;
  }
  
  .loading, .error, .empty {
    padding: 40px 15px;
  }
}
</style>