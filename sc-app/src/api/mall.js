import request from './request'

export function getCategoryTree() {
  return request.get('/mall/category/tree')
}

export function getStorePage(params) {
  return request.get('/mall/store/page', { params })
}

export function getStore(id) {
  return request.get(`/mall/store/${id}`)
}

export function getStoreProducts(storeId) {
  return request.get(`/mall/store-product/store/${storeId}`)
}

export function getProduct(id) {
  return request.get(`/mall/product/${id}`)
}

export function getProductPage(params) {
  return request.get('/mall/product/page', { params })
}

export function getProductStores(productId) {
  return request.get(`/mall/store-product/product/${productId}`)
}
