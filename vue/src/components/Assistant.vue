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
  <div class="microphone">
    <Recorder v-if="this.microphoneVisible === true" />
  </div>
</template>

<script>
import request from "@/utils/request";
import Recorder from "@/components/Recorder";
import { getCookie } from "@/utils/cookie";

export default {
  name: 'Assistant',
  components: {
    Recorder
  },
  data() {
    return {
      user: {},
      sessionId: '',
      microphoneVisible: false,
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
    sendMessage (text) {
      if (text.length > 0) {
        this.newMessagesCount = this.isChatOpen ? this.newMessagesCount : this.newMessagesCount + 1
        this.onMessageWasSent({ author: 'assistant', type: 'text', data: { text } })
      }
    },
    onMessageWasSent (message) {
      // called when the user sends a message
      this.messageList = [ ...this.messageList, message ]
      request.post("/assistant/message", {
        sessionId: this.sessionId,
        sender: this.user.given_name + ' ' + this.user.family_name,
        text: message.data.text
      }).then(res => {
        this.messageList.push({ type: 'text', author: `assistant`, data: { text: res.data } })
      })
    },
    openChat () {
      // called when the user clicks on the fab button to open the chat
      this.isChatOpen = true
      this.newMessagesCount = 0
      this.microphoneVisible = true
    },
    closeChat () {
      // called when the user clicks on the button to close the chat
      this.isChatOpen = false
      this.microphoneVisible = false
    },
    handleScrollToTop () {
      // called when the user scrolls message list to top
      // leverage pagination for loading another page of messages
    },
    handleOnType () {
      // console.log('Emit typing event')
    },
    editMessage(message){
      const m = this.messageList.find(m=>m.id === message.id);
      m.isEdited = true;
      m.data.text = message.data.text;
    }
  }
}
</script>

<style>
.chat {
  position: relative;
  z-index: 9998;
}
.microphone {
  position: fixed;
  bottom: 100px;
  right: 400px;
  z-index: 9999;
}
</style>