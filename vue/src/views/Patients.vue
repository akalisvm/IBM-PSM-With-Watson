<template>
  <div style="font-family: Arial, sans-serif;">
    <el-card style="height: 89vh">
      <template #header>
        <div>
          <span>Healthcare Records</span>
        </div>
      </template>
      <el-empty />
    </el-card>
  </div>
</template>

<script>

import { getCookie } from "@/utils/cookie.utils"

export default {
  name: "Patients",
  data() {
    return {
      user: {},
    }
  },
  created() {
    this.user = JSON.parse(getCookie("user"))
  }
}
</script>

<style scoped>

</style>