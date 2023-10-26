<!-- template 的部分複製貼上即可，不用記 -->
<template>
  <div class="app-container">
    <!--查詢表單-->
    <div class="search-div">
      <el-form label-width="70px" size="small">
        <el-row>
          <el-col :span="24">
            <el-form-item label="角色名稱">
              <el-input
                style="width: 100%"
                v-model="searchObj.roleName"
                placeholder="角色名稱"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 搜尋、重置、添加、批量刪除按鈕 -->
        <!-- 用 disabled、$hasBP 控制按鈕權限 -->
        <el-row style="display: flex">
          <el-button type="primary" icon="el-icon-search" size="mini" :loading="loading" @click="fetchData()">搜尋</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetData">重置</el-button>
          <el-button type="success" icon="el-icon-plus" size="mini" :disabled="$hasBP('btn.sysRole.add')  === false" @click="add">新增</el-button>
          <el-button type="danger" icon="el-icon-delete" class="btn-add" size="mini" :disabled="$hasBP('btn.sysRole.remove')  === false" @click="batchRemove()">批量删除</el-button>
        </el-row>
      </el-form>
    </div>

    <!-- 表格 -->
    <el-table v-loading="listLoading" :data="list" stripe border style="width: 100%; margin-top: 10px" @selection-change="handleSelectionChange">
      <el-table-column type="selection" />

      <el-table-column label="序號" width="70" align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="roleName" label="角色名稱" />
      <el-table-column prop="roleCode" label="角色編碼" />
      <el-table-column prop="createTime" label="創建時間" width="160" />
      <!-- 單一角色管理按鈕（修改、刪除、權限分配） -->
      <!-- 用 disabled、$hasBP 控制按鈕權限 -->
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" size="mini" :disabled="$hasBP('btn.sysRole.update')  === false" @click="edit(scope.row.id)" title="修改"/>
          <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="$hasBP('btn.sysRole.remove')  === false" @click="removeDataById(scope.row.id)" title="刪除"/>
          <el-button type="warning" icon="el-icon-baseball" size="mini" :disabled="$hasBP('btn.sysRole.assignAuth')  === false" @click="showAssignAuth(scope.row)" title="權限分配"/>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 分頁組件 -->
    <el-pagination
      :current-page="page"
      :total="total"
      :page-size="limit"
      style="padding: 30px 0; text-align: center"
      layout="total, prev, pager, next, jumper"
      @current-change="fetchData"
    />

    <!-- 添加/修改角色的彈出視窗（對話框） -->
    <el-dialog title="添加/修改" :visible.sync="dialogVisible" width="40%">
      <el-form ref="dataForm" :model="sysRole" label-width="150px" size="small" style="padding-right: 40px">
        <el-form-item label="角色名稱">
          <el-input v-model="sysRole.roleName" />
        </el-form-item>
        <el-form-item label="角色編碼">
          <el-input v-model="sysRole.roleCode" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" size="small" icon="el-icon-refresh-right">取消</el-button>
        <el-button type="primary" icon="el-icon-check" @click="saveOrUpdate()" size="small">確定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import api from "@/api/system/sysRole.js";

export default {
  // 定義資料模型
  data() {
    return {
      list: [],               // 角色列表
      total: 0,               // 總記錄數
      page: 1,                // 頁碼
      limit: 5,               // 每頁記錄數
      searchObj: {},          // 查詢條件
      multipleSelection: [],  // 批量刪除選中的角色

      dialogVisible: false,   // 是否彈出「添加/修改」對話框
      sysRole: {},            // 將對話框的資料封裝成一個物件
      selections: [],         // 被選中的角色列表（用於批量刪除）
    };
  },
  created() {   // 頁面渲染前獲取資料
    this.fetchData();
  },
  methods: {    // 定義方法
    showAssignAuth(row) {   // 角色權限分配
      this.$router.push('/system/assignAuth?id=' + row.id + '&roleName=' + row.roleName);
    },
    fetchData(current = 1) {
      this.page = current;
      api.getByPage(this.page, this.limit, this.searchObj).then((response) => {
        this.list = response.data.records;  // Page 物件裡的 records 才是真正的資料
        this.total = response.data.total;   // Page 物件裡的 total
      });
    },
    removeDataById(id) {
      this.$confirm("此操作將永久刪除該記錄, 是否繼續？", "提示", {
        confirmButtonText: "確定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          return api.removeById(id);
        })
        .then((response) => {
          this.fetchData(); // 刷新頁面
          this.$message.success("删除成功" || response.message);
        });
    },
    add() {
      this.dialogVisible = true;
    },
    saveOrUpdate() {
      // 根據 this.sysRole 是否有 id 判斷是要 save 還是 update
      if (!this.sysRole.id) {
        this.save();
      } else {
        this.update();
      }
    },
    save() {
      api.save(this.sysRole).then((response) => {
        this.$message.success("添加成功" || response.message);
        this.fetchData();
        this.sysRole.roleName = "";
        this.sysRole.roleCode = "";
        this.dialogVisible = false;
      });
    },
    edit(id) {
      // 修改角色資料
      this.fetchDataById(id); // 獲取角色資料
      this.dialogVisible = true; // 彈出對話框
    },
    fetchDataById(id) {
      api.getById(id).then((response) => {
        this.sysRole = response.data;
      });
    },
    update() {
      api.update(this.sysRole).then((response) => {
        this.$message.success("修改成功" || response.message);
        this.fetchData();
        this.sysRole.roleName = "";
        this.sysRole.roleCode = "";
        this.dialogVisible = false;
      });
    },
    handleSelectionChange(selections) {
      this.selections = selections;
    },
    batchRemove() {
      if (this.selections.length == 0) {
        this.$message.warning("請選擇要刪除的角色");
        return;
      }
      this.$confirm("此操作將永久刪除該記錄, 是否繼續？", "提示", {
        confirmButtonText: "確定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          var idList = [];
          this.selections.forEach((selection) => {
            idList.push(selection.id);
          });
          return api.removeBatch(idList);
        })
        .then((response) => {
          this.fetchData(); // 刷新頁面
          this.$message.success("删除成功" || response.message);
        });
    },
	resetData() {
		this.searchObj = {};
		this.fetchData();
	}
  },
};
</script>
