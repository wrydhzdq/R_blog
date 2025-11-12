<!-- src/components/CommentSection.vue -->
<template>
  <div class="comment-section">
    <h3>💬 评论 ({{ comments.length }})</h3>
    
    <!-- 评论表单 -->
     <!--v-model="newComment.content" 双向绑定评论 -->
    <div class="comment-form">
      <textarea 
      
        v-model="newComment.content"
        placeholder="写下你的评论..."
        rows="3"
        class="comment-input"
        :disabled="submitting"
      ></textarea>
      <div class="form-footer">
        <input 
          v-model="newComment.userName"
          placeholder="你的名字"
          class="name-input"
          :disabled="submitting"
        >
        <button 
          @click="submitComment" 
          :disabled="!canSubmit || submitting"
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
          :disabled="deletingId === comment.id"
        >
          {{ deletingId === comment.id ? '删除中...' : '删除' }}
        </button>
      </div>

      <!-- 空状态 -->
      <div v-if="comments.length === 0 && !loading" class="no-comments">
        <p>还没有评论，快来抢沙发吧！</p>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="comments-loading">
        <p>加载评论中...</p>
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
const deletingId = ref(null)

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
         newComment.value.content.trim()
})

// 🔹 加载评论
const loadComments = async () => {
  try {
    loading.value = true
    console.log('正在加载评论，博客ID:', props.blogId)
    
    const result = await commentService.getCommentsByBlogId(props.blogId)
    
    if (result.code === 200) {
      comments.value = result.data || []
      console.log('✅ 评论加载成功:', comments.value.length, '条')
    } else {
      throw new Error(result.message || '获取评论失败')
    }
  } catch (err) {
    console.error('加载评论失败:', err)
    // 如果后端接口还没完全准备好，使用模拟数据
    comments.value = getMockComments()
  } finally {
    loading.value = false
  }
}

// 🔹 提交评论
const submitComment = async () => {
  if (!canSubmit.value) return
  
  try {
    submitting.value = true
    console.log('提交评论:', newComment.value)
    
    const result = await commentService.addComment(newComment.value)
    
    if (result.code === 200) {
      // 重新加载评论列表
      await loadComments()
      // 清空表单
      newComment.value.content = ''
      // 提示成功
      showMessage('评论发表成功！', 'success')
    }
  } catch (err) {
    console.error('提交评论失败:', err)
    showMessage('评论发表失败: ' + err.message, 'error')
  } finally {
    submitting.value = false
  }
}

// 🔹 删除评论
const deleteComment = async (commentId) => {
  if (!confirm('确定要删除这条评论吗？')) return
  
  try {
    deletingId.value = commentId
    console.log('删除评论:', commentId)
    
    const result = await commentService.deleteComment(commentId)
    
    if (result.code === 200) {
      // 从列表中移除
      comments.value = comments.value.filter(c => c.id !== commentId)
      showMessage('评论删除成功！', 'success')
    }
  } catch (err) {
    console.error('删除评论失败:', err)
    showMessage('删除失败: ' + err.message, 'error')
  } finally {
    deletingId.value = null
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

// 🔹 显示消息
const showMessage = (message, type = 'success') => {
  alert(type === 'success' ? '✅ ' + message : '❌ ' + message)
}

// 🔹 模拟评论数据（用于测试）
const getMockComments = () => {
  return [
    {
      id: 1,
      userName: '热心读者',
      content: '这篇文章写得真好！学到了很多新知识。',
      createTime: new Date().toISOString()
    },
    {
      id: 2, 
      userName: '技术小白',
      content: '讲解得很清晰，谢谢作者的分享！',
      createTime: new Date(Date.now() - 2 * 60 * 60 * 1000).toISOString()
    }
  ]
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
  font-size: 1.3em;
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
  font-size: 14px;
}

.comment-input:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
}

.comment-input:disabled {
  background: #f5f5f5;
  cursor: not-allowed;
}

.form-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
}

.name-input {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  width: 150px;
  font-size: 14px;
}

.name-input:focus {
  outline: none;
  border-color: #3498db;
}

.name-input:disabled {
  background: #f5f5f5;
}

.submit-btn {
  padding: 8px 20px;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background 0.3s;
}

.submit-btn:hover:not(:disabled) {
  background: #2980b9;
}

.submit-btn:disabled {
  background: #bdc3c7;
  cursor: not-allowed;
}

/* 评论列表 */
.comment-list {
  space-y: 12px;
}

.comment-item {
  background: white;
  border: 1px solid #e1e1e1;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 12px;
  transition: box-shadow 0.3s;
}

.comment-item:hover {
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
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
  white-space: pre-wrap;
}

.delete-btn {
  padding: 4px 8px;
  background: #e74c3c;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  transition: background 0.3s;
}

.delete-btn:hover:not(:disabled) {
  background: #c0392b;
}

.delete-btn:disabled {
  background: #95a5a6;
  cursor: not-allowed;
}

.no-comments {
  text-align: center;
  padding: 40px;
  color: #7f8c8d;
  background: #f8f9fa;
  border-radius: 8px;
}

.comments-loading {
  text-align: center;
  padding: 20px;
  color: #7f8c8d;
}
</style>