# Node.js 筆記
* `node 01.js`：在 node js 環境下直接執行 js 檔
* `npm init -y`：初始化 npm 專案（建立 npm 規範好的標準文件與資料夾，類似 maven 的初始化專案）
* `npm install jquery`：安裝 jquery 依賴
* `npm install`：根據配置文件（package.json）下載相關依賴
    * package-json 是在 `npm init -y` 執行時創建的
*  要在 package.json 中加入 `"type": "module"` 才能在 node.js 中使用 es6 的模組化開發功能
* vue-element-admin 是基於 element-ui 的後台管理系統集成方案。
* vue-admin-template 是 vue-element-admin 的精簡版本，我們可以以它為模板進行二次開發。
* `npm run dev`：啟動專案