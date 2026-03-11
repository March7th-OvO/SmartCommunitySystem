<template>
  <div class="page">
    <el-card>
      <el-form :inline="true" :model="query" class="query-form">
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部" clearable style="width:100px">
            <el-option label="待处理" :value="0" />
            <el-option label="已处理" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="load">查询</el-button></el-form-item>
      </el-form>
      <el-table :data="list" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="content" label="内容" show-overflow-tooltip />
        <el-table-column prop="communityId" label="小区ID" width="90" />
        <el-table-column prop="status" label="状态" width="90"><template #default="{ row }">{{ row.status === 1 ? '已处理' : '待处理' }}</template></el-table-column>
        <el-table-column prop="createdAt" label="提交时间" width="170" />
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button v-if="row.status === 0" link type="primary" @click="handle(row)">处理</el-button>
            <span v-else>-</span>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" :page-sizes="[10,20,50]" layout="total,sizes,prev,pager,next" @current-change="load" @size-change="load" class="pagination" />
    </el-card>
    <el-dialog v-model="handleVisible" title="处理投诉" width="480">
      <el-form :model="handleForm" label-width="80">
        <el-form-item label="处理说明"><el-input v-model="handleForm.handleResult" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="handleVisible = false">取消</el-button><el-button type="primary" @click="submitHandle">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getComplaintAdminPage, handleComplaint } from '@/api/community'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const query = reactive({ status: null })
const handleVisible = ref(false)
const handleForm = reactive({ id: null, handleResult: '' })

async function load() {
  loading.value = true
  try {
    const res = await getComplaintAdminPage({ pageNum: pageNum.value, pageSize: pageSize.value, status: query.status ?? undefined })
    list.value = res.data?.records ?? res.data?.list ?? []
    total.value = res.data?.total ?? 0
  } finally {
    loading.value = false
  }
}

function handle(row) {
  handleForm.id = row.id
  handleForm.handleResult = ''
  handleVisible.value = true
}

async function submitHandle() {
  await handleComplaint(handleForm.id, { handleResult: handleForm.handleResult })
  ElMessage.success('已处理')
  handleVisible.value = false
  load()
}

onMounted(load)
</script>

<style scoped>
.page { padding: 0; }
.query-form { margin-bottom: 16px; }
.pagination { margin-top: 16px; justify-content: flex-end; }
</style>
