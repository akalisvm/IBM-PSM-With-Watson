<template xmlns="">
  <div style="font-family: Arial, sans-serif;">
    <el-card style="height: 89vh">
      <div>
        <div>
          <el-input
              v-model="searchInput"
              placeholder="Type name to search patient"
              style="width: 20%"
              @keyup.enter.native="load">
            <template #append>
              <el-button @click="load">
                <el-icon><Search /></el-icon>
              </el-button>
            </template>
          </el-input>
          <el-button style="margin-left: 10px" @click="this.searchInput = ''; this.load()">
            <span>Reset</span>
          </el-button>
          <el-button style="margin-left: 10px" @click="assign">Assign Questionnaire</el-button>
          <el-dialog v-model="dialogVisible" title="Assign Questionnaire To Patients" width="40%" align-center>
            <el-select
                v-model="questionnaireId"
                style="width: 100%;"
                placeholder="Select a questionnaire"
                clearable
                @blur="this.questionnaireId === ''"
            >
              <el-option
                  v-for="item in options"
                  :key="item.id"
                  :label="item.id + ': ' + item.title"
                  :value="item.id"
              />
            </el-select>
            <template #footer>
              <el-button @click="dialogVisible = false">Cancel</el-button>
              <el-button type="primary" @click="save">Save</el-button>
            </template>
          </el-dialog>
          <el-button style="margin-left: 10px">Remind Patients</el-button>
        </div>
        <div style="margin-top: 20px; height: 45vh">
          <el-table
              :data="data"
              :table-layout="tableLayout"
              stripe
              style="width: 100%"
              @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" />
            <el-table-column prop="given_name" label="Given Name" />
            <el-table-column prop="family_name" label="Family Name" />
            <el-table-column prop="gender" label="Gender" />
            <el-table-column prop="birthdate" label="Date of Birth" sortable />
            <el-table-column prop="email" label="Email" />
            <el-table-column prop="phone_number" label="Phone Number" />
            <el-table-column prop="nhs_number" label="NHS Number" />
            <el-table-column prop="questionnaire" label="Questionnaire" />
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
import request from "@/utils/request"
import { getCookie } from "@/utils/cookie.utils"

export default {
  name: "Patients",
  data() {
    return {
      user: {},
      tableLayout: "auto",
      data : [],
      searchInput: "",
      currentPage: 1,
      pageSize: 10,
      total: 0,
      dialogVisible: false,
      questionnaireId: "",
      options: [],
      patientIds: [],
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
      request.get("/patients", {
        params: {
          doctorId: this.user.id,
          searchInput: this.searchInput,
          pageNum: this.currentPage,
          pageSize: this.pageSize
        }
      }).then(res => {
        this.data = res.data.records
        this.total = res.data.total
      });
      request.get("/questionnaires", {
        params: {
          doctorId: this.user.id,
          searchInput: "",
          pageNum: 1,
          pageSize: 0
        }
      }).then(res => {
        this.options = res.data.records
      })
    },
    assign() {
      if(this.patientIds.length === 0) {
        this.$message({
          type: "error",
          message: "Please select at least one patient",
          customClass: 'font'
        })
        return
      }
      this.dialogVisible = true
    },
    save() {
      if(this.questionnaireId === '') {
        this.$message({
          type: "error",
          message: "Please select a questionnaire",
          customClass: 'font'
        })
        return
      }
      request.post("/questionnaires/assign/" + this.questionnaireId, this.patientIds).then(res => {
        if(res.code === '10000') {
          this.load()
        }
        this.dialogVisible = false
      })
    },
    handleSelectionChange(val) {
      this.patientIds = val.map(v => v.id)
    },
    currentChange(pageNum) {
      this.currentPage = pageNum
      this.load()
    }
  }
}
</script>

<style>
.font {
  font-family: Arial, sans-serif;
}
</style>