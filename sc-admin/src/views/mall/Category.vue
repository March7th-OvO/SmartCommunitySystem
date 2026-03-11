<template>
  <div class="page">
    <el-card>
      <div style="margin-bottom:16px">
        <el-button type="primary" @click="openEdit()">新增分类</el-button>
      </div>
      <el-table :data="treeList" v-loading="loading" row-key="id" default-expand-all>
        <el-table-column prop="name" label="名称" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="parentId" label="父级ID" width="90" />
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="editVisible" :title="editId ? '编辑分类' : '新增分类'" width="480">
      <el-form :model="form" label-width="80">
        <el-form-item label="名称"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="父级ID">
          <el-input-number v-model="form.parentId" :min="0" placeholder="0 表示顶级" style="width:100%" />
        </el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sort" :min="0" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCategoryTree, createCategory, updateCategory, deleteCategory } from '@/api/mall'

const loading = ref(false)
const treeList = ref([])
const editVisible = ref(false)
const editId = ref(null)
const form = reactive({ name: '', parentId: null, sort: 0 })

async function load() {
  loading.value = true
  try {
    const res = await getCategoryTree()
    treeList.value = res.data || []
  } finally {
    loading.value = false
  }
}

function openEdit(row) {
  editId.value = row ? row.id : null
  form.name = row ? row.name : ''
  form.parentId = row ? row.parentId : null
  form.sort = row ? (row.sort ?? 0) : 0
  editVisible.value = true
}

async function submitEdit() {
  if (!form.name) return ElMessage.warning('请输入名称')
  const payload = { name: form.name, parentId: form.parentId || null, sort: form.sort }
  if (editId.value) {
    await updateCategory(editId.value, payload)
  } else {
    await createCategory(payload)
  }
  ElMessage.success('保存成功')
  editVisible.value = false
  load()
}

function remove(row) {
  ElMessageBox.confirm('确定删除该分类？', '提示', {
    type: 'warning'
  }).then(async () => {
    await deleteCategory(row.id)
    ElMessage.success('已删除')
    load()
  }).catch(() => {})
}

onMounted(load)
</script>

<style scoped>
.page { padding: 0; }
</style>
