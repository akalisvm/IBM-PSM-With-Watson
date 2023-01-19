<template>
  <div style="font-family: Arial, sans-serif;">
    <el-card>
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <span>Hi, {{ this.user.given_name }}  {{ this.user.family_name }}</span>
        <el-button type="info" @click="dialogVisible = true" plain>Show personal information</el-button>
        <el-dialog v-model="dialogVisible">
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
      </div>
    </el-card>
    <div><br /></div>
    <div>
      <el-card style="height: 80vh;">
        <el-tabs v-model="activeName">
          <el-tab-pane label="Healthcare Records" name="first">
            <el-table :data="recordData" style="width: 100%">
              <el-table-column prop="id" label="ID" />
              <el-table-column prop="time" label="Time" />
              <el-table-column prop="qname" label="Questionnaire Title" />
              <el-table-column prop="result" label="Result" />
              <el-table-column prop="needMeeting" label="Need Meeting" />
              <el-table-column prop="meetingTime" label="Suggested Meeting Time" />
              <el-table-column fixed="right" label="Operation">
                <template #default>
                  <el-button link type="primary" size="small" @click="detail">
                    Detail
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="Outreach Events" name="second">
            Outreach Events
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </div>
  </div>
</template>

<script>

import { getCookie } from "@/utils/cookie.utils"
import request from "@/utils/request";

export default {
  name: "PersonalCenter",
  data() {
    return {
      user: {},
      activeName: 'first',
      doctor: {},
      questionnaire: {},
      dialogVisible: false,
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
      request.get("/user/" + this.user.doctor).then(res => {
        this.doctor = res.data
      });
      request.get("/questionnaires/" + this.user.questionnaire).then(res => {
        this.questionnaire = res.data
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