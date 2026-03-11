<template>
  <div class="complaint-page">
    <h2>投诉建议</h2>
    <el-form :model="form" label-width="90">
      <el-form-item label="小区ID"><el-input-number v-model="form.communityId" :min="1" style="width:100%" /></el-form-item>
      <el-form-item label="房屋信息"><el-input v-model="form.houseInfo" placeholder="选填" /></el-form-item>
      <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
      <el-form-item label="内容"><el-input v-model="form.content" type="textarea" :rows="3" /></el-form-item>
      <el-form-item><el-button type="primary" :loading="submitting" @click="submit">提交</el-button></el-form-item>
    </el-form>
    <h3>我的投诉</h3>
    <div v-if="listLoading">加载中...</div>
    <div v-else class="list">
      <div v-for="c in list" :key="c.id" class="card">
        <div class="title">{{ c.title }} — {{ c.status === 1 ? '已处理' : '待处理' }}</div>
        <div class="time">{{ c.createdAt }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { submitComplaint, getComplaintMy } from '@/api/community'

const form = reactive({ communityId: null, houseInfo: '', title: '', content: '' })
const submitting = ref(false)
const list = ref([])
const listLoading = ref(false)

async function submit() {
  if (!form.communityId || !form.title || !form.content) {
    ElMessage.warning('请填写小区、标题和内容')
    return
  }
  submitting.value = true
  try {
    await submitComplaint(form)
    ElMessage.success('提交成功')
    loadList()
  } finally {
    submitting.value = false
  }
}

async function loadList() {
  listLoading.value = true
  try {
    const res = await getComplaintMy({ pageNum: 1, pageSize: 20 })
    list.value = res.data?.records ?? res.data?.list ?? []
  } finally {
    listLoading.value = false
  }
}

onMounted(loadList)
</script>

<style scoped>
.complaint-page { padding: 16px; }
.list { margin-top: 12px; }
.card { padding: 12px; border: 1px solid #eee; border-radius: 8px; margin-bottom: 8px; }
.card .title { font-weight: 500; }
.card .time { font-size: 12px; color: #909399; margin-top: 4px; }
</style>
