<template>
  <div style="font-weight: bold;
    font-family: Arial, sans-serif;
    color: white;"
  >
    <el-menu
        style="min-height: 94vh; font-weight: bold"
        :default-active="path"
        router
        background-color="#323744"
        text-color="#ffffff"
        active-text-color="#3888e4"
        :collapse="isCollapse"
        :collapse-transition=false
        :style="{ width: isCollapse ? '64px' : '220px' }"
    >
      <el-button
          @click="toggleCollapse"
          style="min-height: 4vh; border-radius: 0" color="#323744"
          :style="{ width: isCollapse ? '63px' : '219px' }"
      >
        <el-icon v-if="!isCollapse"><Fold /></el-icon>
        <el-icon v-if="isCollapse"><Expand /></el-icon>
      </el-button>
      <el-menu-item index="/home" v-if="this.user.role==='patient'">
        <el-icon><HomeFilled /></el-icon>
        <span>My Home</span>
      </el-menu-item>
      <el-menu-item index="/dashboard" v-if="this.user.role==='doctor'">
        <el-icon><TrendCharts /></el-icon>
        <span>Dashboard</span>
      </el-menu-item>
      <el-menu-item index="/patients" v-if="this.user.role==='doctor'">
        <el-icon><UserFilled /></el-icon>
        <span>My Patients</span>
      </el-menu-item>
      <el-menu-item index="/questionnaires" v-if="this.user.role==='doctor'">
        <el-icon><List /></el-icon>
        <span>Questionnaires</span>
      </el-menu-item>
      <el-menu-item index="/records" v-if="this.user.role==='doctor'">
        <el-icon><Files /></el-icon>
        <span>Healthcare Records</span>
      </el-menu-item>
      <el-menu-item index="/events" v-if="this.user.role==='doctor'">
        <el-icon><Platform /></el-icon>
        <span>Outreach Events</span>
      </el-menu-item>
      <el-menu-item index="/test" v-if="this.user.role==='doctor'">
        <el-icon><WarnTriangleFilled /></el-icon>
        <span>Test Area</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
import { getCookie } from "@/utils/cookie"

export default {
  name: "Aside",
  data() {
    return {
      user: {},
      isCollapse: false,
      path: this.$route.path
    }
  },
  created() {
    this.user = JSON.parse(getCookie("user"))
  },
  methods: {
    toggleCollapse() {
      this.isCollapse = !this.isCollapse;
      this.$emit("getIsCollapse", this.isCollapse);
    }
  }
}
</script>