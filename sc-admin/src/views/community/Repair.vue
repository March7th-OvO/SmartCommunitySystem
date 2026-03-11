<template>
  <div class="page">
    <el-card>
      <el-form :inline="true" :model="query" class="query-form">
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部" clearable style="width:100px">
            <el-option label="待处理" :value="0" />
            <el-option label="处理中" :value="1" />
            <el-option label="已完成" :value="2" />
            <el-option label="已取消" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="load">查询</el-button></el-form-item>
      </el-form>
      <el-table :data="list" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="communityId" label="小区ID" width="90" />
        <el-table-column prop="status" label="状态" width="90"><template #default="{ row }">{{ repairStatusMap[row.status] ?? row.status }}</template></el-table-column>
        <el-table-column prop="createdAt" label="提交时间" width="170" />
        <el-table-column label="操作" width="160">
          <template #default="{ row }">
            <el-button v-if="row.status === 0" link type="primary" @click="start(row.id)">开始处理</el-button>
            <el-button v-if="row.status === 1" link type="success" @click="finish(row.id)">完成</el-button>
            <span v-if="row.status >= 2">-</span>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" :page-sizes="[10,20,50]" layout="total,sizes,prev,pager,next" @current-change="load" @size-change="load" class="pagination" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getRepairAdminPage, startRepair, finishRepair } from '@/api/community'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const query = reactive({ status: null })
const repairStatusMap = { 0: '待处理', 1: '处理中', 2: '已完成', 3: '已取消' }

async function load() {
  loading.value = true
  try {
    const res = await getRepairAdminPage({ pageNum: pageNum.value, pageSize: pageSize.value, status: query.status ?? undefined })
    list.value = res.data?.records ?? res.data?.list ?? []
    total.value = res.data?.total ?? 0
  } finally {
    loading.value = false
  }
}

async function start(id) {
  await startRepair(id)
  ElMessage.success('已开始处理')
  load()
}

async function finish(id) {
  await finishRepair(id)
  ElMessage.success('已完成')
  load()
}

onMounted(load)
</script>

<style scoped>
.page { padding: 0; }
.query-form { margin-bottom: 16px; }
.pagination { margin-top: 16px; justify-content: flex-end; }
</style>
