<template>
  <div class="app-container">
    <div class="search-div">
      <el-form label-width="70px" size="small">
        <el-row>
          <el-col :span="8">
            <el-form-item label="關鍵字">
              <el-input
                style="width: 95%"
                v-model="searchObj.keyword"
                placeholder="使用者名稱/真實姓名/手機號碼"
              ></el-input>
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
                style="margin-right: 10px; width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 搜尋、重置、新增按鈕 -->
        <!-- 用 disabled、$hasBP 控制按鈕權限 -->
        <el-row style="display: flex">
          <el-button type="primary" icon="el-icon-search" size="mini" :loading="loading" @click="fetchData()">搜尋</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetData">重置</el-button>
          <el-button type="success" icon="el-icon-plus" size="mini" :disabled="$hasBP('btn.sysUser.add')  === false" @click="add">新增</el-button>
        </el-row>
      </el-form>
    </div>

    <!-- 列表 -->
    <el-table v-loading="listLoading" :data="list" stripe border style="width: 100%; margin-top: 10px">
      
			<el-table-column label="序號" width="70" align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="username" label="使用者名稱"/>
      <el-table-column prop="name" label="真實姓名"/>
      <el-table-column prop="phone" label="手機號碼" />
      <!-- <el-table-column prop="postName" label="職位" width="100" />
      <el-table-column prop="deptName" label="部門" width="100" /> -->

      <el-table-column label="所屬角色">
        <template slot-scope="scope">
          <span v-for="(item, index) in scope.row.roleList" :key="item.id">
            {{ item.roleName }}
            <span v-if="index != scope.row.roleList.length - 1">、</span>
          </span>
        </template>
      </el-table-column>

      <el-table-column label="狀態" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status === 1"
            @change="switchStatus(scope.row)"
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="創建時間" width="160" />

      <!-- 單一使用者管理按鈕（修改、刪除、角色分配） -->
      <!-- 用 disabled、$hasBP 控制按鈕權限 -->
      <el-table-column label="操作" width="180" align="center">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" size="mini" :disabled="$hasBP('btn.sysUser.update')  === false" @click="edit(scope.row.id)" title="修改"/>
          <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="$hasBP('btn.sysUser.remove')  === false" @click="removeDataById(scope.row.id)" title="刪除"/>
          <el-button type="warning" icon="el-icon-baseball" size="mini" :disabled="$hasBP('btn.sysUser.assignRole')  === false" @click="showAssignRole(scope.row)" title="角色分配"/>
        </template>
      </el-table-column>

    </el-table>

    <!-- 分頁組件 -->
    <el-pagination
      :current-page="page"
      :total="total"
      :page-size="limit"
      :page-sizes="[5, 10, 20, 30, 40, 50, 100]"
      style="padding: 30px 0; text-align: center"
      layout="sizes, prev, pager, next, jumper, ->, total, slot"
      @current-change="fetchData"
      @size-change="changeSize"
    />

    <el-dialog title="添加/修改" :visible.sync="dialogVisible" width="40%">
      <el-form
        ref="dataForm"
        :model="sysUser"
        label-width="100px"
        size="small"
        style="padding-right: 40px"
      >
        <el-form-item label="使用者名稱" prop="username">
          <el-input v-model="sysUser.username" />
        </el-form-item>
        <el-form-item v-if="!sysUser.id" label="密碼" prop="password">
          <el-input v-model="sysUser.password" type="password" />
        </el-form-item>
        <el-form-item label="真實姓名" prop="name">
          <el-input v-model="sysUser.name" />
        </el-form-item>
        <el-form-item label="手機號碼" prop="phone">
          <el-input v-model="sysUser.phone" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" size="small" icon="el-icon-refresh-right">取消</el-button>
        <el-button type="primary" icon="el-icon-check" @click="saveOrUpdate()" size="small">確定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="分配角色" :visible.sync="dialogRoleVisible">
      <el-form label-width="80px">
        <el-form-item label="使用者名稱">
          <el-input disabled :value="sysUser.username"></el-input>
        </el-form-item>

        <el-form-item label="角色列表">
          <el-checkbox
            :indeterminate="isIndeterminate"
            v-model="checkAll"
            @change="handleCheckAllChange"
            >全選</el-checkbox
          >
          <div style="margin: 15px 0"></div>
          <el-checkbox-group
            v-model="userRoleIds"
            @change="handleCheckedChange"
          >
            <el-checkbox
              v-for="role in allRoles"
              :key="role.id"
              :label="role.id"
              >{{ role.roleName }}</el-checkbox
            >
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button type="primary" @click="assignRole" size="small"
          >儲存</el-button
        >
        <el-button @click="dialogRoleVisible = false" size="small"
          >取消</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>
  
  <script>
import api from "@/api/system/sysUser";
import roleApi from "@/api/system/sysRole";

const defaultForm = {
  id: "",
  username: "",
  password: "",
  name: "",
  phone: "",
  status: 1,
};

export default {
  data() {
    return {
      listLoading: true,	// 資料是否正在載入
      list: null, 				// banner列表
      total: 0, 					// 資料庫中的總記錄數
      page: 1, 						// 默認頁碼
      limit: 5, 					// 每頁記錄數
      searchObj: {}, 			// 查詢表單物件

      createTimes: [],

      dialogVisible: false,
      sysUser: defaultForm,
      saveBtnDisabled: false,

      dialogRoleVisible: false,
      allRoles: [], 					// 所有角色列表
      userRoleIds: [], 				// 使用者的角色ID的列表
      isIndeterminate: false, // 是否是不確定的
			checkAll: false, 				// 是否全選
    };
  },

  // 生命週期函數：記憶體準備完畢，頁面尚未渲染
  created() {
    console.log("list created......");
    this.fetchData();

    roleApi.getAll().then((response) => {
      this.roleList = response.data;
    });
  },

  // 生命週期函數：記憶體準備完畢，頁面渲染成功
  mounted() {
    console.log("list mounted......");
  },

  methods: {
    // 當頁碼發生改變的時候
    changeSize(size) {
      console.log(size);
      this.limit = size;
      this.fetchData(1);
    },

    // 載入banner清單數據
    fetchData(page = 1) {
      // debugger;
      this.page = page;
      console.log("翻頁。。。" + this.page);

      if (this.createTimes && this.createTimes.length == 2) {
        this.searchObj.createTimeBegin = this.createTimes[0];
        this.searchObj.createTimeEnd = this.createTimes[1];
      }

      api
        .getByPage(this.page, this.limit, this.searchObj)
        .then((response) => {
          //this.list = response.data.list
          this.list = response.data.records;
          this.total = response.data.total;

          // 資料載入並綁定成功
          this.listLoading = false;
        });
    },

    // 重置查詢表單
    resetData() {
      console.log("重置查詢表單");
      this.searchObj = {};
      this.createTimes = [];
      this.fetchData();
    },

    // 根據id刪除資料
    removeDataById(id) {
      // debugger
      this.$confirm("此操作將永久刪除該記錄, 是否繼續?", "提示", {
        confirmButtonText: "確定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          // promise
          // 點擊確定，遠程調用ajax
          return api.removeById(id);
        })
        .then((response) => {
          this.fetchData(1);
          this.$message.success(response.message || "刪除成功");
        })
        .catch(() => {
          this.$message.info("取消刪除");
        });
    },

    // -------------
    add() {
      this.dialogVisible = true;
      this.sysUser = Object.assign({}, defaultForm);
    },

    edit(id) {
      this.dialogVisible = true;
      this.fetchDataById(id);
    },

    fetchDataById(id) {
      api.getById(id).then((response) => {
        this.sysUser = response.data;
      });
    },

    saveOrUpdate() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          this.saveBtnDisabled = true; // 防止表單重複提交
          if (!this.sysUser.id) {
            this.saveData();
          } else {
            this.updateData();
          }
        }
      });
    },

    // 新增
    saveData() {
      api.save(this.sysUser).then((response) => {
        this.$message.success("操作成功");
        this.dialogVisible = false;
        this.fetchData(this.page);
      });
    },

    // 根據id更新記錄
    updateData() {
      api.updateById(this.sysUser).then((response) => {
        this.$message.success(response.message || "操作成功");
        this.dialogVisible = false;
        this.fetchData(this.page);
      });
    },

    showAssignRole(row) {
      this.sysUser = row;
      this.getRoles();
			this.dialogRoleVisible = true;
    },

    getRoles() {
			// console.log("進到 getRoles() 裡...")
      roleApi.getRoles(this.sysUser.id).then((response) => {
        const { allRolesList, assignRoleList } = response.data
        this.allRoles = allRolesList;
        this.userRoleIds = assignRoleList.map((item) => item.id);
        this.checkAll = allRolesList.length === assignRoleList.length;
        this.isIndeterminate = assignRoleList.length > 0 && assignRoleList.length < allRolesList.length;
      });
    },

    /*
      全選勾選狀態發生改變的監聽
		*/
    handleCheckAllChange(value) {
      // value 當前勾選狀態true/false
      // 如果當前全選, userRoleIds就是所有角色id的陣列, 否則是空陣列
      this.userRoleIds = value ? this.allRoles.map((item) => item.id) : [];
      // 如果當前不是全選也不全不選時, 指定為false
      this.isIndeterminate = false;
    },

    /*
      角色列表選中項發生改變的監聽
		*/
    handleCheckedChange(value) {
      const { userRoleIds, allRoles } = this;
      this.checkAll =
        userRoleIds.length === allRoles.length && allRoles.length > 0;
      this.isIndeterminate =
        userRoleIds.length > 0 && userRoleIds.length < allRoles.length;
    },

    assignRole() {
      let assginRoleVo = {
        userId: this.sysUser.id,
        roleIdList: this.userRoleIds,
      };
      roleApi.assignRoles(assginRoleVo).then((response) => {
        this.$message.success("角色分配成功" || response.message);
        this.dialogRoleVisible = false;
        this.fetchData(this.page);
      });
    },

    switchStatus(row) {
      row.status = row.status === 1 ? 0 : 1;
      api.updateStatus(row.id, row.status).then((response) => {
        if (response.code) {
          this.$message.success("操作成功" || response.message);
          this.dialogVisible = false;
          this.fetchData();
        }
      });
    },
  },
};
</script>
