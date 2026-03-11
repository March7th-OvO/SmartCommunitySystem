import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import Layout from '@/views/layout/Layout.vue'

const routes = [
  { path: '/login', name: 'Login', component: () => import('@/views/Login.vue'), meta: { guest: true } },
  { path: '/register', name: 'Register', component: () => import('@/views/Register.vue'), meta: { guest: true } },
  {
    path: '/',
    component: Layout,
    children: [
      { path: '', redirect: '/mall' },
      { path: 'mall', name: 'MallHome', component: () => import('@/views/mall/MallHome.vue') },
      { path: 'mall/store/:id', name: 'StoreDetail', component: () => import('@/views/mall/StoreDetail.vue') },
      { path: 'mall/product/:id', name: 'ProductDetail', component: () => import('@/views/mall/ProductDetail.vue') },
      { path: 'cart', name: 'Cart', component: () => import('@/views/mall/Cart.vue'), meta: { requiresAuth: true } },
      { path: 'order/create', name: 'OrderCreate', component: () => import('@/views/mall/OrderCreate.vue'), meta: { requiresAuth: true } },
      { path: 'order/list', name: 'OrderList', component: () => import('@/views/mall/OrderList.vue'), meta: { requiresAuth: true } },
      { path: 'order/:id', name: 'OrderDetail', component: () => import('@/views/mall/OrderDetail.vue'), meta: { requiresAuth: true } },
      { path: 'community', name: 'CommunityHome', component: () => import('@/views/community/CommunityHome.vue') },
      { path: 'community/notice', name: 'NoticeList', component: () => import('@/views/community/NoticeList.vue') },
      { path: 'community/visitor', name: 'Visitor', component: () => import('@/views/community/Visitor.vue'), meta: { requiresAuth: true } },
      { path: 'community/vehicle', name: 'Vehicle', component: () => import('@/views/community/Vehicle.vue'), meta: { requiresAuth: true } },
      { path: 'community/repair', name: 'Repair', component: () => import('@/views/community/Repair.vue'), meta: { requiresAuth: true } },
      { path: 'community/complaint', name: 'Complaint', component: () => import('@/views/community/Complaint.vue'), meta: { requiresAuth: true } },
      { path: 'community/property-bill', name: 'PropertyBill', component: () => import('@/views/community/PropertyBill.vue'), meta: { requiresAuth: true } },
      { path: 'community/parking', name: 'Parking', component: () => import('@/views/community/Parking.vue') },
      { path: 'me', name: 'Me', component: () => import('@/views/me/Me.vue'), meta: { requiresAuth: true } },
      { path: 'me/profile', name: 'Profile', component: () => import('@/views/me/Profile.vue'), meta: { requiresAuth: true } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, _from, next) => {
  const userStore = useUserStore()
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    next({ path: '/login', query: { redirect: to.fullPath } })
  } else if (to.meta.guest && userStore.isLoggedIn) {
    next({ path: '/' })
  } else {
    next()
  }
})

export default router
