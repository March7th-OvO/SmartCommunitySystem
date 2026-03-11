import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import router from '@/router'

const request = axios.create({
  baseURL: '/api',
  timeout: 15000
})

request.interceptors.request.use(config => {
  const userStore = useUserStore()
  if (userStore.token) {
    config.headers.Authorization = `Bearer ${userStore.token}`
  }
  return config
})

request.interceptors.response.use(
  res => {
    const { code, message, data } = res.data ?? {}
    if (code !== 0 && code !== undefined) {
      ElMessage.error(message || '请求失败')
      if (code === 1002) {
        useUserStore().logout()
        router.replace('/login')
      }
      return Promise.reject(new Error(message || '请求失败'))
    }
    return res.data
  },
  err => {
    const msg = err.response?.data?.message || err.message || '网络错误'
    ElMessage.error(msg)
    if (err.response?.status === 401) {
      const userStore = useUserStore()
      userStore.logout()
      router.replace('/login')
    }
    return Promise.reject(err)
  }
)

export default request
