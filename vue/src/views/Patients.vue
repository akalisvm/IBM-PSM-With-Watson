<template>
  <div>
    <h1>{{ this.user.email }}</h1>
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