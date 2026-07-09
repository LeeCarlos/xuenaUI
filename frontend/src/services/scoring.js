import request from '../utils/request'

export default {
  getYears() {
    return request.get('/scoring/years')
  },
  getMonths(year) {
    return request.get(`/scoring/months/${year}`)
  },
  getStatus(yearMonth) {
    return request.get(`/scoring/status/${yearMonth}`)
  },
  getData(yearMonth) {
    return request.get(`/scoring/data/${yearMonth}`)
  },
  upload(data) {
    return request.post('/scoring/upload', data, { headers: { 'Content-Type': 'multipart/form-data' } })
  },
  downloadTemplate(params) {
    return request.get('/scoring/template', { params, responseType: 'blob' })
  },
  submit(yearMonth, data) {
    return request.post(`/scoring/submit/${yearMonth}`, data)
  },
  reset(yearMonth) {
    return request.post(`/scoring/reset/${yearMonth}`)
  },
}