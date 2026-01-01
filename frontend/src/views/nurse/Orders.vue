<template>
  <div class="orders-page">
    <h2>订单管理</h2>
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="待确认订单" name="pending">
        <el-table :data="pendingOrders" style="width: 100%;">
          <el-table-column prop="id" label="订单号" min-width="80" />
          <el-table-column prop="userName" label="用户姓名" min-width="120" />
          <el-table-column prop="orderTime" label="下单时间" min-width="180" />
          <el-table-column label="操作" min-width="200" flex="1">
            <template #default="{ row }">
              <el-button size="small" type="primary" @click="handleAccept(row)">接单</el-button>
              <el-button size="small" type="danger" @click="handleReject(row)">拒单</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-model:current-page="pendingCurrentPage"
          v-model:page-size="pageSize"
          :total="pendingTotal"
          @current-change="loadPendingOrders"
          layout="total, prev, pager, next"
          style="margin-top: 20px; text-align: center"
        />
      </el-tab-pane>
      <el-tab-pane label="我的订单" name="my">
        <el-table :data="myOrders" style="width: 100%;">
          <el-table-column prop="id" label="订单号" min-width="80" />
          <el-table-column prop="userName" label="用户姓名" min-width="120" />
          <el-table-column prop="orderStatusName" label="订单状态" min-width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.orderStatus)">{{ row.orderStatusName }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="orderTime" label="下单时间" min-width="180" />
          <el-table-column label="操作" min-width="200" flex="1">
            <template #default="{ row }">
              <el-button size="small" @click="handleViewDetail(row)">查看详情</el-button>
              <el-button v-if="row.orderStatus === 1" size="small" type="primary" @click="handleStartService(row)">开始服务</el-button>
              <el-button v-if="row.orderStatus === 5" size="small" type="success" @click="handleComplete(row)">完成服务</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-model:current-page="myCurrentPage"
          v-model:page-size="pageSize"
          :total="myTotal"
          @current-change="loadMyOrders"
          layout="total, prev, pager, next"
          style="margin-top: 20px; text-align: center"
        />
      </el-tab-pane>
    </el-tabs>

    <!-- 弹窗部分内容不变 -->
    <el-dialog v-model="detailVisible" title="订单详情" width="600px">
      <div v-if="currentOrder" class="order-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单号">{{ currentOrder.id }}</el-descriptions-item>
          <el-descriptions-item label="订单状态">
            <el-tag :type="getStatusType(currentOrder.orderStatus)">{{ currentOrder.orderStatusName }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="用户姓名">{{ currentOrder.userName }}</el-descriptions-item>
          <el-descriptions-item label="下单时间">{{ currentOrder.orderTime }}</el-descriptions-item>
          <el-descriptions-item label="确认时间">{{ currentOrder.confirmTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="服务开始时间">{{ currentOrder.serviceStartTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="服务结束时间">{{ currentOrder.serviceEndTime || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="evaluationVisible" title="查看评价" width="600px">
      <div v-if="currentEvaluation" class="evaluation-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="用户">{{ currentEvaluation.userName }}</el-descriptions-item>
          <el-descriptions-item label="评分">
            <el-rate v-model="currentEvaluation.score" disabled show-score />
          </el-descriptions-item>
          <el-descriptions-item label="评价内容" :span="2">{{ currentEvaluation.evaluateContent }}</el-descriptions-item>
          <el-descriptions-item label="评价时间">{{ currentEvaluation.evaluateTime }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <el-button @click="evaluationVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
// script 部分代码完全不变，无需修改
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPendingOrders, getNurseOrders, acceptOrder, rejectOrder, startService, completeOrder } from '@/api/order'
import { getEvaluationByOrderId } from '@/api/evaluation'

const activeTab = ref('pending')
const pendingOrders = ref([])
const myOrders = ref([])
const pendingCurrentPage = ref(1)
const myCurrentPage = ref(1)
const pageSize = ref(10)
const pendingTotal = ref(0)
const myTotal = ref(0)
const detailVisible = ref(false)
const currentOrder = ref(null)
const evaluationVisible = ref(false)
const currentEvaluation = ref(null)

onMounted(() => {
  loadPendingOrders()
})

const handleTabChange = (tab) => {
  if (tab === 'pending') {
    loadPendingOrders()
  } else {
    loadMyOrders()
  }
}

const loadPendingOrders = async () => {
  try {
    const res = await getPendingOrders({
      current: pendingCurrentPage.value,
      size: pageSize.value
    })
    pendingOrders.value = res.data.records
    pendingTotal.value = res.data.total
  } catch (error) {
    console.error(error)
  }
}

const loadMyOrders = async () => {
  try {
    const res = await getNurseOrders({
      current: myCurrentPage.value,
      size: pageSize.value
    })
    myOrders.value = res.data.records
    myTotal.value = res.data.total
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

const handleAccept = async (order) => {
  try {
    await ElMessageBox.confirm(`确认接单吗？`, '提示', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await acceptOrder(order.id)
    ElMessage.success('接单成功')
    loadPendingOrders()
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
    }
  }
}

const handleReject = async (order) => {
  try {
    await ElMessageBox.confirm(`确认拒单吗？`, '提示', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await rejectOrder(order.id)
    ElMessage.success('拒单成功')
    loadPendingOrders()
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
    }
  }
}

const handleViewDetail = (order) => {
  currentOrder.value = order
  detailVisible.value = true
}

const handleStartService = async (order) => {
  try {
    await startService(order.id)
    ElMessage.success('服务已开始')
    loadMyOrders()
  } catch (error) {
    console.error(error)
  }
}

const handleComplete = async (order) => {
  try {
    await ElMessageBox.confirm(`确认完成服务吗？`, '提示', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await completeOrder(order.id)
    ElMessage.success('服务已完成')
    loadMyOrders()
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
    }
  }
}

const handleViewEvaluation = async (order) => {
  try {
    const res = await getEvaluationByOrderId(order.id)
    currentEvaluation.value = res.data
    evaluationVisible.value = true
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

.orders-page :deep(.el-tabs) {
  background: #fff;
  border-radius: 4px;
  padding: 20px;
  width: 100%;
  box-sizing: border-box;
}

.orders-page :deep(.el-tabs__nav-wrap::after) {
  background-color: #f5f7fa;
}

.orders-page :deep(.el-table) {
  border-radius: 4px;
  width: 100% !important;
}

.orders-page :deep(.el-table th) {
  background: #f5f7fa;
  color: #333;
  font-weight: 500;
}

.orders-page :deep(.el-table tr:hover > td) {
  background-color: #f9f9f9;
}

.order-detail {
  padding: 20px 0;
}

.orders-page :deep(.el-pagination) {
  justify-content: center;
  width: 100%;
  margin-top: 20px;
}
</style>