<template>
  <div class="notice-list">
    <h2>社区公告</h2>
    <div v-if="loading">加载中...</div>
    <div v-else class="list">
      <div v-for="n in list" :key="n.id" class="card" @click="showDetail(n)">
        <div class="title">{{ n.title }}</div>
        <div class="time">{{ n.publishTime || n.createdAt }}</div>
      </div>
    </div>
    <el-dialog v-model="detailVisible" :title="current?.title" width="90%">
      <p>{{ current?.content }}</p>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getNoticeList } from '@/api/community'

const loading = ref(true)
const list = ref([])
const detailVisible = ref(false)
const current = ref(null)

async function load() {
  loading.value = true
  try {
    const res = await getNoticeList({ communityId: undefined })
    list.value = Array.isArray(res.data) ? res.data : []
  } finally {
    loading.value = false
  }
}

function showDetail(n) {
  current.value = n
  detailVisible.value = true
}

onMounted(load)
</script>

<style scoped>
.notice-list { padding: 16px; }
.list { display: flex; flex-direction: column; gap: 12px; margin-top: 12px; }
.card { padding: 14px; border: 1px solid #eee; border-radius: 8px; cursor: pointer; }
.card .title { font-weight: 500; }
.card .time { font-size: 12px; color: #909399; margin-top: 4px; }
</style>
