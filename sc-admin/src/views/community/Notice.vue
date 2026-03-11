<template>
  <div class="page">
    <el-card>
      <div style="margin-bottom:16px"><el-button type="primary" @click="openEdit()">发布公告</el-button></div>
      <el-table :data="list" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="communityId" label="小区ID" width="90" />
        <el-table-column prop="status" label="状态" width="90"><template #default="{ row }">{{ row.status === 1 ? '已发布' : '草稿' }}</template></el-table-column>
        <el-table-column prop="publishTime" label="发布时间" width="170" />
        <el-table-column prop="expireTime" label="过期时间" width="170" />
        <el-table-column label="操作" width="160">
          <template #default="{ row }">
            <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="remove(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" :page-sizes="[10,20,50]" layout="total,sizes,prev,pager,next" @current-change="load" @size-change="load" class="pagination" />
    </el-card>
    <el-dialog v-model="editVisible" :title="editId ? '编辑公告' : '发布公告'" width="560">
      <el-form :model="form" label-width="90">
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="内容"><el-input v-model="form.content" type="textarea" :rows="4" /></el-form-item>
        <el-form-item label="小区ID"><el-input-number v-model="form.communityId" :min="0" style="width:100%" /></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="form.status"><el-radio :label="1">已发布</el-radio><el-radio :label="0">草稿</el-radio></el-radio-group></el-form-item>
      </el-form>
      <template #footer><el-button @click="editVisible = false">取消</el-button><el-button type="primary" @click="submitEdit">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getNoticeAdminPage, createNotice, updateNotice, deleteNotice } from '@/api/community'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const editVisible = ref(false)
const editId = ref(null)
const form = reactive({ title: '', content: '', communityId: null, status: 1 })

async function load() {
  loading.value = true
  try {
    const res = await getNoticeAdminPage({ pageNum: pageNum.value, pageSize: pageSize.value })
    list.value = res.data?.records ?? res.data?.list ?? []
    total.value = res.data?.total ?? 0
  } finally {
    loading.value = false
  }
}

function openEdit(row) {
  editId.value = row?.id ?? null
  form.title = row?.title ?? ''
  form.content = row?.content ?? ''
  form.communityId = row?.communityId ?? null
  form.status = row?.status ?? 1
  editVisible.value = true
}

async function submitEdit() {
  if (!form.title) return ElMessage.warning('请输入标题')
  if (editId.value) await updateNotice(editId.value, form)
  else await createNotice(form)
  ElMessage.success('保存成功')
  editVisible.value = false
  load()
}

function remove(row) {
  ElMessageBox.confirm('确定删除该公告？', '提示', { type: 'warning' }).then(async () => {
    await deleteNotice(row.id)
    ElMessage.success('已删除')
    load()
  }).catch(() => {})
}

onMounted(load)
</script>

<style scoped>
.page { padding: 0; }
.pagination { margin-top: 16px; justify-content: flex-end; }
</style>
