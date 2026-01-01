<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>养老服务平台 - 登录</h2>
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="用户名/手机号" prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="密码" prefix-icon="Lock" />
        </el-form-item>
        <el-form-item prop="userType">
          <el-select v-model="loginForm.userType" placeholder="请选择身份" style="width: 100%">
            <el-option label="普通用户" value="0" />
            <el-option label="护工" value="1" />
            <el-option label="管理员" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" style="width: 100%">登录</el-button>
        </el-form-item>
        <el-form-item>
          <el-link type="primary" @click="$router.push('/register')">还没有账号？去注册</el-link>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api/auth'

const router = useRouter()
const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: '',
  userType: '0'
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  userType: [{ required: true, message: '请选择身份', trigger: 'change' }]
}

const handleLogin = async () => {
  await loginFormRef.value.validate()
  loading.value = true
  try {
    const res = await login(loginForm)
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('userType', res.data.userType)
    localStorage.setItem('username', res.data.username)
    localStorage.setItem('userId', res.data.userId)
    ElMessage.success('登录成功')
    
    const userType = res.data.userType
    if (userType === '0') {
      router.push('/user')
    } else if (userType === '1') {
      router.push('/nurse')
    } else if (userType === '2') {
      router.push('/admin')
    }
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 400px;
}

.login-card h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}
</style>
