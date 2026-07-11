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
  export(params) {
    return request.get('/meeting-note/export', { params, responseType: 'blob' }).then(res => {
      const url = window.URL.createObjectURL(new Blob([res]))
      const link = document.createElement('a')
      link.href = url
      link.download = `会议纪要列表_${new Date().toISOString().slice(0, 10)}.xlsx`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
    })
  },
}