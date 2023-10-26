<template>
    <div class="app-container">
  
      <!-- 工具條 -->
      <div class="tools-div">
        <el-button type="success" icon="el-icon-plus" size="mini" @click="add()">添加</el-button>
      </div>

      <!-- 選單管理表 -->
      <el-table
        :data="sysMenuList"
        style="width: 100%;margin-bottom: 20px;margin-top: 10px;"
        row-key="id"
        border
        :default-expand-all="false"
        :tree-props="{children: 'children'}">
  
        <el-table-column prop="name" label="選單名稱" width="160"/>
        <el-table-column label="圖示">
          <template slot-scope="scope">
            <i :class="scope.row.icon"></i>
          </template>
        </el-table-column>
        <el-table-column prop="perms" label="權限標識" width="160"/>
        <el-table-column prop="path" label="路由地址" width="120"/>
        <el-table-column prop="component" label="元件路徑" width="160"/>
        <el-table-column prop="sortValue" label="排序" width="60"/>
        <el-table-column label="狀態" width="80">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status === 1" disabled="true">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="創建時間" width="160"/>
        <!-- 選單管理按鈕（新增下級節點、修改、刪除） -->
        <!-- 用 disabled、$hasBP 控制按鈕權限 -->
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="success" v-if="scope.row.type !== 2" icon="el-icon-plus" size="mini" :disabled="$hasBP('btn.sysMenu.add')  === false" @click="add(scope.row)" title="新增下級節點"/>
            <el-button type="primary" icon="el-icon-edit" size="mini" :disabled="$hasBP('btn.sysMenu.update')  === false" @click="edit(scope.row)" title="修改"/>
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeDataById(scope.row.id)" title="刪除" :disabled="scope.row.children.length > 0 || $hasBP('btn.sysMenu.remove')  === false"/>
          </template>
        </el-table-column>
      </el-table>
  
      <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="40%" >
        <el-form ref="dataForm" :model="sysMenu" label-width="150px" size="small" style="padding-right: 40px;">
          <el-form-item label="上級部門" v-if="sysMenu.id === ''">
            <el-input v-model="sysMenu.parentName" disabled="true"/>
          </el-form-item>
          <el-form-item label="選單類型" prop="type">
            <el-radio-group v-model="sysMenu.type" :disabled="typeDisabled">
              <el-radio :label="0" :disabled="type0Disabled">目錄</el-radio>
              <el-radio :label="1" :disabled="type1Disabled">選單</el-radio>
              <el-radio :label="2" :disabled="type2Disabled">按鈕</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="選單名稱" prop="name">
            <el-input v-model="sysMenu.name"/>
          </el-form-item>
          <el-form-item label="圖示" prop="icon" v-if="sysMenu.type !== 2">
            <el-select v-model="sysMenu.icon" clearable>
              <el-option v-for="item in iconList" :key="item.class" :label="item.class" :value="item.class">
              <span style="float: left;">
               <i :class="item.class"></i>  <!-- 如果動態顯示圖示，這裡添加判斷 -->
              </span>
                <span style="padding-left: 6px;">{{ item.class }}</span>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="排序">
            <el-input-number v-model="sysMenu.sortValue" controls-position="right" :min="0" />
          </el-form-item>
          <el-form-item prop="path">
                <span slot="label">
                  <el-tooltip content="訪問的路由位址，如：`sysUser`" placement="top">
                  <i class="el-icon-question"></i>
                  </el-tooltip>
                  路由地址
                </span>
            <el-input v-model="sysMenu.path" placeholder="請輸入路由位址" />
          </el-form-item>
          <el-form-item prop="component" v-if="sysMenu.type !== 0">
                <span slot="label">
                  <el-tooltip content="訪問的組件路徑，如：`system/user/index`，預設在`views`目錄下" placement="top">
                  <i class="el-icon-question"></i>
                  </el-tooltip>
                  組件路徑
                </span>
            <el-input v-model="sysMenu.component" placeholder="請輸入組件路徑" />
          </el-form-item>
          <el-form-item v-if="sysMenu.type === 2">
            <el-input v-model="sysMenu.perms" placeholder="請輸入權限標識" maxlength="100" />
            <span slot="label">
                  <el-tooltip content="控制器中定義的權限標識，如：@PreAuthorize(hasAuthority('bnt.sysRole.list'))" placement="top">
                  <i class="el-icon-question"></i>
                  </el-tooltip>權限標識</span>
          </el-form-item>
          <el-form-item label="狀態" prop="type">
            <el-radio-group v-model="sysMenu.status">
              <el-radio :label="1">正常</el-radio>
              <el-radio :label="0">停用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false" size="small" icon="el-icon-refresh-right">取 消</el-button>
          <el-button type="primary" icon="el-icon-check" @click="saveOrUpdate()" size="small">確 定</el-button>
        </span>
      </el-dialog>
    </div>
  </template>
  
  
  <script>
  import api from '@/api/system/sysMenu'
  const defaultForm = {
    id: '',
    parentId: '',
    name: '',
    type: 0,
    path: '',
    component: '',
    perms: '',
    icon: '',
    sortValue: 1,
    status: 1
  }
  export default {
    // 定義資料
    data() {
      return {
        sysMenuList: [],
        expandKeys: [], // 需要自動展開的項
  
        typeDisabled: false,
        type0Disabled: false,
        type1Disabled: false,
        type2Disabled: false,
        dialogTitle: '',
  
        dialogVisible: false,
        sysMenu: defaultForm,
        saveBtnDisabled: false,
  
        iconList: [
          {
            class: "el-icon-s-tools",
          },
          {
            class: "el-icon-s-custom",
          },
          {
            class: "el-icon-setting",
          },
          {
            class: "el-icon-user-solid",
          },
          {
            class: "el-icon-s-help",
          },
          {
            class: "el-icon-phone",
          },
          {
            class: "el-icon-s-unfold",
          },
          {
            class: "el-icon-s-operation",
          },
          {
            class: "el-icon-more-outline",
          },
          {
            class: "el-icon-s-check",
          },
          {
            class: "el-icon-tickets",
          },
          {
            class: "el-icon-s-goods",
          },
          {
            class: "el-icon-document-remove",
          },
          {
            class: "el-icon-warning",
          },
          {
            class: "el-icon-warning-outline",
          },
          {
            class: "el-icon-question",
          },
          {
            class: "el-icon-info",
          }
        ]
      }
    },
  
    // 當頁面載入時獲取資料
    created() {
      this.fetchData()
    },
  
    methods: {
      // 調用 api 層獲取資料庫中的資料
      fetchData() {
        console.log('載入列表')
        api.findNodes().then(response => {
          this.sysMenuList = response.data
          console.log(this.sysMenuList)
        })
      },
  
      // 根據id刪除資料
      removeDataById(id) {
        // debugger
        this.$confirm('此操作將永久刪除該記錄, 是否繼續?', '提示', {
          confirmButtonText: '確定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => { // promise
          // 點擊確定，遠程調用 ajax
          return api.removeById(id)
        }).then((response) => {
          this.fetchData()
          this.$message({
            type: 'success',
            message: '刪除成功!'
          })
        }).catch(() => {
           this.$message.info('取消刪除')
        })
      },
  
      // -------------
      add(row){
        debugger
        this.typeDisabled = false
        this.dialogTitle = '添加下級節點'
        this.dialogVisible = true
  
        this.sysMenu = Object.assign({}, defaultForm)
        this.sysMenu.id = ''
        if(row) {
          this.sysMenu.parentId = row.id
          this.sysMenu.parentName = row.name
          // this.sysMenu.component = 'ParentView'
          if(row.type === 0) {
            this.sysMenu.type = 1
            this.typeDisabled = false
            this.type0Disabled = false
            this.type1Disabled = false
            this.type2Disabled = true
          } else if(row.type === 1) {
            this.sysMenu.type = 2
            this.typeDisabled = true
          }
        } else {
          this.dialogTitle = '添加目錄節點'
          this.sysMenu.type = 0
          this.sysMenu.component = 'Layout'
          this.sysMenu.parentId = 0
          this.typeDisabled = true
        }
      },
  
      edit(row) {
        debugger
        this.dialogTitle = '修改節點'
        this.dialogVisible = true
  
        this.sysMenu = Object.assign({}, row)
        this.typeDisabled = true
      },
  
      saveOrUpdate() {
        if(this.sysMenu.type === 0 && this.sysMenu.parentId !== 0) {
          this.sysMenu.component = 'ParentView'
        }
        this.$refs.dataForm.validate(valid => {
          if (valid) {
            this.saveBtnDisabled = true // 防止表單重複提交
            if (!this.sysMenu.id) {
              this.saveData()
            } else {
              this.updateData()
            }
          }
        })
      },
  
      // 新增
      saveData() {
        api.save(this.sysMenu).then(response => {
          this.$message.success('新增成功' || response.message)
          this.dialogVisible = false
          this.fetchData(this.page)
        })
      },
  
      // 根據 id 更新記錄
      updateData() {
        api.updateById(this.sysMenu).then(response => {
          this.$message.success('更新成功' || response.message)
          this.dialogVisible = false
          this.fetchData()
        })
      }
    }
  }
  </script>
