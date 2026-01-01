import request from '@/utils/request'

export const createEvaluation = (data) => {
  return request({
    url: '/evaluation',
    method: 'post',
    data
  })
}

export const getEvaluationByOrderId = (orderId) => {
  return request({
    url: `/evaluation/order/${orderId}`,
    method: 'get'
  })
}

export const getMyEvaluations = (params) => {
  return request({
    url: '/evaluation/my',
    method: 'get',
    params
  })
}
