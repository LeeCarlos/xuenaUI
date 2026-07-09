import request from '../utils/request'

export default {
  list(params) {
    return request.get('/role/list', { params })
  },
  get(id) {
    return request.get(`/role/get/${id}`)
  },
  add(data) {
    return request.post('/role/add', data)
  },
  update(data) {
    return request.put('/role/update', data)
  },
  delete(id) {
    return request.delete(`/role/delete/${id}`)
  },
  assignPermission(data) {
    return request.post('/role/assign-permission', data)
  },
  assignMenu(data) {
    return request.post('/role/assign-menu', data)
  },
  getPermissions(roleId) {
    return request.get(`/role/get-permissions/${roleId}`)
  },
  getMenus(roleId) {
    return request.get(`/role/get-menus/${roleId}`)
  },
}