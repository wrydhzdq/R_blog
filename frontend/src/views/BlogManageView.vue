<!-- src/views/BlogManageView.vue -->
<template>
  <div class="manage-container">
    <div class="gameboy-header">
      <h1>🎮 博客管理</h1>
      <button @click="showCreateForm = true" class="pixel-btn create-btn">
        ✨ 写新博客
      </button>
    </div>

    <!-- 创建/编辑博客表单 -->
    <div v-if="showCreateForm || editingBlog" class="create-form">
      <h3>{{ editingBlog ? '✏️ 编辑博客' : '📝 创建新博客' }}</h3>
      <form @submit.prevent="editingBlog ? updateBlog() : createBlog()">
        <div class="form-group">
          <label>标题</label>
          <input v-model="currentBlog.title" type="text" placeholder="输入博客标题" required :disabled="submitting">
        </div>
        
        <div class="form-group">
          <label>封面图片</label>
          <ImageUpload 
            v-model="currentBlog.coverImage"
            :preview-alt="currentBlog.title"
            :blog-id="currentBlog.id || null"
            @upload="handleImageUpload"
            @error="handleImageError"
          />
          <p class="form-hint">📷 支持 JPG、PNG、GIF 格式，最大 5MB</p>
        </div>
        
        <div class="form-group">
          <label>摘要</label>
          <textarea v-model="currentBlog.summary" rows="3" placeholder="博客摘要" :disabled="submitting"></textarea>
        </div>
        
        <div class="form-group">
          <label>内容</label>
          <textarea v-model="currentBlog.content" rows="6" placeholder="博客内容" required :disabled="submitting"></textarea>
        </div>
        
        <div class="form-group">
          <label>分类</label>
          <select v-model="currentBlog.categoryId" :disabled="submitting">
            <option value="1">技术文章</option>
            <option value="2">生活随笔</option>
            <option value="3">学习笔记</option>
          </select>
        </div>
        
        <div class="form-group">
          <label>状态</label>
          <select v-model="currentBlog.status" :disabled="submitting">
            <option value="1">发布</option>
            <option value="0">草稿</option>
          </select>
        </div>
        
        <div class="form-actions">
          <button type="submit" class="pixel-btn" :disabled="submitting">
            {{ submitting ? '🎯 保存中...' : (editingBlog ? '💾 更新' : '🚀 发布') }}
          </button>
          <button type="button" @click="cancelEdit" class="pixel-btn cancel" :disabled="submitting">取消</button>
        </div>
      </form>
    </div>

    <!-- 博客列表 -->
    <div class="blog-list">
      <div class="list-header">
        <h2>📚 我的博客 ({{ blogs.length }})</h2>
      </div>

      <div v-if="loading" class="loading-screen">
        <div class="pixel-loader"></div>
        <p class="pixel-text">🎮 加载中...</p>
      </div>
      
      <div v-else>
        <div v-for="blog in blogs" :key="blog.id" class="blog-item">
          <div class="blog-info">
            <!-- 显示封面图片 -->
            <div v-if="blog.coverImage" class="blog-cover-preview">
              <img :src="blog.coverImage" :alt="blog.title" class="cover-image" />
            </div>
            <h3>{{ blog.title }}</h3>
            <p class="summary">{{ blog.summary || '暂无摘要' }}</p>
            <div class="blog-meta">
              <span class="status" :class="blog.status === 1 ? 'published' : 'draft'">
                {{ blog.status === 1 ? '🟢 已发布' : '📝 草稿' }}
              </span>
              <span>👁️ {{ blog.viewCount }} 阅读</span>
              <span>📅 {{ formatDate(blog.createTime) }}</span>
              <span v-if="blog.coverImage" class="has-cover">📷 有封面</span>
            </div>
          </div>
          <div class="blog-actions">
            <button @click="editBlog(blog)" class="action-btn edit">✏️ 编辑</button>
            <button @click="toggleStatus(blog)" class="action-btn status">
              {{ blog.status === 1 ? '📝 设草稿' : '🚀 发布' }}
            </button>
            <button @click="deleteBlog(blog.id)" class="action-btn delete">🗑️ 删除</button>
          </div>
        </div>

        <div v-if="blogs.length === 0" class="empty-state">
          <p>📝 还没有博客，点击"写新博客"开始创作吧！</p>
        </div>
      </div>
    </div>

    <!-- 操作反馈 -->
    <div v-if="message" class="pixel-toast" :class="messageType">
      {{ message }}
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import { blogService } from '@/services/blogService'
import ImageUpload from '@/components/ImageUpload.vue'  // 导入图片上传组件

const router = useRouter()
const userStore = useUserStore()

// 检查登录
onMounted(() => {
  if (!userStore.isLoggedIn) {
    alert('请先登录')
    router.push('/login')
  } else {
    loadBlogs()
  }
})

const showCreateForm = ref(false)
const editingBlog = ref(null)
const loading = ref(false)
const submitting = ref(false)
const blogs = ref([])
const message = ref('')
const messageType = ref('')

// 当前编辑的博客数据
const currentBlog = reactive({
  id: null,
  title: '',
  coverImage: '',  // 封面图片URL
  summary: '',
  content: '',
  categoryId: 1,
  status: 1
})

// 图片上传处理
const handleImageUpload = (imageUrl) => {
  console.log('图片上传成功:', imageUrl)
  showMessage('🎉 图片上传成功!', 'success')
}

const handleImageError = (error) => {
  console.error('图片上传失败:', error)
  showMessage('💥 ' + error, 'error')
}

// 加载博客列表
const loadBlogs = async () => {
  try {
    loading.value = true
    const result = await blogService.getBlogList({ page: 1, pageSize: 50 })
    if (result.code === 200) {
      blogs.value = result.data?.list || result.data || []
      showMessage(`✅ 加载了 ${blogs.value.length} 篇博客`, 'success')
    } else {
      throw new Error(result.message || '加载失败')
    }
  } catch (error) {
    console.error('加载博客失败:', error)
    showMessage('💥 加载失败: ' + error.message, 'error')
  } finally {
    loading.value = false
  }
}

// 创建博客
const createBlog = async () => {
  try {
    submitting.value = true
    console.log('创建博客:', currentBlog)
    
    const result = await blogService.saveBlog(currentBlog)
    console.log('创建博客响应:', result)
    
    if (result.code === 200) {
      showMessage('🎉 博客创建成功！', 'success')
      cancelEdit()
      await loadBlogs()
    } else {
      throw new Error(result.message || '创建失败')
    }
  } catch (error) {
    console.error('创建博客失败:', error)
    showMessage('💥 创建失败: ' + error.message, 'error')
  } finally {
    submitting.value = false
  }
}

// 更新博客
const updateBlog = async () => {
  try {
    submitting.value = true
    console.log('更新博客:', currentBlog)
    
    const result = await blogService.updateBlog(currentBlog)
    console.log('更新博客响应:', result)
    
    if (result.code === 200) {
      showMessage('🎉 博客更新成功！', 'success')
      cancelEdit()
      await loadBlogs()
    } else {
      throw new Error(result.message || '更新失败')
    }
  } catch (error) {
    console.error('更新博客失败:', error)
    showMessage('💥 更新失败: ' + error.message, 'error')
  } finally {
    submitting.value = false
  }
}

// 编辑博客
const editBlog = (blog) => {
  console.log('编辑博客:', blog)
  editingBlog.value = blog
  showCreateForm.value = false
  
  // 填充表单数据
  Object.assign(currentBlog, {
    id: blog.id,
    title: blog.title,
    coverImage: blog.coverImage || '',  // 封面图片
    summary: blog.summary,
    content: blog.content,
    categoryId: blog.categoryId,
    status: blog.status
  })
}

// 取消编辑
const cancelEdit = () => {
  showCreateForm.value = false
  editingBlog.value = null
  resetForm()
}

// 重置表单
const resetForm = () => {
  Object.assign(currentBlog, {
    id: null,
    title: '',
    coverImage: '',  // 清空封面图片
    summary: '',
    content: '',
    categoryId: 1,
    status: 1
  })
}

const toggleStatus = async (blog) => {
  try {
    const newStatus = blog.status === 1 ? 0 : 1
    const result = await blogService.updateBlogStatus(blog.id, newStatus)
    if (result.code === 200) {
      blog.status = newStatus
      showMessage('✅ 状态更新成功', 'success')
      await loadBlogs()
    }
  } catch (error) {
    showMessage('💥 状态更新失败', 'error')
  }
}

const deleteBlog = async (id) => {
  if (!confirm('确定要删除这篇博客吗？')) return
  
  try {
    const result = await blogService.deleteBlog(id)
    if (result.code === 200) {
      showMessage('🗑️ 博客删除成功', 'success')
      await loadBlogs()
    }
  } catch (error) {
    showMessage('💥 删除失败', 'error')
  }
}

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('zh-CN')
}

const showMessage = (msg, type = 'info') => {
  message.value = msg
  messageType.value = type
  setTimeout(() => {
    message.value = ''
  }, 3000)
}
</script>

<style scoped>
.manage-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
  min-height: 100vh;
}

.gameboy-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  background: #2c3e50;
  border: 6px solid #34495e;
  border-radius: 16px;
  padding: 1.5rem 2rem;
  color: white;
}

.gameboy-header h1 {
  color: #ecf0f1;
  margin: 0;
  font-size: 1.5rem;
}

.create-btn {
  background: #27ae60;
  color: white;
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 0.8rem;
  border: 3px solid #219653;
}

.create-form {
  background: #34495e;
  border: 4px solid #2c3e50;
  border-radius: 12px;
  padding: 2rem;
  margin-bottom: 30px;
  color: #ecf0f1;
}

.create-form h3 {
  margin-bottom: 1.5rem;
  color: #ecf0f1;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: bold;
  color: #bdc3c7;
  font-size: 0.8rem;
}

.form-group input,
.form-group textarea,
.form-group select {
  width: 100%;
  padding: 12px 16px;
  border: 3px solid #2c3e50;
  border-radius: 8px;
  background: #2c3e50;
  color: #ecf0f1;
  font-family: 'Press Start 2P', cursive;
  font-size: 0.7rem;
}

.form-group input:focus,
.form-group textarea:focus,
.form-group select:focus {
  outline: none;
  border-color: #3498db;
}

.form-group input:disabled,
.form-group textarea:disabled,
.form-group select:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.form-hint {
  color: #95a5a6;
  font-size: 0.6rem;
  margin-top: 0.5rem;
  margin-bottom: 0;
}

.form-actions {
  display: flex;
  gap: 1rem;
  margin-top: 2rem;
}

.pixel-btn {
  padding: 12px 24px;
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

.pixel-btn:hover:not(:disabled) {
  transform: translateY(-2px);
}

.pixel-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

.pixel-btn.cancel {
  background: #95a5a6;
  border-color: #7f8c8d;
}

.blog-list {
  background: #2c3e50;
  border: 6px solid #34495e;
  border-radius: 16px;
  padding: 2rem;
}

.list-header {
  margin-bottom: 1.5rem;
}

.list-header h2 {
  color: #ecf0f1;
  margin: 0;
  font-size: 1.2rem;
}

.loading-screen {
  text-align: center;
  padding: 3rem;
  color: #ecf0f1;
}

.pixel-loader {
  width: 40px;
  height: 40px;
  border: 4px solid #ecf0f1;
  border-top: 4px solid #e74c3c;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.blog-item {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  background: #34495e;
  border: 3px solid #2c3e50;
  border-radius: 8px;
  padding: 1.5rem;
  margin-bottom: 1rem;
  transition: all 0.3s ease;
}

.blog-item:hover {
  border-color: #3498db;
  transform: translateY(-2px);
}

.blog-info {
  flex: 1;
}

/* 博客封面预览 */
.blog-cover-preview {
  width: 80px;
  height: 60px;
  border-radius: 6px;
  overflow: hidden;
  margin-bottom: 0.5rem;
  border: 2px solid #2c3e50;
}

.cover-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.blog-info h3 {
  color: #ecf0f1;
  margin: 0 0 0.5rem 0;
  font-size: 0.9rem;
}

.summary {
  color: #bdc3c7;
  margin: 0 0 1rem 0;
  font-size: 0.7rem;
  line-height: 1.4;
}

.blog-meta {
  display: flex;
  gap: 1rem;
  font-size: 0.6rem;
  color: #95a5a6;
  flex-wrap: wrap;
}

.status.published {
  color: #27ae60;
}

.status.draft {
  color: #f39c12;
}

.has-cover {
  color: #3498db;
}

.blog-actions {
  display: flex;
  gap: 0.5rem;
}

.action-btn {
  padding: 8px 12px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.6rem;
  font-family: 'Press Start 2P', cursive;
  border: 2px solid;
  transition: all 0.3s ease;
}

.action-btn:hover {
  transform: translateY(-1px);
}

.action-btn.edit {
  background: #3498db;
  color: white;
  border-color: #2980b9;
}

.action-btn.status {
  background: #f39c12;
  color: white;
  border-color: #e67e22;
}

.action-btn.delete {
  background: #e74c3c;
  color: white;
  border-color: #c0392b;
}

.empty-state {
  text-align: center;
  padding: 3rem;
  color: #bdc3c7;
}

.pixel-toast {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 1rem 1.5rem;
  border-radius: 8px;
  color: white;
  font-family: 'Press Start 2P', cursive;
  font-size: 0.7rem;
  z-index: 1000;
  animation: slideIn 0.3s ease;
  border: 3px solid;
}

.pixel-toast.success {
  background: #27ae60;
  border-color: #219653;
}

.pixel-toast.error {
  background: #e74c3c;
  border-color: #c0392b;
}

.pixel-toast.info {
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

/* 响应式设计 */
@media (max-width: 768px) {
  .manage-container {
    padding: 10px;
  }
  
  .gameboy-header {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }
  
  .blog-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
  
  .blog-actions {
    width: 100%;
    justify-content: flex-end;
  }
  
  .blog-cover-preview {
    width: 100%;
    height: 120px;
  }
}
</style>