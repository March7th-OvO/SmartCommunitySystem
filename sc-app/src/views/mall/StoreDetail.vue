<template>
  <div class="store-detail">
    <div v-if="loading">加载中...</div>
    <template v-else-if="store">
      <div class="store-info">
        <h2>{{ store.name }}</h2>
        <p>{{ store.address }}</p>
        <p v-if="store.contactPhone">电话：{{ store.contactPhone }}</p>
      </div>
      <h3>商品</h3>
      <div class="product-list">
        <div v-for="sp in products" :key="sp.id" class="product-item">
          <div class="info">
            <span class="name">商品ID {{ sp.productId }}</span>
            <span class="price">¥{{ sp.salePrice }} 库存{{ sp.stock }}</span>
          </div>
          <el-button type="primary" size="small" :disabled="!sp.stock || sp.stock < 1" @click="addCart(sp)">加购物车</el-button>
        </div>
      </div>
    </template>
    <div v-else>门店不存在</div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { getStore, getStoreProducts } from '@/api/mall'
import { addCart as addCartApi } from '@/api/trade'

const route = useRoute()
const userStore = useUserStore()
const loading = ref(true)
const store = ref(null)
const products = ref([])

const storeId = computed(() => Number(route.params.id))

async function load() {
  loading.value = true
  try {
    const [sRes, pRes] = await Promise.all([
      getStore(storeId.value),
      getStoreProducts(storeId.value)
    ])
    store.value = sRes.data
    products.value = (pRes.data || []).filter(p => p.status === 1)
  } catch (e) {
    store.value = null
    products.value = []
  } finally {
    loading.value = false
  }
}

async function addCart(sp) {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    return
  }
  const qty = 1
  if (sp.stock < qty) {
    ElMessage.warning('库存不足')
    return
  }
  try {
    await addCartApi({ storeId: storeId.value, productId: sp.productId, quantity: qty })
    ElMessage.success('已加入购物车')
  } catch (e) {}
}

onMounted(load)
</script>

<style scoped>
.store-detail { padding: 16px; }
.store-info { margin-bottom: 20px; padding: 16px; background: #f5f7fa; border-radius: 8px; }
.store-info h2 { margin: 0 0 8px; }
.store-info p { margin: 4px 0; font-size: 14px; color: #606266; }
h3 { margin: 16px 0 8px; }
.product-list { display: flex; flex-direction: column; gap: 12px; }
.product-item { display: flex; justify-content: space-between; align-items: center; padding: 12px; border: 1px solid #eee; border-radius: 8px; }
.product-item .info { display: flex; flex-direction: column; }
.product-item .name { font-weight: 500; }
.product-item .price { font-size: 12px; color: #f56c6c; margin-top: 4px; }
</style>
