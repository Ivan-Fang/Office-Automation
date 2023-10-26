import request from '@/utils/request';

const baseApiPath = "/admin/system/sysRole";

export default {
    getAll() {
        return request({
            url: `${baseApiPath}/get/all`,
            method: 'get',
        })
    },
    getByPage(page, pageSize, searchObj) {
        return request({
            url: `${baseApiPath}/get/${page}/${pageSize}`,
            method: 'get',
            params: searchObj    // 若要傳遞 request parameter 則使用 "params:"，若要傳遞 request body (json 格式) 則使用 "data:"
        })
    },
    removeById(id) {
        return request({
            url: `${baseApiPath}/remove/${id}`,
            method: 'delete'
        })
    },
    save(sysRole) {
        return request({
            url: `${baseApiPath}/save`,
            method: 'post',
            data: sysRole
        })
    },
    getById(id) {
        return request({
            url: `${baseApiPath}/get/${id}`,
            method: 'get'
        })
    },
    update(sysRole) {
        return request({
            url: `${baseApiPath}/update`,
            method: 'put',
            data: sysRole
        })
    },
    removeBatch(idList) {
        return request({
            url: `${baseApiPath}/remove/batch`,
            method: 'delete',
            data: idList
        })
    },
    getRoles(userId) {
        return request({
            url: `${baseApiPath}/get/roles/${userId}`,
            method: 'get'
        })
    },
    assignRoles(assginRoleVo) {
        return request({
            url: `${baseApiPath}/set/roles`,
            method: 'put',
            data: assginRoleVo
        })
    }
};