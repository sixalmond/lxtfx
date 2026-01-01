<template>
  <div class="complaints-page">
    <h2>投诉管理</h2>
    <el-table :data="complaints" style="width: 100%; max-width: 1200px; margin: 0 auto;">
      <el-table-column prop="id" label="订单号" width="80" />
      <el-table-column prop="userName" label="用户姓名" width="120" />
      <el-table-column prop="nurseName" label="护工姓名" width="120" />
      <el-table-column prop="complaintContent" label="投诉内容" min-width="200" />
      <el-table-column prop="handleResult" label="处理结果" min-width="200">
        <template #default="{ row }">
          <span v-if="row.handleResult">{{ row.handleResult }}</span>
          <span v-else style="color: #999">未处理</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button size="small" @click="handleViewDetail(row)">查看详情</el-button>
          <el-button v-if="!row.handleResult" size="small" type="primary" @click="handleHandleComplaint(row)" style="color: #000;">处理投诉</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      @current-change="loadComplaints"
      layout="total, prev, pager, next"
      style="margin-top: 20px; text-align: center"
    />

    <el-dialog v-model="detailVisible" title="投诉详情" width="700px">
      <div v-if="currentComplaint" class="complaint-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单号">{{ currentComplaint.id }}</el-descriptions-item>
          <el-descriptions-item label="订单状态">
            <el-tag :type="getStatusType(currentComplaint.orderStatus)">{{ currentComplaint.orderStatusName }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="用户姓名">{{ currentComplaint.userName }}</el-descriptions-item>
          <el-descriptions-item label="护工姓名">{{ currentComplaint.nurseName }}</el-descriptions-item>
          <el-descriptions-item label="护工等级">{{ currentComplaint.nurseGrade === 1 ? '初级' : currentComplaint.nurseGrade === 2 ? '中级' : '高级' }}</el-descriptions-item>
          <el-descriptions-item label="月佣金">￥{{ currentComplaint.nursePrice }}</el-descriptions-item>
          <el-descriptions-item label="下单时间">{{ currentComplaint.orderTime }}</el-descriptions-item>
          <el-descriptions-item label="服务结束时间">{{ currentComplaint.serviceEndTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="投诉内容" :span="2">{{ currentComplaint.complaintContent }}</el-descriptions-item>
          <el-descriptions-item label="处理结果" :span="2">
            <span v-if="currentComplaint.handleResult">{{ currentComplaint.handleResult }}</span>
            <span v-else style="color: #999">未处理</span>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="handleVisible" title="处理投诉" width="500px">
      <el-form :model="handleForm" label-width="100px">
        <el-form-item label="订单号">
          <el-input v-model="currentComplaint.id" disabled />
        </el-form-item>
        <el-form-item label="用户姓名">
          <el-input v-model="currentComplaint.userName" disabled />
        </el-form-item>
        <el-form-item label="护工姓名">
          <el-input v-model="currentComplaint.nurseName" disabled />
        </el-form-item>
        <el-form-item label="投诉内容">
          <el-input v-model="currentComplaint.complaintContent" type="textarea" :rows="3" disabled />
        </el-form-item>
        <el-form-item label="处理结果">
          <el-input v-model="handleForm.result" type="textarea" :rows="4" placeholder="请输入处理结果" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitHandle">提交处理结果</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getComplaintOrders, handleComplaint } from '@/api/order'

const complaints = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const detailVisible = ref(false)
const handleVisible = ref(false)
const currentComplaint = ref({})
const handleForm = ref({
  result: ''
})

onMounted(() => {
  loadComplaints()
})

const loadComplaints = async () => {
  try {
    const res = await getComplaintOrders({
      current: currentPage.value,
      size: pageSize.value
    })
    complaints.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error(error)
  }
}

const getStatusType = (status) => {
  const types = {
    0: 'warning',
    1: 'primary',
    2: 'success',
    3: 'info',
    4: 'danger'
  }
  return types[status] || ''
}

const handleViewDetail = (complaint) => {
  currentComplaint.value = complaint
  detailVisible.value = true
}

const handleHandleComplaint = (complaint) => {
  currentComplaint.value = complaint
  handleForm.value.result = ''
  handleVisible.value = true
}

const handleSubmitHandle = async () => {
  try {
    await handleComplaint(currentComplaint.value.id, handleForm.value.result)
    ElMessage.success('投诉处理成功')
    handleVisible.value = false
    loadComplaints()
  } catch (error) {
    console.error(error)
  }
}
</script>

<style scoped>
.complaints-page {
  padding: 30px;
  max-width: 1400px;
  margin: 0 auto;
  background: #f5f7fa;
  min-height: 100vh;
}

.complaints-page h2 {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
  font-weight: 500;
}

.complaints-page :deep(.el-table) {
  border-radius: 4px;
}

.complaints-page :deep(.el-table th) {
  background: #f5f7fa;
  color: #333;
  font-weight: 500;
}

.complaints-page :deep(.el-table tr:hover > td) {
  background-color: #f9f9f9;
}

.complaints-page :deep(.el-button--primary) {
  background-color: #f9f9f9;
}

.complaint-detail {
  padding: 20px 0;
}

.complaints-page :deep(.el-pagination) {
  justify-content: center;
}
</style>
