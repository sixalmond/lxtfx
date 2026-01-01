<template>
  <div class="home-page">
    <div class="banner">
      <div class="banner-content">
        <h1>专业养老服务平台</h1>
        <p>为您提供优质的居家养老服务</p>
      </div>
    </div>

    <div class="features">
      <div class="feature-card">
        <div class="feature-icon">👨‍⚕️</div>
        <h3>专业护工</h3>
        <p>经过严格筛选和培训的专业护工团队</p>
      </div>
      <div class="feature-card">
        <div class="feature-icon">🏠</div>
        <h3>居家服务</h3>
        <p>提供上门养老服务，让老人安享晚年</p>
      </div>
      <div class="feature-card">
        <div class="feature-icon">💝</div>
        <h3>贴心服务</h3>
        <p>24小时在线客服，随时为您解答疑问</p>
      </div>
    </div>

    <div class="recommended-nurses">
      <h2>推荐护工</h2>
      <div class="nurse-grid">
        <div v-for="nurse in recommendedNurses" :key="nurse.id" class="nurse-card" @click="goToNurseDetail(nurse.id)">
          <div class="nurse-avatar">
            <img :src="nurse.avatarUrl || '/default-avatar.png'" :alt="nurse.name" />
          </div>
          <div class="nurse-info">
            <h3>{{ nurse.name }}</h3>
            <p class="nurse-grade">等级：{{ nurse.gradeName }}</p>
            <p class="nurse-price">￥{{ nurse.price }}/月</p>
            <p class="nurse-skills">技能：{{ nurse.skills }}</p>
            <el-tag :type="getStatusType(nurse.status)" size="small">{{ nurse.statusName }}</el-tag>
          </div>
        </div>
      </div>
      <div class="more-btn">
        <el-button type="primary" size="large" @click="$router.push('/user/nurses')">查看更多护工</el-button>
      </div>
    </div>

    <div class="stats">
      <div class="stat-item">
        <div class="stat-number">1000+</div>
        <div class="stat-label">注册用户</div>
      </div>
      <div class="stat-item">
        <div class="stat-number">500+</div>
        <div class="stat-label">专业护工</div>
      </div>
      <div class="stat-item">
        <div class="stat-number">10000+</div>
        <div class="stat-label">服务订单</div>
      </div>
      <div class="stat-item">
        <div class="stat-number">98%</div>
        <div class="stat-label">满意度</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAvailableNurses } from '@/api/nurse'

const recommendedNurses = ref([])

onMounted(() => {
  loadRecommendedNurses()
})

const loadRecommendedNurses = async () => {
  try {
    const res = await getAvailableNurses()
    recommendedNurses.value = res.data.slice(0, 6)
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

const goToNurseDetail = (id) => {
  console.log('Go to nurse detail:', id)
}
</script>

<style scoped>
.home-page {
  min-height: 100vh;
  background: #f5f7fa;
}

.banner {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 80px 20px;
  text-align: center;
}

.banner-content h1 {
  font-size: 48px;
  margin-bottom: 20px;
  font-weight: bold;
}

.banner-content p {
  font-size: 24px;
  opacity: 0.9;
}

.features {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
  padding: 60px 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.feature-card {
  background: white;
  padding: 40px 30px;
  border-radius: 12px;
  text-align: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.feature-icon {
  font-size: 64px;
  margin-bottom: 20px;
}

.feature-card h3 {
  font-size: 24px;
  margin-bottom: 15px;
  color: #333;
}

.feature-card p {
  font-size: 16px;
  color: #666;
  line-height: 1.6;
}

.recommended-nurses {
  padding: 60px 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.recommended-nurses h2 {
  font-size: 36px;
  text-align: center;
  margin-bottom: 40px;
  color: #333;
}

.nurse-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 25px;
  margin-bottom: 40px;
}

.nurse-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
}

.nurse-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.nurse-avatar {
  width: 100%;
  height: 200px;
  overflow: hidden;
  background: #f0f0f0;
}

.nurse-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.nurse-info {
  padding: 20px;
}

.nurse-info h3 {
  font-size: 20px;
  margin-bottom: 10px;
  color: #333;
}

.nurse-grade {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.nurse-price {
  font-size: 18px;
  color: #f56c6c;
  font-weight: bold;
  margin-bottom: 8px;
}

.nurse-skills {
  font-size: 14px;
  color: #999;
  margin-bottom: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.more-btn {
  text-align: center;
}

.stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 30px;
  padding: 60px 20px;
  max-width: 1000px;
  margin: 0 auto;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-item {
  text-align: center;
  padding: 20px;
}

.stat-number {
  font-size: 48px;
  font-weight: bold;
  color: #667eea;
  margin-bottom: 10px;
}

.stat-label {
  font-size: 18px;
  color: #666;
}
</style>
