<template>
  <div style="
    display: flex;
    background-color: #24292f;
    font-family: Arial, sans-serif;
    height: 12vh
    ">
    <div style="padding-top: 0">
      <el-button type="text" style="font-size: 15px; color: white;">
        IBM PSM With Watson
      </el-button>
    </div>
    <div style="flex: 1" />
    <div>
      <el-button type="text" link href="/login">
        Sign up
      </el-button>
    </div>

  </div>
</template>

<script>
import request from "@/utils/request";
import { getCookie } from "@/utils/cookie.utils";
export default {
  name: "HeaderOuter",
  data() {
    return {
      user: {},
      form: {},
    }
  },
  methods: {
  }
}
</script>