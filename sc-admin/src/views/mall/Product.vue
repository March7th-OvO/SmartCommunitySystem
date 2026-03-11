<template>
  <div class="page">
    <el-card>
      <el-form :inline="true" :model="query" class="query-form">
        <el-form-item label="名称"><el-input v-model="query.name" placeholder="模糊" clearable style="width:160px" /></el-form-item>
        <el-form-item label="分类"><el-input v-model="query.categoryId" placeholder="分类ID" clearable style="width:120px" /></el-form-item>
        <el-form-item><el-button type="primary" @click="load">查询</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
      <div style="margin-bottom:12px"><el-button type="primary" @click="openEdit()">新增商品</el-button></div>
      <el-table :data="list" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="名称" />
        <el-table-column prop="categoryId" label="分类ID" width="90" />
        <el-table-column prop="salePrice" label="售价" width="90" />
        <el-table-column prop="status" label="状态" width="80"><template #default="{ row }">{{ row.status === 1 ? '上架' : '下架' }}</template></el-table-column>
        <el-table-column label="操作" width="160">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" :page-sizes="[10,20,50]" layout="total,sizes,prev,pager,next" @current-change="load" @size-change="load" class="pagination" />
    </el-card>
    <el-dialog v-model="editVisible" :title="editId ? '编辑商品' : '新增商品'" width="520">
      <el-form :model="form" label-width="90">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="分类ID"><el-input-number v-model="form.categoryId" :min="0" style="width:100%" /></el-form-item>
        <el-form-item label="售价"><el-input-number v-model="form.salePrice" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="form.status"><el-radio :label="1">上架</el-radio><el-radio :label="0">下架</el-radio></el-radio-group></el-form-item>
      </el-form>
      <template #footer><el-button @click="editVisible = false">取消</el-button><el-button type="primary" @click="submitEdit">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getProductPage, createProduct, updateProduct, deleteProduct } from '@/api/mall'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const query = reactive({ name: '', categoryId: null })
const editVisible = ref(false)
const editId = ref(null)
const form = reactive({ name: '', categoryId: null, salePrice: 0, status: 1 })

async function load() {
  loading.value = true
  try {
    const res = await getProductPage({ pageNum: pageNum.value, pageSize: pageSize.value, name: query.name || undefined, categoryId: query.categoryId ?? undefined })
    list.value = res.data?.records ?? res.data?.list ?? []
    total.value = res.data?.total ?? 0
  } finally {
    loading.value = false
  }
}

function resetQuery() { query.name = ''; query.categoryId = null; pageNum.value = 1; load() }

function openEdit(row) {
  editId.value = row?.id ?? null
  form.name = row?.name ?? ''
  form.categoryId = row?.categoryId ?? null
  form.salePrice = row?.salePrice ?? 0
  form.status = row?.status ?? 1
  editVisible.value = true
}

async function submitEdit() {
  if (!form.name) return ElMessage.warning('请输入名称')
  if (editId.value) await updateProduct(editId.value, form)
  else await createProduct(form)
  ElMessage.success('保存成功')
  editVisible.value = false
  load()
}

function remove(row) {
  ElMessageBox.confirm('确定删除该商品？', '提示', { type: 'warning' }).then(async () => {
    await deleteProduct(row.id)
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
