<template>
  <el-button @click="startRecord">Start Recording</el-button>
  <el-button @click="stopRecord">Stop Recording</el-button>
  <el-button @click="playRecord">Play Recording</el-button>
  <el-button @click="downloadWAVRecord">Download WAV Record</el-button>
  <el-button v-loading="loading" @click="getTranscript">Get Transcript</el-button>
</template>

<script>
import Recorder from 'js-audio-recorder'

export default {
  name: "Recorder.vue",
  data() {
    return {
      loading: false,
      recorder: new Recorder({
        sampleBits: 16,
        sampleRate: 16000,
        numChannels: 1,
      }),
    };
  },
  methods: {
    startRecord() {
      Recorder.getPermission().then(() => {
            this.recorder.start();
          },
          (error) => {
            this.$message({
              message: "Please allow the website to access your microphone.",
              type: "info",
            });
            console.log(`${error.name} : ${error.message}`);
          }
      );
    },
    stopRecord() {
      this.recorder.stop();
    },
    playRecord() {
      this.recorder && this.recorder.play()
    },
    downloadWAVRecord() {
      this.recorder.downloadWAV("audio-file");
    },
    getTranscript() {
      this.loading = true
      let wavBlob = this.recorder.getWAVBlob();
      let formData = new FormData()
      const newBlob = new Blob([wavBlob], { type: 'audio/wav' })
      const fileOfBlob = new File([newBlob],'audio-file.wav')
      formData.append('file', fileOfBlob)
      this.$axios.post("http://localhost:9090/stt/transcript", formData).then(res => {
        this.loading = false
        alert(res.data.data)
      })
    },
  }
}
</script>
