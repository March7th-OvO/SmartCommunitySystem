<template>
  <div class="mall-home">
    <div class="search-bar">
      <el-input v-model="keyword" placeholder="搜索商品" clearable @keyup.enter="doSearch">
        <template #append><el-button @click="doSearch">搜索</el-button></template>
      </el-input>
      <router-link to="/cart" class="cart-link">购物车</router-link>
    </div>
    <div v-if="!keyword" class="sections">
      <h3>分类</h3>
      <div class="category-list">
        <div v-for="c in categories" :key="c.id" class="cat-item" @click="goCategory(c.id)">{{ c.name }}</div>
      </div>
      <h3>门店</h3>
      <div class="store-list">
        <div v-for="s in stores" :key="s.id" class="store-card" @click="$router.push(`/mall/store/${s.id}`)">
          <div class="name">{{ s.name }}</div>
          <div class="addr">{{ s.address || '-' }}</div>
        </div>
      </div>
    </div>
    <div v-else class="search-result">
      <div v-if="searchLoading">加载中...</div>
      <div v-else class="product-grid">
        <div v-for="p in searchList" :key="p.id" class="product-card" @click="$router.push(`/mall/product/${p.id}`)">
          <div class="name">{{ p.name }}</div>
          <div class="price">¥{{ p.salePrice ?? p.price ?? '-' }}</div>
        </div>
      </div>
      <el-pagination v-if="searchTotal > 0" layout="prev,pager,next" :total="searchTotal" :page-size="10" @current-change="onSearchPage" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCategoryTree, getStorePage } from '@/api/mall'
import { searchProducts as searchApi } from '@/api/search'

const keyword = ref('')
const categories = ref([])
const stores = ref([])
const searchList = ref([])
const searchTotal = ref(0)
const searchLoading = ref(false)
const searchPageNum = ref(1)

async function loadCategories() {
  const res = await getCategoryTree()
  categories.value = res.data || []
}

async function loadStores() {
  const res = await getStorePage({ pageNum: 1, pageSize: 20 })
  stores.value = res.data?.records ?? res.data?.list ?? []
}

function doSearch() {
  if (!keyword.value) return
  searchPageNum.value = 1
  runSearch()
}

async function runSearch() {
  searchLoading.value = true
  try {
    const res = await searchApi({ keyword: keyword.value, pageNum: searchPageNum.value, pageSize: 10 })
    const data = res.data || {}
    searchList.value = data.list || []
    searchTotal.value = data.total ?? 0
  } finally {
    searchLoading.value = false
  }
}

function onSearchPage(p) {
  searchPageNum.value = p
  runSearch()
}

function goCategory(id) {
  keyword.value = ''
  searchList.value = []
  searchTotal.value = 0
  // 可按分类筛选：searchApi({ categoryId: id, pageNum: 1, pageSize: 10 }) 后赋值 searchList
}

onMounted(() => {
  loadCategories()
  loadStores()
})
</script>

<style scoped>
.mall-home { padding: 16px; }
.search-bar { margin-bottom: 20px; display: flex; align-items: center; gap: 12px; }
.cart-link { color: #409eff; text-decoration: none; font-size: 14px; white-space: nowrap; }
.sections h3 { font-size: 16px; margin: 16px 0 8px; color: #303133; }
.category-list { display: flex; flex-wrap: wrap; gap: 8px; }
.cat-item { padding: 8px 14px; background: #f5f7fa; border-radius: 8px; cursor: pointer; }
.store-list { display: flex; flex-direction: column; gap: 12px; }
.store-card { padding: 14px; background: #fff; border: 1px solid #eee; border-radius: 8px; cursor: pointer; }
.store-card .name { font-weight: 600; }
.store-card .addr { font-size: 12px; color: #909399; margin-top: 4px; }
.search-result { min-height: 200px; }
.product-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 12px; }
.product-card { padding: 12px; background: #fff; border: 1px solid #eee; border-radius: 8px; cursor: pointer; }
.product-card .name { font-size: 14px; }
.product-card .price { color: #f56c6c; margin-top: 4px; }
</style>
