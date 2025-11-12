<!-- src/components/BlogSearch.vue -->
<template>
  <div class="blog-search">
    <div class="search-container">
      <input
        v-model="searchKeyword"
        @input="handleSearchInput"
        @keyup.enter="emitSearch"
        @focus="showSuggestions = true"
        placeholder="搜索博客标题..."
        class="search-input"
        type="text"
      />
      <button @click="emitSearch" class="search-btn">
        🔍 搜索
      </button>
      <button v-if="searchKeyword" @click="clearSearch" class="clear-btn" title="清空搜索">
        ✕
      </button>
    </div>

    <!-- 搜索建议 -->
    <div v-if="showSuggestions && searchKeyword && searchSuggestions.length > 0" class="search-suggestions">
      <div 
        v-for="suggestion in searchSuggestions" 
        :key="suggestion"
        @click="selectSuggestion(suggestion)"
        class="suggestion-item"
      >
        🔍 {{ suggestion }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'

const emit = defineEmits(['search', 'clear'])

// 响应式数据
const searchKeyword = ref('')
const showSuggestions = ref(false)

// 搜索建议
const searchSuggestions = computed(() => {
  if (!searchKeyword.value) return []
  
  const suggestions = ['Vue3', 'Spring Boot', 'Redis', '数据库', '前端开发', '后端开发', '技术文章', '学习笔记']
  return suggestions.filter(s => s.toLowerCase().includes(searchKeyword.value.toLowerCase())).slice(0, 5)
})

// 处理输入（防抖）
let searchTimer = null
const handleSearchInput = () => {
  clearTimeout(searchTimer)
  showSuggestions.value = true
  
  searchTimer = setTimeout(() => {
    if (searchKeyword.value.trim().length >= 2) {
      emitSearch()
    }
  }, 800)
}

// 发射搜索事件
const emitSearch = () => {
  if (searchKeyword.value.trim()) {
    showSuggestions.value = false
    emit('search', searchKeyword.value.trim())
  }
}

// 选择建议
const selectSuggestion = (suggestion) => {
  searchKeyword.value = suggestion
  emitSearch()
}

// 清空搜索
const clearSearch = () => {
  searchKeyword.value = ''
  showSuggestions.value = false
  emit('clear')
}

// 点击外部关闭建议
const handleClickOutside = (event) => {
  if (!event.target.closest('.blog-search')) {
    showSuggestions.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})
</script>

<style scoped>
.blog-search {
  position: relative;
  margin-bottom: 30px;
}

.search-container {
  display: flex;
  align-items: center;
  gap: 10px;
  position: relative;
  max-width: 600px;
  margin: 0 auto;
}

.search-input {
  flex: 1;
  padding: 12px 20px;
  border: 2px solid #e1e1e1;
  border-radius: 25px;
  font-size: 16px;
  outline: none;
  transition: all 0.3s ease;
  background: white;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.search-input:focus {
  border-color: #3498db;
  box-shadow: 0 2px 12px rgba(52, 152, 219, 0.3);
  transform: translateY(-1px);
}

.search-btn {
  padding: 12px 24px;
  background: linear-gradient(135deg, #3498db, #2980b9);
  color: white;
  border: none;
  border-radius: 25px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 600;
  transition: all 0.3s ease;
  white-space: nowrap;
}

.search-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(52, 152, 219, 0.4);
}

.clear-btn {
  position: absolute;
  right: 130px;
  background: none;
  border: none;
  font-size: 18px;
  color: #7f8c8d;
  cursor: pointer;
  padding: 8px;
  border-radius: 50%;
  transition: all 0.3s ease;
}

.clear-btn:hover {
  background: #f8f9fa;
  color: #e74c3c;
}

/* 搜索建议 */
.search-suggestions {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: white;
  border: 1px solid #e1e1e1;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  z-index: 1000;
  margin-top: 5px;
  max-height: 200px;
  overflow-y: auto;
}

.suggestion-item {
  padding: 12px 16px;
  cursor: pointer;
  border-bottom: 1px solid #f0f0f0;
  transition: background 0.2s ease;
  font-size: 14px;
}

.suggestion-item:hover {
  background: #f8f9fa;
  color: #3498db;
}

.suggestion-item:last-child {
  border-bottom: none;
}
</style>