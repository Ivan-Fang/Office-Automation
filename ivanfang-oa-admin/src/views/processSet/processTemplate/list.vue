<template>
  <div class="app-container">
    <!-- 工具列 -->
    <div class="tools-div">
      <el-button type="success" icon="el-icon-plus" size="mini" @click="add()" :disabled="$hasBP('btn.processTemplate.templateSet')  === false">新增</el-button></el-button>
    </div>

    <!-- 審批模板列表 -->
    <el-table v-loading="listLoading" :data="list" stripe border style="width: 100%;margin-top: 10px;">
      <el-table-column label="序號" width="70" align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>iconPath
      <el-table-column prop="name" label="審批名稱"/>
      <el-table-column label="圖示" width="60">
        <template slot-scope="scope">
          <img :src="scope.row.iconUrl" style="width: 30px;height: 30px;vertical-align: text-bottom;">
        </template>
      </el-table-column>
      <el-table-column prop="processTypeName" label="審批類型"/>
      <el-table-column prop="description" label="描述"/>
      <el-table-column prop="createTime" label="創建時間" width="160"/>
      <el-table-column prop="updateTime" label="更新時間" width="160"/>
      <!-- 操作按鈕：查看、修改、刪除、發佈 -->
      <el-table-column label="操作" width="250" align="center">
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="show(scope.row)" :disabled="$hasBP('btn.processTemplate.list')  === false">查看</el-button>
          <el-button type="text" v-if="scope.row.status == 0" size="mini" @click="edit(scope.row.id)" :disabled="$hasBP('btn.processTemplate.templateSet')  === false">修改</el-button>
          <el-button type="text" v-if="scope.row.status == 0" size="mini" @click="removeDataById(scope.row.id)" :disabled="$hasBP('btn.processTemplate.remove')  === false">刪除</el-button>
          <el-button v-if="scope.row.status == 0" type="text" size="mini" @click="publish(scope.row.id)" :disabled="$hasBP('btn.processTemplate.publish')  === false">發佈</el-button>
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

    <!-- 查看審批模板的對話框 -->
    <el-dialog title="查看審批模板" :visible.sync="formDialogVisible" width="35%">
      <h3>基本資訊</h3>
      <el-divider/>
      <el-form ref="flashPromotionForm" label-width="150px" size="small" style="padding-right: 40px;">
        <el-form-item label="審批類型" style="margin-bottom: 0px;">{{ processTemplate.processTypeName }}</el-form-item>
        <el-form-item label="名稱" style="margin-bottom: 0px;">{{ processTemplate.name }}</el-form-item>
        <el-form-item label="創建時間" style="margin-bottom: 0px;">{{ processTemplate.createTime }}</el-form-item>
      </el-form>
      <h3>表單資訊</h3>
      <el-divider/>
      <div>
        <form-create :rule="rule" :option="option"></form-create>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="formDialogVisible = false" size="small">返回</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
  import api from '@/api/process/processTemplate'

  export default {
    data() {
      return {
        listLoading: true,    // 資料是否正在載入
        list: null,           // banner列表
        total: 0,             // 資料庫中的總記錄數
        page: 1,              // 默認頁碼
        limit: 10,            // 每頁記錄數
        searchObj: {},        // 查詢表單物件

        rule: [],
        option: {},
        processTemplate: {},
        formDialogVisible: false
      }
    },
    // 生命週期函數：記憶體準備完畢，頁面尚未渲染
    created() {
      this.fetchData()
    },
    // 生命週期函數：記憶體準備完畢，頁面渲染成功
    mounted() {
    },
    methods: {
      // 當頁碼發生改變的時候
      changeSize(size) {
        this.limit = size
        this.fetchData(1)
      },
      // 載入banner清單數據
      fetchData(page = 1) {
        // 非同步獲取遠端資料（ajax）
        this.page = page
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
        this.searchObj = {}
        this.fetchData()
      },
      // 根據id刪除資料
      removeDataById(id) {
        this.$confirm('此操作將永久刪除該記錄, 是否繼續?', '提示', {
          confirmButtonText: '確定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => { // promise
          // 點擊確定，遠程調用ajax
          return api.removeById(id)
        }).then((response) => {
          this.fetchData(this.page)
          this.$message.success(response.message)
        }).catch(() => {
          this.$message.info('取消刪除')
        })
      },

      add() {
        this.$router.push('/processSet/templateSet')
      },

      edit(id) {
        this.$router.push('/processSet/templateSet?id=' + id)
      },

      // 查看以定義好的審批模板
      show(row) {
        this.rule = JSON.parse(row.formProps)
        this.option = JSON.parse(row.formOptions)
        this.processTemplate = row
        this.formDialogVisible = true
      },
      
      // 發佈審批模板（部屬流程定義）
      publish(id) {
        api.publish(id).then(response => {
          this.$message.success('發佈成功')
          this.fetchData(this.page)
        })
      }
    }
  }
</script>
