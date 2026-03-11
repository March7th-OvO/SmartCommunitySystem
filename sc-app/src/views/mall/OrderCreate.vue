<template>
  <div class="order-create">
    <h2>确认订单</h2>
    <el-form :model="form" label-width="90">
      <el-form-item label="配送方式">
        <el-radio-group v-model="form.deliveryType">
          <el-radio :label="1">到店自提</el-radio>
          <el-radio :label="2">配送</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item v-if="form.deliveryType === 2" label="收货人"><el-input v-model="form.receiverName" /></el-form-item>
      <el-form-item v-if="form.deliveryType === 2" label="手机"><el-input v-model="form.receiverPhone" /></el-form-item>
      <el-form-item v-if="form.deliveryType === 2" label="地址"><el-input v-model="form.receiverAddress" type="textarea" /></el-form-item>
      <el-form-item label="备注"><el-input v-model="form.remark" placeholder="选填" /></el-form-item>
    </el-form>
    <div class="footer">
      <el-button type="primary" :loading="submitting" @click="submit">提交订单</el-button>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createOrder } from '@/api/trade'

const route = useRoute()
const router = useRouter()
const submitting = ref(false)

const storeId = ref(0)
const items = ref([])

const form = reactive({
  deliveryType: 1,
  receiverName: '',
  receiverPhone: '',
  receiverAddress: '',
  remark: ''
})

onMounted(() => {
  storeId.value = Number(route.query.storeId) || 0
  try {
    items.value = JSON.parse(route.query.items || '[]')
  } catch {
    items.value = []
  }
  if (!storeId.value || !items.value.length) {
    ElMessage.warning('参数错误')
    router.replace('/cart')
  }
})

async function submit() {
  if (form.deliveryType === 2 && (!form.receiverName || !form.receiverPhone || !form.receiverAddress)) {
    ElMessage.warning('请填写收货信息')
    return
  }
  submitting.value = true
  try {
    const res = await createOrder({
      storeId: storeId.value,
      deliveryType: form.deliveryType,
      receiverName: form.receiverName || null,
      receiverPhone: form.receiverPhone || null,
      receiverAddress: form.receiverAddress || null,
      remark: form.remark || null,
      items: items.value
    })
    const orderId = res.data
    ElMessage.success('下单成功')
    router.replace({ path: '/order/' + orderId })
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.order-create { padding: 16px; padding-bottom: 80px; }
.footer { margin-top: 24px; }
</style>
