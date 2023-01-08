<template>
  <div style="font-family: Arial, sans-serif;">
    <el-card style="height: 89vh">
      <template #header>
        <div>
          <span>My Patients</span>
        </div>
      </template>
      <div v-if="this.total !== 0">
        <el-table
            :data="patientsData"
            stripe
            style="width: 100%"
            fit
        >
          <el-table-column prop="given_name" label="Given Name" />
          <el-table-column prop="family_name" label="Family Name" />
          <el-table-column prop="gender" label="Gender"/>
          <el-table-column prop="birthdate" label="Date of Birth" sortable />
          <el-table-column prop="email" label="Email" />
          <el-table-column prop="phone_number" label="Phone Number" />
          <el-table-column prop="nhs_number" label="NHS Number" />
        </el-table>
        <div style="margin: 10px 0">
          <el-pagination
              @current-change="currentChange"
              v-model:current-page="currentPage"
              background
              layout="prev, pager, next, jumper"
              :page-size="pageSize"
              :total="total" />
        </div>
      </div>
      <div v-if="this.total === 0">
        <el-empty />
      </div>
    </el-card>
  </div>
</template>

<script>
import request from "@/utils/request";
import { getCookie } from "@/utils/cookie.utils"

export default {
  name: "Patients",
  data() {
    return {
      user: {},
      patientsData : [],
      currentPage: 1,
      pageSize: 10,
      total: 0
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
      request.get("/doctor/patients", {
        params: {
          givenName: this.user.given_name,
          familyName: this.user.family_name,
          pageNum: this.currentPage,
          pageSize: this.pageSize
        }
      }).then(res => {
        console.log(res)
        this.patientsData = res.data.records
        this.total = res.data.total
      })
    },
    currentChange(pageNum) {
      this.currentPage = pageNum
      this.load()
    }
  }
}
</script>

<style scoped>

</style>