<template xmlns="">
  <div style="font-family: Arial, sans-serif;">
    <el-row :gutter="20">
      <el-col :span="10">
        <el-card style="height: 43vh">
          <el-input
              v-model="searchTemplateInput"
              placeholder="Type id or title to search template"
              style="width: 40%"
              clearable
          />
          <el-button type="info" plain @click="loadTemplate" style="margin-left: 10px;">
            <el-icon><Search /></el-icon>
          </el-button>
          <el-button
              style="margin-left: 10px"
              @click="resetTemplate">
            <span>Reset</span>
          </el-button>
          <el-button
              type="primary"
              style="margin-left: 10px"
              @click="createTemplate">
            <span>Create</span>
          </el-button>
          <div v-loading="templateLoading" style="margin-top: 20px; min-height: 25vh">
            <el-table
                :data="templateData"
                :table-layout="tableLayout"
                style="width: 100%"
            >
              <el-table-column prop="id" label="ID"/>
              <el-table-column prop="title" label="Template Title" show-overflow-tooltip>
                <template #default="scope">
                  <el-button link @click="previewTemplate(scope.row)">
                    {{ scope.row.title }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column fixed="right" label="Operation" >
                <template #default="scope">
                  <el-button plain size="small" @click="editTemplate(scope.row)">
                    Edit
                  </el-button>
                  <el-button plain size="small" @click="applyTemplate(scope.row)">
                    Apply
                  </el-button>
                  <el-popconfirm title="Are you sure?" @confirm="deleteTemplate(scope.row.id)">
                    <template #reference>
                      <el-button type="danger" size="small">
                        Delete
                      </el-button>
                    </template>
                  </el-popconfirm>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div style="margin-top: 20px">
            <el-pagination
                @current-change="templateCurrentChange"
                v-model:current-page="templateCurrentPage"
                background
                layout="prev, pager, next, jumper"
                :page-size="templatePageSize"
                :total="templateTotal" />
          </div>
        </el-card>
        <el-card style="margin-top: 28px; height: 43vh">
          <el-input
              v-model="searchQuestionnaireInput"
              placeholder="Type id or title to search questionnaire"
              style="width: 40%"
              clearable
          />
          <el-button type="info" plain @click="loadQuestionnaire" style="margin-left: 10px;">
            <el-icon><Search /></el-icon>
          </el-button>
          <el-button
              style="margin-left: 10px"
              @click="resetQuestionnaire">
            <span>Reset</span>
          </el-button>
          <el-button
              type="primary"
              style="margin-left: 10px"
              @click="createQuestionnaire"
          >
            <span>Create</span>
          </el-button>
          <div v-loading="questionnaireLoading" style="margin-top: 20px; min-height: 25vh">
            <el-table
                :data="questionnaireData"
                :table-layout="tableLayout"
                style="width: 100%"
            >
              <el-table-column prop="id" label="ID"/>
              <el-table-column prop="title" label="Questionnaire Title" show-overflow-tooltip>
                <template #default="scope">
                  <el-button link @click="previewQuestionnaire(scope.row)">
                    {{ scope.row.title }}
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column fixed="right" label="Operation" >
                <template #default="scope">
                  <el-button plain size="small" @click="editQuestionnaire(scope.row)">
                    Edit
                  </el-button>
                  <el-button plain size="small" @click="checkQuestionnaire(scope.row.id)">
                    Check
                  </el-button>
                  <el-popconfirm title="Are you sure?" @confirm="deleteQuestionnaire(scope.row.id)">
                    <template #reference>
                      <el-button type="danger" size="small">
                        Delete
                      </el-button>
                    </template>
                  </el-popconfirm>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div style="margin-top: 20px">
            <el-pagination
                @current-change="questionnaireCurrentChange"
                v-model:current-page="questionnaireCurrentPage"
                background
                layout="prev, pager, next, jumper"
                :page-size="questionnairePageSize"
                :total="questionnaireTotal" />
          </div>
        </el-card>
      </el-col>
      <el-col :span="14">
        <div>
          <el-card style="height: 89vh">
            <template #header>
              <div style="display: flex; justify-content: space-between; align-items: center;">
                <span>Preview</span>
                <el-button v-if="this.clickOn !== ''" @click="this.clickOn = ''">Close</el-button>
              </div>
            </template>
            <el-empty v-if="this.clickOn === ''" description="No selected template or questionnaire" />
            <div v-if="this.clickOn !== ''">
              <el-alert v-if="this.clickOn === 'template'"
                        title="Templates could not be assigned to patients."
                        type="info"
                        show-icon
                        :closable="false"
              />
              <h1 style="text-align: center">{{ this.previewForm.title }}</h1>
              <div v-if="this.previewForm.description !== ''"
                   style="margin-bottom: 20px; text-align: center">
                {{ this.previewForm.description }}
              </div>
              <el-form :label-position="'top'">
                <el-scrollbar height="70vh">
                  <el-form-item
                      v-for="(question, index) in this.previewForm.questions"
                      :key="index"
                      :label="'Question ' + (parseInt(index) + 1) + ': ' + question.question"
                      :prop="'questions.' + index + '.question'"
                  >
                    <el-input v-model="question.answer" autosize type="textarea" />
                  </el-form-item>
                  <div v-if="this.clickOn === 'questionnaire'">
                    <el-form-item :label="'Question ' + (parseInt(this.previewForm.questions.length) + 1) +
                    ': As a result, do you think better, same or worse than the last time you give us a feedback?'">
                      <el-radio-group
                          v-model="this.result"
                          @change="this.needMeeting = ''; this.meetingTime = ''"
                      >
                        <el-radio label="Better" border>Better</el-radio>
                        <el-radio label="Same" border>Same</el-radio>
                        <el-radio label="Worse" border>Worse</el-radio>
                      </el-radio-group>
                    </el-form-item>
                    <div v-if="this.result === 'Worse'">
                      <el-form-item :label="'Question ' + (parseInt(this.previewForm.questions.length) + 2) +
                        ': Do you need a meeting with your doctor?'">
                        <el-radio-group v-model="this.needMeeting" @change="this.meetingTime = ''">
                          <el-radio label="Yes" border>Yes</el-radio>
                          <el-radio label="No" border>No</el-radio>
                        </el-radio-group>
                      </el-form-item>
                      <el-form-item
                          v-if="this.needMeeting === 'Yes'"
                          :label="'Question ' + (parseInt(this.previewForm.questions.length) + 3) +
                          ': What time is good for you to have a meeting?'"
                      >
                        <el-date-picker
                            v-model="this.meetingTime"
                            type="datetime"
                            placeholder="Select date and time"
                            format="YYYY-MM-DD HH:mm"
                            :disabled-date="disabledDate"
                        />
                      </el-form-item>
                    </div>
                  </div>
                </el-scrollbar>
              </el-form>
            </div>
          </el-card>
        </div>
      </el-col>
    </el-row>
    <!-- Create Template/Questionnaire Dialog -->
    <el-dialog v-model="dialogVisible" width="40%" align-center>
      <template #header>
        <span>{{ this.dialogTitle }}</span>
        <el-tooltip placement="top">
          <template #content>
            The system will automatically add questions to ask patients how the patient feels and if he needs a meeting.
          </template>
          <el-icon v-if="this.dialogMode === 2" style="margin-left: 5px"><InfoFilled /></el-icon>
        </el-tooltip>
      </template>
      <div v-if="this.dialogMode !== 5">
        <el-form ref="form" :model="form" :rules="rules" label-width="100px">
          <el-form-item prop="title" label="Title">
            <el-input v-model="form.title" />
          </el-form-item>
          <el-form-item prop="description" label="Description">
            <el-input v-model="form.description" type="textarea" autosize />
          </el-form-item>
          <el-form-item
              v-for="(question, index) in form.questions"
              :key="index"
              :label="'Question ' + (parseInt(index) + 1)"
              :prop="'questions.' + index + '.question'"
              :rules="rules.questions"
          >
            <el-input v-model="question.question">
              <template #append>
                <el-button type="danger" plain @click.prevent="deleteQuestion(question)">
                  Delete
                </el-button>
              </template>
            </el-input>
          </el-form-item>
        </el-form>
      </div>
      <div v-else-if="this.dialogMode === 5">
        <el-table
            :data="patientData"
            :table-layout="tableLayout"
            stripe
            style="width: 100%"
            height="300"
        >
          <el-table-column prop="given_name" label="Given Name" />
          <el-table-column prop="family_name" label="Family Name" />
        </el-table>
      </div>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button v-if="this.dialogMode !== 5" @click="addQuestion">Add a new question</el-button>
        <el-button type="primary" @click="submit">Submit</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";
import { getCookie } from "@/utils/cookie";

export default {
  name: "Questionnaires",
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
      searchTemplateInput: "",
      templateCurrentPage: 1,
      templatePageSize: 5,
      templateTotal: 0,
      templateLoading: false,
      questionnaireData: [],
      searchQuestionnaireInput: "",
      questionnaireCurrentPage: 1,
      questionnairePageSize: 5,
      questionnaireTotal: 0,
      questionnaireLoading: false,
      patientData: [],
      dialogVisible: false,
      dialogTitle: "",
      dialogMode: 0,
      clickOn: "",
      previewForm: {},
      result: "",
      needMeeting: "",
      meetingTime: "",
    }
  },
  created() {
    this.user = JSON.parse(getCookie("user"))
    this.$nextTick(() => {
      this.load()
    })
  },
  methods: {
    load() {
      this.loadTemplate()
      this.loadQuestionnaire()
    },
    loadTemplate() {
      this.templateLoading = true
      request.post("/templates/get", {
        doctorId: this.user.id,
        searchInput: this.searchTemplateInput.trim().toLowerCase(),
        pageNum: this.templateCurrentPage,
        pageSize: this.templatePageSize
      }).then(res => {
        this.templateData = res.data.rows
        this.templateTotal = res.data.total
        this.templateLoading = false
      })
    },
    loadQuestionnaire() {
      this.questionnaireLoading = true
      request.post("/questionnaires/get", {
        doctorId: this.user.id,
        searchInput: this.searchQuestionnaireInput.trim().toLowerCase(),
        pageNum: this.questionnaireCurrentPage,
        pageSize: this.questionnairePageSize
      }).then(res => {
        this.questionnaireData = res.data.rows
        this.questionnaireTotal = res.data.total
        this.questionnaireLoading = false
      })
    },
    templateCurrentChange(pageNum) {
      this.templateCurrentPage = pageNum
      this.loadTemplate()
    },
    createTemplate() {
      this.dialogVisible = true
      this.dialogTitle = "Create Template"
      this.dialogMode = 1
      this.form = {
        creatorId: this.user.id,
        title: "",
        questions: [
          { question: "", answer: "" }
        ]
      }
    },
    resetTemplate() {
      this.searchTemplateInput = ""
      this.templateCurrentPage = 1
      this.loadTemplate()
    },
    previewTemplate(row) {
      this.clickOn = "template"
      this.previewForm = JSON.parse(JSON.stringify(row))
    },
    editTemplate(row) {
      this.dialogVisible = true
      this.dialogTitle = "Edit Template"
      this.dialogMode = 3
      this.form = JSON.parse(JSON.stringify(row))
    },
    applyTemplate(row) {
      this.dialogVisible = true
      this.dialogTitle = "Create Questionnaire From Template"
      this.dialogMode = 2
      this.form = JSON.parse(JSON.stringify(row))
    },
   deleteTemplate(id) {
      request.delete("/templates/" + id).then(res => {
        if(res.code === "10000") {
          this.$message({
            type: "success",
            message: "You have deleted a template.",
            customClass: "font"
          })
          this.loadTemplate()
        }
      })
    },
    questionnaireCurrentChange(pageNum) {
      this.questionnaireCurrentPage = pageNum
      this.loadQuestionnaire()
    },
    createQuestionnaire() {
      this.dialogVisible = true
      this.dialogTitle = "Create Questionnaire"
      this.dialogMode = 2
      this.form = {
        creatorId: this.user.id,
        title: "",
        questions: [
          { question: "", answer: "" }
        ]
      }
    },
    resetQuestionnaire() {
      this.searchQuestionnaireInput = ""
      this.questionnaireCurrentPage = 1
      this.loadQuestionnaire()
    },
    previewQuestionnaire(row) {
      this.clickOn = "questionnaire"
      this.previewForm = JSON.parse(JSON.stringify(row))
    },
    editQuestionnaire(row) {
      this.dialogVisible = true
      this.dialogTitle = "Edit Questionnaire"
      this.dialogMode = 4
      this.form = JSON.parse(JSON.stringify(row))
    },
    checkQuestionnaire(id) {
      this.dialogVisible = true
      this.dialogTitle = "Check All Patients Assigned To This Questionnaire"
      this.dialogMode = 5
      request.get("/questionnaires/check/" + id).then(res => {
        this.patientData = res.data
      })
    },
    deleteQuestionnaire(id) {
      request.delete("/questionnaires/" + id).then(res => {
        if(res.code === "10000") {
          this.$message({
            type: "success",
            message: "You have deleted a questionnaire.",
            customClass: "font"
          })
          this.loadQuestionnaire()
        }
      })
    },
    addQuestion() {
      this.form.questions.push({ question: "", answer: "" })
    },
    deleteQuestion(question) {
      const index = this.form.questions.indexOf(question)
      if(index !== -1) {
        this.form.questions.splice(index, 1)
      }
    },
    submit() {
      this.$refs.form.validate((valid) => {
        if(valid) {
          if(this.dialogMode === 1) {
            if(this.form.questions.length === 0) {
              this.$message({
                type: "error",
                message: "The template should contain at least one question.",
                customClass: "font"
              })
              return
            }
            request.post("/templates", this.form).then(res => {
              if(res.code === '10000') {
                this.$message({
                  type: "success",
                  message: "You have created a new template.",
                  customClass: "font"
                })
                this.dialogVisible = false
                this.loadTemplate()
              }
            })
          } else if(this.dialogMode === 2) {
            if(this.form.questions.length === 0) {
              this.$message({
                type: "error",
                message: "The questionnaire should contain at least one question.",
                customClass: "font"
              })
              return
            }
            request.post("/questionnaires", this.form).then(res => {
              if(res.code === '10000') {
                this.$message({
                  type: "success",
                  message: "You have created a new questionnaire.",
                  customClass: "font"
                })
                this.dialogVisible = false
                this.loadQuestionnaire()
              }
            })
          } else if(this.dialogMode === 3) {
            if(this.form.questions.length === 0) {
              this.$message({
                type: "error",
                message: "The template should contain at least one question.",
                customClass: "font"
              })
              return
            }
            request.put("/templates", this.form).then(res => {
              if(res.code === '10000') {
                this.$message({
                  type: "success",
                  message: "You have edited a template.",
                  customClass: "font"
                })
                this.dialogVisible = false
                this.loadTemplate()
              }
            })
          } else if(this.dialogMode === 4) {
            if(this.form.questions.length === 0) {
              this.$message({
                type: "error",
                message: "The questionnaire should contain at least one question.",
                customClass: "font"
              })
              return
            }
            request.put("/questionnaires", this.form).then(res => {
              if(res.code === '10000') {
                this.$message({
                  type: "success",
                  message: "You have edited a questionnaire.",
                  customClass: "font"
                })
                this.dialogVisible = false
                this.loadQuestionnaire()
              }
            })
          }
        } else {
          this.$message({
            type: "error",
            message: "Please enter all required information and try again.",
            customClass: "font"
          })
        }
      })
    },
    disabledDate(time) {
      return new Date(time).getTime() < Date.now() - 8.64e7
    }
  }
}
</script>

<style>
.font {
  font-family: Arial, sans-serif;
}
</style>