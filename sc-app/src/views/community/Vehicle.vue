<template>
  <div class="vehicle-page">
    <h2>我的车辆</h2>
    <el-form v-if="showForm" :model="form" label-width="90">
      <el-form-item label="小区ID"><el-input-number v-model="form.communityId" :min="1" style="width:100%" /></el-form-item>
      <el-form-item label="车牌号"><el-input v-model="form.plateNumber" /></el-form-item>
      <el-form-item label="车位ID"><el-input-number v-model="form.parkingSpaceId" :min="0" style="width:100%" placeholder="选填" /></el-form-item>
      <el-form-item><el-button type="primary" @click="bind">绑定</el-button><el-button @click="showForm = false">取消</el-button></el-form-item>
    </el-form>
    <el-button v-else type="primary" @click="showForm = true">添加车辆</el-button>
    <div class="list">
      <div v-for="v in list" :key="v.id" class="card">
        <div>车牌：{{ v.plateNumber }} 小区{{ v.communityId }}</div>
        <el-button link type="danger" @click="unbind(v.id)">解绑</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getVehicleMy, bindVehicle, unbindVehicle } from '@/api/community'

const list = ref([])
const showForm = ref(false)
const form = reactive({ communityId: null, plateNumber: '', parkingSpaceId: null })

async function load() {
  const res = await getVehicleMy()
  list.value = res.data ?? []
}

async function bind() {
  if (!form.communityId || !form.plateNumber) {
    ElMessage.warning('请填写小区和车牌')
    return
  }
  await bindVehicle({ communityId: form.communityId, plateNumber: form.plateNumber, parkingSpaceId: form.parkingSpaceId || null })
  ElMessage.success('绑定成功')
  showForm.value = false
  load()
}

async function unbind(id) {
  await unbindVehicle(id)
  ElMessage.success('已解绑')
  load()
}

onMounted(load)
</script>

<style scoped>
.vehicle-page { padding: 16px; }
.list { margin-top: 16px; }
.card { display: flex; justify-content: space-between; align-items: center; padding: 12px; border: 1px solid #eee; border-radius: 8px; margin-bottom: 8px; }
</style>
