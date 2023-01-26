<template>
  <div style="font-family: Arial, sans-serif;">
    <el-card style="height: 89vh">
      <!-- Outreach Events Functional Area -->
      <div>
        <el-input
            v-model="searchInput"
            placeholder="Type id or title to search event"
            style="width: 20%"
            clearable
            @keyup.enter.native="loadEvent"
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
            v-model="platformFilter"
            placeholder="Select platform"
            clearable
            style="margin-left: 10px; width: 10%"
        >
          <el-option label="Microsoft Teams" value="Microsoft Teams" />
          <el-option label="Webex" value="Webex" />
          <el-option label="WhatsApp" value="WhatsApp" />
          <el-option label="Phone Call" value="Phone Call" />
        </el-select>
        <el-select
            v-model="resultFilter"
            placeholder="Select result"
            clearable
            style="margin-left: 10px; width: 10%"
        >
          <el-option label="Pending" value="Pending" />
          <el-option label="Success" value="Success" />
          <el-option label="Fail" value="Fail" />
        </el-select>
        <el-button type="info" plain @click="loadEvent" style="margin-left: 10px;">
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
      <!-- Outreach Events Table Area -->
      <div v-loading="loading" style="margin-top: 20px; min-height: 46vh">
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
          <el-table-column prop="participantName" label="Participant Name" />
          <el-table-column prop="title" label="Title">
            <template #default="scope">
              <el-button link @click="detail(scope.row)">
                {{ scope.row.title }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column prop="platform" label="Platform" />
          <el-table-column prop="meetingTime" label="Scheduled Meeting Time" sortable>
            <template #default="scope">
              {{ formatDate(scope.row.meetingTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="result" label="Result">
            <template #default="scope">
              <el-tag v-if="scope.row.result === 'Pending'" type="info" light>Pending</el-tag>
              <el-tag v-if="scope.row.result === 'Success'" type="success" light>Success</el-tag>
              <el-tag v-if="scope.row.result === 'Fail'" type="danger" light>Fail</el-tag>
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="Operation">
            <template #default="scope">
              <el-button :disabled="scope.row.result !== 'Pending'" plain size="small" @click="reschedule(scope.row)">
                Reschedule Event
              </el-button>
              <el-button type="primary" plain size="small" @click="give(scope.row)">
                Give Feedback
              </el-button>
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
    <!-- Outreach Event Details Drawer -->
    <el-drawer
        v-model="drawerVisible"
        title="Outreach Event Details"
        direction="rtl"
        size="30%"
    >
      <el-descriptions
          direction="vertical"
          :column="1"
          border
      >
        <el-descriptions-item label="Organiser">
          {{ this.eventForm.organiserName }}
        </el-descriptions-item>
        <el-descriptions-item label="Participant">
          {{ this.eventForm.participantName }}
        </el-descriptions-item>
        <el-descriptions-item label="Title">
          {{ this.eventForm.title }}
        </el-descriptions-item>
        <el-descriptions-item label="Description">
          {{ this.eventForm.description }}
        </el-descriptions-item>
        <el-descriptions-item label="Platform">
          {{ this.eventForm.platform }}
        </el-descriptions-item>
        <el-descriptions-item label="Scheduled Meeting Time">
          {{ formatDate(this.eventForm.meetingTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="Last Meeting Time">
          <span v-if="formatDate(this.eventForm.lastMeetingTime) !== '1970-01-01 01:00'">
            {{ formatDate(this.eventForm.lastMeetingTime) }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label="Repeat">
          {{ this.eventForm.repeat }}
        </el-descriptions-item>
        <el-descriptions-item label="Last Successful Meeting Time">
          <span v-if="formatDate(this.eventForm.lastSuccessfulMeetingTime) !== '1970-01-01 01:00'">
            {{ formatDate(this.eventForm.lastSuccessfulMeetingTime) }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label="Next Meeting Time">
          <span v-if="formatDate(this.eventForm.nextMeetingTime) !== '1970-01-01 01:00'">
            {{ formatDate(this.eventForm.nextMeetingTime) }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label="Result">
          <el-tag v-if="this.eventForm.result === 'Pending'" type="info" light>Pending</el-tag>
          <el-tag v-if="this.eventForm.result === 'Success'" type="success" light>Success</el-tag>
          <el-tag v-if="this.eventForm.result === 'Fail'" type="danger" light>Fail</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="Feedback">
          {{ this.eventForm.feedback }}
        </el-descriptions-item>
      </el-descriptions>
    </el-drawer>
    <!-- Reschedule Outreach Event Dialog -->
    <el-dialog v-model="rescheduleDialogVisible" width="40%" align-center>
      <template #header>
        <span v-if="this.clickOn === 'reschedule'">Reschedule Outreach Event</span>
        <span v-if="this.clickOn === 'write'">Write Feedback</span>
      </template>
      <el-form ref="eventForm" :model="eventForm" :rules="rules" label-width="120px">
        <el-form-item label="Organiser">
          {{ eventForm.organiserName }}
        </el-form-item>
        <el-form-item label="Participant">
          {{ eventForm.participantName }}
        </el-form-item>
        <el-form-item prop="title" label="Title">
          <el-input v-model="eventForm.title" />
        </el-form-item>
        <el-form-item prop="description" label="Description">
          <el-input v-model="eventForm.description" type="textarea" autosize />
        </el-form-item>
        <el-form-item prop="platform" label="Platform">
          <el-select v-model="eventForm.platform" clearable>
            <el-option label="Microsoft Teams" value="Microsoft Teams" />
            <el-option label="Webex" value="Webex" />
            <el-option label="WhatsApp" value="WhatsApp" />
            <el-option label="Phone Call" value="Phone Call" />
          </el-select>
        </el-form-item>
        <el-form-item prop="meetingTime" label="Meeting Time">
          <el-date-picker
              v-model="eventForm.meetingTime"
              type="datetime"
              placeholder="Select date and time"
              format="YYYY-MM-DD HH:mm"
              :disabled-date="disabledDate"
          />
        </el-form-item>
        <el-form-item prop="repeat" label="Repeat">
          <el-select v-model="eventForm.repeat" clearable>
            <el-option label="Does not repeat" value="Does not repeat" />
            <el-option label="Weekly" value="Weekly" />
            <el-option label="Monthly" value="Monthly" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="submit">Submit</el-button>
      </template>
    </el-dialog>
    <!-- Give Result Dialog -->
    <el-dialog v-model="giveDialogVisible" width="40%" align-center>
      <template #header>
        Give Feedback
      </template>
      <el-form ref="eventForm" :model="eventForm" :rules="rules" label-width="120px">
        <el-form-item label="Organiser">
          {{ eventForm.organiserName }}
        </el-form-item>
        <el-form-item label="Participant">
          {{ eventForm.participantName }}
        </el-form-item>
        <el-form-item prop="result" label="Result">
          <el-select v-model="eventForm.result" clearable>
            <el-option label="Pending" value="Pending" />
            <el-option label="Success" value="Success" />
            <el-option label="Fail" value="Fail" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="eventForm.result !== 'Pending'" prop="feedback" label="Feedback">
          <el-input v-model="eventForm.feedback" type="textarea"/>
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
  name: "Events",
  data() {
    return {
      user: {},
      tableLayout: "auto",
      eventForm: {},
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
        ],
        result: [
          { required: true, message: 'Please select a event result', trigger: 'change' },
        ]
      },
      data:[],
      searchInput: "",
      patientFilter: "",
      platformFilter: "",
      resultFilter: "",
      currentPage: 1,
      pageSize: 10,
      total: 0,
      eventIdList: [],
      remoteList: [],
      options: [],
      clickOn: "",
      loading: false,
      drawerVisible: false,
      rescheduleDialogVisible: false,
      giveDialogVisible: false
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
      this.loadEvent()
      this.loadRemote()
    },
    loadEvent() {
      this.loading = true
      request.post("/events/get", {
        userId: this.user.id,
        userRole: this.user.role,
        searchInput: this.searchInput.trim().toLowerCase(),
        patientFilter: this.patientFilter,
        platformFilter: this.platformFilter,
        resultFilter: this.resultFilter,
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
    reset() {
      this.searchInput = ""
      this.patientFilter = ""
      this.platformFilter = ""
      this.resultFilter = ""
      this.loadEvent()
    },
    deleteBatch() {
      if(this.eventIdList.length === 0) {
        this.$message({
          type: "error",
          message: "Please select at least one event.",
          customClass: "font"
        })
        return
      }
      request.post("/events/delete/batch", this.eventIdList).then(res => {
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
    detail(row) {
      this.drawerVisible = true
      this.eventForm = JSON.parse(JSON.stringify(row))
    },
    reschedule(row) {
      this.clickOn = "reschedule"
      this.rescheduleDialogVisible = true
      this.eventForm = JSON.parse(JSON.stringify(row))
    },
    give(row) {
      this.clickOn = "give"
      this.giveDialogVisible = true
      this.eventForm = JSON.parse(JSON.stringify(row))
    },
    submit() {
      this.$refs.eventForm.validate((valid) => {
        if(valid) {
          if(this.clickOn === "reschedule") {
            request.put("/events", this.eventForm).then(res => {
              if(res.code === '10000') {
                this.$message({
                  type: "success",
                  message: "You have rescheduled a event.",
                  customClass: "font"
                })
                this.rescheduleDialogVisible = false
                this.loadEvent()
              }
            })
          } else if(this.clickOn === "give") {
            request.put("/events", this.eventForm).then(res => {
              if(res.code === '10000') {
                this.$message({
                  type: "success",
                  message: "You have given a feedback.",
                  customClass: "font"
                })
                this.giveDialogVisible = false
                this.loadEvent()
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
    currentChange(pageNum) {
      this.currentPage = pageNum
      this.load()
    },
    handleSelectionChange(val) {
      this.eventIdList = val.map(v => v.id)
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