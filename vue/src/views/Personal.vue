<template>
  <div style="font-family: Arial, sans-serif;">
    <el-card>
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <span>Hi, {{ this.user.email }}</span>
        <el-button type="info" plain>Show Personal Information</el-button>
      </div>
    </el-card>
    <div><br /></div>
    <div>
      <el-card style="height: 80vh;">
        <el-tabs v-model="activeName">
          <el-tab-pane label="Healthcare Records" name="first">
            <el-table :data="healthcareData" style="width: 100%">
              <el-table-column prop="no." label="No."/>
              <el-table-column prop="time" label="Time" />
              <el-table-column prop="qname" label="Questionnaire Name" />
              <el-table-column prop="result" label="Result" />
              <el-table-column fixed="right" label="Operation">
                <template #default>
                  <el-button link type="primary" size="small" @click="handleClick">
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

export default {
  name: "Personal",
  data() {
    return {
      user: {},
      activeName: 'first',
    }
  },
  created() {
    this.user = JSON.parse(getCookie("user"))
  }
}
</script>

<style scoped>

</style>