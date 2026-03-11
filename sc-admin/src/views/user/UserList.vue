<template>
  <div class="page">
    <el-card>
      <el-form :inline="true" :model="query" class="query-form">
        <el-form-item label="手机号">
          <el-input v-model="query.phone" placeholder="模糊" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="全部" clearable style="width:100px">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="load">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="list" v-loading="loading" stripe>
        <el-table-column prop="userId" label="用户ID" width="90" />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="nickname" label="昵称" width="120" />
        <el-table-column prop="userType" label="类型" width="80">
          <template #default="{ row }">{{ userTypeMap[row.userType] || row.userType }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="roleNames" label="角色">
          <template #default="{ row }">{{ (row.roleNames || []).join('、') || '-' }}</template>
        </el-table-column>
        <el-table-column prop="createdAt" label="注册时间" width="170" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openStatus(row)">状态</el-button>
            <el-button link type="primary" @click="openRoles(row)">角色</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next"
        @current-change="load"
        @size-change="load"
        class="pagination"
      />
    </el-card>
    <el-dialog v-model="statusVisible" title="修改状态" width="360">
      <el-form :model="statusForm" label-width="80">
        <el-form-item label="状态">
          <el-radio-group v-model="statusForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="statusVisible = false">取消</el-button>
        <el-button type="primary" @click="submitStatus">确定</el-button>
      </template>
    </el-dialog>
    <el-dialog v-model="rolesVisible" title="分配角色" width="400">
      <el-form label-width="80">
        <el-form-item label="角色">
          <el-select v-model="rolesForm.roleIds" multiple placeholder="请选择" style="width:100%">
            <el-option v-for="r in roleOptions" :key="r.roleId" :label="r.roleName" :value="r.roleId" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rolesVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRoles">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserList, updateUserStatus, assignRoles, getRoleOptions } from '@/api/user'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const query = reactive({ phone: '', status: null })
const userTypeMap = { 0: '普通', 1: '管理员' }

const statusVisible = ref(false)
const statusForm = reactive({ userId: null, status: 1 })
const rolesVisible = ref(false)
const rolesForm = reactive({ userId: null, roleIds: [] })
const roleOptions = ref([])

async function load() {
  loading.value = true
  try {
    const res = await getUserList({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      phone: query.phone || undefined,
      status: query.status ?? undefined
    })
    const data = res.data || {}
    list.value = data.list || []
    total.value = data.total ?? 0
  } finally {
    loading.value = false
  }
}

function resetQuery() {
  query.phone = ''
  query.status = null
  pageNum.value = 1
  load()
}

function openStatus(row) {
  statusForm.userId = row.userId
  statusForm.status = row.status
  statusVisible.value = true
}

async function submitStatus() {
  await updateUserStatus({ userId: statusForm.userId, status: statusForm.status })
  ElMessage.success('已更新')
  statusVisible.value = false
  load()
}

async function openRoles(row) {
  rolesForm.userId = row.userId
  rolesForm.roleIds = [...(row.roleIds || [])]
  if (roleOptions.value.length === 0) {
    const res = await getRoleOptions()
    roleOptions.value = res.data || []
  }
  rolesVisible.value = true
}

async function submitRoles() {
  await assignRoles({ userId: rolesForm.userId, roleIds: rolesForm.roleIds })
  ElMessage.success('已更新')
  rolesVisible.value = false
  load()
}

onMounted(load)
</script>

<style scoped>
.page { padding: 0; }
.query-form { margin-bottom: 16px; }
.pagination { margin-top: 16px; justify-content: flex-end; }
</style>
