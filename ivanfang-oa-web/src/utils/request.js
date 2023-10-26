import axios from "axios";

// 創建axios實例
const service = axios.create({
  baseURL: "http://localhost:8805", // api 的 base_url
  timeout: 30000 // 請求超時時間
});

// http request 攔截器
service.interceptors.request.use(config => {
    // let token = window.localStorage.getItem("token") || "eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLO0NAdiQyMdpdLi1CLPFKAYhJmXmJsK1JKYkpuZp1QLAHH6Dv1CAAAA.GJ3I20MduH-OSvsHYFF_yqN9NVeMnqDv8MBVMah00tUUAqqzrvs7xLWzpcbt0B9cPl1U4I5G8I3VAZMMP-sB5w";
    let token = window.localStorage.getItem("token") || "";
    if (token != "") {
      config.headers["token"] = token;
    }
    return config;
  },
  err => {
    return Promise.reject(err);
  });

// http response 攔截器
service.interceptors.response.use(response => {
    if (response.data.code == 208) {
      // debugger
      // 替換# 後臺獲取不到#後面的參數
      let url = window.location.href.replace('#', 'guiguoa')
      window.location = 'http://ggkt2.vipgz1.91tunnel.com/admin/wechat/authorize?returnUrl=' + url
    } else {
      if (response.data.code == 200) {
        return response.data;
      } else {
        // 209沒有權限，系統會自動跳轉授權登錄的，已在App.vue處理過，不需要提示
        if (response.data.code != 209) {
          alert(response.data.message || "error");
        }
        return Promise.reject(response);
      }
    }
  },
  error => {
    return Promise.reject(error.response);   // 返回介面返回的錯誤資訊
  });

export default service;
