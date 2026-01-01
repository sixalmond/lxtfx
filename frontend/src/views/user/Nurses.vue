<template>
  <div class="nurses-page">
    <h2>空闲护工列表</h2>
    <div class="nurse-list">
      <el-card v-for="nurse in nurses" :key="nurse.id" class="nurse-card" shadow="hover">
        <div class="nurse-info">
          <el-avatar :size="80" :src="nurse.avatarUrl || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
          <div class="nurse-details">
            <h3>{{ nurse.name }}</h3>
            <p>年龄：{{ nurse.age }}岁</p>
            <p>等级：{{ nurse.gradeName }}</p>
            <p>技能：{{ nurse.skills }}</p>
            <p class="price">￥{{ nurse.price }}/月</p>
          </div>
        </div>
        <div class="nurse-actions">
          <el-button type="primary" @click="handleOrder(nurse)">立即下单</el-button>
          <el-button @click="handleViewDetail(nurse)">查看详情</el-button>
        </div>
      </el-card>
    </div>

    <el-dialog v-model="detailVisible" title="护工详情" width="700px">
      <div v-if="currentNurse" class="nurse-detail">
        <el-avatar :size="100" :src="currentNurse.avatarUrl || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
        <h3>{{ currentNurse.name }}</h3>
        <p>年龄：{{ currentNurse.age }}岁</p>
        <p>等级：{{ currentNurse.gradeName }}</p>
        <p>技能：{{ currentNurse.skills }}</p>
        <p class="price">￥{{ currentNurse.price }}/月</p>
        <p>状态：{{ currentNurse.statusName }}</p>
        
        <el-divider content-position="left">用户评价</el-divider>
        
        <div v-if="currentNurse.evaluations && currentNurse.evaluations.length > 0" class="evaluations-list">
          <div v-for="evaluation in currentNurse.evaluations" :key="evaluation.id" class="evaluation-item">
            <div class="evaluation-header">
              <span class="evaluation-user">{{ evaluation.userName }}</span>
              <span class="evaluation-time">{{ evaluation.evaluateTime }}</span>
              <el-rate v-model="evaluation.score" disabled show-score />
            </div>
            <div class="evaluation-content">{{ evaluation.evaluateContent }}</div>
          </div>
        </div>
        <div v-else class="no-evaluations">暂无评价</div>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleOrder(currentNurse)">立即下单</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAvailableNurses, getNurseDetail } from '@/api/nurse'
import { createOrder } from '@/api/order'

const nurses = ref([])
const detailVisible = ref(false)
const currentNurse = ref(null)

onMounted(() => {
  loadNurses()
})

const loadNurses = async () => {
  try {
    const res = await getAvailableNurses()
    nurses.value = res.data
  } catch (error) {
    console.error(error)
  }
}

const handleViewDetail = async (nurse) => {
  try {
    const res = await getNurseDetail(nurse.id)
    currentNurse.value = res.data
    detailVisible.value = true
  } catch (error) {
    console.error(error)
  }
}

const handleOrder = async (nurse) => {
  try {
    await ElMessageBox.confirm(`确认选择护工 ${nurse.name} 吗？`, '确认下单', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await createOrder({ nurseId: nurse.id })
    ElMessage.success('下单成功，请等待护工确认')
    detailVisible.value = false
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
    }
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
  font-size: 32px;
  margin-bottom: 30px;
  color: #333;
  font-weight: 600;
}

.nurse-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 25px;
  margin-bottom: 30px;
}

.nurse-card {
  background: #fff;
  border-radius: 16px;
  padding: 25px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  display: flex;
  flex-direction: column;
  min-height: 380px;
  transition: transform 0.3s, box-shadow 0.3s;
}

.nurse-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.12);
}

.nurse-info {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e8ecf4 100%);
  border-radius: 12px;
  flex-shrink: 0;
}

.nurse-details {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.nurse-details h3 {
  margin: 0 0 12px 0;
  color: #333;
  font-size: 20px;
  font-weight: 600;
}

.nurse-details p {
  margin: 6px 0;
  color: #555;
  font-size: 14px;
  line-height: 1.6;
}

.nurse-details .price {
  color: #f56c6c;
  font-size: 22px;
  font-weight: bold;
  margin-top: 12px;
}

.nurse-actions {
  display: flex;
  gap: 12px;
  padding: 20px 0 0;
  margin-top: auto;
}

.nurse-actions .el-button {
  flex: 1;
  height: 40px;
  font-size: 15px;
}

.nurse-detail {
  text-align: center;
  padding: 20px;
}

.nurse-detail h3 {
  margin: 20px 0 15px 0;
  color: #333;
  font-size: 26px;
  font-weight: 600;
}

.nurse-detail p {
  margin: 12px 0;
  color: #555;
  font-size: 16px;
  line-height: 1.8;
}

.nurse-detail .price {
  color: #f56c6c;
  font-size: 28px;
  font-weight: bold;
  margin: 20px 0;
}

.evaluations-list {
  margin-top: 20px;
  text-align: left;
}

.evaluation-item {
  padding: 15px;
  margin-bottom: 15px;
  background: #f9f9f9;
  border-radius: 8px;
  border-left: 3px solid #409eff;
}

.evaluation-header {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 10px;
  flex-wrap: wrap;
}

.evaluation-user {
  font-weight: 600;
  color: #333;
  font-size: 15px;
}

.evaluation-time {
  color: #999;
  font-size: 13px;
}

.evaluation-content {
  color: #666;
  font-size: 14px;
  line-height: 1.6;
}

.no-evaluations {
  text-align: center;
  color: #999;
  padding: 30px;
  font-size: 15px;
}
</style>
