<template>
  <div class="me-page">
    <div class="user-card">
      <div class="name">{{ profile?.nickname || profile?.phone || '用户' }}</div>
      <div class="phone">{{ profile?.phone }}</div>
      <div class="wallet">钱包余额：¥{{ profile?.walletBalance ?? '0.00' }}</div>
    </div>
    <div class="menu">
      <router-link to="/me/profile" class="item">
        <span>个人资料</span>
        <el-icon><ArrowRight /></el-icon>
      </router-link>
      <router-link to="/cart" class="item">
        <span>购物车</span>
        <el-icon><ArrowRight /></el-icon>
      </router-link>
      <router-link to="/order/list" class="item">
        <span>我的订单</span>
        <el-icon><ArrowRight /></el-icon>
      </router-link>
    </div>
    <div class="logout">
      <el-button type="danger" plain @click="logout">退出登录</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { getProfile } from '@/api/user'

const router = useRouter()
const userStore = useUserStore()
const profile = ref(null)

async function load() {
  try {
    const res = await getProfile()
    profile.value = res.data
  } catch (e) {
    profile.value = null
  }
}

function logout() {
  userStore.logout()
  router.push('/login')
}

onMounted(load)
</script>

<style scoped>
.me-page { padding: 16px; }
.user-card { padding: 24px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius: 12px; color: #fff; margin-bottom: 20px; }
.user-card .name { font-size: 18px; font-weight: 600; }
.user-card .phone, .user-card .wallet { font-size: 14px; margin-top: 8px; opacity: 0.9; }
.menu { background: #fff; border-radius: 8px; overflow: hidden; border: 1px solid #eee; }
.item { display: flex; justify-content: space-between; align-items: center; padding: 14px 16px; text-decoration: none; color: #303133; border-bottom: 1px solid #eee; }
.item:last-child { border-bottom: none; }
.logout { margin-top: 24px; text-align: center; }
</style>
