<template>
  <div style="
    display: flex;
    background-color: #24292f;
    font-family: Arial, sans-serif;
    height: 6vh
    ">
    <div style="padding-top: 0">
      <el-button link style="font-weight: bold; font-size: 20px; color: white;">
        IBM PSM With Watson
      </el-button>
    </div>
    <div style="flex: 1" />
    <div style="min-width: 80px; padding-top: 10px; font-family: Arial, sans-serif">
      <el-dropdown v-if="this.login">
        <el-button link style="color: white;">
          {{ this.user.email }}
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item> Sign up with {{ this.user.email }} </el-dropdown-item>
            <el-dropdown-item @click="goToLogin" divided>Logout</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
      <el-button link @click="goToLogin" style="font-size: 20px; color: white" v-if="!this.login">
        Sign in
      </el-button>
    </div>
  </div>
</template>

<script>
import { getCookie } from "@/utils/cookie.utils";
export default {
  name: "Header",
  data() {
    return {
      user: {},
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
  },
  methods: {
    goToLogin() {
      this.$router.push("/login")
    }
  }
}
</script>