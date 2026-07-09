import request from '../utils/request'

export default {
  list(params) {
    return request.get('/menu/list', { params })
  },
  get(id) {
    return request.get(`/menu/get/${id}`)
  },
  add(data) {
    return request.post('/menu/add', data)
  },
  update(data) {
    return request.put('/menu/update', data)
  },
  delete(id) {
    return request.delete(`/menu/delete/${id}`)
  },
  tree() {
    return request.get('/menu/tree')
  },
  getUserMenu() {
    return request.get('/menu/user-menu')
  },
}