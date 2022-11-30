<template>
  <div style="font-family: Arial, sans-serif;">
    <el-card>
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <span>Hi, {{ this.user.email }}</span>
        <el-button type="info" plain>Show Personal Information</el-button>
      </div>
    </el-card>
    <div><br /></div>
    <el-card style="height: 80vh;">
      <el-tabs v-model="activeName">
        <el-tab-pane label="Status" name="first">
          Status
        </el-tab-pane>
        <el-tab-pane label="Outreach Events" name="second">
          Outreach Events
        </el-tab-pane>
        <el-tab-pane label="Online Chatting" name="third">
          Online Chatting
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>

import { getCookie } from "@/utils/cookie.utils"

export default {
  name: "Personal",
  data() {
    return {
      user: {},
      activeName: 'first'
    }
  },
  created() {
    this.user = JSON.parse(getCookie("user"))
  }
}
</script>

<style scoped>

</style>