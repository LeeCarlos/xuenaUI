import request from '../utils/request'

export default {
  getStats(params) {
    return request.get('/overview/stats', { params })
  },
  getTrend(params) {
    return request.get('/overview/trend', { params })
  },
  getCategories() {
    return request.get('/overview/categories')
  },
  getSuppliers() {
    return request.get('/overview/suppliers')
  },
  getYearMonths() {
    return request.get('/overview/year-months')
  },
  getGradeDistribution(params) {
    return request.get('/overview/grade-distribution', { params })
  },
  getDimensionAvg(params) {
    return request.get('/overview/dimension-avg', { params })
  },
}