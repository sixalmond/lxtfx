<template>
  <div class="orders-page">
    <h2>订单管理</h2>
    <el-form :inline="true" class="filter-form">
      <el-form-item label="订单状态">
        <el-select v-model="filterStatus" placeholder="全部" clearable @change="loadOrders">
          <el-option label="待确认" :value="0" />
          <el-option label="已接单" :value="1" />
          <el-option label="已完成" :value="2" />
          <el-option label="已取消" :value="3" />
          <el-option label="投诉中" :value="4" />
          <el-option label="服务中" :value="5" />
        </el-select>
      </el-form-item>
    </el-form>
    <el-table :data="orders" style="width: 100%;">
      <el-table-column prop="id" label="订单号" min-width="80" />
      <el-table-column prop="userName" label="用户姓名" min-width="120" />
      <el-table-column prop="nurseName" label="护工姓名" min-width="120" />
      <el-table-column prop="orderStatusName" label="订单状态" min-width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.orderStatus)">{{ row.orderStatusName }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="orderTime" label="下单时间" min-width="180" />
      <el-table-column label="操作" min-width="200" flex="1">
        <template #default="{ row }">
          <el-button size="small" @click="handleViewDetail(row)">查看详情</el-button>
          <el-button v-if="row.orderStatus === 4" size="small" type="success" @click="handleHandleComplaint(row)">处理投诉</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      @current-change="loadOrders"
      layout="total, prev, pager, next"
      style="margin-top: 20px; text-align: center"
    />

    <!-- 弹窗部分内容不变，保留原有功能 -->
    <el-dialog v-model="detailVisible" title="订单详情" width="700px">
      <div v-if="currentOrder" class="order-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单号">{{ currentOrder.id }}</el-descriptions-item>
          <el-descriptions-item label="订单状态">
            <el-tag :type="getStatusType(currentOrder.orderStatus)">{{ currentOrder.orderStatusName }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="用户姓名">{{ currentOrder.userName }}</el-descriptions-item>
          <el-descriptions-item label="护工姓名">{{ currentOrder.nurseName }}</el-descriptions-item>
          <el-descriptions-item label="护工等级">{{ currentOrder.nurseGrade === 1 ? '初级' : currentOrder.nurseGrade === 2 ? '中级' : '高级' }}</el-descriptions-item>
          <el-descriptions-item label="月佣金">￥{{ currentOrder.nursePrice }}</el-descriptions-item>
          <el-descriptions-item label="下单时间">{{ currentOrder.orderTime }}</el-descriptions-item>
          <el-descriptions-item label="确认时间">{{ currentOrder.confirmTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="服务开始时间">{{ currentOrder.serviceStartTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="服务结束时间">{{ currentOrder.serviceEndTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="投诉内容" :span="2">
            <span v-if="currentOrder.complaintContent">{{ currentOrder.complaintContent }}</span>
            <span v-else>-</span>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="complaintVisible" title="处理投诉" width="500px">
      <el-form :model="complaintForm" label-width="100px">
        <el-form-item label="订单号">
          <el-input v-model="currentOrder.id" disabled />
        </el-form-item>
        <el-form-item label="用户姓名">
          <el-input v-model="currentOrder.userName" disabled />
        </el-form-item>
        <el-form-item label="护工姓名">
          <el-input v-model="currentOrder.nurseName" disabled />
        </el-form-item>
        <el-form-item label="投诉内容">
          <el-input v-model="currentOrder.complaintContent" type="textarea" :rows="3" disabled />
        </el-form-item>
        <el-form-item label="处理结果">
          <el-input v-model="complaintForm.result" type="textarea" :rows="4" placeholder="请输入处理结果" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="complaintVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitComplaint">提交处理结果</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
// script 部分无需修改，保留原有业务逻辑
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { pageOrders, handleComplaint } from '@/api/order'

const orders = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const filterStatus = ref(null)
const detailVisible = ref(false)
const complaintVisible = ref(false)
const currentOrder = ref({})
const complaintForm = ref({
  result: ''
})

onMounted(() => {
  loadOrders()
})

const loadOrders = async () => {
  try {
    const res = await pageOrders({
      current: currentPage.value,
      size: pageSize.value,
      status: filterStatus.value
    })
    orders.value = res.data.records
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
    4: 'danger',
    5: 'primary'
  }
  return types[status] || ''
}

const handleViewDetail = (order) => {
  currentOrder.value = order
  detailVisible.value = true
}

const handleHandleComplaint = (order) => {
  currentOrder.value = order
  complaintForm.value.result = ''
  complaintVisible.value = true
}

const handleSubmitComplaint = async () => {
  try {
    await handleComplaint(currentOrder.value.id, complaintForm.value.result)
    ElMessage.success('投诉处理成功，订单状态已更新')
    complaintVisible.value = false
    loadOrders()
  } catch (error) {
    console.error(error)
  }
}
</script>

<style scoped>
.orders-page {
  padding: 30px;
  margin: 0 auto;
  background: #f5f7fa;
  min-height: 100vh;
  box-sizing: border-box;
}

.orders-page h2 {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
  font-weight: 500;
}

/* 筛选表单优化：铺满宽度，与表格对齐 */
.filter-form {
  margin-bottom: 20px;
  padding: 15px 20px;
  background: #fff;
  border-radius: 4px;
  width: 100%;
  box-sizing: border-box;
}

.orders-page :deep(.el-form-item) {
  margin-bottom: 0;
}

.orders-page :deep(.el-select) {
  width: 200px;
}

/* 表格优化：强制铺满，保留原有边框样式 */
.orders-page :deep(.el-table) {
  border-radius: 4px;
  border: 1px solid #e8ecf4;
  width: 100% !important;
}

.orders-page :deep(.el-table th) {
  background: #f5f7fa;
  color: #333;
  font-weight: 500;
  border-bottom: 2px solid #e8ecf4;
}

.orders-page :deep(.el-table tr:hover > td) {
  background-color: #f9f9f9;
}

.orders-page :deep(.el-table td) {
  border-bottom: 1px solid #ececec;
}

.orders-page :deep(.el-tag) {
  border: none;
}

.order-detail {
  padding: 20px 0;
}

/* 分页组件优化：铺满宽度，与表格对齐 */
.orders-page :deep(.el-pagination) {
  justify-content: center;
  width: 100%;
  margin-top: 20px;
}

.orders-page :deep(.el-pagination button) {
  border-radius: 4px;
  margin: 0 4px;
  border: 1px solid #e8ecf4;
}

.orders-page :deep(.el-pagination .el-pager li) {
  border-radius: 4px;
  margin: 0 4px;
  border: 1px solid #000000;
}

.orders-page :deep(.el-pagination .el-pager li.active) {
  background: #409eff;
  color: #fff;
  border-color: #409eff;
}
</style>