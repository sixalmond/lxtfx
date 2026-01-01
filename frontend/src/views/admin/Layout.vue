<template>
  <div class="admin-layout">
    <el-header class="header">
      <div class="header-content">
        <h1>养老服务平台 - 管理端</h1>
        <div class="header-actions">
          <div class="user-info">
            <span class="user-name">{{ username }}</span>
            <el-tag type="danger" size="small">{{ userTypeText }}</el-tag>
          </div>
          <el-button type="primary" @click="$router.push('/admin/users')">用户管理</el-button>
          <el-button type="primary" @click="$router.push('/admin/nurses')">护工管理</el-button>
          <el-button type="primary" @click="$router.push('/admin/orders')">订单管理</el-button>
          <el-button type="primary" @click="$router.push('/admin/complaints')">投诉管理</el-button>
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
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()

const username = ref('')
const userType = ref('2')

const userTypeText = computed(() => {
  const typeMap = {
    '0': '普通用户',
    '1': '护工',
    '2': '管理员'
  }
  return typeMap[userType.value] || '未知'
})

onMounted(() => {
  username.value = localStorage.getItem('username') || ''
  userType.value = localStorage.getItem('userType') || '2'
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
.admin-layout {
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
}

.main {
  padding: 20px;
}
</style>
