import request from '../utils/request'

export default {
  uploadExcel(data) {
    return request.post('/upload/excel', data, { headers: { 'Content-Type': 'multipart/form-data' } })
  },
  downloadTemplate(params) {
    return request.get('/upload/template', { params, responseType: 'blob' })
  },
  merge(data) {
    return request.post('/upload/merge', data)
  },
  deleteYearMonth(yearMonth) {
    return request.delete(`/upload/year-month/${yearMonth}`)
  },
}