<template>
  <el-header style="margin: 0; padding: 0">
    <Header/>
  </el-header>
  <div style="font-family: Arial, sans-serif; text-align: center">
    <div style="width: 100%; height: 88vh; overflow: hidden; background-color: aliceblue">
      <div style="width: 400px; margin: 150px auto;">
        <el-card>
          <img width="190" src="../../public/IXNNHS.png" />
          <div style="color: black; font-size: 30px; padding-bottom: 30px">
            Welcome to <br/> IBM PSM With Watson
          </div>
          <el-form ref="form" :model="form" :rules="rules" @keyup.enter.native="login">
            <el-form-item prop="email">
              <el-input prefix-icon="Message" v-model="form.email" placeholder="Enter your email" />
            </el-form-item>
            <el-form-item prop="password">
              <el-input prefix-icon="Lock" v-model="form.password" type="password" autocomplete="off" show-password placeholder="Enter your password" />
            </el-form-item>
            <div style="text-align: center">
              <el-button type="primary" @click="login" style="width: 100%"><b>Sign in with existing users</b></el-button>
            </div>
          </el-form>
          <el-divider>OR</el-divider>
          <div>
            <a href="https://oidc.mock.signin.nhs.uk/authorize?scope=openid%20profile%20email%20phone%20gp_registration_details&client_id=832a7164-93f7-4f23-9c77-4a2205227fab&redirect_uri=http://localhost:9090/login/nhs&response_type=code&state=relying%20party-specific%20state&nonce=noncevalue">
              <img width="360" src="../../public/NHSLogin.png" alt="NHS login button"/>
            </a>
          </div>
        </el-card>
      </div>
    </div>
    <div>
      <p>Copyright &copy 2022-2023 by Tianang Chen. All rights reserved.</p>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request"
import Header from "@/components/Header"
import { getCookie, delCookie } from "@/utils/cookie.utils"

export default {
  name: 'Login',
  components: {
    Header
  },
  data() {
    return {
      user: {},
      form: {},
      rules: {
        email: [
          { required: true, message: 'Please enter your email', trigger: 'blur' }
        ],
        password: [
          { required: true, message: 'Please enter your password', trigger: 'blur'}
        ]
      }
    }
  },
  created() {
    if(getCookie("user")) {
      delCookie("user")
    }
  },
  methods: {
    login() {
      this.$refs.form.validate((valid) => {
        if(valid) {
          request.post("/login/app", this.form).then(res => {
            if(res.code === "10000") {
              if(res.data.role === "patient") {
                this.$router.push("/center")
              } else {
                this.$router.push("/patients")
              }
            } else {
              this.$message({
                type: "error",
                message: res.msg,
                customClass: 'font'
              })
            }
          })
        } else {
          this.$message({
            type: "error",
            message: "Please enter required information",
            customClass: 'font'
          })
        }
      })
    }
  }
}
</script>

<style>
.font {
  font-family: Arial, sans-serif;
}
</style>