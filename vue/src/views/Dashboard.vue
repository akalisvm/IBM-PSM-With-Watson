<template>
  <div style="font-family: Arial, sans-serif;">
    <el-row :gutter="20">
      <el-col :span="18">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card style="height: 30vh; font-weight: bold">
              <template #header>
                <span>Number of Patients</span>
              </template>
              <el-row>
                <span style="font-size: 10vh; margin: auto">
                  {{ this.numberOfPatients }}
                </span>
              </el-row>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card style="height: 30vh; font-weight: bold">
              <template #header>
                <span>Number of Templates</span>
              </template>
              <el-row>
                <span style="font-size: 10vh; margin: auto">
                  {{ this.numberOfTemplates }}
                </span>
              </el-row>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card style="height: 30vh; font-weight: bold">
              <template #header>
                <span>Number of Questionnaires</span>
              </template>
              <el-row>
                <span style="font-size: 10vh; margin: auto">
                  {{ this.numberOfQuestionnaires }}
                </span>
              </el-row>
            </el-card>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-card style="margin-top: 20px; height: 57vh; font-weight: bold">
              <template #header>
                <span>Healthcare Records History</span>
              </template>
              <div v-loading="loading" class="echart" id="mychart" :style="myChartStyle" />
            </el-card>
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="6">
        <el-card style="height: 89vh">
          <template #header>
            <span style="font-weight: bold">Upcoming Events</span>
          </template>
          <ul class="infinite-list"  style="overflow: auto">
            <li v-for="i in upcomingEvents.length" :key="i" >
              <el-card shadow="hover" class="infinite-list-item">
                <span style="font-size: large; font-weight: bold">{{ this.upcomingEvents[i - 1].title }}</span>
                <br />
                <div style="height: 10px"></div>
                <span>{{ this.upcomingEvents[i - 1].platform }}</span>
                <br />
                <div style="height: 10px"></div>
                <span>{{ formatDate(this.upcomingEvents[i - 1].meetingTime) }}, {{ this.upcomingEvents[i - 1].participantName }}</span>
                <br />
                <div style="height: 10px"></div>
                <span>{{ this.upcomingEvents[i - 1].repeat }}</span>
              </el-card>
            </li>
          </ul>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import request from "@/utils/request";
import { getCookie } from "@/utils/cookie";
import { formatDate } from "@/utils/date";
import * as echarts from "echarts";

export default {
  name: "Dashboard",
  data() {
    return {
      user: {},
      numberOfPatients: 0,
      numberOfTemplates: 0,
      numberOfQuestionnaires: 0,
      upcomingEvents: [],
      loading: false,
      myChart: {},
      xData: ["Day 1", "Day 2", "Day 3", "Day 4", "Day 5", "Day 6", "Day 7"],
      yData: [0, 0, 0, 0, 0, 0, 0],
      myChartStyle: { float: "left", width: "100%", height: "50vh" }
    }
  },
  created() {
    this.user = JSON.parse(getCookie("user"))
    this.$nextTick(() => {
      this.load()
    })
  },
  watch: {
    myChart: {
      handler() {
        this.loading = false
      },
      immediate: true
    }
  },
  mounted() {
    this.loading = true
    this.initEcharts()
  },
  methods: {
    load() {
      request.get("/users/number/" + this.user.id).then(res => {
        this.numberOfPatients = res.data
      })
      request.get("/templates/number/" + this.user.id).then(res => {
        this.numberOfTemplates = res.data
      })
      request.get("/questionnaires/number/" + this.user.id).then(res => {
        this.numberOfQuestionnaires = res.data
      })
      request.get("/events/upcoming/" + this.user.id).then(res => {
        this.upcomingEvents = res.data
      })
    },
    async initEcharts() {
      await request.get("/records/history/xdata").then(res => {
        this.xData = res.data
      })
      await request.get("/records/history/ydata/" + this.user.id).then(res => {
        this.yData = res.data
      })
      const option = {
        xAxis: {
          data: this.xData
        },
        yAxis: {},
        series: [
          {
            data: this.yData,
            type: "line",
            label: {
              show: true,
              position: "top",
              fontSize: 16
            }
          }
        ]
      };
      this.myChart = echarts.init(document.getElementById("mychart"));
      this.myChart.setOption(option);
      window.addEventListener("resize", () => {
        this.myChart.resize();
      });
    },
    formatDate(time) {
      return formatDate(new Date(time), "yyyy-MM-dd HH:mm")
    }
  }
}
</script>

<style>
.infinite-list {
  padding: 0;
  margin: 0;
  list-style: none;
}
.infinite-list .infinite-list-item {
  display: flex;
  height: 15vh;
  margin-bottom: 10px;
}
</style>
