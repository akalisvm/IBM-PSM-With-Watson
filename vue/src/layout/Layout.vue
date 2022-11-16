<template>
  <div class="common-layout">
    <el-container>
      <el-header style="padding: 0; height: 6vh">
        <Header/>
      </el-header>
      <el-container style="padding: 0">
        <el-aside>
          <Aside/>
        </el-aside>
        <el-main>
          <router-view/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style>
</style>

<script>
import Aside from "@/components/Aside";
import Header from "@/components/Header";
export default {
  name: "Layout",
  components: {
    Aside,
    Header
  }
}
</script>