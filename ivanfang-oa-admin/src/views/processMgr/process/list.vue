<template>
  <div class="app-container">
    <div class="search-div">
      <el-form label-width="70px" size="small">
        <el-row>
          <el-col :span="8">
            <el-form-item label="關鍵字">
              <el-input style="width: 95%" v-model="searchObj.keyword" placeholder="審批編號/標題/手機號碼/姓名"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="狀態">
              <el-select
                v-model="searchObj.status"
                placeholder="請選狀態" style="width: 100%;"
              >
                <el-option
                  v-for="item in statusList"
                  :key="item.status"
                  :label="item.name"
                  :value="item.status"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="操作時間">
              <el-date-picker
                v-model="createTimes"
                type="datetimerange"
                range-separator="至"
                start-placeholder="開始時間"
                end-placeholder="結束時間"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="margin-right: 10px;width: 100%;"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="display:flex">
          <el-button type="primary" icon="el-icon-search" size="mini" :loading="loading" @click="fetchData()">搜尋
          </el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetData">重置</el-button>
        </el-row>
      </el-form>
    </div>
    <!-- 列表 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      stripe
      border
      style="width: 100%;margin-top: 10px;"
    >

      <el-table-column
        label="序號"
        width="70"
        align="center"
      >
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="processCode" label="審批編號" width="130"/>
      <el-table-column prop="title" label="標題" width="180"/>
      <el-table-column prop="name" label="用戶"/>
      <el-table-column prop="processTypeName" label="審批類型"/>
      <el-table-column prop="processTemplateName" label="審批模板"/>
      <el-table-column prop="description" label="描述" width="180"/>
      <el-table-column label="狀態">
        <template slot-scope="scope">
          {{ scope.row.status === 1 ? '審批中' : scope.row.status === 2 ? '完成' : '駁回' }}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="創建時間" width="160"/>

      <el-table-column label="操作" width="120" align="center">
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="show(scope.row.id)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分頁組件 -->
    <el-pagination
      :current-page="page"
      :total="total"
      :page-size="limit"
      :page-sizes="[5, 10, 20, 30, 40, 50, 100]"
      style="padding: 30px 0; text-align: center;"
      layout="sizes, prev, pager, next, jumper, ->, total, slot"
      @current-change="fetchData"
      @size-change="changeSize"
    />
  </div>
</template>
  
<script>
  import api from '@/api/process/process'
  
  export default {
    data() {
      return {
        listLoading: true,  // 資料是否正在載入
        list: null,         // banner列表
        total: 0,           // 資料庫中的總記錄數
        page: 1,            // 默認頁碼
        limit: 10,          // 每頁記錄數
        searchObj: {},      // 查詢表單物件
        statusList: [
          { 'status': '1', 'name': '進行中' },
          { 'status': '2', 'name': '已完成' },
          { 'status': '-1', 'name': '駁回' }
        ],
        createTimes: []
      }
    },
  
    // 生命週期函數：記憶體準備完畢，頁面尚未渲染
    created() {
      console.log('list created......')
      this.fetchData()
    },
  
    // 生命週期函數：記憶體準備完畢，頁面渲染成功
    mounted() {
      console.log('list mounted......')
    },
  
    methods: {
      // 當頁碼發生改變的時候
      changeSize(size) {
        console.log(size)
        this.limit = size
        this.fetchData(1)
      },
  
      // 載入banner清單數據
      fetchData(page = 1) {
        console.log('翻頁。。。' + page)
        // 非同步獲取遠端資料（ajax）
        this.page = page
  
        if (this.createTimes && this.createTimes.length === 2) {
          this.searchObj.createTimeBegin = this.createTimes[0]
          this.searchObj.createTimeEnd = this.createTimes[1]
        }
  
        api.getPageList(this.page, this.limit, this.searchObj).then(
          response => {
            this.list = response.data.records
            this.total = response.data.total
  
            // 資料載入並綁定成功
            this.listLoading = false
          }
        )
      },
  
      // 重置查詢表單
      resetData() {
        console.log('重置查詢表單')
        this.searchObj = {}
        this.fetchData()
      },
        
      show(id) {
         console.log(id)
      }
    }
  }
</script>
