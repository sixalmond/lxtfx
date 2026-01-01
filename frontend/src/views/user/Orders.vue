<template>
  <div class="orders-page">
    <h2>我的订单</h2>
    <el-table :data="orders" style="width: 100%;">
  <el-table-column prop="id" label="订单号" min-width="80" />
  <el-table-column prop="nurseName" label="护工姓名" min-width="120" />
  <el-table-column prop="orderStatusName" label="订单状态" min-width="100">
    <template #default="{ row }">
      <el-tag :type="getStatusType(row.orderStatus)">{{ row.orderStatusName }}</el-tag>
    </template>
  </el-table-column>
  <el-table-column prop="orderTime" label="下单时间" min-width="180" />
  <el-table-column label="操作" min-width="250" flex="1">
    <template #default="{ row }">
      <el-button size="small" @click="handleViewDetail(row)">查看详情</el-button>
      <el-button v-if="row.orderStatus === 2 && !row.evaluated" size="small" type="primary" @click="handleEvaluate(row)">评价</el-button>
      <el-button v-if="row.evaluated" size="small" disabled>已评价</el-button>
      <el-button v-if="row.orderStatus === 0 || row.orderStatus === 1" size="small" type="danger" @click="handleCancel(row)">取消</el-button>
      <el-button v-if="row.orderStatus === 2 && !row.handleResult" size="small" type="warning" @click="handleComplaint(row)">投诉</el-button>
      <el-button v-if="row.handleResult" size="small" disabled>已处理</el-button>
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

    <el-dialog v-model="detailVisible" title="订单详情" width="600px">
      <div v-if="currentOrder" class="order-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单号">{{ currentOrder.id }}</el-descriptions-item>
          <el-descriptions-item label="订单状态">
            <el-tag :type="getStatusType(currentOrder.orderStatus)">{{ currentOrder.orderStatusName }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="护工姓名">{{ currentOrder.nurseName }}</el-descriptions-item>
          <el-descriptions-item label="护工等级">{{ currentOrder.nurseGrade === 1 ? '初级' : currentOrder.nurseGrade === 2 ? '中级' : '高级' }}</el-descriptions-item>
          <el-descriptions-item label="月佣金">￥{{ currentOrder.nursePrice }}</el-descriptions-item>
          <el-descriptions-item label="下单时间">{{ currentOrder.orderTime }}</el-descriptions-item>
          <el-descriptions-item label="确认时间">{{ currentOrder.confirmTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="服务开始时间">{{ currentOrder.serviceStartTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="服务结束时间">{{ currentOrder.serviceEndTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="投诉内容" :span="2">{{ currentOrder.complaintContent || '-' }}</el-descriptions-item>
          <el-descriptions-item label="处理结果" :span="2">{{ currentOrder.handleResult || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="evaluateVisible" title="评价护工" width="500px">
      <el-form :model="evaluateForm" label-width="80px">
        <el-form-item label="评分">
          <el-rate v-model="evaluateForm.score" />
        </el-form-item>
        <el-form-item label="评价内容">
          <el-input v-model="evaluateForm.evaluateContent" type="textarea" :rows="4" placeholder="请输入评价内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="evaluateVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitEvaluate">提交评价</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="complaintVisible" title="投诉订单" width="500px">
      <el-form :model="complaintForm" label-width="80px">
        <el-form-item label="投诉内容">
          <el-input v-model="complaintForm.content" type="textarea" :rows="4" placeholder="请输入投诉内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="complaintVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitComplaint">提交投诉</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMyOrders, cancelOrder, complaintOrder } from '@/api/order'
import { createEvaluation } from '@/api/evaluation'

const orders = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const detailVisible = ref(false)
const evaluateVisible = ref(false)
const complaintVisible = ref(false)
const currentOrder = ref(null)
const evaluateForm = ref({
  orderId: null,
  score: 5,
  evaluateContent: ''
})
const complaintForm = ref({
  content: ''
})

onMounted(() => {
  loadOrders()
})

const loadOrders = async () => {
  try {
    const res = await getMyOrders({
      current: currentPage.value,
      size: pageSize.value
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
    4: 'danger'
  }
  return types[status] || ''
}

const handleViewDetail = (order) => {
  currentOrder.value = order
  detailVisible.value = true
}

const handleEvaluate = (order) => {
  evaluateForm.value.orderId = order.id
  evaluateForm.value.score = 5
  evaluateForm.value.evaluateContent = ''
  evaluateVisible.value = true
}

const handleSubmitEvaluate = async () => {
  try {
    await createEvaluation(evaluateForm.value)
    ElMessage.success('评价成功')
    evaluateVisible.value = false
    loadOrders()
  } catch (error) {
    console.error(error)
  }
}

const handleCancel = async (order) => {
  try {
    await ElMessageBox.confirm('确认取消该订单吗？', '提示', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await cancelOrder(order.id)
    ElMessage.success('订单已取消')
    loadOrders()
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
    }
  }
}

const handleComplaint = (order) => {
  currentOrder.value = order
  complaintForm.value.content = ''
  complaintVisible.value = true
}

const handleSubmitComplaint = async () => {
  try {
    await complaintOrder(currentOrder.value.id, complaintForm.value.content)
    ElMessage.success('投诉已提交')
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

.orders-page :deep(.el-table) {
  border-radius: 4px;
  border: 1px solid #e8ecf4;
  width: 100% !important; /* 强制表格铺满 */
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
  border-bottom: 1px solid #f0f0f0;
}

.orders-page :deep(.el-tag) {
  border: none;
}

.order-detail {
  padding: 20px 0;
}

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
  border: 1px solid #e8ecf4;
}

.orders-page :deep(.el-pagination .el-pager li.active) {
  background: #409eff;
  color: #fff;
  border-color: #409eff;
}

.orders-page :deep(.el-dialog__header) {
  background: #f5f7fa;
  border-bottom: 1px solid #e8ecf4;
}

.orders-page :deep(.el-dialog__title) {
  color: #333;
  font-size: 18px;
  font-weight: 500;
}

.orders-page :deep(.el-descriptions__label) {
  background: #f9f9f9;
  font-weight: 500;
}

.orders-page :deep(.el-descriptions__body) {
  background: #fff;
  border: 1px solid #e8ecf4;
}
</style>