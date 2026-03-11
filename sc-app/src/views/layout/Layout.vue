<template>
  <div class="layout">
    <div class="main">
      <router-view />
    </div>
    <div class="tabbar">
      <router-link to="/mall" class="tab" :class="{ active: isMall }">
        <el-icon><Goods /></el-icon>
        <span>商城</span>
      </router-link>
      <router-link to="/community" class="tab" :class="{ active: isCommunity }">
        <el-icon><OfficeBuilding /></el-icon>
        <span>社区</span>
      </router-link>
      <router-link to="/me" class="tab" :class="{ active: isMe }">
        <el-icon><User /></el-icon>
        <span>我的</span>
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const isMall = computed(() => route.path.startsWith('/mall') || route.path === '/cart' || route.path.startsWith('/order'))
const isCommunity = computed(() => route.path.startsWith('/community'))
const isMe = computed(() => route.path.startsWith('/me'))
</script>

<style scoped>
.layout { min-height: 100vh; display: flex; flex-direction: column; padding-bottom: 60px; }
.main { flex: 1; }
.tabbar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 56px;
  background: #fff;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: space-around;
  align-items: center;
  z-index: 100;
}
.tab {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
  color: #909399;
  text-decoration: none;
  font-size: 12px;
}
.tab .el-icon { font-size: 22px; }
.tab.active { color: #409eff; }
</style>
