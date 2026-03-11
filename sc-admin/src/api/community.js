import request from './request'

// 小区
export function getCommunityList() {
  return request.get('/community/list')
}

// 公告
export function getNoticeList(params) {
  return request.get('/community/notice/list', { params })
}
export function getNoticeAdminPage(params) {
  return request.get('/community/notice/admin/page', { params })
}
export function getNotice(id) {
  return request.get(`/community/notice/${id}`)
}
export function createNotice(data) {
  return request.post('/community/notice/admin', data)
}
export function updateNotice(id, data) {
  return request.put(`/community/notice/admin/${id}`, data)
}
export function deleteNotice(id) {
  return request.delete(`/community/notice/admin/${id}`)
}

// 访客
export function getVisitorAdminPage(params) {
  return request.get('/community/visitor/admin/page', { params })
}
export function auditVisitor(id, params) {
  return request.post(`/community/visitor/admin/${id}/audit`, null, { params })
}

// 车位
export function getParkingAdminList(params) {
  return request.get('/community/parking/admin/list', { params })
}

// 报修
export function getRepairAdminPage(params) {
  return request.get('/community/repair/admin/page', { params })
}
export function startRepair(id) {
  return request.post(`/community/repair/admin/${id}/start`)
}
export function finishRepair(id) {
  return request.post(`/community/repair/admin/${id}/finish`)
}

// 投诉
export function getComplaintAdminPage(params) {
  return request.get('/community/complaint/admin/page', { params })
}
export function handleComplaint(id, params) {
  return request.post(`/community/complaint/admin/${id}/handle`, null, { params })
}

// 物业费
export function getPropertyBillAdminPage(params) {
  return request.get('/community/property-bill/admin/page', { params })
}
export function createPropertyBill(data) {
  return request.post('/community/property-bill/admin', data)
}
