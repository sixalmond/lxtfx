import request from '@/utils/request'

export const getAvailableNurses = () => {
  return request({
    url: '/nurse/available',
    method: 'get'
  })
}

export const getNurseDetail = (id) => {
  return request({
    url: `/nurse/${id}`,
    method: 'get'
  })
}

export const pageNurses = (params) => {
  return request({
    url: '/nurse/page',
    method: 'get',
    params
  })
}

export const updateNurseStatus = (id, status) => {
  return request({
    url: `/nurse/${id}/status`,
    method: 'put',
    params: { status }
  })
}

export const updateNurseGrade = (id, grade) => {
  return request({
    url: `/nurse/${id}/grade`,
    method: 'put',
    params: { grade }
  })
}
