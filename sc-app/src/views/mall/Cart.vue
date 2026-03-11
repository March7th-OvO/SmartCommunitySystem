<template>
  <div class="cart-page">
    <h2>购物车</h2>
    <div v-if="loading">加载中...</div>
    <div v-else-if="!list.length">购物车为空，<router-link to="/mall">去逛逛</router-link></div>
    <template v-else>
      <div class="cart-list">
        <div v-for="item in list" :key="item.id" class="cart-item">
          <el-checkbox v-model="item.checked" :true-label="1" :false-label="0" @change="toggleChecked(item)" />
          <div class="info">
            <span>门店{{ item.storeId }} 商品{{ item.productId }}</span>
            <span class="qty">
              <el-input-number v-model="item.quantity" :min="1" :max="99" size="small" @change="(v)=>updateQty(item,v)" />
            </span>
          </div>
          <el-button link type="danger" @click="removeItem(item)">删除</el-button>
        </div>
      </div>
      <div class="footer">
        <el-button type="primary" :disabled="!checkedCount" @click="goOrder">去结算 ({{ checkedCount }} 件)</el-button>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getCartList, updateCartQuantity, setCartChecked, removeCartItem } from '@/api/trade'

const router = useRouter()
const loading = ref(true)
const list = ref([])

const checkedCount = computed(() => list.value.filter(i => i.checked === 1).length)

async function load() {
  loading.value = true
  try {
    const res = await getCartList()
    const arr = res.data || []
    list.value = arr.map(i => ({ ...i, checked: i.checked ?? 0 }))
  } finally {
    loading.value = false
  }
}

function toggleChecked(item) {
  setCartChecked(item.id, item.checked).catch(() => { item.checked = item.checked === 1 ? 0 : 1 })
}

function updateQty(item, v) {
  if (v == null) return
  updateCartQuantity(item.id, v).catch(() => load())
}

function removeItem(item) {
  removeCartItem(item.id).then(() => load())
}

function goOrder() {
  const checked = list.value.filter(i => i.checked === 1)
  if (!checked.length) return
  const byStore = {}
  checked.forEach(i => {
    if (!byStore[i.storeId]) byStore[i.storeId] = []
    byStore[i.storeId].push(i)
  })
  const storeIds = Object.keys(byStore)
  if (storeIds.length > 1) {
    ElMessage.warning('请仅勾选同一门店的商品再结算')
    return
  }
  const storeId = Number(storeIds[0])
  const items = byStore[storeId].map(i => ({ productId: i.productId, quantity: i.quantity }))
  router.push({ path: '/order/create', query: { storeId, items: JSON.stringify(items) } })
}

onMounted(load)
</script>

<style scoped>
.cart-page { padding: 16px; padding-bottom: 80px; }
.cart-list { display: flex; flex-direction: column; gap: 12px; }
.cart-item { display: flex; align-items: center; gap: 12px; padding: 12px; border: 1px solid #eee; border-radius: 8px; }
.cart-item .info { flex: 1; display: flex; flex-direction: column; gap: 8px; }
.cart-item .qty { display: inline-block; }
.footer { position: fixed; bottom: 60px; left: 0; right: 0; padding: 12px 16px; background: #fff; border-top: 1px solid #eee; }
</style>
