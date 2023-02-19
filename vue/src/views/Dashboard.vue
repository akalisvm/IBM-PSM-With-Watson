<template>
  <div style="font-family: Arial, sans-serif;">
    <el-row :gutter="20">
      <el-col :span="18">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card style="height: 30vh; font-weight: bold">
              <template #header>
                <span>Number of Patients</span>
              </template>
              <el-row>
                <span style="font-size: 10vh; margin: auto">
                  {{ this.numberOfPatients }}
                </span>
              </el-row>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card style="height: 30vh; font-weight: bold">
              <template #header>
                <span>Number of Templates</span>
              </template>
              <el-row>
                <span style="font-size: 10vh; margin: auto">
                  {{ this.numberOfTemplates }}
                </span>
              </el-row>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card style="height: 30vh; font-weight: bold">
              <template #header>
                <span>Number of Questionnaires</span>
              </template>
              <el-row>
                <span style="font-size: 10vh; margin: auto">
                  {{ this.numberOfQuestionnaires }}
                </span>
              </el-row>
            </el-card>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-card style="margin-top: 20px; height: 57vh; font-weight: bold">
              <template #header>
                <span>Questionnaire History</span>
              </template>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="6">
        <el-card style="height: 89vh; font-weight: bold">
          <template #header>
            <span>Upcoming Events</span>
          </template>
          <ul class="infinite-list"  style="overflow: auto">
            <li v-for="i in upcomingEvents.length" :key="i" >
              <el-card shadow="hover" class="infinite-list-item">
                {{ this.upcomingEvents[i - 1] }}
              </el-card>
            </li>
          </ul>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import request from "@/utils/request";
import { getCookie } from "@/utils/cookie";
import { formatDate } from "@/utils/date";

export default {
  name: "Dashboard.vue",
  data() {
    return {
      user: {},
      numberOfPatients: 0,
      numberOfTemplates: 0,
      numberOfQuestionnaires: 0,
      upcomingEvents: ['a', 'b', 'c', 'd', 'e']
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
      request.get("/users/number/" + this.user.id).then(res => {
        this.numberOfPatients = res.data
      })
      request.get("/templates/number/" + this.user.id).then(res => {
        this.numberOfTemplates = res.data
      })
      request.get("/questionnaires/number/" + this.user.id).then(res => {
        this.numberOfQuestionnaires = res.data
      })
    },
    formatDate(time) {
      return formatDate(new Date(time), "yyyy-MM-dd HH:mm")
    }
  }
}
</script>

<style>
.infinite-list {
  padding: 0;
  margin: 0;
  list-style: none;
}
.infinite-list .infinite-list-item {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 15vh;
  margin-bottom: 10px;
}
.infinite-list .infinite-list-item + .list-item {
  margin-top: 10px;
}
</style>
