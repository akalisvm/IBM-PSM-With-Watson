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
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import request from "@/utils/request";
import { getCookie } from "@/utils/cookie";

export default {
  name: "Dashboard.vue",
  data() {
    return {
      user: {},
      numberOfPatients: 0,
      numberOfTemplates: 0,
      numberOfQuestionnaires: 0,
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
    }
  }
}
</script>
