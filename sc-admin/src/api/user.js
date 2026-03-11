import request from './request'

/** 用户列表（分页） */
export function getUserList(params) {
  return request.get('/user/admin/list', { params })
}

/** 更新用户状态 */
export function updateUserStatus(data) {
  return request.put('/user/admin/status', data)
}

/** 分配角色 */
export function assignRoles(data) {
  return request.put('/user/admin/roles', data)
}

/** 角色选项 */
export function getRoleOptions() {
  return request.get('/user/admin/roles')
}
