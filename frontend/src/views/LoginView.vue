<!-- src/views/LoginView.vue -->
<template>
  <div class="login-container">
    <div class="gameboy-style">
      <div class="screen">
        <h1>🎮 R-Blog</h1>
        <p class="subtitle">博客系统登录</p>
        
        <form @submit.prevent="handleLogin" class="login-form">
          <div class="input-group">
            <label>👤 用户名</label>
            <input 
              v-model="form.username" 
              type="text" 
              placeholder="请输入用户名"
              required
              :disabled="loading"
            >
          </div>
          
          <div class="input-group">
            <label>🔒 密码</label>
            <input 
              v-model="form.password" 
              type="password" 
              placeholder="请输入密码"
              required
              :disabled="loading"
            >
          </div>
          
          <button 
            type="submit" 
            class="pixel-btn"
            :disabled="loading"
          >
            {{ loading ? '🎯 登录中...' : '🎮 开始游戏' }}
          </button>
        </form>
        
        <!-- 测试提示（开发阶段可以保留） -->
        <div class="test-tip">
          <p>测试账号: <strong>admin</strong> / <strong>123456</strong></p>
        </div>
        
        <div v-if="message" class="message" :class="messageType">
          {{ message }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import { adminService } from '@/services/adminService'

const router = useRouter()
const userStore = useUserStore()

// 清空预先填写的账号密码
const form = reactive({
  username: '',
  password: ''
})
const loading = ref(false)
const message = ref('')
const messageType = ref('')

onMounted(() => {
  if (userStore.isLoggedIn) {
    router.push('/')
  }
})

const handleLogin = async () => {
  if (!form.username || !form.password) {
    showMessage('请输入用户名和密码', 'error')
    return
  }

  loading.value = true
  message.value = ''

  try {
    console.log('调用登录接口:', form.username)
    
    const result = await adminService.login(form.username, form.password)
    console.log('登录响应:', result)
    
    if (result.code === 200 && result.data) {
      const userData = {
        id: result.data.id,
        username: result.data.username,
        nickname: result.data.nickname,
        loginTime: new Date().toISOString()
      }
      
      userStore.setUser(userData)
      showMessage('🎉 登录成功！', 'success')
      
      setTimeout(() => {
        router.push('/')
      }, 1000)
    } else {
      showMessage(`❌ ${result.message || '登录失败'}`, 'error')
    }
  } catch (error) {
    console.error('登录错误:', error)
    showMessage('🔌 登录失败，请检查用户名密码', 'error')
  } finally {
    loading.value = false
  }
}

const showMessage = (msg, type = 'info') => {
  message.value = msg
  messageType.value = type
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #ff6b6b 0%, #4ecdc4 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.gameboy-style {
  background: #2c3e50;
  border: 8px solid #34495e;
  border-radius: 20px;
  padding: 2rem;
  width: 100%;
  max-width: 400px;
  box-shadow: 0 20px 40px rgba(0,0,0,0.3);
}

.screen {
  background: #8bbc9c;
  border: 6px solid #74a580;
  border-radius: 8px;
  padding: 2rem;
  text-align: center;
}

h1 {
  color: #2c3e50;
  margin-bottom: 0.5rem;
  font-size: 1.8rem;
}

.subtitle {
  color: #34495e;
  margin-bottom: 2rem;
  font-weight: bold;
}

.login-form {
  text-align: left;
}

.input-group {
  margin-bottom: 1.5rem;
}

label {
  display: block;
  color: #2c3e50;
  margin-bottom: 0.5rem;
  font-weight: bold;
  font-size: 0.9rem;
}

input {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #34495e;
  border-radius: 8px;
  background: white;
  font-size: 16px;
  box-sizing: border-box;
}

input:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.2);
}

input:disabled {
  background: #f5f5f5;
  cursor: not-allowed;
}

.pixel-btn {
  width: 100%;
  padding: 16px;
  background: #e74c3c;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 18px;
  cursor: pointer;
  margin-top: 1rem;
  font-weight: bold;
  transition: all 0.3s ease;
}

.pixel-btn:hover:not(:disabled) {
  background: #c0392b;
  transform: translateY(-2px);
}

.pixel-btn:disabled {
  background: #7f8c8d;
  cursor: not-allowed;
  transform: none;
}

.test-tip {
  margin-top: 1rem;
  padding: 10px;
  background: rgba(52, 152, 219, 0.2);
  border-radius: 6px;
  font-size: 0.8rem;
  color: #2c3e50;
}

.test-tip p {
  margin: 0;
}

.message {
  margin-top: 1rem;
  padding: 12px;
  border-radius: 6px;
  text-align: center;
  font-weight: bold;
}

.message.success {
  background: #27ae60;
  color: white;
}

.message.error {
  background: #e74c3c;
  color: white;
}

.message.info {
  background: #3498db;
  color: white;
}
</style>