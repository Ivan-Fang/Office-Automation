<template>
  <div class="app-container">
    <el-steps :active="stepIndex" finish-status="success">
      <el-step title="基本設置"></el-step>
      <el-step title="表單設置"></el-step>
      <el-step title="流程設置"></el-step>
    </el-steps>

    <div class="tools-div">
      <el-button v-if="stepIndex > 1" icon="el-icon-check" type="primary" size="small" @click="pre()">上一步
      </el-button>
      <el-button icon="el-icon-check" type="primary" size="small" @click="next()">{{
          stepIndex == 3 ? '提交保存' : '下一步'
        }}
      </el-button>
      <el-button type="primary" size="small" @click="back()">返回</el-button>
    </div>

    <!-- 第一步 -->
    <div v-show="stepIndex == 1" style="margin-top: 20px;">
      <el-form ref="flashPromotionForm" label-width="150px" size="small" style="padding-right: 40px;">
        <el-form-item label="審批類型">
          <el-select v-model="processTemplate.processTypeId" placeholder="請選擇審批類型">
            <el-option :key="item.id" v-for="item in processTypeList" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="審批名稱">
          <el-input v-model="processTemplate.name"/>
        </el-form-item>
        <el-form-item label="審批圖示">
          <el-select v-model="processTemplate.iconUrl" placeholder="請選擇審批圖示">
            <el-option :key="item.id" v-for="item in iconUrlList" :label="item.iconUrl" :value="item.iconUrl">
              <img :src="item.iconUrl" style="width: 30px;height: 30px;vertical-align: text-bottom;">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="描述">
          <el-input v-model="processTemplate.description"/>
        </el-form-item>
      </el-form>
    </div>

    <!-- 第二步 -->
    <div v-show="stepIndex == 2" style="margin-top: 20px;">
      <!-- 表单构建器 -->
      <fc-designer class="form-build" ref="designer"/>
    </div>

    <!-- 第三步 -->
    <div v-show="stepIndex == 3" style="margin-top: 20px;">
      <el-upload
        class="upload-demo"
        drag
        action="/dev-api/admin/process/processTemplate/uploadProcessDefinition"
        :headers="uploadHeaders"
        multiple="false"
        :before-upload="beforeUpload"
        :on-success="onUploadSuccess"
        :file-list="fileList"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">將 Activiti 流程設計檔拖到此處，或<em>點擊上傳</em></div>
        <div class="el-upload__tip" slot="tip">只能上傳 zip 壓縮檔，且不超過 2048 KB</div>
        <div class="el-upload__tip" slot="tip">zip 檔名稱必須和 xml 檔中的 process id 一致</div>
          <div class="el-upload__tip" slot="tip">xml 檔必須附上 .bpmn20 的後綴，例如 leave.bpmn20.xml</div>
      </el-upload>
    </div>
  </div>
</template>
  
<script>
  import api from '@/api/process/processTemplate'
  import processTypeApi from '@/api/process/processType'
  import store from '@/store'

  const defaultForm = {
    id: '',
    name: '',
    iconUrl: '',
    formProps: '',
    formOptions: '',
    processDefinitionKey: '',
    processDefinitionPath: '',
    description: ''
  }
  export default {
    data() {
      return {
        stepIndex: 1,
        processTypeList: [],
        processTemplate: defaultForm,
        iconUrlList: [
          { iconUrl: 'https://gw.alicdn.com/tfs/TB1t695CFYqK1RjSZLeXXbXppXa-102-102.png', tag: '請假' },
          { iconUrl: 'https://gw.alicdn.com/tfs/TB1bHOWCSzqK1RjSZFjXXblCFXa-112-112.png', tag: '出差' },
          { iconUrl: 'https://gw.alicdn.com/tfs/TB1cbCYCPTpK1RjSZKPXXa3UpXa-112-112.png', tag: '機票出差' },
          { iconUrl: 'https://gw.alicdn.com/tfs/TB1cbCYCPTpK1RjSZKPXXa3UpXa-112-112.png', tag: '機票改簽' },
          { iconUrl: 'https://gw.alicdn.com/tfs/TB1e76lCOLaK1RjSZFxXXamPFXa-112-112.png', tag: '外出' },
          { iconUrl: 'https://gw.alicdn.com/tfs/TB1Yfa0CG6qK1RjSZFmXXX0PFXa-112-112.png', tag: '補卡申請' },
          { iconUrl: 'https://gw.alicdn.com/tfs/TB1Y8PlCNjaK1RjSZKzXXXVwXXa-112-112.png', tag: '加班' },
          { iconUrl: 'https://gw.alicdn.com/tfs/TB11X99CNTpK1RjSZFKXXa2wXXa-102-102.png', tag: '居家隔離' },
          { iconUrl: 'https://gw.alicdn.com/tfs/TB1_YG.COrpK1RjSZFhXXXSdXXa-102-102.png', tag: '請假' },
          { iconUrl: 'https://gw.alicdn.com/tfs/TB13ca1CMDqK1RjSZSyXXaxEVXa-102-102.png', tag: '調職' },
          { iconUrl: 'https://gw.alicdn.com/tfs/TB1U9iBCSzqK1RjSZPcXXbTepXa-102-102.png', tag: '離職' },
          { iconUrl: 'https://gw.alicdn.com/tfs/TB11pS_CFzqK1RjSZSgXXcpAVXa-102-102.png', tag: '費用申請' },
          { iconUrl: 'https://gw.alicdn.com/tfs/TB1t695CFYqK1RjSZLeXXbXppXa-102-102.png', tag: '用章申請' },
          { iconUrl: 'https://gw.alicdn.com/tfs/TB13f_aCQzoK1RjSZFlXXai4VXa-102-102.png', tag: '攜章外出' },
          { iconUrl: 'https://gw.alicdn.com/tfs/TB1_YG.COrpK1RjSZFhXXXSdXXa-102-102.png', tag: '學期內分期' },
          { iconUrl: 'https://gw.alicdn.com/tfs/TB1_YG.COrpK1RjSZFhXXXSdXXa-102-102.png', tag: '特殊學費' },
          { iconUrl: 'https://gw.alicdn.com/tfs/TB1Yfa0CG6qK1RjSZFmXXX0PFXa-112-112.png', tag: '充值卡申領' },
          { iconUrl: 'https://gw.alicdn.com/tfs/TB1e76lCOLaK1RjSZFxXXamPFXa-112-112.png', tag: '禮品申領' },
          { iconUrl: 'https://gw.alicdn.com/tfs/TB1FNG.CMHqK1RjSZFgXXa7JXXa-102-102.png', tag: '郵寄快遞申請' },
          { iconUrl: 'https://gw.alicdn.com/imgextra/i3/O1CN01LLn0YV1LhBXs7T2iO_!!6000000001330-2-tps-120-120.png', tag: '合同審批' },
          { iconUrl: 'https://gw.alicdn.com/tfs/TB1e76lCOLaK1RjSZFxXXamPFXa-112-112.png', tag: '合同借閱' },
          { iconUrl: 'https://gw.alicdn.com/tfs/TB1e76lCOLaK1RjSZFxXXamPFXa-112-112.png', tag: '魔點臨時開門許可權' },
          { iconUrl: 'https://gw.alicdn.com/tfs/TB1bHOWCSzqK1RjSZFjXXblCFXa-112-112.png', tag: '北京科技園車證審批' },
          { iconUrl: 'https://gw.alicdn.com/tfs/TB1e76lCOLaK1RjSZFxXXamPFXa-112-112.png', tag: '魔點訪客提前預約審批' }
        ],

        uploadHeaders: {
          'token': store.getters.token
        },
        fileList: []
      }
    },

    created() {
      let id = this.$route.query.id
      console.log(id)
      if (id > 0) {
        this.fetchDataById(id)
      }
      this.fetchProcessTypeData()
    },

    methods: {
      pre() {
        this.stepIndex -= 1
      },

      next() {

        if (this.stepIndex === 2) {
          this.processTemplate.formProps = JSON.stringify(this.$refs.designer.getRule())
          this.processTemplate.formOptions = JSON.stringify(this.$refs.designer.getOption())
          console.log(JSON.stringify(this.processTemplate))
        }
        
        if (this.stepIndex === 3) {
          this.saveOrUpdate()
        }

        this.stepIndex += 1
        
        console.log("stepIndex = " + this.stepIndex)
      },

      fetchProcessTypeData() {
        processTypeApi.findAll().then(response => {
          this.processTypeList = response.data
        })
      },
      fetchDataById(id) {
        api.getById(id).then(response => {
          this.processTemplate = response.data
          // 給表單設計器賦值
          this.$refs.designer.setRule(JSON.parse(this.processTemplate.formProps))
          this.$refs.designer.setOption(JSON.parse(this.processTemplate.formOptions))
          this.fileList = [{
            name: this.processTemplate.processDefinitionPath,
            url: this.processTemplate.processDefinitionPath
          }]
        })
      },

      saveOrUpdate() {
        this.saveBtnDisabled = true // 防止表單重複提交
        if (!this.processTemplate.id) {
          this.saveData()
        } else {
          this.updateData()
        }
      },

      // 新增
      saveData() {
        api.save(this.processTemplate).then(response => {
          this.$router.push('/processSet/processTemplate')
        })
      },

      // 根據id更新記錄
      updateData() {
        api.updateById(this.processTemplate).then(response => {
          this.$router.push('/processSet/processTemplate')
        })
      },

      // 檔上傳限制條件
      beforeUpload(file) {
        const isZip = file.type === 'application/x-zip-compressed'
        const isLt2M = file.size / 1024 / 1024 < 2

        if (!isZip) {
          this.$message.error('檔案格式不正確!')
          return false
        }
        if (!isLt2M) {
          this.$message.error('上傳頭像圖片大小不能超過 2MB!')
          return false
        }
        return true
      },

      // 上傳圖片成功的回檔
      onUploadSuccess(res, file) {
        // 填充上傳文件列表
        this.processTemplate.processDefinitionPath = res.data.processDefinitionPath
        this.processTemplate.processDefinitionKey = res.data.processDefinitionKey
      },

      back() {
        this.$router.push('/processSet/processTemplate')
      }
    }
  }
</script>
