import request from "@/utils/request"

var baseApiPath = "/admin/system/sysMenu"

export default {
    findNodes() {
        return request({
            url: `${baseApiPath}/find/nodes`,
            method: 'get'
        })
    },
    removeById(id) {
        return request({
            url: `${baseApiPath}/remove/${id}`,
            method: 'delete'
        })
    },
    save(sysMenu) {
        return request({
            url: `${baseApiPath}/save`,
            method: 'post',
            data: sysMenu
        })
    },
    updateById(sysMenu) {
        return request({
            url: `${baseApiPath}/update`,
            method: 'put',
            data: sysMenu
        })
    },
    toAssign(roleId) {
        return request({
            url: `${baseApiPath}/get/menu/${roleId}`,
            method: 'get'
        })
    },
    doAssign(assignMenuVo) {
        return request({
            url: `${baseApiPath}/set/menu`,
            method: 'post',
            data: assignMenuVo
        })
    }
}