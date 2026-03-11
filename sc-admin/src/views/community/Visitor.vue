<template>
  <div class="page">
    <el-card>
      <el-form :inline="true" :model="query" class="query-form">
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部" clearable style="width:100px">
            <el-option label="待审核" :value="0" />
            <el-option label="通过" :value="1" />
            <el-option label="拒绝" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="load">查询</el-button></el-form-item>
      </el-form>
      <el-table :data="list" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="visitorName" label="访客姓名" width="100" />
        <el-table-column prop="visitorPhone" label="访客电话" width="120" />
        <el-table-column prop="communityId" label="小区ID" width="90" />
        <el-table-column prop="status" label="状态" width="90"><template #default="{ row }">{{ visitorStatusMap[row.status] ?? row.status }}</template></el-table-column>
        <el-table-column prop="visitTime" label="预约时间" width="170" />
        <el-table-column prop="createdAt" label="提交时间" width="170" />
        <el-table-column label="操作" width="140">
          <template #default="{ row }">
            <template v-if="row.status === 0">
              <el-button link type="success" @click="audit(row.id, 1)">通过</el-button>
              <el-button link type="danger" @click="audit(row.id, 2)">拒绝</el-button>
            </template>
            <span v-else>-</span>
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
import { getVisitorAdminPage, auditVisitor } from '@/api/community'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const query = reactive({ status: null })
const visitorStatusMap = { 0: '待审核', 1: '通过', 2: '拒绝' }

async function load() {
  loading.value = true
  try {
    const res = await getVisitorAdminPage({ pageNum: pageNum.value, pageSize: pageSize.value, status: query.status ?? undefined })
    list.value = res.data?.records ?? res.data?.list ?? []
    total.value = res.data?.total ?? 0
  } finally {
    loading.value = false
  }
}

async function audit(id, status) {
  await auditVisitor(id, { status, remark: status === 1 ? '通过' : '拒绝' })
  ElMessage.success(status === 1 ? '已通过' : '已拒绝')
  load()
}

onMounted(load)
</script>

<style scoped>
.page { padding: 0; }
.query-form { margin-bottom: 16px; }
.pagination { margin-top: 16px; justify-content: flex-end; }
</style>
