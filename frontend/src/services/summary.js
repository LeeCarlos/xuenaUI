import request from '../utils/request'

export default {
  getSummary(params) {
    return request.get('/summary', { params })
  },
}
