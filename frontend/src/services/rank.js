import request from '../utils/request'

export default {
  compare(params) {
    return request.get('/rank/compare', { params })
  },
  export(data) {
    return request.post('/rank/export', data, { responseType: 'blob' })
  },
}