<template>
  <div class="container">
    <van-nav-bar
      title="使用者資訊"
    />
    <div class="detail-wrap">
      <div class="item">
        使用者名稱：{{ user.username }}
      </div>
      <div class="item">
        真實姓名：{{ user.name }}
      </div>
      <div class="item">
        手機號碼：{{ user.phone }}
      </div>
      <div class="item">
        所屬角色：<span v-for="(item, index) in user.roleList" :key="item.id">{{ item.roleName }}<span v-if="index != user.roleList.length - 1">、</span></span>
      </div>
      <div class="item">
        創建時間：{{ user.createTime }}
      </div>
    </div>

  </div>
</template>

<script>
import api from '@/api/userInfo'
export default {
  name: "process",

  data() {
    return {
      user: { }
    };
  },

  created(){
    this.fetchData();
  },

  methods: {
    fetchData() {
      // debugger
      api.getCurrentUser().then(response => {
        this.user = response.data
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.container {
  padding: 20px;

  // margin: 20px;

  .detail-wrap {
    .item {

      margin-top: 10px;

      h5 {
        color: #838485;
        margin: 10px;
      }

      p {
        color: #1B1F22;
        margin: 0;
      }
    }
  }
}
</style>
