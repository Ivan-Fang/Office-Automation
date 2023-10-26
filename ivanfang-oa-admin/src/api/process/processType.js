import request from '@/utils/request'

const api_name = '/admin/process/processType'

export default {

  getPageList(page, pageSize) {
    return request({
      url: `${api_name}/get/${page}/${pageSize}`,
      method: 'get'
    })
  },
  getById(id) {
    return request({
      url: `${api_name}/get/${id}`,
      method: 'get'
    })
  },
  findAll() {
    return request({
      url: `${api_name}/get/all`,
      method: 'get'
    })
  },
  save(role) {
    return request({
      url: `${api_name}/save`,
      method: 'post',
      data: role
    })
  },
  updateById(role) {
    return request({
      url: `${api_name}/update`,
      method: 'put',
      data: role
    })
  },
  removeById(id) {
    return request({
      url: `${api_name}/remove/${id}`,
      method: 'delete'
    })
  }
}