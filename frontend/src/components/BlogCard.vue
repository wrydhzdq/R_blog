<!-- src/components/BlogCard.vue -->
<template>
  <div class="blog-card" @click="handleClick">
    <!-- 封面图片 -->
    <div v-if="blog?.coverImage" class="blog-cover">
      <img :src="blog.coverImage" :alt="blog.title" />
    </div>
    
    <div class="blog-content">
      <!-- 标题和状态 -->
      <div class="blog-header">
        <h3 class="blog-title">{{ blog?.title || '未知标题' }}</h3>
        <span class="blog-status" :class="getStatusClass(blog?.status)">
          {{ getStatusText(blog?.status) }}
        </span>
      </div>
      
      <!-- 摘要 -->
      <p class="blog-summary">{{ blog?.summary || '暂无摘要' }}</p>
      
      <!-- 底部信息 -->
      <div class="blog-footer">
        <span class="view-count">👁️ {{ blog?.viewCount || 0 }} 阅读</span>
        <span class="create-time">📅 {{ formatDate(blog?.createTime) }}</span>
        
        <!-- 操作按钮 -->
        <div class="blog-actions" @click.stop>
          <button @click="handleLike" class="action-btn">👍 点赞</button>
          <button @click="handleShare" class="action-btn">📤 分享</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// 组件通信总结:父组件用子组件(就是这个卡片)显示界面 → 用户操作子组件 → 子组件emit通知父组件 → 父组件执行实际业务逻辑

// 1. 定义 Props - 接收父组件传递的数据
const props = defineProps({
  blog: {
    type: Object,
    required: true  // 必须传递
  }
})

// 2. 定义 Emits - 定义子组件可以触发的事件
const emit = defineEmits([
  'view-detail',  // 查看详情事件
  'like',         // 点赞事件  
  'share'         // 分享事件
])

// 3. 处理方法
const handleClick = () => {
  console.log('点击博客卡片:', props.blog?.id)
  // 向父组件发送 view-detail 事件，并传递博客ID
  emit('view-detail', props.blog?.id)
}

const handleLike = () => {
  console.log('点赞博客:', props.blog?.id)
  // 阻止事件冒泡，避免触发卡片点击
  emit('like', props.blog?.id)
}

const handleShare = () => {
  console.log('分享博客:', props.blog)
  emit('share', props.blog)
}

// 4. 辅助函数
const getStatusClass = (status) => {
  return status === 1 ? 'published' : 'draft'
}

const getStatusText = (status) => {
  return status === 1 ? '已发布' : '草稿'
}

const formatDate = (dateString) => {
  try {
    return new Date(dateString).toLocaleDateString('zh-CN')
  } catch {
    return '未知日期'
  }
}
</script>

<style scoped>
.blog-card {
  background: #34495e;
  border: 4px solid #2c3e50;
  border-radius: 12px;
  margin-bottom: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  overflow: hidden;
  font-family: 'Press Start 2P', cursive;
  color: #ecf0f1;
}

.blog-card:hover {
  transform: translateY(-4px);
  border-color: #e74c3c;
  box-shadow: 0 8px 25px rgba(231, 76, 60, 0.3);
}

.blog-cover {
  width: 100%;
  height: 160px;
  background: #2c3e50;
  overflow: hidden;
}

.blog-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.blog-content {
  padding: 20px;
}

.blog-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.blog-title {
  margin: 0;
  color: #ecf0f1;
  font-size: 0.9rem;
  line-height: 1.4;
  flex: 1;
}

.blog-status {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.6rem;
  font-weight: bold;
  margin-left: 10px;
  border: 2px solid;
}

.blog-status.published {
  background: #27ae60;
  color: white;
  border-color: #219653;
}

.blog-status.draft {
  background: #f39c12;
  color: white;
  border-color: #e67e22;
}

.blog-summary {
  color: #bdc3c7;
  line-height: 1.6;
  margin-bottom: 15px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  font-size: 0.7rem;
}

.blog-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.6rem;
  color: #95a5a6;
}

.view-count, .create-time {
  display: flex;
  align-items: center;
  gap: 4px;
}

.blog-actions {
  display: flex;
  gap: 8px;
}

.action-btn {
  padding: 6px 12px;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 0.6rem;
  cursor: pointer;
  transition: all 0.2s;
  border: 2px solid #2980b9;
  font-family: 'Press Start 2P', cursive;
}

.action-btn:hover {
  background: #2980b9;
  transform: translateY(-1px);
}

.action-btn:last-child {
  background: #9b59b6;
  border-color: #8e44ad;
}

.action-btn:last-child:hover {
  background: #8e44ad;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .blog-header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .blog-status {
    margin-left: 0;
    margin-top: 8px;
  }
  
  .blog-footer {
    flex-direction: column;
    gap: 10px;
    align-items: flex-start;
  }
  
  .blog-actions {
    width: 100%;
    justify-content: flex-end;
  }
}
</style>