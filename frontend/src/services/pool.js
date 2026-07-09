import request from '../utils/request'

export default {
  list(params) {
    return request.get('/supplier/pool', { params })
  },
  get(id) {
    return request.get(`/supplier/pool/${id}`)
  },
  create(data) {
    return request.post('/supplier/pool', data)
  },
  update(id, data) {
    return request.put(`/supplier/pool/${id}`, data)
  },
  delete(id) {
    return request.delete(`/supplier/pool/${id}`)
  },
  enable(id) {
    return request.post(`/supplier/pool/${id}/enable`)
  },
  disable(id) {
    return request.post(`/supplier/pool/${id}/disable`)
  },
}