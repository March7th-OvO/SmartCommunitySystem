import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import Layout from '@/views/layout/Layout.vue'

const routes = [
  { path: '/login', name: 'Login', component: () => import('@/views/Login.vue'), meta: { guest: true } },
  {
    path: '/',
    component: Layout,
    meta: { requiresAuth: true },
    children: [
      { path: '', redirect: '/dashboard' },
      { path: 'dashboard', name: 'Dashboard', component: () => import('@/views/Dashboard.vue') },
      { path: 'user', name: 'UserList', component: () => import('@/views/user/UserList.vue') },
      { path: 'mall/category', name: 'Category', component: () => import('@/views/mall/Category.vue') },
      { path: 'mall/store', name: 'Store', component: () => import('@/views/mall/Store.vue') },
      { path: 'mall/product', name: 'Product', component: () => import('@/views/mall/Product.vue') },
      { path: 'trade/order', name: 'OrderList', component: () => import('@/views/trade/OrderList.vue') },
      { path: 'community/notice', name: 'Notice', component: () => import('@/views/community/Notice.vue') },
      { path: 'community/visitor', name: 'Visitor', component: () => import('@/views/community/Visitor.vue') },
      { path: 'community/repair', name: 'Repair', component: () => import('@/views/community/Repair.vue') },
      { path: 'community/complaint', name: 'Complaint', component: () => import('@/views/community/Complaint.vue') },
      { path: 'community/property-bill', name: 'PropertyBill', component: () => import('@/views/community/PropertyBill.vue') },
      { path: 'community/parking', name: 'Parking', component: () => import('@/views/community/Parking.vue') }
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
