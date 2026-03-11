import request from './request'

export function getCommunityList() {
  return request.get('/community/list')
}

export function getNoticeList(params) {
  return request.get('/community/notice/list', { params })
}

export function getNotice(id) {
  return request.get(`/community/notice/${id}`)
}

export function submitVisitor(data) {
  return request.post('/community/visitor/submit', data)
}

export function getVisitorMy(params) {
  return request.get('/community/visitor/my', { params })
}

export function getVehicleMy() {
  return request.get('/community/vehicle/my')
}

export function bindVehicle(data) {
  return request.post('/community/vehicle/bind', data)
}

export function unbindVehicle(id) {
  return request.delete(`/community/vehicle/${id}`)
}

export function submitRepair(data) {
  return request.post('/community/repair/submit', data)
}

export function getRepairMy(params) {
  return request.get('/community/repair/my', { params })
}

export function cancelRepair(id) {
  return request.post(`/community/repair/${id}/cancel`)
}

export function submitComplaint(data) {
  return request.post('/community/complaint/submit', data)
}

export function getComplaintMy(params) {
  return request.get('/community/complaint/my', { params })
}

export function getPropertyBillMy(params) {
  return request.get('/community/property-bill/my', { params })
}

export function payPropertyBill(billId) {
  return request.post(`/community/property-bill/${billId}/pay`)
}

export function getParkingList(communityId, status) {
  return request.get('/community/parking/list', { params: { communityId, status } })
}
