<template>
  <div class="repair-page">
    <h2>报修</h2>
    <el-form :model="form" label-width="90">
      <el-form-item label="小区ID"><el-input-number v-model="form.communityId" :min="1" style="width:100%" /></el-form-item>
      <el-form-item label="房屋信息"><el-input v-model="form.houseInfo" placeholder="如 1栋101" /></el-form-item>
      <el-form-item label="类别"><el-input v-model="form.category" placeholder="如 水电" /></el-form-item>
      <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
      <el-form-item label="描述"><el-input v-model="form.content" type="textarea" :rows="3" /></el-form-item>
      <el-form-item><el-button type="primary" :loading="submitting" @click="submit">提交</el-button></el-form-item>
    </el-form>
    <h3>我的报修</h3>
    <div v-if="listLoading">加载中...</div>
    <div v-else class="list">
      <div v-for="r in list" :key="r.id" class="card">
        <div class="title">{{ r.title }} — {{ statusText(r.status) }}</div>
        <div class="time">{{ r.createdAt }}</div>
        <el-button v-if="r.status === 0" link type="danger" @click="cancel(r.id)">取消</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { submitRepair, getRepairMy, cancelRepair } from '@/api/community'

const form = reactive({ communityId: null, houseInfo: '', category: '', title: '', content: '' })
const submitting = ref(false)
const list = ref([])
const listLoading = ref(false)

const statusText = (s) => ({ 0: '待处理', 1: '处理中', 2: '已完成', 3: '已取消' })[s] ?? s

async function submit() {
  if (!form.communityId || !form.title) { ElMessage.warning('请填写小区和标题'); return }
  submitting.value = true
  try {
    await submitRepair(form)
    ElMessage.success('提交成功')
    loadList()
  } finally {
    submitting.value = false
  }
}

async function loadList() {
  listLoading.value = true
  try {
    const res = await getRepairMy({ pageNum: 1, pageSize: 20 })
    list.value = res.data?.records ?? res.data?.list ?? []
  } finally {
    listLoading.value = false
  }
}

function cancel(id) {
  cancelRepair(id).then(() => { ElMessage.success('已取消'); loadList() })
}

onMounted(loadList)
</script>

<style scoped>
.repair-page { padding: 16px; }
.list { margin-top: 12px; }
.card { padding: 12px; border: 1px solid #eee; border-radius: 8px; margin-bottom: 8px; }
.card .title { font-weight: 500; }
.card .time { font-size: 12px; color: #909399; margin-top: 4px; }
</style>
