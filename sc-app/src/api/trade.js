import request from './request'

export function getCartList() {
  return request.get('/trade/cart/list')
}

export function addCart(data) {
  return request.post('/trade/cart/add', data)
}

export function updateCartQuantity(cartItemId, quantity) {
  return request.put(`/trade/cart/${cartItemId}/quantity`, null, { params: { quantity } })
}

export function setCartChecked(cartItemId, checked) {
  return request.put(`/trade/cart/${cartItemId}/checked`, null, { params: { checked } })
}

export function removeCartItem(cartItemId) {
  return request.delete(`/trade/cart/${cartItemId}`)
}

export function createOrder(data) {
  return request.post('/trade/order/create', data)
}

export function payOrder(orderId) {
  return request.post(`/trade/order/${orderId}/pay`)
}

export function cancelOrder(orderId, reason) {
  return request.post(`/trade/order/${orderId}/cancel`, null, { params: { reason } })
}

export function getOrderPage(params) {
  return request.get('/trade/order/page', { params })
}

export function getOrder(orderId) {
  return request.get(`/trade/order/${orderId}`)
}

export function getOrderItems(orderId) {
  return request.get(`/trade/order/${orderId}/items`)
}

export function finishOrder(orderId) {
  return request.post(`/trade/order/${orderId}/finish`)
}
