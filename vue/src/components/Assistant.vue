<template>
  <div class="chat">
    <beautiful-chat
        :participants="participants"
        :onMessageWasSent="onMessageWasSent"
        :messageList="messageList"
        :newMessagesCount="newMessagesCount"
        :isOpen="isChatOpen"
        :close="closeChat"
        :open="openChat"
        :showEdition="true"
        :showDeletion="true"
        :deletionConfirmation="true"
        :showTypingIndicator="showTypingIndicator"
        :showLauncher="true"
        :showCloseButton="true"
        :colors="colors"
        :alwaysScrollToBottom="alwaysScrollToBottom"
        :disableUserListToggle="false"
        :messageStyling="messageStyling"
        @onType="handleOnType"
        @edit="editMessage">
    </beautiful-chat>
  </div>
  <div class="recorder" v-if="this.recorderVisible === true">
    <el-button size="large" @click="startRecord" circle>
      <el-icon><Microphone /></el-icon>
    </el-button>
    <el-dialog v-model="dialogVisible" width="30%" draggable style="font-family: Arial, sans-serif;">
      <span v-if="this.process === 'start'">Recording audio...</span>
      <span v-if="this.process === 'submit'">Recognising the transcript...</span>
      <template #footer>
        <el-button v-if="this.process === 'start'" @click="submitRecord">Submit</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";
import { getCookie } from "@/utils/cookie";
import Recorder from 'js-audio-recorder';

export default {
  name: 'Assistant',
  data() {
    return {
      user: {},
      sessionId: '',
      recorderVisible: false,
      loading: false,
      dialogVisible: false,
      process: '',
      recorder: new Recorder({
        sampleBits: 16,
        sampleRate: 16000,
        numChannels: 1,
      }),
      participants: [
        {
          id: 'assistant',
          name: 'Watson Assistant',
          imageUrl: 'https://cache.globalcatalog.cloud.ibm.com/api/v1/7045626d-55e3-4418-be11-683a26dbc1e5/artifacts/cache/5c18a362b3f855bff471377ff580f369-public/WatsonAssistant.svg'
        }
      ], // the list of all the participant of the conversation. `name` is the username, `id` is used to establish the author of a message, `imageUrl` is supposed to be the user avatar.
      messageList: [], // the list of the messages to show, can be paginated and adjusted dynamically
      newMessagesCount: 0,
      isChatOpen: false, // to determine whether the chat window should be open or closed
      showTypingIndicator: '', // when set to a value matching the participant.id it shows the typing indicator for the specific user
      colors: {
        header: {
          bg: '#4e8cff',
          text: '#ffffff'
        },
        launcher: {
          bg: '#4e8cff'
        },
        messageList: {
          bg: '#ffffff'
        },
        sentMessage: {
          bg: '#4e8cff',
          text: '#ffffff'
        },
        receivedMessage: {
          bg: '#eaeaea',
          text: '#222222'
        },
        userInput: {
          bg: '#f4f7f9',
          text: '#565867'
        }
      }, // specifies the color scheme for the component
      alwaysScrollToBottom: false, // when set to true always scrolls the chat to the bottom when new events are in (new message, user starts typing...)
      messageStyling: true // enables *bold* /emph/ _underline_ and such (more info at github.com/mattezza/msgdown)
    }
  },
  created() {
    this.user = JSON.parse(getCookie("user"))
    this.sessionId = getCookie("sessionId")
  },
  methods: {
    onMessageWasSent (message) {
      // called when the user sends a message
      this.messageList = [ ...this.messageList, message ]
      request.post("/assistant/message", {
        sessionId: this.sessionId,
        author: this.user.given_name + ' ' + this.user.family_name,
        text: message.data.text
      }).then(res => {
        this.messageList.push({ type: 'text', author: `assistant`, data: { text: res.data } })
      })
    },
    openChat () {
      // called when the user clicks on the fab button to open the chat
      this.isChatOpen = true
      this.newMessagesCount = 0
      this.recorderVisible = true
    },
    closeChat () {
      // called when the user clicks on the button to close the chat
      this.isChatOpen = false
      this.recorderVisible = false
    },
    handleOnType () {
      // console.log('Emit typing event')
    },
    editMessage(message){
      const m = this.messageList.find(m=>m.id === message.id);
      m.isEdited = true;
      m.data.text = message.data.text;
    },
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
    getTranscript() {
      let wavBlob = this.recorder.getWAVBlob();
      let formData = new FormData()
      const newBlob = new Blob([wavBlob], { type: 'audio/wav' })
      const fileOfBlob = new File([newBlob],'audio-file.wav')
      formData.append('file', fileOfBlob)
      this.$axios.post("http://localhost:9090/stt/transcript", formData).then(res => {
        this.dialogVisible = false
        this.onMessageWasSent({ type: "text", author: "me", data: { text: res.data.data } })
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

<style>
.chat {
  position: relative;
  z-index: 9998;
}
.recorder {
  position: fixed;
  bottom: 110px;
  right: 400px;
  z-index: 9999;
}
</style>