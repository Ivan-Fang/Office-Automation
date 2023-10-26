// 生產環境導入組件
module.exports = file => () => import('@/views/' + file + '.vue')
