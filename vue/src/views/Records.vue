<template>
  <div style="font-family: Arial, sans-serif;">
    <el-card style="height: 89vh">
      <!-- Healthcare Records Functional Area -->
      <div>
        <el-input
            v-model="searchInput"
            placeholder="Type id to search record"
            style="width: 11%"
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
          <el-table-column fixed="right" label="Operation">
            <template #default="scope">
              <div v-if="scope.row.questionnaire.result === 'Worse'">
                <el-button  type="primary" plain size="small" @click="schedule(scope.row)">
                  Schedule
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
          <el-select v-model="scheduleForm.platform">
            <el-option label="Microsoft Teams" value="teams" />
            <el-option label="Webex" value="webex" />
            <el-option label="WhatsApp" value="whatsapp" />
            <el-option label="Phone Call" value="call" />
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
          <el-select v-model="scheduleForm.repeat">
            <el-option label="Does not repeat" value="no" />
            <el-option label="Weekly" value="weekly" />
            <el-option label="Monthly" value="monthly" />
          </el-select>
        </el-form-item>
      </el-form>
    </el-dialog>
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
      dialogVisible: false,
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
      this.loadRecord()
      this.loadRemote()
    },
    loadRecord() {
      this.loading = true
      request.post("/records/get", {
        userId: this.user.id,
        userRole: this.user.role,
        searchInput: this.searchInput,
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
        searchInput: this.patientFilter,
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
      this.loadRecord()
    },
    deleteBatch() {
      if(this.recordIdList.length === 0) {
        this.$message({
          type: "error",
          message: "Please select at least one record",
          customClass: "font"
        })
        return
      }
      request.post("/records/delete/batch", this.recordIdList).then(res => {
        if(res.code === "10000") {
          this.$message({
            type: "success",
            message: "You have deleted selected records",
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
        participantId: row.creatorId,
        participantName: row.creatorName,
        meetingTime: row.questionnaire.meetingTime
      }
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
</script>

<style>
.font {
  font-family: Arial, sans-serif;
}
</style>