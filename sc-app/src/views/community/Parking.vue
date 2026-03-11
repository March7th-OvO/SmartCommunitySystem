<template>
  <div class="parking-page">
    <h2>车位查询</h2>
    <el-form :inline="true" :model="query">
      <el-form-item label="小区ID"><el-input v-model="query.communityId" placeholder="必填" style="width:120px" /></el-form-item>
      <el-form-item><el-button type="primary" @click="load">查询</el-button></el-form-item>
    </el-form>
    <div v-if="loading">加载中...</div>
    <div v-else class="list">
      <div v-for="p in list" :key="p.id" class="card">
        {{ p.spaceNo }} — {{ p.status === 1 ? '已占用' : '空闲' }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { getParkingList } from '@/api/community'

const query = reactive({ communityId: '' })
const loading = ref(false)
const list = ref([])

async function load() {
  if (!query.communityId) return
  loading.value = true
  try {
    const res = await getParkingList(query.communityId)
    list.value = res.data ?? []
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.parking-page { padding: 16px; }
.list { margin-top: 12px; }
.card { padding: 10px; border: 1px solid #eee; border-radius: 8px; margin-bottom: 8px; }
</style>
