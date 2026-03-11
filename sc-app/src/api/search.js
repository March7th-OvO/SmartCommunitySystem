import request from './request'

export function searchProducts(params) {
  return request.get('/search', { params })
}
