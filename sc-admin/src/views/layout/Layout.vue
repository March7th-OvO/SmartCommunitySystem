<template>
  <el-container class="layout">
    <el-aside width="220px" class="aside">
      <div class="logo">智慧社区管理</div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#1a2332"
        text-color="#b0b8c4"
        active-text-color="#409eff"
      >
        <el-menu-item index="/dashboard">
          <el-icon><Odometer /></el-icon>
          <span>工作台</span>
        </el-menu-item>
        <el-sub-menu index="user-menu">
          <template #title>
            <el-icon><User /></el-icon>
            <span>用户管理</span>
          </template>
          <el-menu-item index="/user">用户列表</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="mall-menu">
          <template #title>
            <el-icon><Goods /></el-icon>
            <span>商城管理</span>
          </template>
          <el-menu-item index="/mall/category">分类</el-menu-item>
          <el-menu-item index="/mall/store">门店</el-menu-item>
          <el-menu-item index="/mall/product">商品</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="trade-menu">
          <template #title>
            <el-icon><List /></el-icon>
            <span>交易</span>
          </template>
          <el-menu-item index="/trade/order">订单列表</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="community-menu">
          <template #title>
            <el-icon><OfficeBuilding /></el-icon>
            <span>社区物业</span>
          </template>
          <el-menu-item index="/community/notice">公告</el-menu-item>
          <el-menu-item index="/community/visitor">访客</el-menu-item>
          <el-menu-item index="/community/repair">报修</el-menu-item>
          <el-menu-item index="/community/complaint">投诉</el-menu-item>
          <el-menu-item index="/community/property-bill">物业费</el-menu-item>
          <el-menu-item index="/community/parking">车位</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <span class="title">{{ pageTitle }}</span>
        <div class="user">
          <span>{{ userStore.nickname || userStore.phone }}</span>
          <el-button type="danger" link @click="handleLogout">退出</el-button>
        </div>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)

const pageTitle = computed(() => {
  const map = {
    '/dashboard': '工作台',
    '/user': '用户列表',
    '/mall/category': '分类管理',
    '/mall/store': '门店管理',
    '/mall/product': '商品管理',
    '/trade/order': '订单列表',
    '/community/notice': '公告管理',
    '/community/visitor': '访客审核',
    '/community/repair': '报修处理',
    '/community/complaint': '投诉处理',
    '/community/property-bill': '物业费',
    '/community/parking': '车位管理'
  }
  return map[route.path] || '智慧社区'
})

function handleLogout() {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.layout { height: 100vh; }
.aside { background: #1a2332; }
.logo {
  height: 56px;
  line-height: 56px;
  text-align: center;
  color: #fff;
  font-weight: 600;
  border-bottom: 1px solid #2a3548;
}
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  border-bottom: 1px solid #eee;
  padding: 0 20px;
}
.title { font-size: 18px; font-weight: 500; }
.user { display: flex; align-items: center; gap: 12px; }
.main { background: #f5f7fa; padding: 20px; overflow: auto; }
</style>
