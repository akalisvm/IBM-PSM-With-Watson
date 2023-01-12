<template xmlns="">
  <div style="font-family: Arial, sans-serif;">
    <el-row :gutter="20">
      <el-col :span="10">
        <el-card style="height: 40vh">
          <el-input
              v-model="searchTemplateTitle"
              placeholder="Type to search template"
              clearable
              style="width: 50%"
          >
            <template #append>
              <el-button @click="templateLoad">
                <el-icon><Search /></el-icon>
              </el-button>
            </template>
          </el-input>
          <el-button
              type="primary"
              style="margin-left: 10px"
              @click="createTemplate">
            <span>Create</span>
          </el-button>
          <el-table
              :data="templateData"
              :table-layout="tableLayout"
              stripe
              style="width: 100%"
              fit
          >
            <el-table-column prop="id" label="ID"/>
            <el-table-column prop="title" label="Template Title" />
            <el-table-column fixed="right" label="Operations" >
              <template #default>
                <el-button link type="primary" size="small" @click="templateDetail">
                  Detail
                </el-button>
                <el-button link type="primary" size="small" @click="templateEdit">
                  Edit
                </el-button>
                <el-button type="danger" size="small" @click="templateDelete">
                  Delete
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="margin: 10px 0">
            <el-pagination
                @current-change="templateCurrentChange"
                v-model:current-page="templateCurrentPage"
                background
                layout="prev, pager, next, jumper"
                :hide-on-single-page="true"
                :page-size="templatePageSize"
                :total="templateTotal" />
          </div>
        </el-card>
        <el-card style="margin-top: 20px; height: 47vh">
          <el-input
              v-model="searchQuestionnaireTitle"
              placeholder="Type to search questionnaire"
              clearable
              style="width: 50%"
          >
            <template #append>
              <el-button @click="questionnaireLoad">
                <el-icon><Search /></el-icon>
              </el-button>
            </template>
          </el-input>
          <el-button
              type="primary"
              style="margin-left: 10px"
              @click="dialogVisible = true;
              dialogTitle = 'Create Questionnaire';
              dialogMode = 2"
          >
            <span>Create</span>
          </el-button>
        </el-card>
      </el-col>
      <el-col :span="14">
        <div v-if="this.clickOn === ''">
          <el-card style="height: 89vh">
            <template #header>
              <span>Detailed Description</span>
            </template>
            <el-empty description="No selected template or questionnaire" />
          </el-card>
        </div>
        <div v-if="this.clickOn === 'template'">
          <el-row>
            <el-col :span="24">
              <el-card style="height: 19vh">
                Display Template
              </el-card>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="10">
              <el-card style="margin-top: 20px; height: 68vh">
                Display Template
              </el-card>
            </el-col>
            <el-col :span="14">
              <el-card style="margin-top: 20px; height: 68vh">
                Display Template
              </el-card>
            </el-col>
          </el-row>
        </div>
        <div v-if="this.clickOn === 'questionnaire'">
          <el-row>
            <el-col :span="24">
              <el-card style="height: 19vh">
                Display Template
              </el-card>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="10">
              <el-card style="margin-top: 20px; height: 68vh">
                Display Template
              </el-card>
            </el-col>
            <el-col :span="14">
              <el-card style="margin-top: 20px; height: 68vh">
                Display Template
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-col>
    </el-row>
    <el-dialog v-model="dialogVisible" width="40%" align-center>
      <template #header>
        <span>{{ this.dialogTitle }}</span>
        <el-tooltip placement="top">
          <template #content>
            The system will automatically add a question to the questionnaire: <br />
            Do you need a meeting appointment with your doctor? <br />
            If the patient selected "yes", there will be an alert and you need to call the patient.
          </template>
          <el-icon v-if="this.dialogMode === 2" style="margin-left: 5px"><InfoFilled /></el-icon>
        </el-tooltip>
      </template>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item prop="title" label="Title">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item
            v-for="(question, index) in form.questions"
            :key="index"
            :label="'Question ' + (parseInt(index) + 1)"
            :prop="'questions.' + index + '.value'"
            :rules="rules.questions"
        >
          <el-input v-model="question.value">
            <template #append>
              <el-button type="danger" plain @click.prevent="removeQuestion(question)">
                Delete
              </el-button>
            </template>
          </el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button @click="addQuestion">Add a new question</el-button>
        <el-button type="primary" @click="confirm">Confirm</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { getCookie } from "@/utils/cookie.utils";
import request from "@/utils/request";

export default {
  name: "Questionnaire",
  data() {
    return {
      user: {},
      form: {},
      rules: {
        title: [
          { required: true, message: 'Please enter the title', trigger: 'blur' }
        ],
        questions: [
          { required: true, message: 'Please enter your question', trigger: 'blur' }
        ]
      },
      tableLayout: "auto",
      templateData: [],
      questionnaireData: [],
      searchTemplateTitle: "",
      searchQuestionnaireTitle: "",
      templateCurrentPage: 1,
      templatePageSize: 5,
      templateTotal: 0,
      dialogVisible: false,
      dialogTitle: "",
      dialogMode: 0,
      clickOn: "",
    }
  },
  created() {
    this.user = JSON.parse(getCookie("user"))
    this.load()
    this.$nextTick(() => {
      this.load()
    })
  },
  methods: {
    load() {
      this.templateLoad()
    },
    templateLoad() {
      request.get("/questionnaire/template", {
        params: {
          id: this.user.id,
          searchTitle: this.searchTemplateTitle,
          pageNum: this.templateCurrentPage,
          pageSize: this.templatePageSize
        }
      }).then(res => {
        this.templateData = res.data.records
        this.total = res.data.total
      })
    },
    templateCurrentChange(pageNum) {
      this.templateCurrentPage = pageNum
      this.templateLoad()
    },
    createTemplate() {
      this.dialogVisible = true
      this.dialogTitle = "Create Template"
      this.dialogMode = 1
      this.form = {
        creatorId: this.user.id,
        title: "",
        questions: [
          { value: "" }
        ]
      }
    },
    addQuestion() {
      this.form.questions.push({ value: "" })
    },
    removeQuestion(question) {
      const index = this.form.questions.indexOf(question)
      if(index !== -1) {
        this.form.questions.splice(index, 1)
      }
    },
    confirm() {
      this.$refs.form.validate((valid) => {
        if(valid) {
          if(this.dialogMode === 1) {
            if(this.form.questions.length === 0) {
              this.$message({
                type: "error",
                message: "The template should contain at least one question",
                customClass: 'font'
              })
              return
            }
            request.post("/questionnaire/template", this.form).then(res => {
              console.log(res)
              if(res.code === '10000') {
                this.$message({
                  type: "success",
                  message: "You have created a new template",
                  customClass: 'font'
                })
                this.dialogVisible = false
                this.templateLoad()
              }
            })
          } else if(this.dialogMode === 2) {
            if(this.form.questions.length === 0) {
              this.$message({
                type: "error",
                message: "The template should contain at least one question",
                customClass: 'font'
              })
              return
            }
            request.post("/questionnaire", this.form).then(res => {
              console.log(res)
              if(res.code === '10000') {
                this.$message({
                  type: "success",
                  message: "You have created a new questionnaire",
                  customClass: 'font'
                })
                this.dialogVisible = false
                this.questionnaireLoad()
              }
            })
          }
        } else {
          this.$message({
            type: "error",
            message: "Please enter all required information",
            customClass: 'font'
          })
        }
      })
    },
  }
}
</script>

<style>
  .font {
    font-family: Arial, sans-serif;
  }
</style>