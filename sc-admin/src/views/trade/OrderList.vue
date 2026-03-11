<template>
  <div class="page">
    <el-card>
      <el-form :inline="true" :model="query" class="query-form">
        <el-form-item label="订单状态">
          <el-select v-model="query.orderStatus" placeholder="全部" clearable style="width:120px">
            <el-option label="待支付" :value="0" />
            <el-option label="待发货" :value="1" />
            <el-option label="待取货" :value="2" />
            <el-option label="已完成" :value="3" />
            <el-option label="已取消" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="load">查询</el-button></el-form-item>
      </el-form>
      <el-table :data="list" v-loading="loading" stripe>
        <el-table-column prop="id" label="订单ID" width="90" />
        <el-table-column prop="orderNo" label="订单号" width="160" />
        <el-table-column prop="userId" label="用户ID" width="80" />
        <el-table-column prop="storeId" label="门店ID" width="80" />
        <el-table-column prop="totalAmount" label="金额" width="90" />
        <el-table-column prop="orderStatus" label="状态" width="90">
          <template #default="{ row }">{{ orderStatusMap[row.orderStatus] ?? row.orderStatus }}</template>
        </el-table-column>
        <el-table-column prop="receiverName" label="收货人" width="100" />
        <el-table-column prop="receiverPhone" label="手机" width="120" />
        <el-table-column prop="createdAt" label="下单时间" width="170" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.orderStatus === 1" link type="primary" @click="deliver(row)">发货</el-button>
            <el-button v-if="row.orderStatus === 2" link type="primary" @click="finish(row)">确认完成</el-button>
            <el-button link @click="showItems(row)">明细</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" :page-sizes="[10,20,50]" layout="total,sizes,prev,pager,next" @current-change="load" @size-change="load" class="pagination" />
    </el-card>
    <el-dialog v-model="itemsVisible" title="订单明细" width="560">
      <el-table :data="orderItems" size="small"><el-table-column prop="productId" label="商品ID" width="90" /><el-table-column prop="productName" label="商品名" /><el-table-column prop="quantity" label="数量" width="80" /><el-table-column prop="salePrice" label="单价" width="90" /></el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getOrderAdminPage, deliverOrder, finishOrder, getOrderItems } from '@/api/trade'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const query = reactive({ orderStatus: null })
const orderStatusMap = { 0: '待支付', 1: '待发货', 2: '待取货', 3: '已完成', 4: '已取消' }
const itemsVisible = ref(false)
const orderItems = ref([])

async function load() {
  loading.value = true
  try {
    const res = await getOrderAdminPage({ pageNum: pageNum.value, pageSize: pageSize.value, orderStatus: query.orderStatus ?? undefined })
    list.value = res.data?.records ?? []
    total.value = res.data?.total ?? 0
  } finally {
    loading.value = false
  }
}

async function deliver(row) {
  await deliverOrder(row.id)
  ElMessage.success('已发货')
  load()
}

async function finish(row) {
  await finishOrder(row.id)
  ElMessage.success('已确认完成')
  load()
}

async function showItems(row) {
  const res = await getOrderItems(row.id)
  orderItems.value = res.data ?? []
  itemsVisible.value = true
}

onMounted(load)
</script>

<style scoped>
.page { padding: 0; }
.query-form { margin-bottom: 16px; }
.pagination { margin-top: 16px; justify-content: flex-end; }
</style>
