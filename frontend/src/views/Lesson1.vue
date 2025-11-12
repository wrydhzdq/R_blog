<template>
    <div class="lesson-container">
        <h1>🎯 Vue3 第一课：基础语法</h1>

        <!-- 1. 数据绑定 -->
        <div class="section">
            <h2>1. 数据绑定</h2>
            <p>消息：<strong>{{ message }}</strong></p>
            <p>计数：<span class="count">{{ count }}</span></p>
        </div>

        <!-- 2. 事件处理 -->
        <div class="section">
            <h2>2. 事件处理</h2>
            <button @click="addCount" class="btn">+1</button>
            <button @click="minusCount" class="btn">-1</button>
            <button @click="resetCount" class="btn reset">重置</button>
        </div>

        <!-- 3. 输入框绑定 -->
        <!-- placeholder绑定到 inputText 变量 -->
        <div class="section">
            <h2>3. 输入框数据绑定</h2>
            <input v-model="inputText" placeholder="在这里输入文字..." class="input">
            <p>实时显示：{{ inputText }}</p>
        </div>

        <!-- 4. 条件显示 -->
        <div class="section">
            <h2>4. 条件显示</h2>
            <p v-if="count > 5" class="success">🎉 计数大于5！很棒！</p>
            <p v-else-if="count < 0" class="error">❌ 计数不能小于0！</p>
            <p v-else class="info">💪 继续点击按钮增加计数</p>
        </div>

        <!-- 5. 列表显示 -->
        <div class="section">
            <h2>5. 列表显示</h2>
            <ul>
                <li v-for="(item, index) in items" :key="index" class="list-item">
                    {{ index + 1 }}. {{ item }}
                </li>
            </ul>
            <button @click="addItem" class="btn">添加水果</button>
        </div>
    </div>
</template>

<script setup>
// 导入需要的功能
import { ref } from 'vue'

// 1. 定义数据（响应式变量）
const message = ref('欢迎学习Vue3！')
const count = ref(0)
const inputText = ref('')
const items = ref(['苹果', '香蕉', '橙子'])

// 2. 定义方法
function addCount() {
    count.value++
    console.log('计数增加到：', count.value)
}

function minusCount() {
    if (count.value > 0) {
        count.value--
        console.log('计数减少到：', count.value)
    }
}

function resetCount() {
    count.value = 0
    console.log('计数已重置')
}

function addItem() {
    const fruits = ['草莓', '葡萄', '西瓜', '菠萝', '芒果']
    const randomFruit = fruits[Math.floor(Math.random() * fruits.length)]
    items.value.push(randomFruit)
    console.log('添加了：', randomFruit)
}
</script>

<style scoped>
/* 盒子模型 */
.lesson-container {
    /* 最大宽度 */
    max-width: 800px;
    /* 为边距 居中对齐 */
    margin: 0 auto;
    /* 内边距 */
    padding: 20px;
    font-family: Arial, sans-serif;
}

.section {
    margin: 30px 0;
    padding: 20px;
    /* 边框 */
    border: 2px solid #e1e1e1;
    /* 圆角 */
    border-radius: 10px;
    background: #f9f9f9;
}

h1 {
    color: #2c3e50;
    /* 居中对齐 */
    text-align: center;
    margin-bottom: 30px;
}

h2 {
    color: #34495e;
    border-left: 4px solid #3498db;
    padding-left: 10px;
}

.btn {
    padding: 10px 20px;
    margin: 5px;
    border: none;
    border-radius: 5px;
    /* 鼠标悬停时的背景颜色 */
    background: #3498db;
    color: white;
    cursor: pointer;
    font-size: 16px;
}

.btn:hover {
    background: #2980b9;
}

.reset {
    background: #e74c3c;
}

.reset:hover {
    background: #c0392b;
}

.input {
    padding: 10px;
    border: 2px solid #bdc3c7;
    border-radius: 5px;
    font-size: 16px;
    width: 300px;
}

.count {
    font-size: 24px;
    font-weight: bold;
    color: #e74c3c;
}

.success {
    color: #27ae60;
    font-weight: bold;
}

.error {
    color: #e74c3c;
    font-weight: bold;
}

.info {
    color: #f39c12;
    font-weight: bold;
}

.list-item {
    padding: 5px;
    margin: 5px 0;
    background: white;
    border-radius: 3px;
}
</style>