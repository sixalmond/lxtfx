import request from '@/utils/request'

export const pageUsers = (params) => {
  return request({
    url: '/user/page',
    method: 'get',
    params
  })
}

export const updateUserType = (id, userType) => {
  return request({
    url: `/user/${id}/type`,
    method: 'put',
    data: { userType }
  })
}

export const deleteUser = (id) => {
  return request({
    url: `/user/${id}`,
    method: 'delete'
  })
}
