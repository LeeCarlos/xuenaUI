import request from '../utils/request'

export default {
  list(params) {
    return request.get('/permission/list', { params })
  },
  get(id) {
    return request.get(`/permission/get/${id}`)
  },
  add(data) {
    return request.post('/permission/add', data)
  },
  update(data) {
    return request.put('/permission/update', data)
  },
  delete(id) {
    return request.delete(`/permission/delete/${id}`)
  },
  tree() {
    return request.get('/permission/tree')
  },
}