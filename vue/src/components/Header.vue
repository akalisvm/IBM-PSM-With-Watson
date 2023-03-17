<template>
  <div style="
    display: flex;
    background-color: #24292f;
    font-family: Arial, sans-serif;
    height: 6vh
    ">
    <div style="padding-top: 15px; padding-left: 15px">
      <el-button link style="font-weight: bold; font-size: 20px; color: white;">
        IBM PSM With Watson
      </el-button>
    </div>
    <div style="flex: 1" />
    <div style="min-width: 80px; padding-top: 15px; padding-right: 15px; font-family: Arial, sans-serif">
      <el-button color="#24292f" @click="goToLogin" style="font-size: 20px; color: white" v-if="!this.login">
        Sign in
      </el-button>
      <el-button color="#24292f" @click="goToLogin" style="font-size: 20px; color: white" v-if="this.login">
        Sign out
      </el-button>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import { getCookie } from "@/utils/cookie";

export default {
  name: "Header",
  data() {
    return {
      user: {},
      sessionId: "",
      login: false
    }
  },
  created() {
    if(getCookie("user")) {
      this.user = JSON.parse(getCookie("user"))
      this.login = true
    } else {
      this.login = false
    }
    if(getCookie("sessionId")) {
      this.sessionId = getCookie("sessionId")
    }
  },
  methods: {
    goToLogin() {
      this.$router.push("/login")
      request.get("/assistant/delete/" + this.sessionId)
    }
  }
}
</script>