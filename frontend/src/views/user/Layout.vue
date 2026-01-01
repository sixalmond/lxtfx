<template>
  <div class="user-layout">
    <el-header class="header">
      <div class="header-content">
        <h1>养老服务平台 - 用户端</h1>
        <div class="header-actions">
          <div class="user-info">
            <span class="user-name">{{ username }}</span>
            <el-tag type="success" size="small">{{ userTypeText }}</el-tag>
          </div>
          <el-button :type="isHomeActive ? 'primary' : ''" @click="$router.push('/user/home')">首页</el-button>
          <el-button :type="isNursesActive ? 'primary' : ''" @click="$router.push('/user/nurses')">服务下单</el-button>
          <el-button :type="isForumActive ? 'primary' : ''" @click="$router.push('/user/forum')">社区论坛</el-button>
          <el-button :type="isOrdersActive ? 'primary' : ''" @click="$router.push('/user/orders')">我的订单</el-button>
          <el-button type="danger" @click="handleLogout">退出登录</el-button>
        </div>
      </div>
    </el-header>
    <el-main class="main">
      <router-view />
    </el-main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

const username = ref('')
const userType = ref('0')

const userTypeText = computed(() => {
  const typeMap = {
    '0': '普通用户',
    '1': '护工',
    '2': '管理员'
  }
  return typeMap[userType.value] || '未知'
})

const isNursesActive = computed(() => {
  return route.path === '/user/nurses'
})

const isOrdersActive = computed(() => {
  return route.path === '/user/orders'
})

const isHomeActive = computed(() => {
  return route.path === '/user/home'
})

const isForumActive = computed(() => {
  return route.path === '/user/forum'
})

onMounted(() => {
  username.value = localStorage.getItem('username') || ''
  userType.value = localStorage.getItem('userType') || '0'
})

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userType')
  localStorage.removeItem('username')
  localStorage.removeItem('userId')
  ElMessage.success('退出成功')
  router.push('/login')
}
</script>

<style scoped>
.user-layout {
  min-height: 100vh;
  background: #f5f5f5;
}

.header {
  background: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}

.header-content h1 {
  margin: 0;
  font-size: 24px;
  color: #333;
}

.header-actions {
  display: flex;
  gap: 10px;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-name {
  font-size: 16px;
  color: #333;
  font-weight: 500;
}

.main {
  padding: 20px;
}
</style>
