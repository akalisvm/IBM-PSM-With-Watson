<template xmlns="">
  <div style="font-family: Arial, sans-serif;">
    <el-card style="height: 89vh">
      <div>
        <div style="margin-bottom: 15px">
          <el-input
              v-model="searchName"
              placeholder="Type to search patient"
              clearable
              style="width: 20%"
              @keyup.enter.native="load">
            <template #append>
              <el-button @click="load">
                <el-icon><Search /></el-icon>
              </el-button>
            </template>
          </el-input>
          <el-button style="margin-left: 10px">
            <span>Remind patients to complete questionnaire</span>
          </el-button>
        </div>
        <div style="margin-top: 20px; height: 45vh">
          <el-table
              :data="patientsData"
              :table-layout="tableLayout"
              stripe
              style="width: 100%"
          >
            <el-table-column type="selection" />
            <el-table-column prop="given_name" label="Given Name"/>
            <el-table-column prop="family_name" label="Family Name" />
            <el-table-column prop="gender" label="Gender" />
            <el-table-column prop="birthdate" label="Date of Birth" sortable />
            <el-table-column prop="email" label="Email" />
            <el-table-column prop="phone_number" label="Phone Number" />
            <el-table-column prop="nhs_number" label="NHS Number" />
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
      tableLayout: "auto",
      patientsData : [],
      searchName: "",
      currentPage: 1,
      pageSize: 10,
      total: 0,
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
      request.get("/mypatients", {
        params: {
          id: this.user.id,
          searchName: this.searchName,
          pageNum: this.currentPage,
          pageSize: this.pageSize
        }
      }).then(res => {
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