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
          <el-option label="Microsoft Teams" value="teams" />
          <el-option label="Webex" value="webex" />
          <el-option label="WhatsApp" value="whatsapp" />
          <el-option label="Phone Call" value="call" />
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
        >
          <el-table-column prop="id" label="ID" />
          <el-table-column prop="createTime" label="Create Time" />
          <el-table-column prop="title" label="Title" />
          <el-table-column prop="platform" label="Platform" />
          <el-table-column prop="meetingTime" label="Scheduled Meeting Time" sortable />
          <el-table-column fixed="right" label="Operation">
            <template #default="scope">
              <div>
                <el-button v-if="scope.row.feedback === ''" type="primary" plain size="small" @click="write(scope.row.id)">
                  Write Feedback
                </el-button>
                <el-button v-if="scope.row.feedback !== ''" type="primary" plain size="small" @click="edit(scope.row.id)">
                  Edit Feedback
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
      data:[],
      searchInput: "",
      patientFilter: "",
      platformFilter: "",
      resultFilter: "",
      currentPage: 1,
      pageSize: 10,
      total: 0,
      options: [],
      loading: false,
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

    },
    loadEvent() {

    },
    remote() {

    },
    reset() {

    },
    deleteBatch() {

    },
    currentChange(pageNum) {
      this.currentPage = pageNum
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