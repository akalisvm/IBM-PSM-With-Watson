<template>
  <el-button @click="startRecord" circle>
    <el-icon><Microphone /></el-icon>
  </el-button>
  <el-dialog v-model="dialogVisible" width="30%" draggable style="font-family: Arial, sans-serif;">
    <span v-if="this.process === 'start'">Recording audio...</span>
    <span v-if="this.process === 'submit'">Recognising the transcript...</span>
    <span v-if="this.process === 'recognised'">{{ this.transcript }}</span>
    <template #footer>
      <el-button v-if="this.process === 'start'" @click="submitRecord">Submit</el-button>
      <el-button v-if="this.process === 'recognised'" type="primary" @click="dialogVisible = false; this.process === ''">
        Confirm
      </el-button>
    </template>
  </el-dialog>
</template>

<script>
import Recorder from 'js-audio-recorder';

export default {
  name: "Recorder",
  data() {
    return {
      loading: false,
      dialogVisible: false,
      process: '',
      transcript: '',
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
            this.process = 'start'
            this.dialogVisible = true
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
      let wavBlob = this.recorder.getWAVBlob();
      let formData = new FormData()
      const newBlob = new Blob([wavBlob], { type: 'audio/wav' })
      const fileOfBlob = new File([newBlob],'audio-file.wav')
      formData.append('file', fileOfBlob)
      this.$axios.post("http://localhost:9090/stt/transcript", formData).then(res => {
        this.transcript = res.data.data
        this.process = 'recognised'
      })
    },
    submitRecord() {
      this.process = 'submit'
      this.stopRecord()
      this.getTranscript()
    }
  }
}
</script>
