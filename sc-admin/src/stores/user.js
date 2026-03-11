import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('sc_admin_token') || '')
  const userId = ref(localStorage.getItem('sc_admin_userId') ? Number(localStorage.getItem('sc_admin_userId')) : null)
  const nickname = ref(localStorage.getItem('sc_admin_nickname') || '')
  const phone = ref(localStorage.getItem('sc_admin_phone') || '')

  const isLoggedIn = computed(() => !!token.value)

  function setLogin(res) {
    token.value = res.token || ''
    userId.value = res.userId ?? null
    nickname.value = res.nickname || ''
    phone.value = res.phone || ''
    localStorage.setItem('sc_admin_token', token.value)
    if (userId.value != null) localStorage.setItem('sc_admin_userId', String(userId.value))
    localStorage.setItem('sc_admin_nickname', nickname.value)
    localStorage.setItem('sc_admin_phone', phone.value)
  }

  function logout() {
    token.value = ''
    userId.value = null
    nickname.value = ''
    phone.value = ''
    localStorage.removeItem('sc_admin_token')
    localStorage.removeItem('sc_admin_userId')
    localStorage.removeItem('sc_admin_nickname')
    localStorage.removeItem('sc_admin_phone')
  }

  return { token, userId, nickname, phone, isLoggedIn, setLogin, logout }
})
