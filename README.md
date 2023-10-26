# 辦公自動化系統

## 專案介紹

* 本專案是一套辦公自動化系統，用於管理員工資訊以及將辦公流程自動化。

* 本專案可分為管理端與員工端兩部分。

    * 管理端用於管理員工資訊、管理員工權限，以及管理辦公流程。
    
    * 員工端負責讓員工在線上申請與審批各項辦公流程。

* 本專案採前後端分離模式開發。前端頁面是套 `vue-admin-template` 的模板，本專案主要實現「後端」以及「前後端 API 串接」的部分。

* 本專案使用的技術如下：

    * 基礎框架：`SpringBoot`
    
    * 資料操作：`Spring`、`Spring MVC`、`MyBatis`、`MyBatis Plus`

    * 資料庫管理系統：`MySQL`

    * 資料校驗：`Spring Security`、`JWT`（Json Web Token）

    * 資料緩存：`Redis`

    * API 文件生成：`Knife4j`

    * 工作流引擎：`Activiti`

    * 依賴管理：`Maven`

    * 前端框架：`Vue`、`Element UI`、`Axios`

* 本專案為尚硅谷[《雲尚辦公》](https://www.bilibili.com/video/BV1oM41177Jd/)的 Course Project。

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
      cd ivanfang-oa-parent/service-oa/target
      java -jar service-oa.jar
      ```

    * 執行前端程式（管理端）
      ```
      cd ivanfang-oa-admin
      npm install
      npm run dev
      ```

    * 執行前端程式（員工端）
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

## 文件說明

* /ivanfang-oa-parent：後端程式碼

* /ivanfang-oa-admin：前端程式碼 - 管理端

* /ivanfang-oa-web：前端程式碼 - 員工端
