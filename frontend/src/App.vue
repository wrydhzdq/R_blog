<!-- src/App.vue -->
<template>
  <div id="app">
    <AppHeader v-if="userStore.isLoggedIn" />
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import AppHeader from '@/components/AppHeader.vue'
import { useUserStore } from '@/stores/userStore'

const userStore = useUserStore()

onMounted(() => {
  console.log('App初始化完成，登录状态:', userStore.isLoggedIn)
})
</script>

<style>
/* 导入像素字体 */
@import url('https://fonts.googleapis.com/css2?family=Press+Start+2P&display=swap');

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Press Start 2P', cursive, 'Microsoft YaHei', sans-serif;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  line-height: 1.6;
  min-height: 100vh;
  color: #2c3e50;
  image-rendering: pixelated;
}

#app {
  min-height: 100vh;
}

.main-content {
  padding: 0;
}

/* 像素风滚动条 */
::-webkit-scrollbar {
  width: 12px;
}

::-webkit-scrollbar-track {
  background: #2c3e50;
  border: 2px solid #34495e;
}

::-webkit-scrollbar-thumb {
  background: #e74c3c;
  border-radius: 0;
  border: 2px solid #c0392b;
}

::-webkit-scrollbar-thumb:hover {
  background: #c0392b;
}

/* 像素风按钮基础样式 */
.pixel-btn {
  font-family: 'Press Start 2P', cursive;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease;
  image-rendering: pixelated;
  border: 3px solid;
}

.pixel-btn:hover {
  transform: translateY(-2px);
}

.pixel-btn:active {
  transform: translateY(0);
}

.pixel-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

/* 像素风输入框 */
.pixel-input {
  font-family: 'Press Start 2P', cursive;
  border: 3px solid #34495e;
  background: white;
  padding: 12px 16px;
  font-size: 14px;
  image-rendering: pixelated;
}

.pixel-input:focus {
  outline: none;
  border-color: #3498db;
  box-shadow: 0 0 0 2px rgba(52, 152, 219, 0.3);
}

/* 像素风卡片 */
.pixel-card {
  background: #34495e;
  border: 4px solid #2c3e50;
  border-radius: 12px;
  transition: all 0.3s ease;
  image-rendering: pixelated;
}

.pixel-card:hover {
  transform: translateY(-4px);
  border-color: #e74c3c;
  box-shadow: 0 8px 25px rgba(231, 76, 60, 0.3);
}

/* 游戏机屏幕效果 */
.gameboy-screen {
  background: #8bbc9c;
  border: 6px solid #74a580;
  border-radius: 12px;
  image-rendering: pixelated;
}

/* 像素风文字 */
.pixel-text {
  font-family: 'Press Start 2P', cursive;
  image-rendering: pixelated;
}

/* 响应式设计 */
@media (max-width: 768px) {
  body {
    font-size: 12px;
  }
  
  .pixel-btn {
    padding: 10px 16px;
    font-size: 12px;
  }
  
  .pixel-input {
    padding: 10px 12px;
    font-size: 12px;
  }
}

/* 动画效果 */
@keyframes pixel-blink {
  0%, 50% { opacity: 1; }
  51%, 100% { opacity: 0; }
}

@keyframes pixel-float {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-10px); }
}

@keyframes pixel-shake {
  0%, 100% { transform: translateX(0); }
  25% { transform: translateX(-5px); }
  75% { transform: translateX(5px); }
}

.pixel-blink {
  animation: pixel-blink 1.4s infinite;
}

.pixel-float {
  animation: pixel-float 3s ease-in-out infinite;
}

.pixel-shake {
  animation: pixel-shake 0.5s ease-in-out;
}

/* 游戏风格颜色变量 */
:root {
  --pixel-red: #e74c3c;
  --pixel-blue: #3498db;
  --pixel-green: #27ae60;
  --pixel-yellow: #f39c12;
  --pixel-purple: #9b59b6;
  --pixel-dark: #2c3e50;
  --pixel-light: #ecf0f1;
  --pixel-screen: #8bbc9c;
}

/* 像素风徽章 */
.pixel-badge {
  font-family: 'Press Start 2P', cursive;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.6rem;
  border: 2px solid;
  image-rendering: pixelated;
}

.pixel-badge.success {
  background: var(--pixel-green);
  border-color: #219653;
  color: white;
}

.pixel-badge.warning {
  background: var(--pixel-yellow);
  border-color: #e67e22;
  color: white;
}

.pixel-badge.danger {
  background: var(--pixel-red);
  border-color: #c0392b;
  color: white;
}

.pixel-badge.info {
  background: var(--pixel-blue);
  border-color: #2980b9;
  color: white;
}

/* 链接样式 */
.pixel-link {
  color: var(--pixel-blue);
  text-decoration: none;
  border-bottom: 2px solid transparent;
  transition: all 0.3s ease;
}

.pixel-link:hover {
  border-bottom-color: var(--pixel-blue);
  color: #2980b9;
}

/* 选择器样式 */
.pixel-select {
  font-family: 'Press Start 2P', cursive;
  border: 3px solid #34495e;
  background: white;
  padding: 10px 12px;
  font-size: 14px;
  image-rendering: pixelated;
  appearance: none;
  background-image: url("data:image/svg+xml;charset=US-ASCII,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 4 5'><path fill='%23333' d='M2 0L0 2h4zm0 5L0 3h4z'/></svg>");
  background-repeat: no-repeat;
  background-position: right 12px center;
  background-size: 12px;
}

.pixel-select:focus {
  outline: none;
  border-color: var(--pixel-blue);
}
</style>