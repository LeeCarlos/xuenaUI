import request from '../utils/request'

export default {
  list(params) {
    return request.get('/pool/list', { params })
  },
  create(data) {
    return request.post('/pool/create', data)
  },
  update(id, data) {
    return request.put(`/pool/update/${id}`, data)
  },
  delete(id) {
    return request.delete(`/pool/delete/${id}`)
  },
  toggle(id) {
    return request.put(`/pool/toggle/${id}`)
  },
  import(data) {
    return request.post('/pool/import', data, { headers: { 'Content-Type': 'multipart/form-data' } })
  },
  export(params) {
    return request.post('/pool/export', params, { responseType: 'blob' })
  },
}