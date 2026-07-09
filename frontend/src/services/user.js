import request from '../utils/request'

export default {
  list(params) {
    return request.get('/user/list', { params })
  },
  get(id) {
    return request.get(`/user/get/${id}`)
  },
  add(data) {
    return request.post('/user/add', data)
  },
  update(data) {
    return request.put('/user/update', data)
  },
  delete(id) {
    return request.delete(`/user/delete/${id}`)
  },
  assignRole(data) {
    return request.post('/user/assign-role', data)
  },
  getRoles(userId) {
    return request.get(`/user/get-roles/${userId}`)
  },
}