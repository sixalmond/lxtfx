<template>
  <div class="nurses-page">
    <h2>护工管理</h2>
    <el-table :data="nurses" style="width: 100%; max-width: 1200px; margin: 0 auto;">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="姓名" width="120" />
      <el-table-column prop="age" label="年龄" width="80" />
      <el-table-column prop="gradeName" label="等级" width="100" />
      <el-table-column prop="price" label="月佣金" width="120">
        <template #default="{ row }">
          ￥{{ row.price }}
        </template>
      </el-table-column>
      <el-table-column prop="statusName" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">{{ row.statusName }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="skills" label="技能" min-width="200" />
      <el-table-column label="操作" width="250">
        <template #default="{ row }">
          <el-button size="small" @click="handleUpdateStatus(row)">修改状态</el-button>
          <el-button size="small" type="primary" @click="handleUpdateGrade(row)">修改等级</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      @current-change="loadNurses"
      layout="total, prev, pager, next"
      style="margin-top: 20px; text-align: center"
    />

    <el-dialog v-model="statusVisible" title="修改护工状态" width="400px">
      <el-form :model="statusForm" label-width="80px">
        <el-form-item label="护工姓名">
          <el-input v-model="currentNurse.name" disabled />
        </el-form-item>
        <el-form-item label="当前状态">
          <el-tag :type="getStatusType(currentNurse.status)">{{ currentNurse.statusName }}</el-tag>
        </el-form-item>
        <el-form-item label="新状态">
          <el-select v-model="statusForm.status" placeholder="请选择状态">
            <el-option label="空闲" :value="0" />
            <el-option label="接单中" :value="1" />
            <el-option label="休息" :value="2" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="statusVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitStatus">确认</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="gradeVisible" title="修改护工等级" width="400px">
      <el-form :model="gradeForm" label-width="80px">
        <el-form-item label="护工姓名">
          <el-input v-model="currentNurse.name" disabled />
        </el-form-item>
        <el-form-item label="当前等级">
          <el-tag>{{ currentNurse.gradeName }}</el-tag>
        </el-form-item>
        <el-form-item label="新等级">
          <el-select v-model="gradeForm.grade" placeholder="请选择等级">
            <el-option label="初级" :value="1" />
            <el-option label="中级" :value="2" />
            <el-option label="高级" :value="3" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="gradeVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitGrade">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { pageNurses, updateNurseStatus, updateNurseGrade } from '@/api/nurse'

const nurses = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const statusVisible = ref(false)
const gradeVisible = ref(false)
const currentNurse = ref({})
const statusForm = ref({
  status: 0
})
const gradeForm = ref({
  grade: 1
})

onMounted(() => {
  loadNurses()
})

const loadNurses = async () => {
  try {
    const res = await pageNurses({
      current: currentPage.value,
      size: pageSize.value
    })
    nurses.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error(error)
  }
}

const getStatusType = (status) => {
  const types = {
    0: 'success',
    1: 'warning',
    2: 'info'
  }
  return types[status] || ''
}

const handleUpdateStatus = (nurse) => {
  currentNurse.value = nurse
  statusForm.value.status = nurse.status
  statusVisible.value = true
}

const handleSubmitStatus = async () => {
  try {
    await updateNurseStatus(currentNurse.value.id, statusForm.value.status)
    ElMessage.success('状态修改成功')
    statusVisible.value = false
    loadNurses()
  } catch (error) {
    console.error(error)
  }
}

const handleUpdateGrade = (nurse) => {
  currentNurse.value = nurse
  gradeForm.value.grade = nurse.grade
  gradeVisible.value = true
}

const handleSubmitGrade = async () => {
  try {
    await updateNurseGrade(currentNurse.value.id, gradeForm.value.grade)
    ElMessage.success('等级修改成功')
    gradeVisible.value = false
    loadNurses()
  } catch (error) {
    console.error(error)
  }
}
</script>

<style scoped>
.nurses-page {
  padding: 30px;
  max-width: 1400px;
  margin: 0 auto;
  background: #f5f7fa;
  min-height: 100vh;
}

.nurses-page h2 {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
  font-weight: 500;
}

.nurses-page :deep(.el-table) {
  border-radius: 4px;
}

.nurses-page :deep(.el-table th) {
  background: #f5f7fa;
  color: #333;
  font-weight: 500;
}

.nurses-page :deep(.el-table tr:hover > td) {
  background-color: #f9f9f9;
}

.nurses-page :deep(.el-pagination) {
  justify-content: center;
}
</style>
