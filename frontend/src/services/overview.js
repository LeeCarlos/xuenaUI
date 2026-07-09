import request from '../utils/request'

export default {
  getStats(params) {
    return request.get('/overview/stats', { params })
  },
  getTrend(params) {
    return request.get('/overview/trend', { params })
  },
  getGradeDistribution(params) {
    return request.get('/overview/grade-distribution', { params })
  },
  getDimensionAvg(params) {
    return request.get('/overview/dimension-avg', { params })
  },
}