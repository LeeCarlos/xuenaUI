import request from '../utils/request'

export default {
  list(params) {
    return request.get('/department-score', { params })
  },
  get(id) {
    return request.get(`/department-score/${id}`)
  },
  create(data) {
    return request.post('/department-score', data)
  },
  update(id, data) {
    return request.put(`/department-score/${id}`, data)
  },
  delete(id) {
    return request.delete(`/department-score/${id}`)
  },
  submit(id) {
    return request.post(`/department-score/${id}/submit`)
  },
  complete(id) {
    return request.post(`/department-score/${id}/complete`)
  },
  export(params) {
    return request.get('/department-score/export', { params, responseType: 'blob' }).then(res => {
      const url = window.URL.createObjectURL(new Blob([res]))
      const link = document.createElement('a')
      link.href = url
      link.download = `部门打分列表_${new Date().toISOString().slice(0, 10)}.xlsx`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
    })
  },
}