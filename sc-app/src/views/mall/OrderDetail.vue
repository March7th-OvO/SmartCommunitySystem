<template>
  <div class="order-detail">
    <div v-if="loading">加载中...</div>
    <template v-else-if="order">
      <div class="info">
        <p>订单号：{{ order.orderNo }}</p>
        <p>状态：{{ statusText(order.orderStatus) }}</p>
        <p>金额：¥{{ order.totalAmount }}</p>
        <p>收货人：{{ order.receiverName }} {{ order.receiverPhone }}</p>
        <p v-if="order.receiverAddress">地址：{{ order.receiverAddress }}</p>
      </div>
      <h3>商品明细</h3>
      <div class="items">
        <div v-for="i in items" :key="i.id" class="item">
          {{ i.productName || '商品' + i.productId }} × {{ i.quantity }} ¥{{ i.salePrice }}
        </div>
      </div>
      <div class="act">
        <el-button v-if="order.orderStatus === 0" type="primary" @click="pay">去支付</el-button>
        <el-button v-if="order.orderStatus === 0" @click="cancel">取消订单</el-button>
        <el-button v-if="order.orderStatus === 2" type="primary" @click="finish">确认收货</el-button>
      </div>
    </template>
    <div v-else>订单不存在</div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getOrder, getOrderItems, payOrder, cancelOrder, finishOrder } from '@/api/trade'

const route = useRoute()
const loading = ref(true)
const order = ref(null)
const items = ref([])

const orderId = computed(() => Number(route.params.id))

const statusText = (s) => ({ 0: '待支付', 1: '待发货', 2: '待取货', 3: '已完成', 4: '已取消' })[s] ?? s

async function load() {
  loading.value = true
  try {
    const [oRes, iRes] = await Promise.all([
      getOrder(orderId.value),
      getOrderItems(orderId.value)
    ])
    order.value = oRes.data
    items.value = iRes.data || []
  } catch (e) {
    order.value = null
    items.value = []
  } finally {
    loading.value = false
  }
}

function pay() {
  payOrder(orderId.value).then(() => { ElMessage.success('支付成功'); load() })
}

function cancel() {
  cancelOrder(orderId.value).then(() => { ElMessage.success('已取消'); load() })
}

function finish() {
  finishOrder(orderId.value).then(() => { ElMessage.success('已确认收货'); load() })
}

onMounted(load)
</script>

<style scoped>
.order-detail { padding: 16px; }
.info p { margin: 6px 0; }
h3 { margin: 16px 0 8px; }
.items { margin-bottom: 16px; }
.item { padding: 8px 0; border-bottom: 1px solid #eee; }
.act { display: flex; gap: 12px; }
</style>
