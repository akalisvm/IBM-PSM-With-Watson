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
      <el-dropdown>
        <el-button link>
          {{ this.user.email }}
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item> Sign up with {{ this.user.email }} </el-dropdown-item>
            <el-dropdown-item @click="exit" divided>Logout</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>

  </div>
</template>

<script>
import request from "@/utils/request";
import { getCookie } from "@/utils/cookie.utils";
export default {
  name: "Header",
  data() {
    return {
      user: {},
      form: {},
      path: this.$route.path,
      breadList: []
    }
  },
  created() {
    this.user = JSON.parse(getCookie("user"))
    this.getMatched()
  },
  watch: {
    $route(to, form) {
      this.breadList = this.$route.matched
    }
  },
  methods: {
    getMatched() {
      this.breadList = this.$route.matched
    },
    exit() {
      this.$router.push("/login")
    }
  }
}
</script>