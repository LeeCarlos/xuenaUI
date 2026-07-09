import request from '../utils/request'

export default {
  list() {
    return request.get('/category')
  },
  get(id) {
    return request.get(`/category/${id}`)
  },
  create(data) {
    return request.post('/category', data)
  },
  update(id, data) {
    return request.put(`/category/${id}`, data)
  },
  delete(id) {
    return request.delete(`/category/${id}`)
  },
}