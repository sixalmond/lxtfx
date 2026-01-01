<template>
  <div class="evaluations-page">
    <h2>我的评价</h2>
    <div class="evaluations-grid">
      <el-card v-for="evaluation in evaluations" :key="evaluation.id" class="evaluation-card">
        <div class="evaluation-header">
          <div class="user-info">
            <span class="user-name">{{ evaluation.userName }}</span>
            <span class="evaluate-time">{{ evaluation.evaluateTime }}</span>
          </div>
          <el-rate v-model="evaluation.score" disabled show-score />
        </div>
        <div class="evaluation-content">{{ evaluation.evaluateContent }}</div>
      </el-card>
    </div>

    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      @current-change="loadEvaluations"
      layout="total, prev, pager, next"
      style="margin-top: 20px; text-align: center"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyEvaluations } from '@/api/evaluation'

const evaluations = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

onMounted(() => {
  loadEvaluations()
})

const loadEvaluations = async () => {
  try {
    const res = await getMyEvaluations({
      current: currentPage.value,
      size: pageSize.value
    })
    evaluations.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error(error)
  }
}
</script>

<style scoped>
.evaluations-page {
  padding: 30px;
  max-width: 1400px;
  margin: 0 auto;
  background: #f5f7fa;
  min-height: 100vh;
}

.evaluations-page h2 {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
  font-weight: 500;
}

.evaluations-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.evaluation-card {
  transition: all 0.3s;
}

.evaluation-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.evaluation-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e8ecf4;
}

.user-info {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.user-name {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

.evaluate-time {
  font-size: 12px;
  color: #999;
}

.evaluation-content {
  font-size: 14px;
  line-height: 1.6;
  color: #666;
  min-height: 60px;
}

.evaluations-page :deep(.el-rate) {
  height: 20px;
}

.evaluations-page :deep(.el-rate__icon) {
  font-size: 18px;
}

.evaluations-page :deep(.el-rate__text) {
  font-size: 14px;
  color: #f7ba2a;
  margin-left: 10px;
}

.evaluations-page :deep(.el-pagination) {
  justify-content: center;
}

.evaluations-page :deep(.el-pagination button) {
  border-radius: 4px;
  margin: 0 4px;
  border: 1px solid #e8ecf4;
}

.evaluations-page :deep(.el-pagination .el-pager li) {
  border-radius: 4px;
  margin: 0 4px;
  border: 1px solid #e8ecf4;
}

.evaluations-page :deep(.el-pagination .el-pager li.active) {
  background: #409eff;
  color: #fff;
  border-color: #409eff;
}
</style>
