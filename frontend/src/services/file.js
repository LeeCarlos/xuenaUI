import request from '../utils/request'

export default {
  upload(file, fileType, description) {
    const formData = new FormData()
    formData.append('file', file)
    formData.append('fileType', fileType)
    if (description) {
      formData.append('description', description)
    }
    return request.post('/file/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },
  downloadByStoreKey(storeKey) {
    return request.get(`/file/download/${storeKey}`, { responseType: 'blob' }).then(res => {
      const url = window.URL.createObjectURL(new Blob([res]))
      const link = document.createElement('a')
      link.href = url
      link.download = storeKey
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
    })
  },
  downloadByFileName(fileName) {
    return request.get('/file/download', { params: { fileName }, responseType: 'blob' }).then(res => {
      const url = window.URL.createObjectURL(new Blob([res]))
      const link = document.createElement('a')
      link.href = url
      link.download = fileName
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
    })
  },
  list(params) {
    return request.get('/file', { params })
  },
  get(id) {
    return request.get(`/file/${id}`)
  },
  delete(id) {
    return request.delete(`/file/${id}`)
  },
}