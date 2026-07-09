import request from '../utils/request'

export default {
  list(params) {
    return request.get('/department-score', { params })
  },
  get(id) {
    return request.get(`/department-score/${id}`)
  },
  create(data) {
    return request.post('/department-score', data)
  },
  update(id, data) {
    return request.put(`/department-score/${id}`, data)
  },
  delete(id) {
    return request.delete(`/department-score/${id}`)
  },
  submit(id) {
    return request.post(`/department-score/${id}/submit`)
  },
  complete(id) {
    return request.post(`/department-score/${id}/complete`)
  },
}