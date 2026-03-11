<template>
  <div class="order-list">
    <h2>我的订单</h2>
    <el-tabs v-model="statusTab" @tab-change="load">
      <el-tab-pane label="全部" name="" />
      <el-tab-pane label="待支付" name="0" />
      <el-tab-pane label="待发货" name="1" />
      <el-tab-pane label="待取货" name="2" />
      <el-tab-pane label="已完成" name="3" />
      <el-tab-pane label="已取消" name="4" />
    </el-tabs>
    <div v-if="loading">加载中...</div>
    <div v-else class="list">
      <div v-for="o in list" :key="o.id" class="order-card" @click="$router.push(`/order/${o.id}`)">
        <div class="head">
          <span>订单号 {{ o.orderNo }}</span>
          <el-tag :type="statusType(o.orderStatus)">{{ statusText(o.orderStatus) }}</el-tag>
        </div>
        <div class="body">金额 ¥{{ o.totalAmount }} · {{ o.createdAt }}</div>
        <div class="act">
          <el-button v-if="o.orderStatus === 0" type="primary" size="small" @click.stop="pay(o)">去支付</el-button>
          <el-button v-if="o.orderStatus === 0" size="small" @click.stop="cancel(o)">取消</el-button>
          <el-button v-if="o.orderStatus === 2" type="primary" size="small" @click.stop="finish(o)">确认收货</el-button>
        </div>
      </div>
    </div>
    <el-pagination v-if="total > 0" layout="prev,pager,next" :total="total" :page-size="10" @current-change="onPage" />
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { getOrderPage, payOrder, cancelOrder, finishOrder } from '@/api/trade'

const statusTab = ref('')
const loading = ref(false)
const list = ref([])
const total = ref(0)
const pageNum = ref(1)

const statusText = (s) => ({ 0: '待支付', 1: '待发货', 2: '待取货', 3: '已完成', 4: '已取消' })[s] ?? s
const statusType = (s) => ({ 0: 'warning', 1: 'primary', 2: 'primary', 3: 'success', 4: 'info' })[s] ?? 'info'

async function load() {
  loading.value = true
  try {
    const res = await getOrderPage({
      pageNum: pageNum.value,
      pageSize: 10,
      orderStatus: statusTab.value ? Number(statusTab.value) : undefined
    })
    list.value = res.data?.records ?? res.data?.list ?? []
    total.value = res.data?.total ?? 0
  } finally {
    loading.value = false
  }
}

function onPage(p) {
  pageNum.value = p
  load()
}

function pay(o) {
  payOrder(o.id).then(() => { ElMessage.success('支付成功'); load() })
}

function cancel(o) {
  cancelOrder(o.id).then(() => { ElMessage.success('已取消'); load() })
}

function finish(o) {
  finishOrder(o.id).then(() => { ElMessage.success('已确认收货'); load() })
}

watch(statusTab, () => { pageNum.value = 1; load() }, { immediate: true })
</script>

<style scoped>
.order-list { padding: 16px; }
.list { display: flex; flex-direction: column; gap: 12px; margin-top: 12px; }
.order-card { padding: 12px; border: 1px solid #eee; border-radius: 8px; cursor: pointer; }
.order-card .head { display: flex; justify-content: space-between; align-items: center; }
.order-card .body { margin: 8px 0; font-size: 14px; color: #606266; }
.order-card .act { display: flex; gap: 8px; }
</style>
