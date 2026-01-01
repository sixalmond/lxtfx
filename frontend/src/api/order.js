import request from '@/utils/request'

export const createOrder = (data) => {
  return request({
    url: '/order',
    method: 'post',
    data
  })
}

export const getOrderDetail = (id) => {
  return request({
    url: `/order/${id}`,
    method: 'get'
  })
}

export const getMyOrders = (params) => {
  return request({
    url: '/order/my',
    method: 'get',
    params
  })
}

export const getNurseOrders = (params) => {
  return request({
    url: '/order/nurse',
    method: 'get',
    params
  })
}

export const getPendingOrders = (params) => {
  return request({
    url: '/order/pending',
    method: 'get',
    params
  })
}

export const pageOrders = (params) => {
  return request({
    url: '/order/page',
    method: 'get',
    params
  })
}

export const acceptOrder = (id) => {
  return request({
    url: `/order/${id}/accept`,
    method: 'put'
  })
}

export const rejectOrder = (id) => {
  return request({
    url: `/order/${id}/reject`,
    method: 'put'
  })
}

export const startService = (id) => {
  return request({
    url: `/order/${id}/start`,
    method: 'put'
  })
}

export const completeOrder = (id) => {
  return request({
    url: `/order/${id}/complete`,
    method: 'put'
  })
}

export const cancelOrder = (id) => {
  return request({
    url: `/order/${id}/cancel`,
    method: 'put'
  })
}

export const complaintOrder = (id, content) => {
  return request({
    url: `/order/${id}/complaint`,
    method: 'put',
    data: { content }
  })
}

export const getComplaintOrders = (params) => {
  return request({
    url: '/order/complaints',
    method: 'get',
    params
  })
}

export const handleComplaint = (id, result) => {
  return request({
    url: `/order/${id}/handle-complaint`,
    method: 'put',
    data: { result }
  })
}
