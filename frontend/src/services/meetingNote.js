import request from '../utils/request'

export default {
  list(params) {
    return request.get('/meeting-note', { params })
  },
  get(id) {
    return request.get(`/meeting-note/${id}`)
  },
  create(data) {
    return request.post('/meeting-note', data)
  },
  update(id, data) {
    return request.put(`/meeting-note/${id}`, data)
  },
  delete(id) {
    return request.delete(`/meeting-note/${id}`)
  },
}