<template>
  <div class="visitor-page">
    <h2>访客登记</h2>
    <el-form :model="form" label-width="90">
      <el-form-item label="小区ID"><el-input-number v-model="form.communityId" :min="1" style="width:100%" /></el-form-item>
      <el-form-item label="访客姓名"><el-input v-model="form.visitorName" /></el-form-item>
      <el-form-item label="访客电话"><el-input v-model="form.visitorPhone" /></el-form-item>
      <el-form-item label="车牌"><el-input v-model="form.vehicleNo" placeholder="选填" /></el-form-item>
      <el-form-item label="来访事由"><el-input v-model="form.visitReason" /></el-form-item>
      <el-form-item label="预约时间"><el-date-picker v-model="form.visitTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" style="width:100%" /></el-form-item>
      <el-form-item><el-button type="primary" :loading="submitting" @click="submit">提交</el-button></el-form-item>
    </el-form>
    <h3>我的登记</h3>
    <div v-if="listLoading">加载中...</div>
    <div v-else class="list">
      <div v-for="v in list" :key="v.id" class="card">
        <div>{{ v.visitorName }} {{ v.visitorPhone }} — {{ statusText(v.status) }}</div>
        <div class="time">{{ v.visitTime }} {{ v.createdAt }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { submitVisitor, getVisitorMy } from '@/api/community'

const form = reactive({
  communityId: null,
  visitorName: '',
  visitorPhone: '',
  vehicleNo: '',
  visitReason: '',
  visitTime: ''
})
const submitting = ref(false)
const list = ref([])
const listLoading = ref(false)

const statusText = (s) => ({ 0: '待审核', 1: '通过', 2: '拒绝', 3: '已结束' })[s] ?? s

async function submit() {
  if (!form.communityId || !form.visitorName || !form.visitorPhone || !form.visitReason || !form.visitTime) {
    ElMessage.warning('请填写必填项')
    return
  }
  submitting.value = true
  try {
    await submitVisitor(form)
    ElMessage.success('提交成功')
    loadList()
  } finally {
    submitting.value = false
  }
}

async function loadList() {
  listLoading.value = true
  try {
    const res = await getVisitorMy({ pageNum: 1, pageSize: 20 })
    list.value = res.data?.records ?? res.data?.list ?? []
  } finally {
    listLoading.value = false
  }
}

onMounted(loadList)
</script>

<style scoped>
.visitor-page { padding: 16px; }
.list { margin-top: 12px; }
.card { padding: 12px; border: 1px solid #eee; border-radius: 8px; margin-bottom: 8px; }
.card .time { font-size: 12px; color: #909399; margin-top: 4px; }
</style>
