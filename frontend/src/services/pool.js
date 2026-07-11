import request from '../utils/request'

export default {
  list(params) {
    return request.get('/supplier/pool', { params })
  },
  get(id) {
    return request.get(`/supplier/pool/${id}`)
  },
  create(data) {
    return request.post('/supplier/pool', data)
  },
  update(id, data) {
    return request.put(`/supplier/pool/${id}`, data)
  },
  delete(id) {
    return request.delete(`/supplier/pool/${id}`)
  },
  batchDelete(ids) {
    return request.delete('/supplier/pool/batch', { data: ids })
  },
  enable(id) {
    return request.post(`/supplier/pool/${id}/enable`)
  },
  disable(id) {
    return request.post(`/supplier/pool/${id}/disable`)
  },
  import(formData) {
    return request.post('/supplier/pool/import', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },
  export(params) {
    return request.get('/supplier/pool/export', { params, responseType: 'blob' }).then(res => {
      const url = window.URL.createObjectURL(new Blob([res]))
      const link = document.createElement('a')
      link.href = url
      link.download = `供应商列表_${new Date().toISOString().slice(0, 10)}.xlsx`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
    })
  },
  exportTemplate() {
    return request.get('/supplier/pool/export/template', { responseType: 'blob' }).then(res => {
      const url = window.URL.createObjectURL(new Blob([res]))
      const link = document.createElement('a')
      link.href = url
      link.download = '供应商导入模板.xlsx'
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
    })
  },
}