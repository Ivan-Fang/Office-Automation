<template>
  <div class="app-container">
    <!-- 工具條 -->
    <div class="tools-div">
      <el-button type="success" icon="el-icon-plus" size="mini" @click="add" :disabled="$hasBP('btn.processType.add')  === false">新增</el-button>
    </div>
    <!-- banner列表 -->
    <el-table v-loading="listLoading" :data="list" stripe border style="width: 100%;margin-top: 10px;">
      <el-table-column type="selection" width="55"/>
      <el-table-column label="序號" width="70" align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="name" label="類型名稱"/>
      <el-table-column prop="description" label="描述"/>
      <el-table-column prop="createTime" label="創建時間"/>
      <el-table-column prop="updateTime" label="更新時間"/>
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="edit(scope.row.id)" :disabled="$hasBP('btn.processType.update')  === false">修改</el-button>
          <el-button type="text" size="mini" @click="removeDataById(scope.row.id)" :disabled="$hasBP('btn.processType.remove')  === false">刪除</el-button>
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
    <el-dialog title="添加/修改" :visible.sync="dialogVisible" width="40%">
      <el-form ref="flashPromotionForm" label-width="150px" size="small" style="padding-right: 40px;">
        <el-form-item label="類型名稱">
          <el-input v-model="processType.name"/>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="processType.description"/>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" size="small">取消</el-button>
        <el-button type="primary" @click="saveOrUpdate()" size="small">確定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import api from '@/api/process/processType'
  
  const defaultForm = {
    id: '',
    name: '',
    description: ''
  }
  export default {
    data() {
      return {
        listLoading: true,  // 資料是否正在載入
        list: null,         // banner列表
        total: 0,           // 資料庫中的總記錄數
        page: 1,            // 默認頁碼
        limit: 10,          // 每頁記錄數
        searchObj: {},      // 查詢表單物件
        dialogVisible: false,
        processType: defaultForm,
        saveBtnDisabled: false
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
        console.log(size)
        this.limit = size
        this.fetchData(1)
      },
      // 載入清單數據
      fetchData(page = 1) {
        this.page = page
        api.getPageList(this.page, this.limit, this.searchObj).then(response => {
          this.list = response.data.records
          this.total = response.data.total
          // 資料載入並綁定成功
          this.listLoading = false
        })
      },
      // 重置查詢表單
      resetData() {
        console.log('重置查詢表單')
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
        this.dialogVisible = true
        this.processType = Object.assign({}, defaultForm)
      },
      edit(id) {
        this.dialogVisible = true
        this.fetchDataById(id)
      },
      fetchDataById(id) {
        api.getById(id).then(response => {
          this.processType = response.data
        })
      },
      saveOrUpdate() {
        this.saveBtnDisabled = true // 防止表單重複提交
        if (!this.processType.id) {
          this.saveData()
        } else {
          this.updateData()
        }
      },
      // 新增
      saveData() {
        api.save(this.processType).then(response => {
          this.$message.success("審批類型新增成功" || response.message)
          this.dialogVisible = false
          this.fetchData(this.page)
        })
      },
      // 根據 id 更新記錄
      updateData() {
        api.updateById(this.processType).then(response => {
          this.$message.success("審批類型修改成功" || response.message)
          this.dialogVisible = false
          this.fetchData(this.page)
        })
      }
    }
  }
</script>
  
