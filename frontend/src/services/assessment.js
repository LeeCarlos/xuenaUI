import request from '../utils/request'

export default {
  list(params) {
    return request.get('/assessment', { params })
  },
  get(id) {
    return request.get(`/assessment/${id}`)
  },
  create(data) {
    return request.post('/assessment', data)
  },
  update(id, data) {
    return request.put(`/assessment/${id}`, data)
  },
  delete(id) {
    return request.delete(`/assessment/${id}`)
  },
  submit(id) {
    return request.post(`/assessment/${id}/submit`)
  },
  lock(id) {
    return request.post(`/assessment/${id}/lock`)
  },
}