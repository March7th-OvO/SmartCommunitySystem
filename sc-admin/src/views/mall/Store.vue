<template>
  <div class="page">
    <el-card>
      <el-form :inline="true" :model="query" class="query-form">
        <el-form-item label="名称"><el-input v-model="query.name" placeholder="模糊" clearable style="width:160px" /></el-form-item>
        <el-form-item><el-button type="primary" @click="load">查询</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
      <div style="margin-bottom:12px"><el-button type="primary" @click="openEdit()">新增门店</el-button></div>
      <el-table :data="list" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="名称" />
        <el-table-column prop="address" label="地址" show-overflow-tooltip />
        <el-table-column prop="contactPhone" label="联系电话" width="120" />
        <el-table-column label="操作" width="160">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" :page-sizes="[10,20,50]" layout="total,sizes,prev,pager,next" @current-change="load" @size-change="load" class="pagination" />
    </el-card>
    <el-dialog v-model="editVisible" :title="editId ? '编辑门店' : '新增门店'" width="500">
      <el-form :model="form" label-width="90">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="地址"><el-input v-model="form.address" /></el-form-item>
        <el-form-item label="联系电话"><el-input v-model="form.contactPhone" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="editVisible = false">取消</el-button><el-button type="primary" @click="submitEdit">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getStorePage, createStore, updateStore, deleteStore } from '@/api/mall'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const query = reactive({ name: '' })
const editVisible = ref(false)
const editId = ref(null)
const form = reactive({ name: '', address: '', contactPhone: '' })

async function load() {
  loading.value = true
  try {
    const res = await getStorePage({ pageNum: pageNum.value, pageSize: pageSize.value, name: query.name || undefined })
    list.value = res.data?.records ?? res.data?.list ?? []
    total.value = res.data?.total ?? 0
  } finally {
    loading.value = false
  }
}

function resetQuery() { query.name = ''; pageNum.value = 1; load() }

function openEdit(row) {
  editId.value = row?.id ?? null
  form.name = row?.name ?? ''
  form.address = row?.address ?? ''
  form.contactPhone = row?.contactPhone ?? ''
  editVisible.value = true
}

async function submitEdit() {
  if (!form.name) return ElMessage.warning('请输入名称')
  if (editId.value) await updateStore(editId.value, form)
  else await createStore(form)
  ElMessage.success('保存成功')
  editVisible.value = false
  load()
}

function remove(row) {
  ElMessageBox.confirm('确定删除该门店？', '提示', { type: 'warning' }).then(async () => {
    await deleteStore(row.id)
    ElMessage.success('已删除')
    load()
  }).catch(() => {})
}

onMounted(load)
</script>

<style scoped>
.page { padding: 0; }
.query-form { margin-bottom: 16px; }
.pagination { margin-top: 16px; justify-content: flex-end; }
</style>
