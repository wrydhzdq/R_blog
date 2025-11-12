<!-- src/components/ImageUpload.vue -->
<template>
  <div class="image-upload">
    <!-- 图片预览 -->
    <div v-if="imageUrl" class="image-preview">
      <img :src="imageUrl" :alt="previewAlt" class="preview-image" />
      <div class="preview-actions">
        <button @click="removeImage" class="pixel-btn delete-btn" type="button" :disabled="uploading">
          🗑️ 删除
        </button>
      </div>
    </div>

    <!-- 上传区域 -->
    <div v-else class="upload-area" @click="triggerFileInput" :class="{ 'is-dragover': isDragover }">
      <input
        ref="fileInput"
        type="file"
        @change="handleFileSelect"
        @dragenter="isDragover = true"
        @dragleave="isDragover = false"
        @dragover.prevent
        @drop="handleDrop"
        accept="image/*"
        class="file-input"
        :disabled="uploading"
      />
      <div class="upload-content">
        <div class="upload-icon">📷</div>
        <p class="upload-text">{{ uploading ? '🎯 上传中...' : '点击或拖拽图片到此处' }}</p>
        <p class="upload-hint">支持 JPG、PNG、GIF，最大 5MB</p>
      </div>
    </div>

    <!-- 上传进度 -->
    <div v-if="uploading" class="upload-progress">
      <div class="progress-bar">
        <div class="progress-fill" :style="{ width: progress + '%' }"></div>
      </div>
      <p class="progress-text">上传中... {{ progress }}%</p>
    </div>

    <!-- 错误提示 -->
    <div v-if="error" class="upload-error">
      💥 {{ error }}
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { fileService } from '@/services/fileService'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  previewAlt: {
    type: String,
    default: '预览图片'
  },
  blogId: {
    type: Number,
    default: null
  }
})

const emit = defineEmits(['update:modelValue', 'upload', 'error'])

const fileInput = ref(null)
const imageUrl = ref(props.modelValue)
const uploading = ref(false)
const progress = ref(0)
const error = ref('')
const isDragover = ref(false)

// 监听modelValue变化
watch(() => props.modelValue, (newVal) => {
  imageUrl.value = newVal
})

// 触发文件选择
const triggerFileInput = () => {
  if (!uploading.value) {
    fileInput.value?.click()
  }
}

// 处理文件选择
const handleFileSelect = (event) => {
  const file = event.target.files[0]
  if (file) {
    validateAndUpload(file)
  }
  event.target.value = ''
}

// 处理拖放
const handleDrop = (event) => {
  event.preventDefault()
  isDragover.value = false
  
  if (uploading.value) return
  
  const files = event.dataTransfer.files
  if (files.length > 0) {
    const file = files[0]
    validateAndUpload(file)
  }
}

// 验证并上传文件
const validateAndUpload = async (file) => {
  error.value = ''

  // 验证文件类型
  const validTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'image/webp']
  if (!validTypes.includes(file.type)) {
    error.value = '只支持 JPG、PNG、GIF 格式的图片'
    emit('error', error.value)
    return
  }

  // 验证文件大小 (5MB)
  const maxSize = 5 * 1024 * 1024
  if (file.size > maxSize) {
    error.value = '图片大小不能超过 5MB'
    emit('error', error.value)
    return
  }

  // 预览图片
  const reader = new FileReader()
  reader.onload = (e) => {
    imageUrl.value = e.target.result
  }
  reader.readAsDataURL(file)

  // 上传文件到服务器
  await uploadFile(file)
}

// 上传文件到服务器
const uploadFile = async (file) => {
  try {
    uploading.value = true
    progress.value = 0
    error.value = ''

    // 模拟进度（实际进度需要后端支持）
    const progressInterval = setInterval(() => {
      if (progress.value < 90) {
        progress.value += 10
      }
    }, 200)

    let result
    console.log('开始上传图片:', file.name, '大小:', file.size, '类型:', file.type)

    if (props.blogId) {
      // 上传博客封面
      console.log('上传博客封面，博客ID:', props.blogId)
      result = await fileService.uploadBlogCover(props.blogId, file)
    } else {
      // 通用图片上传
      console.log('通用图片上传')
      result = await fileService.uploadImage(file)
    }

    clearInterval(progressInterval)
    progress.value = 100

    console.log('上传成功，返回URL:', result)

    // 使用后端返回的真实URL
    imageUrl.value = result
    emit('update:modelValue', result)
    emit('upload', result)

    // 完成
    setTimeout(() => {
      uploading.value = false
      progress.value = 0
    }, 500)

  } catch (err) {
    console.error('上传失败:', err)
    error.value = '上传失败: ' + (err.message || '请检查网络连接')
    emit('error', err.message)
    uploading.value = false
    progress.value = 0
    // 上传失败时清除预览
    imageUrl.value = ''
    emit('update:modelValue', '')
  }
}

// 删除图片
const removeImage = () => {
  if (!uploading.value) {
    imageUrl.value = ''
    emit('update:modelValue', '')
    error.value = ''
  }
}
</script>

<style scoped>
.image-upload {
  font-family: 'Press Start 2P', cursive;
}

/* 上传区域 */
.upload-area {
  border: 3px dashed #34495e;
  border-radius: 12px;
  padding: 3rem 2rem;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #2c3e50;
  position: relative;
}

.upload-area:hover:not(:has(.file-input:disabled)) {
  border-color: #3498db;
  background: #34495e;
}

.upload-area.is-dragover {
  border-color: #27ae60;
  background: rgba(39, 174, 96, 0.1);
}

.file-input {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
  cursor: pointer;
}

.file-input:disabled {
  cursor: not-allowed;
}

.upload-content {
  pointer-events: none;
}

.upload-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.upload-text {
  color: #ecf0f1;
  margin-bottom: 0.5rem;
  font-size: 0.8rem;
}

.upload-hint {
  color: #95a5a6;
  font-size: 0.6rem;
  margin: 0;
}

/* 图片预览 */
.image-preview {
  border: 3px solid #34495e;
  border-radius: 12px;
  overflow: hidden;
  background: #2c3e50;
}

.preview-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  display: block;
}

.preview-actions {
  padding: 1rem;
  text-align: center;
  background: #34495e;
}

.delete-btn {
  background: #e74c3c;
  color: white;
  border: 2px solid #c0392b;
  padding: 8px 16px;
  border-radius: 6px;
  font-size: 0.6rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.delete-btn:hover:not(:disabled) {
  background: #c0392b;
  transform: translateY(-1px);
}

.delete-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

/* 上传进度 */
.upload-progress {
  margin-top: 1rem;
  padding: 1rem;
  background: #34495e;
  border-radius: 8px;
  border: 2px solid #2c3e50;
}

.progress-bar {
  width: 100%;
  height: 20px;
  background: #2c3e50;
  border-radius: 10px;
  overflow: hidden;
  margin-bottom: 0.5rem;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #3498db, #27ae60);
  border-radius: 10px;
  transition: width 0.3s ease;
}

.progress-text {
  color: #ecf0f1;
  font-size: 0.6rem;
  margin: 0;
  text-align: center;
}

/* 错误提示 */
.upload-error {
  margin-top: 1rem;
  padding: 1rem;
  background: #e74c3c;
  color: white;
  border-radius: 8px;
  border: 2px solid #c0392b;
  font-size: 0.7rem;
  text-align: center;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .upload-area {
    padding: 2rem 1rem;
  }
  
  .upload-icon {
    font-size: 2rem;
  }
  
  .upload-text {
    font-size: 0.7rem;
  }
  
  .upload-hint {
    font-size: 0.5rem;
  }
}
</style>