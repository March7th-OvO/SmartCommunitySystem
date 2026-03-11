import request from './request'

// 分类
export function getCategoryTree() {
  return request.get('/mall/category/tree')
}
export function getCategory(id) {
  return request.get(`/mall/category/${id}`)
}
export function createCategory(data) {
  return request.post('/mall/category', data)
}
export function updateCategory(id, data) {
  return request.put(`/mall/category/${id}`, data)
}
export function deleteCategory(id) {
  return request.delete(`/mall/category/${id}`)
}

// 门店
export function getStorePage(params) {
  return request.get('/mall/store/page', { params })
}
export function getStore(id) {
  return request.get(`/mall/store/${id}`)
}
export function createStore(data) {
  return request.post('/mall/store', data)
}
export function updateStore(id, data) {
  return request.put(`/mall/store/${id}`, data)
}
export function deleteStore(id) {
  return request.delete(`/mall/store/${id}`)
}

// 商品
export function getProductPage(params) {
  return request.get('/mall/product/page', { params })
}
export function getProduct(id) {
  return request.get(`/mall/product/${id}`)
}
export function createProduct(data) {
  return request.post('/mall/product', data)
}
export function updateProduct(id, data) {
  return request.put(`/mall/product/${id}`, data)
}
export function deleteProduct(id) {
  return request.delete(`/mall/product/${id}`)
}
