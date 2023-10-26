import request from '@/utils/request'

const api_name = '/admin/process'

export default {

  getPageList(page, limit, searchObj) {
    return request({
      url: `${api_name}/get/${page}/${limit}`,
      method: 'get',
      params: searchObj  // url查詢字串或表單鍵值對
    })
  }

}
