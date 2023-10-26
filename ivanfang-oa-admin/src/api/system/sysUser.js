import request from '@/utils/request';

const baseApiPath = "/admin/system/sysUser";

export default {
    getByPage(page, pageSize, searchObj) {
        return request({
            url: `${baseApiPath}/get/${page}/${pageSize}`,
            method: 'get',
            params: searchObj
        })
    },
    getById(id) {
        return request({
            url: `${baseApiPath}/get/${id}`,
            method: 'get'
        })
    },
    save(sysUser) {
        return request({
            url: `${baseApiPath}/save`,
            method: 'post',
            data: sysUser
        })
    },
    updateById(sysUser) {
        return request({
            url: `${baseApiPath}/update`,
            method: 'put',
            data: sysUser
        })
    },
    removeById(id) {
        return request({
            url: `${baseApiPath}/remove/${id}`,
            method: 'delete'
        })
    },
    removeBatch(idList) {
        return request({
            url: `${baseApiPath}/remove/batch`,
            method: 'delete',
            data: idList
        })
    },
    updateStatus(id, status) {
        return request({
            url: `${baseApiPath}/update/status/${id}/${status}`,
            method: 'put'
        })
    }
}