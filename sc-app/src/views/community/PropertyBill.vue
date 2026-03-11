<template>
  <div class="property-bill-page">
    <h2>物业费</h2>
    <div v-if="loading">加载中...</div>
    <div v-else class="list">
      <div v-for="b in list" :key="b.id" class="card">
        <div>{{ b.houseInfo }} {{ b.billPeriod }} — ¥{{ b.amount }} {{ b.status === 1 ? '已缴' : '未缴' }}</div>
        <div class="time">应缴 {{ b.dueDate }}</div>
        <el-button v-if="b.status === 0" type="primary" size="small" :loading="payingId === b.id" @click="pay(b)">缴纳</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getPropertyBillMy, payPropertyBill } from '@/api/community'

const loading = ref(true)
const list = ref([])
const payingId = ref(null)

async function load() {
  loading.value = true
  try {
    const res = await getPropertyBillMy({ pageNum: 1, pageSize: 50 })
    list.value = res.data?.records ?? res.data?.list ?? []
  } finally {
    loading.value = false
  }
}

async function pay(b) {
  payingId.value = b.id
  try {
    await payPropertyBill(b.id)
    ElMessage.success('缴纳成功')
    load()
  } finally {
    payingId.value = null
  }
}

onMounted(load)
</script>

<style scoped>
.property-bill-page { padding: 16px; }
.list { margin-top: 12px; }
.card { display: flex; flex-wrap: wrap; align-items: center; gap: 8px; padding: 12px; border: 1px solid #eee; border-radius: 8px; margin-bottom: 8px; }
.card .time { font-size: 12px; color: #909399; width: 100%; }
</style>
