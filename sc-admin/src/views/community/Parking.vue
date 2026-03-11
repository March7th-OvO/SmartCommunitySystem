<template>
  <div class="page">
    <el-card>
      <el-form :inline="true" :model="query" class="query-form">
        <el-form-item label="小区ID"><el-input v-model="query.communityId" placeholder="小区ID" clearable style="width:120px" /></el-form-item>
        <el-form-item><el-button type="primary" @click="load">查询</el-button></el-form-item>
      </el-form>
      <el-table :data="list" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="communityId" label="小区ID" width="90" />
        <el-table-column prop="spaceNo" label="车位号" width="120" />
        <el-table-column prop="spaceType" label="类型" width="90" />
        <el-table-column prop="status" label="状态" width="90"><template #default="{ row }">{{ row.status === 1 ? '已占用' : '空闲' }}</template></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getParkingAdminList } from '@/api/community'

const loading = ref(false)
const list = ref([])
const query = reactive({ communityId: '' })

async function load() {
  if (!query.communityId) {
    list.value = []
    return
  }
  loading.value = true
  try {
    const res = await getParkingAdminList({ communityId: query.communityId })
    list.value = res.data ?? []
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<style scoped>
.page { padding: 0; }
.query-form { margin-bottom: 16px; }
</style>
