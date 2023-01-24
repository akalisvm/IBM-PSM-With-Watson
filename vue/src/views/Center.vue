<template>
  <div style="font-family: Arial, sans-serif;">
    <el-card>
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <span>Hi, {{ this.user.given_name }}  {{ this.user.family_name }}</span>
        <el-button type="info" @click="infoDialogVisible = true" plain>Show personal information</el-button>
      </div>
    </el-card>
    <div><br /></div>
    <div v-loading="fillLoading">
      <el-card v-if="this.clickOn === ''" style="height: 80vh;">
        <el-tabs v-model="activeName">
          <el-tab-pane label="Healthcare Records" name="first">
            <!-- Healthcare Records Functional Area -->
            <div>
              <el-input
                  v-model="searchRecordInput"
                  placeholder="Type id to search record"
                  style="width: 15%"
                  clearable
                  @keyup.enter.native="loadRecord">
              </el-input>
              <el-select
                  v-model="resultFilter"
                  placeholder="Select result"
                  clearable
                  style="margin-left: 10px; width: 10%"
              >
                <el-option label="Better" value="Better" />
                <el-option label="Same" value="Same" />
                <el-option label="Worse" value="Worse" />
              </el-select>
              <el-select
                  v-model="needMeetingFilter"
                  placeholder="select need meeting"
                  clearable
                  style="margin-left: 10px; width: 11%">
                <el-option label="Yes" value="Yes" />
                <el-option label="No" value="No" />
              </el-select>
              <el-button type="info" plain @click="loadRecord" style="margin-left: 10px;">
                <el-icon><Search /></el-icon>
              </el-button>
              <el-button style="margin-left: 10px" @click="this.searchRecordInput = ''; this.load()">
                Reset
              </el-button>
              <el-button type="primary" style="margin-left: 10px" @click="fill">
                Fill Questionnaire
              </el-button>
            </div>
            <!-- Healthcare Records Table Area -->
            <div v-loading="loading" style="margin-top: 20px; min-height: 46vh">
              <el-table
                  :data="recordData"
                  style="width: 100%"
                  :table-layout="tableLayout"
              >
                <el-table-column prop="id" label="ID" />
                <el-table-column prop="createTime" label="Create Time">
                  <template #default="scope">
                    {{ formatDate(scope.row.createTime) }}
                  </template>
                </el-table-column>
                <el-table-column prop="questionnaire.title" label="Questionnaire Title" show-overflow-tooltip>
                  <template #default="scope">
                    <el-button link @click="detail(scope.row)">
                      {{ scope.row.questionnaire.title }}
                    </el-button>
                  </template>
                </el-table-column>
                <el-table-column prop="questionnaire.result" label="Result">
                  <template #default="scope">
                    <el-tag
                        :type="scope.row.questionnaire.result === 'Worse' ? 'danger' : 'success'"
                        disable-transitions
                    >
                      {{ scope.row.questionnaire.result }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="questionnaire.needMeeting" label="Need Meeting" />
                <el-table-column prop="questionnaire.meetingTime" label="Suggested Meeting Time">
                  <template #default="scope">
                    <div v-if="formatDate(scope.row.questionnaire.meetingTime) !== '1970-01-01 01:00'">
                      {{ formatDate(scope.row.questionnaire.meetingTime) }}
                    </div>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <div style="margin: 10px 0">
              <el-pagination
                  @current-change="recordCurrentChange"
                  v-model:current-page="recordCurrentPage"
                  background
                  layout="prev, pager, next, jumper"
                  :page-size="recordPageSize"
                  :total="recordTotal" />
            </div>
          </el-tab-pane>
          <el-tab-pane label="Outreach Events" name="second">
            <!-- Outreach Events Functional Area -->
            <div>
              <el-input
                  v-model="searchEventInput"
                  placeholder="Type id to search event"
                  style="width: 15%"
                  clearable
                  @keyup.enter.native="loadEvent">
              </el-input>
            </div>
            <!-- Outreach Events Table Area -->
            <div v-loading="loading" style="margin-top: 20px; min-height: 46vh">
              <el-table
                  :data="eventData"
                  style="width: 100%"
                  :table-layout="tableLayout"
              >
                <el-table-column prop="id" label="ID" />
                <el-table-column prop="createTime" label="Create Time" />
                <el-table-column prop="title" label="Title" />
                <el-table-column prop="platform" label="Platform" />
                <el-table-column prop="meetingTime" label="Scheduled Meeting Time" sortable />
                <el-table-column fixed="right" label="Operation">
                  <template #default="scope">
                    <el-button v-if="scope.row.feedback !== ''" type="primary" plain size="small" @click="review(scope.row.id)">
                      Review Feedback
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <div style="margin: 10px 0">
              <el-pagination
                  @current-change="eventCurrentChange"
                  v-model:current-page="eventCurrentPage"
                  background
                  layout="prev, pager, next, jumper"
                  :page-size="eventPageSize"
                  :total="eventTotal" />
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-card>
      <!-- Questionnaire Form -->
      <el-card v-if="this.clickOn === 'fill'" style="height: 80vh;">
        <template #header>
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <el-button @click="back">Back</el-button>
            <div>
              <el-button @click="reset">Reset</el-button>
              <el-button type="primary" @click="submit">Submit</el-button>
            </div>
          </div>
        </template>
        <h1 style="text-align: center">{{ this.form.questionnaire.title }}</h1>
        <div v-if="this.form.questionnaire.description !== ''"
             style="margin-bottom: 20px; text-align: center">
          <span>{{ this.form.questionnaire.description }}</span>
        </div>
        <el-form ref="form" :model="form" :rules="rules" :label-position="'top'">
          <el-scrollbar height="60vh">
            <el-form-item
                v-for="(question, index) in form.questionnaire.questions"
                :key="index"
                :label="'Question ' + (parseInt(index) + 1) + ': ' + question.question"
                :prop="'questionnaire.questions.' + index + '.answer'"
                :rules="rules.answers"
            >
              <el-input v-model="question.answer" autosize type="textarea" clearable />
            </el-form-item>
            <el-form-item
                prop="questionnaire.result"
                :label="'Question ' + (parseInt(this.form.questionnaire.questions.length) + 1) +
                    ': As a result, do you think better, same or worse than the last time you give us a feedback?'"
            >
              <el-radio-group
                  v-model="form.questionnaire.result"
                  @change="this.form.questionnaire.needMeeting = '';
                  this.form.questionnaire.meetingTime = ''"
              >
                <el-radio label="Better" border>Better</el-radio>
                <el-radio label="Same" border>Same</el-radio>
                <el-radio label="Worse" border>Worse</el-radio>
              </el-radio-group>
            </el-form-item>
            <div v-if="this.form.questionnaire.result === 'Worse'">
              <el-form-item
                  prop="questionnaire.needMeeting"
                  :label="'Question ' + (parseInt(this.form.questionnaire.questions.length) + 2) +
                        ': Do you need a meeting with your doctor?'"
              >
                <el-radio-group
                    v-model="form.questionnaire.needMeeting"
                    @change="this.form.questionnaire.meetingTime = ''"
                >
                  <el-radio label="Yes" border>Yes</el-radio>
                  <el-radio label="No" border>No</el-radio>
                </el-radio-group>
              </el-form-item>
              <div v-if="this.form.questionnaire.needMeeting === 'Yes'">
                <el-form-item
                    prop="questionnaire.meetingTime"
                    :label="'Question ' + (parseInt(this.form.questionnaire.questions.length) + 3) +
                      ': What time is good for you to have a meeting?'"
                >
                  <el-date-picker
                      v-model="form.questionnaire.meetingTime"
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
      </el-card>
      <!-- Personal Information Demonstration Dialog -->
      <el-dialog v-model="infoDialogVisible">
        <el-descriptions
            title="Personal Information"
            direction="vertical"
            :column="4"
            border
        >
          <el-descriptions-item label="Given Name">
            {{ this.user.given_name }}
          </el-descriptions-item>
          <el-descriptions-item label="Family Name">
            {{ this.user.family_name }}
          </el-descriptions-item>
          <el-descriptions-item label="Gender">
            {{ this.user.gender }}
          </el-descriptions-item>
          <el-descriptions-item label="Date of Birth">
            {{ this.user.birthdate }}
          </el-descriptions-item>
          <el-descriptions-item label="Email">
            {{ this.user.email }}
          </el-descriptions-item>
          <el-descriptions-item label="Phone Number">
            {{ this.user.phone_number }}
          </el-descriptions-item>
          <el-descriptions-item label="NHS Number">
            {{ this.user.nhs_number }}
          </el-descriptions-item>
          <el-descriptions-item label="My Doctor">
            {{ this.doctor.given_name }}  {{ this.doctor.family_name }}
          </el-descriptions-item>
          <el-descriptions-item label="My Questionnaire">
            {{ this.questionnaire.title }}
          </el-descriptions-item>
        </el-descriptions>
      </el-dialog>
      <!-- Questionnaire Detail Drawer -->
      <el-drawer
          v-model="drawerVisible"
          title="Questionnaire Details"
          direction="rtl"
          size="30%"
      >
        <el-descriptions
            direction="vertical"
            :column="1"
            border
        >
          <el-descriptions-item label="Questionnaire Title">
            {{ this.form.questionnaire.title }}
          </el-descriptions-item>
          <el-descriptions-item label="Questions and Answers">
            <el-form label-position="top">
              <el-form-item
                  v-for="(question, index) in this.form.questionnaire.questions"
                  :key="index"
                  :label="'Question ' + (parseInt(index) + 1) + ': ' + question.question"
              >
                {{ question.answer }}
              </el-form-item>
              <el-form-item
                  :label="'Question ' + (parseInt(this.form.questionnaire.questions.length) + 1) +
                    ': As a result, do you think better, same or worse than the last time you give us a feedback?'"
              >
                {{ this.form.questionnaire.result }}
              </el-form-item>
              <div v-if="this.form.questionnaire.result === 'Worse'">
                <el-form-item
                    :label="'Question ' + (parseInt(this.form.questionnaire.questions.length) + 2) +
                        ': Do you need a meeting with your doctor?'"
                >
                  {{ this.form.questionnaire.needMeeting }}
                </el-form-item>
                <div v-if="this.form.questionnaire.needMeeting === 'Yes'">
                  <el-form-item
                      :label="'Question ' + (parseInt(this.form.questionnaire.questions.length) + 3) +
                      ': What time is good for you to have a meeting?'"
                  >
                    <div v-if="formatDate(this.form.questionnaire.meetingTime) !== '1970-01-01 01:00'">
                      {{ formatDate(this.form.questionnaire.meetingTime) }}
                    </div>
                  </el-form-item>
                </div>
              </div>
            </el-form>
          </el-descriptions-item>
        </el-descriptions>
      </el-drawer>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import { getCookie } from "@/utils/cookie";
import { formatDate } from "@/utils/date";

export default {
  name: "Center",
  data() {
    return {
      user: {},
      activeName: "first",
      tableLayout: "auto",
      clickOn: "",
      form: {},
      doctor: {},
      questionnaire: {},
      rules: {
        answers: [
          { required: true, message: 'Please enter your answer', trigger: 'blur' }
        ],
        questionnaire: {
          result: [
              { required: true, message: 'Please select a result', trigger: 'change' },
          ],
          needMeeting: [
            { required: true, message: 'Please tell us whether you need a meeting', trigger: 'change' },
          ],
          meetingTime: [
            { required: true, message: 'Please select date and time', trigger: 'change' },
          ],
        }
      },
      recordData: [],
      searchRecordInput: "",
      resultFilter: "",
      needMeetingFilter: "",
      recordCurrentPage: 1,
      recordPageSize: 10,
      recordTotal: 0,
      eventData: [],
      searchEventInput: "",
      platformFilter: "",
      attendanceFilter: "",
      eventCurrentPage: 1,
      eventPageSize: 10,
      eventTotal: 0,
      loading: false,
      fillLoading: false,
      infoDialogVisible: false,
      drawerVisible: false,
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
      this.loadDoctor()
      this.loadRecord()
      if(this.user.questionnaire !== '') {
        this.loadQuestionnaire()
      }
    },
    loadDoctor() {
      request.get("/users/" + this.user.doctor).then(res => {
        this.doctor = res.data
      })
    },
    loadQuestionnaire() {
      request.get("/questionnaires/" + this.user.questionnaire).then(res => {
        this.questionnaire = res.data
      })
    },
    loadRecord() {
      this.loading = true
      request.post("/records/get", {
        userId: this.user.id,
        userRole: this.user.role,
        searchInput: this.searchRecordInput,
        patientFilter: "",
        resultFilter: this.resultFilter,
        needMeetingFilter: this.needMeetingFilter,
        pageNum: this.recordCurrentPage,
        pageSize: this.recordPageSize
      }).then(res => {
        this.recordData = res.data.rows
        this.recordTotal = res.data.total
        this.loading = false
      })
    },
    fill() {
      if(this.user.questionnaire !== '') {
        this.fillLoading = true
        setTimeout(() => {
          this.clickOn = "fill"
          this.form = {
            creatorId: this.user.id,
            questionnaire: this.questionnaire
          }
          this.fillLoading = false
        }, 200)

      } else {
        this.$message({
          type: "error",
          message: "You haven't been assigned a questionnaire",
          customClass: "font"
        })
      }
    },
    back() {
      this.fillLoading = true
      setTimeout(() => {
        this.clickOn = ""
        this.fillLoading = false
      }, 200)
    },
    reset() {
      this.fillLoading = true
      setTimeout(() => {
        request.get("/questionnaires/" + this.user.questionnaire).then(res => {
          this.questionnaire = res.data
          this.form = {
            creatorId: this.user.id,
            questionnaire: res.data
          }
        })
        this.fillLoading = false
      }, 200)
    },
    submit() {
      this.$refs.form.validate((valid) => {
        if(valid) {
          request.post("/records", this.form).then(res => {
            if(res.code === '10000') {
              this.$message({
                type: "success",
                message: "You have created a new healthcare record",
                customClass: "font"
              })
              this.clickOn = ""
              this.loadQuestionnaire()
              this.loadRecord()
            }
          })
        } else {
          this.$message({
            type: "error",
            message: "Please enter all required information",
            customClass: "font"
          })
        }
      })
    },
    detail(row) {
      this.drawerVisible = true
      this.form = row
    },
    recordCurrentChange(pageNum) {
      this.recordCurrentPage = pageNum
      this.load()
    },
    eventCurrentChange(pageNum) {
      this.eventCurrentPage = pageNum
      this.load()
    },
    disabledDate(time) {
      return new Date(time).getTime() < Date.now() - 8.64e7
    },
    formatDate(time) {
      return formatDate(new Date(time), "yyyy-MM-dd HH:mm")
    }
  }
}
</script>

<style>
.font {
  font-family: Arial, sans-serif;
}
</style>