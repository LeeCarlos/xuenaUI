import request from '../utils/request'

export default {
  getMonthly(params) {
    return request.get('/detail/monthly', { params })
  },
  exportCurrent(data) {
    return request.post('/detail/export/current', data, { responseType: 'blob' })
  },
  exportAll(data) {
    return request.post('/detail/export/all', data, { responseType: 'blob' })
  },
}