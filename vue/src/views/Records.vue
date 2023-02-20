<template>
  <div style="font-family: Arial, sans-serif;">
    <el-card style="height: 89vh">
      <!-- Healthcare Records Functional Area -->
      <div>
        <el-input
            v-model="searchInput"
            placeholder="Type id or questionnaire title to search record"
            style="width: 20%"
            clearable
            @keyup.enter.native="loadRecord"
        />
        <el-select
            v-model="patientFilter"
            placeholder="Select patient name"
            clearable
            filterable
            remote
            :reserve-keyword="false"
            :remote-method="remote"
            :loading="loading"
            style="margin-left: 10px; width: 10%"
        >
          <el-option
              v-for="patient in options"
              :key="patient.id"
              :label="patient.given_name + ' ' + patient.family_name"
              :value="patient.id"
          />
        </el-select>
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
            placeholder="Select need meeting"
            clearable
            style="margin-left: 10px; width: 11%">
          <el-option label="Yes" value="Yes" />
          <el-option label="No" value="No" />
        </el-select>
        <el-button type="info" plain @click="loadRecord" style="margin-left: 10px;">
          <el-icon><Search /></el-icon>
        </el-button>
        <el-button style="margin-left: 10px" @click="reset">
          Reset
        </el-button>
        <el-popconfirm title="Are you sure?" @confirm="deleteBatch">
          <template #reference>
            <el-button type="danger" style="margin-left: 10px">
              Delete
            </el-button>
          </template>
        </el-popconfirm>
      </div>
      <!-- Healthcare Records Table Area -->
      <div v-loading="loading" style="margin-top: 20px; min-height: 47vh">
        <el-table
            :data="data"
            style="width: 100%"
            :table-layout="tableLayout"
            @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" />
          <el-table-column prop="id" label="ID" />
          <el-table-column prop="createTime" label="Create Time">
            <template #default="scope">
              {{ formatDate(scope.row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="creatorName" label="Patient Name" />
          <el-table-column prop="questionnaire.title" label="Questionnaire Title" show-overflow-tooltip>
            <template #default="scope">
              <el-button link @click="detail(scope.row)">
                {{ scope.row.questionnaire.title }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column prop="questionnaire.result" label="Result">
            <template #default="scope">
              <el-tag :type="scope.row.questionnaire.result === 'Worse' ? 'danger' : 'success'" light>
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
          <el-table-column fixed="right" label="Operation">
            <template #default="scope">
              <div v-if="scope.row.questionnaire.result === 'Worse'">
                <el-button  type="primary" plain size="small" @click="schedule(scope.row)">
                  Schedule Event
                </el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div style="margin: 10px 0">
        <el-pagination
            @current-change="currentChange"
            v-model:current-page="currentPage"
            background
            layout="prev, pager, next, jumper"
            :page-size="pageSize"
            :total="total" />
      </div>
    </el-card>
    <!-- Questionnaire Details Drawer -->
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
        <el-descriptions-item label="Title">
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
    <!-- Schedule Outreach Event Dialog -->
    <el-dialog v-model="dialogVisible" width="40%" align-center>
      <template #header>
        Schedule Outreach Event
      </template>
      <el-form ref="scheduleForm" :model="scheduleForm" :rules="rules" label-width="120px">
        <el-form-item label="Organiser">
          {{ this.user.given_name }} {{ this.user.given_name }}
        </el-form-item>
        <el-form-item label="Participant">
          {{ scheduleForm.participantName }}
        </el-form-item>
        <el-form-item prop="title" label="Title">
          <el-input v-model="scheduleForm.title" />
        </el-form-item>
        <el-form-item prop="description" label="Description">
          <el-input v-model="scheduleForm.description" type="textarea" autosize />
        </el-form-item>
        <el-form-item prop="platform" label="Platform">
          <el-select v-model="scheduleForm.platform" clearable>
            <el-option label="Microsoft Teams" value="Microsoft Teams" />
            <el-option label="Webex" value="Webex" />
            <el-option label="WhatsApp" value="WhatsApp" />
            <el-option label="Phone Call" value="Phone Call" />
          </el-select>
        </el-form-item>
        <el-form-item prop="meetingTime" label="Meeting Time">
          <el-date-picker
              v-model="scheduleForm.meetingTime"
              type="datetime"
              placeholder="Select date and time"
              format="YYYY-MM-DD HH:mm"
              :disabled-date="disabledDate"
          />
        </el-form-item>
        <el-form-item prop="repeat" label="Repeat">
          <el-select v-model="scheduleForm.repeat" clearable>
            <el-option-group>
              <el-option label="Does not repeat" value="Does not repeat" />
            </el-option-group>
            <el-option-group label="Repeat weekly">
              <el-option label="Every 1 week" value="Every 1 week" />
              <el-option label="Every 2 weeks" value="Every 2 weeks" />
              <el-option label="Every 3 weeks" value="Every 3 weeks" />
              <el-option label="Every 4 weeks" value="Every 4 weeks" />
              <el-option label="Every 5 weeks" value="Every 5 weeks" />
              <el-option label="Every 6 weeks" value="Every 6 weeks" />
              <el-option label="Every 7 weeks" value="Every 7 weeks" />
              <el-option label="Every 8 weeks" value="Every 8 weeks" />
              <el-option label="Every 9 weeks" value="Every 9 weeks" />
              <el-option label="Every 10 weeks" value="Every 10 weeks" />
              <el-option label="Every 11 weeks" value="Every 11 weeks" />
              <el-option label="Every 12 weeks" value="Every 12 weeks" />
            </el-option-group>
            <el-option-group label="Repeat monthly">
              <el-option label="Every 1 month" value="Every 1 month" />
              <el-option label="Every 2 months" value="Every 2 months" />
              <el-option label="Every 3 months" value="Every 3 months" />
            </el-option-group>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submit">Submit</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";
import { getCookie } from "@/utils/cookie";
import { formatDate } from "@/utils/date";

export default {
  name: "Records",
  data() {
    return {
      user: {},
      tableLayout: "auto",
      form: {},
      scheduleForm: {},
      rules: {
        title: [
          { required: true, message: 'Please enter the title', trigger: 'blur' }
        ],
        platform: [
          { required: true, message: 'Please select a platform', trigger: 'change' },
        ],
        meetingTime: [
          { required: true, message: 'Please select date and time', trigger: 'change' },
        ],
        repeat: [
          { required: true, message: 'Please select a repeat plan', trigger: 'change' },
        ]
      },
      data: [],
      recordIdList: [],
      remoteList: [],
      options: [],
      searchInput: "",
      patientFilter: "",
      resultFilter: "",
      needMeetingFilter: "",
      currentPage: 1,
      pageSize: 10,
      total: 0,
      loading: false,
      remoteLoading: false,
      drawerVisible: false,
      dialogVisible: false,
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
      this.loadRecord()
      this.loadRemote()
    },
    loadRecord() {
      this.loading = true
      request.post("/records/get", {
        userId: this.user.id,
        userRole: this.user.role,
        searchInput: this.searchInput.trim().toLowerCase(),
        patientFilter: this.patientFilter,
        resultFilter: this.resultFilter,
        needMeetingFilter: this.needMeetingFilter,
        pageNum: this.currentPage,
        pageSize: this.pageSize
      }).then(res => {
        this.data = res.data.rows
        this.total = res.data.total
        this.loading = false
      })
    },
    loadRemote() {
      request.post("/users/get", {
        doctorId: this.user.id,
        searchInput: this.patientFilter.trim().toLowerCase(),
        patientFilter: "",
        resultFilter: this.resultFilter,
        needMeetingFilter: this.needMeetingFilter,
        pageNum: 1,
        pageSize: 0
      }).then(res => {
        this.remoteList = res.data.rows
      })
    },
    remote(query) {
      if(query !== '') {
        this.remoteLoading = true
        setTimeout(() => {
          this.remoteLoading = false
          this.options = this.remoteList.filter(patient => {
            const fullname = patient.given_name + " " + patient.family_name
            return fullname.toLowerCase().indexOf(query.toLowerCase()) > -1
          })
        }, 200)
      } else {
        this.options = []
      }
    },
    detail(row) {
      this.drawerVisible = true
      this.form = row
    },
    reset() {
      this.searchInput = ""
      this.patientFilter = ""
      this.resultFilter = ""
      this.needMeetingFilter = ""
      this.currentPage = 1
      this.loadRecord()
    },
    deleteBatch() {
      if(this.recordIdList.length === 0) {
        this.$message({
          type: "error",
          message: "Please select at least one record.",
          customClass: "font"
        })
        return
      }
      request.post("/records/delete/batch", this.recordIdList).then(res => {
        if(res.code === "10000") {
          this.$message({
            type: "success",
            message: "You have deleted selected records.",
            customClass: "font"
          })
          this.load()
        }
      })
    },
    schedule(row) {
      this.dialogVisible = true
      this.scheduleForm = {
        organiserId: this.user.id,
        organiserName: this.user.given_name + " " + this.user.family_name,
        participantId: row.creatorId,
        participantName: row.creatorName,
        title: "",
        description: "",
        platform: "",
        meetingTime: row.questionnaire.meetingTime,
        repeat: "Does not repeat"
      }
    },
    submit() {
      this.$refs.scheduleForm.validate((valid) => {
        if(valid) {
          request.post("/events", this.scheduleForm).then(res => {
            if(res.code === '10000') {
              this.$message({
                type: "success",
                message: "You have scheduled a new event.",
                customClass: "font"
              })
              this.clickOn = ""
              this.dialogVisible = false
            } else {
              console.log(res.msg)
              this.$message({
                type: "error",
                message: res.msg,
                customClass: "font"
              })
              this.clickOn = ""
              this.dialogVisible = false
            }
          })
        } else {
          this.$message({
            type: "error",
            message: "Please enter all required information and try again.",
            customClass: "font"
          })
        }
      })
    },
    currentChange(pageNum) {
      this.currentPage = pageNum
      this.load()
    },
    handleSelectionChange(val) {
      this.recordIdList = val.map(v => v.id)
    },
    disabledDate(time) {
      return new Date(time).getTime() < Date.now() - 8.64e7
    },
    formatDate(time) {
      return formatDate(new Date(time), "yyyy-MM-dd HH:mm")
    }
  }
}

window.watsonAssistantChatOptions = {
  integrationID: "3d6702df-1c29-43dd-acbf-ca05b8c5d99c", // The ID of this integration.
  region: "eu-gb", // The region your integration is hosted in.
  serviceInstanceID: "3074a022-733c-4ccf-8389-a74bdcdb89a3", // The ID of your service instance.
  onLoad: function(instance) { instance.render(); }
};
setTimeout(function(){
  const t=document.createElement('script');
  t.src="https://web-chat.global.assistant.watson.appdomain.cloud/versions/" + (window.watsonAssistantChatOptions.clientVersion || 'latest') + "/WatsonAssistantChatEntry.js";
  document.head.appendChild(t);
});
</script>

<style>
.font {
  font-family: Arial, sans-serif;
}
</style>