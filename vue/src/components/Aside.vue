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
      <el-menu-item index="/patients">
        <el-icon><UserFilled /></el-icon>
        <span>Patients</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
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
    let userStr = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(userStr)
  },
  methods: {
    toggleCollapse() {
      this.isCollapse = !this.isCollapse;
      this.$emit("getIsCollapse", this.isCollapse);
    }
  }
}
</script>