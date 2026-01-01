<template>
  <div class="users-page">
    <h2>用户管理</h2>
    <el-table :data="users" style="width: 100%; max-width: 1200px; margin: 0 auto;">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="realName" label="姓名" width="120" />
      <el-table-column prop="phone" label="电话" width="150" />
      <el-table-column prop="address" label="地址" min-width="200" />
      <el-table-column prop="userTypeName" label="用户类型" width="120">
        <template #default="{ row }">
          <el-tag :type="getUserTypeColor(row.userType)">{{ row.userTypeName }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="250">
        <template #default="{ row }">
          <el-button size="small" @click="handleUpdateType(row)">修改类型</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      @current-change="loadUsers"
      layout="total, prev, pager, next"
      style="margin-top: 20px; text-align: center"
    />

    <el-dialog v-model="typeVisible" title="修改用户类型" width="400px">
      <el-form :model="typeForm" label-width="80px">
        <el-form-item label="用户姓名">
          <el-input v-model="currentUser.realName" disabled />
        </el-form-item>
        <el-form-item label="当前类型">
          <el-tag :type="getUserTypeColor(currentUser.userType)">{{ currentUser.userTypeName }}</el-tag>
        </el-form-item>
        <el-form-item label="新类型">
          <el-select v-model="typeForm.userType" placeholder="请选择用户类型">
            <el-option label="普通用户" :value="0" />
            <el-option label="护工" :value="1" />
            <el-option label="管理员" :value="2" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="typeVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitType">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { pageUsers, updateUserType, deleteUser } from '@/api/user'

const users = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const typeVisible = ref(false)
const currentUser = ref({})
const typeForm = ref({
  userType: 0
})

onMounted(() => {
  loadUsers()
})

const loadUsers = async () => {
  try {
    const res = await pageUsers({
      current: currentPage.value,
      size: pageSize.value
    })
    users.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error(error)
  }
}

const getUserTypeColor = (type) => {
  const colors = {
    0: 'success',
    1: 'warning',
    2: 'danger'
  }
  return colors[type] || ''
}

const handleUpdateType = (user) => {
  currentUser.value = user
  typeForm.value.userType = user.userType
  typeVisible.value = true
}

const handleSubmitType = async () => {
  try {
    await updateUserType(currentUser.value.id, typeForm.value.userType)
    ElMessage.success('用户类型修改成功')
    typeVisible.value = false
    loadUsers()
  } catch (error) {
    console.error(error)
  }
}

const handleDelete = async (user) => {
  try {
    await ElMessageBox.confirm(`确认删除用户 ${user.realName} 吗？`, '提示', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteUser(user.id)
    ElMessage.success('用户删除成功')
    loadUsers()
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
    }
  }
}
</script>

<style scoped>
.users-page {
  padding: 30px;
  max-width: 1400px;
  margin: 0 auto;
  background: #f5f7fa;
  min-height: 100vh;
}

.users-page h2 {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
  font-weight: 500;
}

.users-page :deep(.el-table) {
  border-radius: 4px;
}

.users-page :deep(.el-table th) {
  background: #f5f7fa;
  color: #333;
  font-weight: 500;
}

.users-page :deep(.el-table tr:hover > td) {
  background-color: #f9f9f9;
}

.users-page :deep(.el-pagination) {
  justify-content: center;
}
</style>
