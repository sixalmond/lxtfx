import request from '@/utils/request'

export const getForumPosts = (params) => {
  return request({
    url: '/forum/posts',
    method: 'get',
    params
  })
}

export const getPostDetail = (id) => {
  return request({
    url: `/forum/posts/${id}`,
    method: 'get'
  })
}

export const createPost = (data) => {
  return request({
    url: '/forum/posts',
    method: 'post',
    data
  })
}

export const getPostReplies = (id) => {
  return request({
    url: `/forum/posts/${id}/replies`,
    method: 'get'
  })
}

export const createReply = (data) => {
  return request({
    url: '/forum/replies',
    method: 'post',
    data
  })
}

export const likePost = (id) => {
  return request({
    url: `/forum/posts/${id}/like`,
    method: 'put'
  })
}

export const likeReply = (id) => {
  return request({
    url: `/forum/replies/${id}/like`,
    method: 'put'
  })
}
