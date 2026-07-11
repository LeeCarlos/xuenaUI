import request from '../utils/request'

export default {
  async list(item) {
    const res = await request.get('/dict', { params: { item } })
    return { ...res, data: res.data?.list || res.data }
  },
  get(id) {
    return request.get(`/dict/${id}`)
  },
  create(data) {
    return request.post('/dict', data)
  },
  update(id, data) {
    return request.put(`/dict/${id}`, data)
  },
  delete(id) {
    return request.delete(`/dict/${id}`)
  },
}