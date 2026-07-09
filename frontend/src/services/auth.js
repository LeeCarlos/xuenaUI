import request from '../utils/request'

export default {
  login(data) {
    return request.post('/auth/login', data)
  },
  logout() {
    return request.post('/auth/logout')
  },
  refresh(data) {
    return request.post('/auth/refresh', data)
  },
}