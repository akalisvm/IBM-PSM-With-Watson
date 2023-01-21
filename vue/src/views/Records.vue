<template>
  <div style="font-family: Arial, sans-serif;">
    <span>Healthcare Records</span>
  </div>
</template>

<script>
import request from "@/utils/request";
import { getCookie } from "@/utils/cookie";
import { formatDate } from "@/utils/date";

export default {
  name: "Records"
}
</script>

<style>
.font {
  font-family: Arial, sans-serif;
}
</style>