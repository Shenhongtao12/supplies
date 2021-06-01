<template>
  <div class="login">
    <el-row :gutter="20">
  <el-col :span="10">
    <div class="block">
      <div class="demonstration">
        <img style="width: 18%; height: 18%" src="./log.png" />
        <div class="text">海底捞 | 智慧库房管理平台</div>
      </div>
      <img class="logo" style="width: 100%; height: 100%" src="./logo.png" />
    </div>
  </el-col>
  <el-col :span="10">
    <el-form
      ref="loginForm"
      :model="loginForm"
      :rules="loginRules"
      class="login-form"
    >
      <h3 class="title">账号登录</h3>
      <el-form-item prop="workNumber">
        <el-input
          v-model="loginForm.workNumber"
          type="text"
          auto-complete="off"
          maxlength="20"
          placeholder="请输入工号"
        >
          <svg-icon
            slot="prefix"
            icon-class="user"
            class="el-input__icon input-icon"
          />
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          auto-complete="off"
          placeholder="请输入密码"
          maxlength="20"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon
            slot="prefix"
            icon-class="password"
            class="el-input__icon input-icon"
          />
        </el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-input
          v-model="loginForm.code"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          maxlength="4"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon
            slot="prefix"
            icon-class="validCode"
            class="el-input__icon input-icon"
          />
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img" />
        </div>
      </el-form-item>
      <!-- <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox> -->
      <el-form-item style="width: 100%">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width: 100%"
          @click.native.prevent="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
      </el-form-item>
    </el-form>
  </el-col>
</el-row>


    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright © 2018-2021 ruoyi.vip All Rights Reserved.</span>
    </div>
  </div>
</template>

<script>
import { Base64 } from "js-base64";

export default {
  name: "Login",
  data() {
    return {
      codeUrl: "",
      cookiePassword: "",
      loginForm: {
        workNumber: "",
        password: "",
        code: "",
      },
      loginRules: {
        workNumber: [
          { required: true, trigger: "blur", message: "用户名不能为空" },
          {
            min: 4,
            max: 20,
            message: "长度在 4 到 20 个字符",
            trigger: "blur",
          },
        ],
        password: [
          { required: true, trigger: "blur", message: "密码不能为空" },
          {
            min: 6,
            max: 20,
            message: "长度在 6 到 20 个字符",
            trigger: "blur",
          },
        ],
        code: [
          { required: true, trigger: "change", message: "验证码不能为空" },
          {
            min: 4,
            max: 4,
            message: "长度为 4 个字符",
            trigger: "blur",
          },
        ],
      },
      loading: false,
      redirect: "/index",
    };
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true,
    },
  },
  created() {
    this.getCode();
  },
  methods: {
    getCode() {
      this.api({
        url: "/admin/getCircleCaptcha",
        method: "get",
        responseType: "blob",
      })
        .then((res) => {
          let blob = new Blob([res.data], { type: "image/png" });
          let url = window.URL.createObjectURL(blob);
          this.codeUrl = url;
        })
        .catch(() => {
          this.$message.error(data);
        });
    },
    handleLogin() {
      let loginForms = {
        workNumber: this.loginForm.workNumber,
        password: Base64.encode(this.loginForm.password),
        code: this.loginForm.code,
      };
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          this.loading = true;
          this.$store
            .dispatch("Login", loginForms)
            .then((data) => {
              this.loading = false;
              if (data.data.code == 200) {
                this.$message({
                  message: "登陆成功！",
                  type: "success",
                });
                this.$router.push({ path: "/" });
              } else {
                this.$message.error(data.data.message);
              }
            })
            .catch(() => {
              this.loading = false;
            });
        } else {
          return false;
        }
      });
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss">
.login{
padding-top: 15%;
}
.title {
  margin: 0px auto 30px auto;
  text-align: left;
  color: #707070;
}

.login-form {
  margin: auto;
  border-radius: 6px;
  margin-left: 30%;
  margin-top: 5%;
  background: #ffffff;
  border: solid 1px gainsboro;
  width: 80%;
  padding: 25px 25px 5px 25px;
  .el-input {
    height: 38px;
    input {
      height: 38px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
.login-code-img {
  height: 38px;
}
.block {
  margin-left: 20%;
}
.text {
  margin-top: -13%;
  margin-left: 20%;
}
.logo {
  margin-top: 10%;
}
</style>
