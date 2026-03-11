import request from './request'

/** 管理端订单分页 */
export function getOrderAdminPage(params) {
  return request.get('/trade/order/admin/page', { params })
}

/** 发货 */
export function deliverOrder(orderId) {
  return request.post(`/trade/order/${orderId}/deliver`)
}

/** 完成（管理端代用户确认） */
export function finishOrder(orderId) {
  return request.post(`/trade/order/${orderId}/finish`)
}

/** 订单明细 */
export function getOrderItems(orderId) {
  return request.get(`/trade/order/${orderId}/items`)
}
