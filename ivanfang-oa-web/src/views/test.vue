<template>
  <div>
    <div style="margin-left: 10px; margin-top: 20px;">
      <h1>帳號切換</h1>
      <table style="word-break:break-all">
        <tr style="height: 50px;">
          <td>
            請選擇欲切換的帳號
          </td>
          <td>
            <el-button @click="admin()" type="default" size="mini">admin</el-button>
            <el-button @click="cheyenne()" type="default" size="mini">Cheyenne</el-button>
            <el-button @click="mateo()" type="default" size="mini">Mateo</el-button>
            <el-button @click="amy()" type="default" size="mini">Amy</el-button>
            <el-button @click="glenn()" type="default" size="mini">Glenn</el-button>
          </td>
        </tr>
        <tr style="height: 50px;">
          <td style="width: 200px;">
            當前使用者（登入者）
          </td>
          <td style="width: 1100px;">
            {{ username }}
          </td>
        </tr>
        <tr style="height: 50px;">
          <td style="width: 100px;">
            當前 token
          </td>
          <td style="width: 1200px;">
            {{ token }}
          </td>
        </tr>
      </table>
    </div>

    <hr/>

    <div style="margin-left: 10px; margin-top: 20px;">
      <h1>操作</h1>

      <el-button type="default" size="mini">
        <a href="http://localhost:9090/#/" target="_blank">審批申請</a>
      </el-button>
      
      <el-button type="default" size="mini">
        <a href="http://localhost:9090/#/list/2" target="_blank">審批列表</a>
      </el-button>

      <el-button type="default" size="mini">
        <a href="http://localhost:9090/#/user" target="_blank">使用者資訊</a>
      </el-button>
    </div>

  </div>

</template>

<script>
  import request from '@/utils/request'
  export default {
    name: "Test",

    data() {
      return {
        token: '',
        username: '',
        password: ''
      };
    },

    created(){
      this.token = window.localStorage.getItem('token')
      this.username = window.localStorage.getItem('username')
    },

    methods: {
      cheyenne() {
        window.localStorage.setItem('token', '');
        let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLO0NDc3MjE10VEqLU4t8kwBiUHYeYm5qUA9zhmplal5ealKtQAxn7zIRgAAAA.ui7xKhaRFWsGGgmWUeljAgwwvsgHf5FtDAMIsPpHR_X9WhtaUdUYNESXIm0uEe9yisNuJcoS1lU69Z8Mnt0EMw'
        window.localStorage.setItem('token', token);
        this.token = window.localStorage.getItem('token');

        let username = 'Cheyenne';
        window.localStorage.setItem('username', username);
        this.username = username;

        this.password = 'cheyenne123';

        let loginInfo = {};
        loginInfo.username = this.username;
        loginInfo.password = this.password;
        this.login(loginInfo);
      },

      mateo() {
        window.localStorage.setItem('token', '');
        let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLO0NDc3MjE10VEqLU4t8kwBiplD2HmJualAPb6JJan5SrUAI_ufzEMAAAA.LLRyMVYWmD6jYrTTxWhNApvu1HfEri3g33so3YBcusPoVkOxh1COFbBI9V2DOzl8LDQ_iEoYNivq1KP5HTp2Ag'
        window.localStorage.setItem('token', token);
        this.token = window.localStorage.getItem('token');
        
        let username = 'Mateo';
        window.localStorage.setItem('username', username);
        this.username = username;

        this.password = 'mateo123';

        let loginInfo = {};
        loginInfo.username = this.username;
        loginInfo.password = this.password;
        this.login(loginInfo);
      },

      amy() {
        window.localStorage.setItem('token', '');
        let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLO0NDc3MjE10VEqLU4t8kwBihlD2HmJuakgPbmVSrUAQlK9wkEAAAA.I2Rz7eK9LTEH7Sq4ZjkEAOCHuBw2Ns1We52UVDkx-UHFE2iiPErSlgrQvzybz78VMzs1SXOKM0aW6_Y3DoHZLQ'
        window.localStorage.setItem('token', token);
        this.token = window.localStorage.getItem('token');

        let username = 'Amy';
        window.localStorage.setItem('username', username);
        this.username = username;

        this.password = 'amy123';

        let loginInfo = {};
        loginInfo.username = this.username;
        loginInfo.password = this.password;
        this.login(loginInfo);
      },

      glenn() {
        window.localStorage.setItem('token', '');
        let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLO0NDc3MjE10VEqLU4t8kwBikHZeYm5qUA97jmpeXlKtQBUZiwkQwAAAA.CFUQQNXrcD0qwkY9ral2B8MIJo-Ru24QxgvGqiP_7FW0pAxU7yqVXEzkFNYmc0FDaed2hhKFiGRVDPaJqjy2Jg'
        window.localStorage.setItem('token', token);
        this.token = window.localStorage.getItem('token');
        
        let username = 'Glenn';
        window.localStorage.setItem('username', username);
        this.username = username;

        this.password = 'glenn123';

        let loginInfo = {};
        loginInfo.username = this.username;
        loginInfo.password = this.password;
        this.login(loginInfo);
      },

      admin() {
        window.localStorage.setItem('token', '');
        let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLO0NDc3MjE10VEqLU4t8kwBikGYeYm5qUAtiSm5mXlKtQAUf4XyQgAAAA.6sM1Eg-V_-Ui7CjBMDloOzDvHZaTRlbkWz3tbr4i0VY392Y-ONGuOx-Gagrvwdif-3SUljMqhwzqrmR3D38ApA'
        window.localStorage.setItem('token', token);
        this.token = window.localStorage.getItem('token');

        let username = 'admin';
        window.localStorage.setItem('username', username);
        this.username = username;
        
        this.password = 'admin123';

        let loginInfo = {};
        loginInfo.username = this.username;
        loginInfo.password = this.password;
        this.login(loginInfo);
      },

      login(data) {
        return request({
          url: '/admin/system/index/login',
          method: 'post',
          data
        })
      }
    }
  }
</script>

<style lang="scss">

</style>