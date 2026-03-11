<template>
  <div class="product-detail">
    <div v-if="loading">加载中...</div>
    <template v-else-if="product">
      <div class="info">
        <h2>{{ product.name }}</h2>
        <p class="price">¥{{ product.salePrice }}</p>
        <p v-if="product.description">{{ product.description }}</p>
      </div>
      <div class="stores">
        <h3>可购门店</h3>
        <div v-for="sp in storeProducts" :key="sp.id" class="store-row">
          <span>门店ID {{ sp.storeId }} — ¥{{ sp.salePrice }} 库存{{ sp.stock }}</span>
          <el-button type="primary" size="small" :disabled="!sp.stock || sp.stock < 1" @click="addCart(sp)">加购物车</el-button>
        </div>
      </div>
    </template>
    <div v-else>商品不存在</div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { getProduct, getProductStores } from '@/api/mall'
import { addCart as addCartApi } from '@/api/trade'

const route = useRoute()
const userStore = useUserStore()
const loading = ref(true)
const product = ref(null)
const storeProducts = ref([])

const productId = computed(() => Number(route.params.id))

async function load() {
  loading.value = true
  try {
    const [pRes, sRes] = await Promise.all([
      getProduct(productId.value),
      getProductStores(productId.value)
    ])
    product.value = pRes.data
    storeProducts.value = (sRes.data || []).filter(sp => sp.status === 1 && sp.stock > 0)
  } catch (e) {
    product.value = null
    storeProducts.value = []
  } finally {
    loading.value = false
  }
}

async function addCart(sp) {
  if (!userStore.isLoggedIn) { ElMessage.warning('请先登录'); return }
  try {
    await addCartApi({ storeId: sp.storeId, productId: productId.value, quantity: 1 })
    ElMessage.success('已加入购物车')
  } catch (e) {}
}

onMounted(load)
</script>

<style scoped>
.product-detail { padding: 16px; }
.info { margin-bottom: 20px; }
.info h2 { margin: 0 0 8px; }
.info .price { color: #f56c6c; font-size: 18px; }
.stores h3 { margin: 16px 0 8px; }
.store-row { display: flex; justify-content: space-between; align-items: center; padding: 10px 0; border-bottom: 1px solid #eee; }
</style>
