<template>
  <div class="page">
    <el-card>
      <div style="margin-bottom:16px"><el-button type="primary" @click="openCreate">新建账单</el-button></div>
      <el-table :data="list" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="communityId" label="小区ID" width="90" />
        <el-table-column prop="userId" label="用户ID" width="90" />
        <el-table-column prop="houseInfo" label="房屋" width="120" />
        <el-table-column prop="billPeriod" label="账期" width="100" />
        <el-table-column prop="billType" label="类型" width="100" />
        <el-table-column prop="amount" label="金额" width="90" />
        <el-table-column prop="status" label="状态" width="90"><template #default="{ row }">{{ row.status === 1 ? '已缴' : '未缴' }}</template></el-table-column>
        <el-table-column prop="dueDate" label="应缴日期" width="120" />
        <el-table-column prop="createdAt" label="创建时间" width="170" />
      </el-table>
      <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" :page-sizes="[10,20,50]" layout="total,sizes,prev,pager,next" @current-change="load" @size-change="load" class="pagination" />
    </el-card>
    <el-dialog v-model="createVisible" title="新建物业费账单" width="480">
      <el-form :model="form" label-width="90">
        <el-form-item label="小区ID"><el-input-number v-model="form.communityId" :min="0" style="width:100%" /></el-form-item>
        <el-form-item label="用户ID"><el-input-number v-model="form.userId" :min="0" style="width:100%" /></el-form-item>
        <el-form-item label="房屋信息"><el-input v-model="form.houseInfo" /></el-form-item>
        <el-form-item label="账期"><el-input v-model="form.billPeriod" placeholder="如 2025-01" /></el-form-item>
        <el-form-item label="金额"><el-input-number v-model="form.amount" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="应缴日期"><el-date-picker v-model="form.dueDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="createVisible = false">取消</el-button><el-button type="primary" @click="submitCreate">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getPropertyBillAdminPage, createPropertyBill } from '@/api/community'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const createVisible = ref(false)
const form = reactive({ communityId: null, userId: null, houseInfo: '', billPeriod: '', amount: 0, dueDate: '' })

async function load() {
  loading.value = true
  try {
    const res = await getPropertyBillAdminPage({ pageNum: pageNum.value, pageSize: pageSize.value })
    list.value = res.data?.records ?? res.data?.list ?? []
    total.value = res.data?.total ?? 0
  } finally {
    loading.value = false
  }
}

function openCreate() {
  form.communityId = null
  form.userId = null
  form.houseInfo = ''
  form.billPeriod = ''
  form.amount = 0
  form.dueDate = ''
  createVisible.value = true
}

async function submitCreate() {
  await createPropertyBill(form)
  ElMessage.success('已创建')
  createVisible.value = false
  load()
}

onMounted(load)
</script>

<style scoped>
.page { padding: 0; }
.pagination { margin-top: 16px; justify-content: flex-end; }
</style>
