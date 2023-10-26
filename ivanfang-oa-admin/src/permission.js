import router from './router'
import store from './store'
import { getToken } from '@/utils/auth'
import { Message } from 'element-ui'
import NProgress from 'nprogress'                 // 橫向進度條提示: 在跳轉路由時使用
import 'nprogress/nprogress.css'                  // 橫向進度條樣式
import getPageTitle from '@/utils/get-page-title' // 獲取應用頭部標題的函數
import Layout from '@/layout'
import ParentView from '@/components/ParentView'
const _import = require('./router/_import_'+process.env.NODE_ENV) // 獲取組件的方法

NProgress.configure({ showSpinner: false }) // NProgress Configuration
const whiteList = ['/login']                // no redirect whitelist
router.beforeEach(async(to, from, next) => {
  NProgress.start()
  // set page title
  document.title = getPageTitle(to.meta.title)
  // determine whether the user has logged in
  const hasToken = getToken()
  if (hasToken) {
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      next({ path: '/' })
      NProgress.done()
    } else {
      const hasGetUserInfo = store.getters.name
      if (hasGetUserInfo) {
        next()
      } else {
        try {
          // get user info
          await store.dispatch('user/getInfo')  // 請求獲取使用者資訊
          if (store.getters.menus.length < 1) {
            global.antRouter = []
            next()
          }
          const menus = filterAsyncRouter(store.getters.menus)  // 1. 過濾路由
          console.log(menus)
          router.addRoutes(menus)   // 2. 動態添加路由
          let lastRou = [{ path: '*', redirect: '/404', hidden: true }]
          router.addRoutes(lastRou)
          global.antRouter = menus  // 3. 將路由資料傳遞給全域變數，做側邊欄菜單渲染工作
          next({
            ...to,
            replace: true
          })
          //next()
        } catch (error) {
          // remove token and go to login page to re-login
          console.log(error)
          await store.dispatch('user/resetToken')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else { /* has no token*/
    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => { // finish progress bar
  NProgress.done()
}) // 遍歷後臺傳來的路由字串，轉換為元件物件
function filterAsyncRouter(asyncRouterMap) {
  const accessedRouters = asyncRouterMap.filter(route => {
    if (route.component) {
      if (route.component === 'Layout') {
        route.component = Layout
      } else if (route.component === 'ParentView') {
        route.component = ParentView
      } else {
        try {
          route.component = _import(route.component)    // 導入組件
        } catch (error) {
          debugger
          console.log(error)
          route.component = _import('dashboard/index')  // 導入組件
        }
      }
    }
    if (route.children && route.children.length > 0) {
      route.children = filterAsyncRouter(route.children)
    } else {
      delete route.children
    }
    return true
  })
  return accessedRouters
}
