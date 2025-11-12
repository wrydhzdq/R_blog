// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import BlogListView from '@/views/BlogListView.vue'
import BlogDetailView from '@/views/BlogDetailView.vue'
import LoginView from '@/views/LoginView.vue'
import { useUserStore } from '@/stores/userStore'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: LoginView
  },
  {
    path: '/',
    name: 'Home',
    component: BlogListView,
    meta: { requiresAuth: true }  // 需要登录
  },
  {
    path: '/blog/:id',
    name: 'BlogDetail', 
    component: BlogDetailView,
    meta: { requiresAuth: true }  // 需要登录
  },
  {
    path: '/category/:categoryId',
    name: 'CategoryBlogs',
    component: BlogListView,
    props: (route) => ({ 
      categoryId: parseInt(route.params.categoryId) 
    }),
    meta: { requiresAuth: true }  // 需要登录
  },
  {
  path: '/manage',
  name: 'BlogManage',
  component: () => import('@/views/BlogManageView.vue'),
  meta: { requiresAuth: true }
}
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 添加路由守卫 - 检查登录状态
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  console.log('路由跳转:', from.path, '->', to.path)
  console.log('登录状态:', userStore.isLoggedIn)
  
  // 如果路由需要认证且用户未登录，跳转到登录页
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    console.log('未登录，跳转到登录页')
    next('/login')
  } else {
    // 如果已登录但访问登录页，跳转到首页
    if (to.path === '/login' && userStore.isLoggedIn) {
      console.log('已登录，跳转到首页')
      next('/')
    } else {
      next()
    }
  }
})

export default router