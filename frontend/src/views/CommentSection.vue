<!-- src/components/CommentSection.vue -->
<template>
  <div class="comment-section">
    <h3>💬 评论 ({{ comments.length }})</h3>
    
    <!-- 评论表单 -->
    <div class="comment-form">
      <textarea 
        v-model="newComment.content"
        placeholder="写下你的评论..."
        rows="3"
        class="comment-input"
      ></textarea>
      <div class="form-footer">
        <input 
          v-model="newComment.userName"
          placeholder="你的名字"
          class="name-input"
        >
        <button 
          @click="submitComment" 
          :disabled="!canSubmit"
          class="submit-btn"
        >
          {{ submitting ? '提交中...' : '发表评论' }}
        </button>
      </div>
    </div>

    <!-- 评论列表 -->
    <div class="comment-list">
      <div 
        v-for="comment in comments" 
        :key="comment.id" 
        class="comment-item"
      >
        <div class="comment-header">
          <span class="user-name">{{ comment.userName }}</span>
          <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
        </div>
        <p class="comment-content">{{ comment.content }}</p>
        <button 
          @click="deleteComment(comment.id)"
          class="delete-btn"
        >
          删除
        </button>
      </div>

      <!-- 空状态 -->
      <div v-if="comments.length === 0" class="no-comments">
        <p>还没有评论，快来抢沙发吧！</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { commentService } from '@/services/commentService'

const props = defineProps({
  blogId: {
    type: Number,
    required: true
  }
})

// 响应式数据
const comments = ref([])
const loading = ref(false)
const submitting = ref(false)

// 新评论数据
const newComment = ref({
  blogId: props.blogId,
  userName: '',
  content: '',
  userId: 1 // 暂时写死，后面接用户系统
})

// 计算属性：是否可以提交
const canSubmit = computed(() => {
  return newComment.value.userName.trim() && 
         newComment.value.content.trim() &&
         !submitting.value
})

// 🔹 加载评论
const loadComments = async () => {
  try {
    loading.value = true
    console.log('正在加载评论...')
    
    const result = await commentService.getCommentsByBlogId(props.blogId)
    
    if (result.code === 200) {
      comments.value = result.data || []
      console.log('✅ 评论加载成功:', comments.value.length, '条')
    } else {
      throw new Error(result.message || '获取评论失败')
    }
  } catch (err) {
    console.error('加载评论失败:', err)
    // 模拟数据用于测试
    comments.value = [
      {
        id: 1,
        userName: '热心读者',
        content: '这篇文章写得真好！',
        createTime: '2024-01-15T10:30:00'
      },
      {
        id: 2, 
        userName: '技术小白',
        content: '学到了很多新知识，谢谢分享！',
        createTime: '2024-01-15T11:20:00'
      }
    ]
  } finally {
    loading.value = false
  }
}

// 🔹 提交评论
const submitComment = async () => {
  try {
    submitting.value = true
    console.log('提交评论:', newComment.value)
    
    const result = await commentService.addComment(newComment.value)
    
    if (result.code === 200) {
      // 重新加载评论列表
      await loadComments()
      // 清空表单
      newComment.value.content = ''
      alert('评论发表成功！')
    }
  } catch (err) {
    console.error('提交评论失败:', err)
    alert('评论发表失败: ' + err.message)
  } finally {
    submitting.value = false
  }
}

// 🔹 删除评论
const deleteComment = async (commentId) => {
  if (!confirm('确定要删除这条评论吗？')) return
  
  try {
    console.log('删除评论:', commentId)
    
    const result = await commentService.deleteComment(commentId)
    
    if (result.code === 200) {
      // 从列表中移除
      comments.value = comments.value.filter(c => c.id !== commentId)
      alert('评论删除成功！')
    }
  } catch (err) {
    console.error('删除评论失败:', err)
    alert('删除失败: ' + err.message)
  }
}

// 🔹 格式化时间
const formatTime = (timeString) => {
  try {
    return new Date(timeString).toLocaleString('zh-CN', {
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    })
  } catch {
    return '未知时间'
  }
}

onMounted(() => {
  console.log('CommentSection 组件已挂载，博客ID:', props.blogId)
  loadComments()
})
</script>

<style scoped>
.comment-section {
  margin-top: 40px;
  padding-top: 30px;
  border-top: 2px solid #e1e1e1;
}

.comment-section h3 {
  color: #2c3e50;
  margin-bottom: 20px;
}

/* 评论表单 */
.comment-form {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 30px;
}

.comment-input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  resize: vertical;
  font-family: inherit;
  margin-bottom: 10px;
}

.comment-input:focus {
  outline: none;
  border-color: #3498db;
}

.form-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.name-input {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  width: 150px;
}

.submit-btn {
  padding: 8px 20px;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.submit-btn:disabled {
  background: #bdc3c7;
  cursor: not-allowed;
}

.submit-btn:hover:not(:disabled) {
  background: #2980b9;
}

/* 评论列表 */
.comment-item {
  background: white;
  border: 1px solid #e1e1e1;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 12px;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.user-name {
  font-weight: bold;
  color: #2c3e50;
}

.comment-time {
  color: #7f8c8d;
  font-size: 12px;
}

.comment-content {
  color: #34495e;
  line-height: 1.5;
  margin-bottom: 10px;
}

.delete-btn {
  padding: 4px 8px;
  background: #e74c3c;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
}

.delete-btn:hover {
  background: #c0392b;
}

.no-comments {
  text-align: center;
  padding: 40px;
  color: #7f8c8d;
  background: #f8f9fa;
  border-radius: 8px;
}
</style>