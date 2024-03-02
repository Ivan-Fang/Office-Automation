# 辦公自動化系統

## 專案介紹

* 本專案是一套辦公自動化系統，用於管理員工資訊以及將辦公流程自動化。

* 本專案可分為管理端與員工端兩部分。

    * 管理端用於管理員工資訊、管理員工權限，以及管理辦公流程。
    
    * 員工端負責讓員工在線上申請與審批各項辦公流程。

* 本專案採前後端分離模式開發。前端頁面是套 `vue-admin-template` 的模板，本專案主要實現「後端」以及「前後端 API 串接」的部分。

* 本專案使用的技術如下：

    * 後端業務邏輯：`SpringBoot`
    
    * 資料庫操作：`MyBatis`、`MyBatis Plus`

    * 資料庫管理：`MySQL`

    * 資料校驗：`Spring Security`、`JWT`（Json Web Token）

    * 資料緩存：`Redis`

    * API 文件生成：`Knife4j`

    * 工作流引擎：`Activiti`

    * 依賴管理：`Maven`

    * 前端框架：`Vue`、`Element Plus`、`Axios`

* 本專案為尚硅谷[《雲尚辦公》](https://www.bilibili.com/video/BV1oM41177Jd/)的 Course Project。


## 專案截圖
<img src="https://github.com/Ivan-Fang/Office-Automation-System/assets/40261483/bc5c1f5d-bd6f-49af-8fa4-1b066805057d" width="600px">
<img src="https://github.com/Ivan-Fang/Office-Automation-System/assets/40261483/96463490-5c74-45e7-8230-8b7f11da5eaa" width="600px">
<img src="https://github.com/Ivan-Fang/Office-Automation-System/assets/40261483/85022133-ea9b-48a2-b0ac-aefe494150cd" width="600px">
<img src="https://github.com/Ivan-Fang/Office-Automation-System/assets/40261483/c4b1c25e-19ac-47c9-9de7-26a970585fee" width="600px">
<img src="https://github.com/Ivan-Fang/Office-Automation-System/assets/40261483/d2479f69-dbfc-4e2f-ae38-34bb477d49ff" width="600px"><br/>
<img src="https://github.com/Ivan-Fang/Office-Automation-System/assets/40261483/2c70255b-1e50-49a8-ad2b-39a5dffa8120" height="300px">
<img src="https://github.com/Ivan-Fang/Office-Automation-System/assets/40261483/4538005a-a0ee-431e-b1d0-36f7e048c2ec "height="300px">



## 細節說明

本專案使用以下技術完成各項功能的開發：

* 使用 `SpringBoot`、`Spring`、`Spring MVC`、`MyBatis`、`MyBatis Plus` 進行使用者、角色、選單（權限）、審批類型、審批模板的增刪改查，以及角色分配、權限分配。

* 使用 `Activiti` 進行工作流的定義、部屬、啟動，以及任務的查詢與審批

* 使用 `JWT` 對登入資料進行加密

* 使用 `Spring Security` 進行登入驗證與授權驗證
    
* 使用 `Redis` 緩存使用者名稱即其權限，以進行授權驗證

* 使用 RESTful API 風格設計前後端 API

* 使用 `Knife4j` 生成後端 API 文件

## 使用步驟

1. 啟動專案

    * 執行 `ivanfang-oa.sql` 裡的所有指令以建立 database 與相關 table。

    * 執行後端程式（注意本專案使用 Java 8 進行開發，請確保執行環境一樣是 Java 8）
      ```
      cd ivanfang-oa-parent
      mvn spring-boot:run
      ```

    * 執行前端程式（管理端）
      ```
      cd ivanfang-oa-admin
      npm install
      npm run dev
      ```

    * 執行前端程式（員工端，注意本專案使用 Node.js 14 進行開發，請確保執行環境一樣是 Node.js 14）
      ```
      cd ivanfang-oa-web
      npm install
      npm run serve
      ```
    
    * 啟動 redis-server，並建立一個資料庫，其 ip:port 為 127.0.0.1:6379

2. 使用專案

    * 使用管理端
      ```
      http://localhost:9528
      ```

    * 使用員工端
      ```
      http://localhost:9090/#/test
      ```
    
    * 查看後端 API 文檔
      ```
      http://localhost:8805/doc.html
      ```

    * 如果想自定義流程的話記得啟動 Activiti，並將定義好的 .zip 檔上傳到管理端。
      1. cd apache-tomcat-8.5.91/bin

      2. 使用 startup.bat 或 startup.sh 啟動 tomcat

      3. 開啟網址：http://localhost:8080/activiti-explorer

      4. 使用預設帳號（ketmit）密碼（ketmit） 登入

      5. 到 Processes → Model workspace → New model 創建自定義流程，並下載成 .zip 檔

## 文件說明

* /ivanfang-oa-parent：後端程式碼

* /ivanfang-oa-admin：前端程式碼 - 管理端

* /ivanfang-oa-web：前端程式碼 - 員工端
