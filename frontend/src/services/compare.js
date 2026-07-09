import request from '../utils/request'

export default {
  getRadar(params) {
    return request.get('/compare/radar', { params })
  },
  getHeatmap(params) {
    return request.get('/compare/heatmap', { params })
  },
  getCategories() {
    return request.get('/compare/categories')
  },
  getSuppliers() {
    return request.get('/compare/suppliers')
  },
}