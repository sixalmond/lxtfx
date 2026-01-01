<template>
  <div class="forum-page">
    <div class="forum-header">
      <h2>社区论坛</h2>
      <el-button type="primary" @click="showCreateDialog = true">发布帖子</el-button>
    </div>

    <div class="posts-list">
      <div v-for="post in posts" :key="post.id" class="post-card" @click="viewPostDetail(post.id)">
        <div class="post-header">
          <div class="post-author">
            <div class="author-avatar">
              <img :src="post.userAvatar || '/default-avatar.png'" :alt="post.userName" />
            </div>
            <div class="author-info">
              <span class="author-name">{{ post.userName }}</span>
              <span class="post-time">{{ formatTime(post.createTime) }}</span>
            </div>
          </div>
        </div>
        <h3 class="post-title">{{ post.title }}</h3>
        <p class="post-content">{{ post.content }}</p>
        <div class="post-stats">
          <span class="stat-item">
            <i class="el-icon-view"></i> {{ post.viewCount }} 浏览
          </span>
          <span class="stat-item">
            <i class="el-icon-chat-line-round"></i> {{ post.replyCount }} 回复
          </span>
          <span class="stat-item">
            <i class="el-icon-star-off"></i> {{ post.likeCount }} 点赞
          </span>
        </div>
      </div>
    </div>

    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :total="total"
      @current-change="loadPosts"
      layout="total, prev, pager, next"
      style="margin-top: 20px; text-align: center"
    />

    <el-dialog v-model="showCreateDialog" title="发布帖子" width="600px">
      <el-form :model="postForm" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="postForm.title" placeholder="请输入标题" maxlength="200" show-word-limit />
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="postForm.content" type="textarea" :rows="6" placeholder="请输入内容" maxlength="1000" show-word-limit />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="handleCreatePost">发布</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="showDetailDialog" title="帖子详情" width="800px">
      <div v-if="currentPost" class="post-detail">
        <div class="detail-header">
          <h3>{{ currentPost.title }}</h3>
          <div class="detail-meta">
            <span>{{ currentPost.userName }}</span>
            <span>{{ formatTime(currentPost.createTime) }}</span>
          </div>
        </div>
        <div class="detail-content">{{ currentPost.content }}</div>
        <div class="detail-stats">
          <span>浏览：{{ currentPost.viewCount }}</span>
          <span>回复：{{ currentPost.replyCount }}</span>
          <span>点赞：{{ currentPost.likeCount }}</span>
          <el-button type="primary" size="small" @click="handleLikePost">点赞</el-button>
        </div>

        <div class="replies-section">
          <h4>回复列表</h4>
          <div v-if="replies.length === 0" class="no-replies">暂无回复</div>
          <div v-else>
            <div v-for="reply in replies" :key="reply.id" class="reply-item">
              <div class="reply-header">
                <span class="reply-author">{{ reply.userName }}</span>
                <span class="reply-time">{{ formatTime(reply.createTime) }}</span>
              </div>
              <div class="reply-content">{{ reply.content }}</div>
              <div class="reply-actions">
                <span>点赞：{{ reply.likeCount }}</span>
                <el-button type="text" size="small" @click="handleLikeReply(reply.id)">点赞</el-button>
              </div>
            </div>
          </div>
        </div>

        <div class="reply-form">
          <el-input v-model="replyForm.content" type="textarea" :rows="3" placeholder="发表你的回复..." />
          <el-button type="primary" style="margin-top: 10px" @click="handleCreateReply">发表回复</el-button>
        </div>
      </div>
      <template #footer>
        <el-button @click="showDetailDialog = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getForumPosts, getPostDetail, createPost, getPostReplies, createReply, likePost, likeReply } from '@/api/forum'

const posts = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const showCreateDialog = ref(false)
const showDetailDialog = ref(false)
const currentPost = ref(null)
const replies = ref([])
const postForm = ref({
  title: '',
  content: ''
})
const replyForm = ref({
  postId: null,
  content: ''
})

onMounted(() => {
  loadPosts()
})

const loadPosts = async () => {
  try {
    const res = await getForumPosts({
      current: currentPage.value,
      size: pageSize.value
    })
    posts.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error(error)
  }
}

const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)

  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  if (days < 7) return `${days}天前`
  return date.toLocaleDateString()
}

const handleCreatePost = async () => {
  if (!postForm.value.title.trim()) {
    ElMessage.warning('请输入标题')
    return
  }
  if (!postForm.value.content.trim()) {
    ElMessage.warning('请输入内容')
    return
  }

  try {
    await createPost(postForm.value)
    ElMessage.success('发布成功')
    showCreateDialog.value = false
    postForm.value = { title: '', content: '' }
    loadPosts()
  } catch (error) {
    console.error(error)
  }
}

const viewPostDetail = async (postId) => {
  try {
    const res = await getPostDetail(postId)
    currentPost.value = res.data
    showDetailDialog.value = true
    loadReplies(postId)
  } catch (error) {
    console.error(error)
  }
}

const loadReplies = async (postId) => {
  try {
    const res = await getPostReplies(postId)
    replies.value = res.data
    replyForm.value.postId = postId
  } catch (error) {
    console.error(error)
  }
}

const handleCreateReply = async () => {
  if (!replyForm.value.content.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }

  try {
    await createReply(replyForm.value)
    ElMessage.success('回复成功')
    replyForm.value.content = ''
    loadReplies(currentPost.value.id)
    currentPost.value.replyCount++
  } catch (error) {
    console.error(error)
  }
}

const handleLikePost = async () => {
  try {
    await likePost(currentPost.value.id)
    ElMessage.success('点赞成功')
    currentPost.value.likeCount++
  } catch (error) {
    console.error(error)
  }
}

const handleLikeReply = async (replyId) => {
  try {
    await likeReply(replyId)
    ElMessage.success('点赞成功')
    const reply = replies.value.find(r => r.id === replyId)
    if (reply) reply.likeCount++
  } catch (error) {
    console.error(error)
  }
}
</script>

<style scoped>
.forum-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
  background: #f5f7fa;
  min-height: 100vh;
}

.forum-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 15px 20px;
  background: #fff;
  border-radius: 4px;
}

.forum-header h2 {
  margin: 0;
  color: #333;
  font-size: 20px;
  font-weight: 500;
}

.forum-header :deep(.el-button--primary) {
  padding: 10px 25px;
}

.posts-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.post-card {
  background: white;
  border-radius: 4px;
  padding: 20px;
  cursor: pointer;
  border: 1px solid #e8ecf4;
}

.post-card:hover {
  border-color: #409eff;
}

.post-header {
  margin-bottom: 15px;
}

.post-author {
  display: flex;
  align-items: center;
  gap: 12px;
}

.author-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  background: #f0f0f0;
}

.author-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.author-info {
  display: flex;
  flex-direction: column;
}

.author-name {
  font-weight: 500;
  color: #333;
  font-size: 15px;
}

.post-time {
  font-size: 13px;
  color: #999;
  margin-top: 4px;
}

.post-title {
  font-size: 18px;
  color: #333;
  margin-bottom: 10px;
  font-weight: 500;
  line-height: 1.4;
}

.post-content {
  color: #666;
  line-height: 1.6;
  margin-bottom: 15px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.post-stats {
  display: flex;
  gap: 20px;
  color: #999;
  font-size: 13px;
  padding-top: 10px;
  border-top: 1px solid #f0f0f0;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.post-detail {
  padding: 20px 0;
}

.detail-header h3 {
  font-size: 20px;
  color: #333;
  margin-bottom: 15px;
  font-weight: 500;
}

.detail-meta {
  display: flex;
  gap: 15px;
  color: #999;
  font-size: 13px;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e8ecf4;
}

.detail-content {
  color: #333;
  line-height: 1.8;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 4px;
  margin-bottom: 20px;
}

.detail-stats {
  display: flex;
  gap: 20px;
  align-items: center;
  margin-bottom: 20px;
  color: #666;
  font-size: 14px;
  padding: 10px 15px;
  background: #f0f9ff;
  border-radius: 4px;
}

.replies-section {
  margin-top: 20px;
}

.replies-section h4 {
  font-size: 18px;
  color: #333;
  margin-bottom: 15px;
  font-weight: 500;
  padding-bottom: 10px;
  border-bottom: 1px solid #e8ecf4;
}

.no-replies {
  text-align: center;
  color: #999;
  padding: 30px 0;
  font-size: 14px;
  background: #f9f9f9;
  border-radius: 4px;
}

.reply-item {
  background: #f9f9f9;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 15px;
  border: 1px solid #e8ecf4;
}

.reply-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  padding-bottom: 10px;
  border-bottom: 1px solid #e8ecf4;
}

.reply-author {
  font-weight: 500;
  color: #333;
  font-size: 15px;
}

.reply-time {
  color: #999;
  font-size: 13px;
}

.reply-content {
  color: #666;
  line-height: 1.6;
  font-size: 14px;
}

.forum-page :deep(.el-pagination) {
  justify-content: center;
  margin-top: 20px;
}

.forum-page :deep(.el-dialog__header) {
  background: #f5f7fa;
  color: #333;
}

.forum-page :deep(.el-dialog__title) {
  color: #333;
  font-size: 18px;
  font-weight: 500;
}
</style>
