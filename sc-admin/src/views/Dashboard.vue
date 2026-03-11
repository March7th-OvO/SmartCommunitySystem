<template>
  <div>
    <h2 style="margin-top:0">工作台</h2>
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat">
            <el-icon :size="32" color="#409eff"><User /></el-icon>
            <span>用户管理</span>
          </div>
          <p>管理用户、角色与状态</p>
          <el-button type="primary" link @click="$router.push('/user')">进入</el-button>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat">
            <el-icon :size="32" color="#67c23a"><Goods /></el-icon>
            <span>商城管理</span>
          </div>
          <p>分类、门店、商品</p>
          <el-button type="primary" link @click="$router.push('/mall/category')">进入</el-button>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat">
            <el-icon :size="32" color="#e6a23c"><List /></el-icon>
            <span>订单</span>
          </div>
          <p>订单列表、发货与完成</p>
          <el-button type="primary" link @click="$router.push('/trade/order')">进入</el-button>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="stat">
            <el-icon :size="32" color="#909399"><OfficeBuilding /></el-icon>
            <span>社区物业</span>
          </div>
          <p>公告、访客、报修、投诉、物业费、车位</p>
          <el-button type="primary" link @click="$router.push('/community/notice')">进入</el-button>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
</script>

<style scoped>
.stat { display: flex; align-items: center; gap: 12px; margin-bottom: 8px; font-weight: 600; }
.stat span { font-size: 16px; }
p { color: #909399; font-size: 13px; margin: 0 0 12px; }
</style>
