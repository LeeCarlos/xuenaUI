import request from '../utils/request'

export default {
  list(params) {
    return request.get('/assessment', { params })
  },
  get(id) {
    return request.get(`/assessment/${id}`)
  },
  create(data) {
    return request.post('/assessment', data)
  },
  update(id, data) {
    return request.put(`/assessment/${id}`, data)
  },
  delete(id) {
    return request.delete(`/assessment/${id}`)
  },
  submit(id) {
    return request.post(`/assessment/${id}/submit`)
  },
  lock(id) {
    return request.post(`/assessment/${id}/lock`)
  },
  export(params) {
    return request.get('/assessment/export', { params, responseType: 'blob' }).then(res => {
      const url = window.URL.createObjectURL(new Blob([res]))
      const link = document.createElement('a')
      link.href = url
      link.download = `考核列表_${new Date().toISOString().slice(0, 10)}.xlsx`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
    })
  },
  exportTemplate() {
    return request.get('/assessment/export/template', { responseType: 'blob' }).then(res => {
      const url = window.URL.createObjectURL(new Blob([res]))
      const link = document.createElement('a')
      link.href = url
      link.download = '考核导入模板.xlsx'
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
    })
  },
}