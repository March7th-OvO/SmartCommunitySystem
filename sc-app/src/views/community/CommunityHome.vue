<template>
  <div class="community-home">
    <h2>社区服务</h2>
    <div class="menu-grid">
      <router-link to="/community/notice" class="item">
        <el-icon><Bell /></el-icon>
        <span>公告</span>
      </router-link>
      <router-link to="/community/visitor" class="item">
        <el-icon><User /></el-icon>
        <span>访客登记</span>
      </router-link>
      <router-link to="/community/vehicle" class="item">
        <el-icon><Van /></el-icon>
        <span>我的车辆</span>
      </router-link>
      <router-link to="/community/repair" class="item">
        <el-icon><Setting /></el-icon>
        <span>报修</span>
      </router-link>
      <router-link to="/community/complaint" class="item">
        <el-icon><ChatDotRound /></el-icon>
        <span>投诉建议</span>
      </router-link>
      <router-link to="/community/property-bill" class="item">
        <el-icon><Wallet /></el-icon>
        <span>物业费</span>
      </router-link>
      <router-link to="/community/parking" class="item">
        <el-icon><Position /></el-icon>
        <span>车位</span>
      </router-link>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.community-home { padding: 16px; }
.menu-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; margin-top: 16px; }
.item { display: flex; flex-direction: column; align-items: center; gap: 8px; padding: 20px; background: #fff; border: 1px solid #eee; border-radius: 12px; text-decoration: none; color: #303133; }
.item .el-icon { font-size: 28px; color: #409eff; }
</style>
