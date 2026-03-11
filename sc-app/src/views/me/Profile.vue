<template>
  <div class="profile-page">
    <h2>个人资料</h2>
    <el-form v-if="form" :model="form" label-width="80">
      <el-form-item label="手机号"><span>{{ form.phone }}</span></el-form-item>
      <el-form-item label="昵称"><el-input v-model="form.nickname" /></el-form-item>
      <el-form-item><el-button type="primary" :loading="saving" @click="save">保存</el-button></el-form-item>
    </el-form>
    <h3>修改密码</h3>
    <el-form :model="pwdForm" label-width="80">
      <el-form-item label="原密码"><el-input v-model="pwdForm.oldPassword" type="password" show-password /></el-form-item>
      <el-form-item label="新密码"><el-input v-model="pwdForm.newPassword" type="password" show-password /></el-form-item>
      <el-form-item><el-button @click="changePwd">修改密码</el-button></el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getProfile, updateProfile, changePassword } from '@/api/user'

const form = ref(null)
const saving = ref(false)
const pwdForm = reactive({ oldPassword: '', newPassword: '' })

async function load() {
  const res = await getProfile()
  form.value = { phone: res.data?.phone, nickname: res.data?.nickname ?? '', avatar: res.data?.avatar, gender: res.data?.gender }
}

async function save() {
  saving.value = true
  try {
    await updateProfile({ nickname: form.value.nickname, avatar: form.value.avatar, gender: form.value.gender })
    ElMessage.success('保存成功')
  } finally {
    saving.value = false
  }
}

async function changePwd() {
  if (!pwdForm.oldPassword || !pwdForm.newPassword) {
    ElMessage.warning('请填写原密码和新密码')
    return
  }
  await changePassword({ oldPassword: pwdForm.oldPassword, newPassword: pwdForm.newPassword })
  ElMessage.success('密码已修改')
  pwdForm.oldPassword = ''
  pwdForm.newPassword = ''
}

onMounted(load)
</script>

<style scoped>
.profile-page { padding: 16px; }
h3 { margin: 24px 0 12px; }
</style>
