import store from '@/store'

/**
 * 判斷當前使用者是否有此按鈕權限
 * @param {按鈕權限字串} permission 
 */
export default function hasBtnPermission(permission) {
  // 得到當前使用者的所有按鈕權限
  const myBtns = store.getters.buttons
  // 如果指定的功能權限在myBtns中, 返回true ==> 這個按鈕就會顯示, 否則隱藏
  return myBtns.indexOf(permission) !== -1
}
