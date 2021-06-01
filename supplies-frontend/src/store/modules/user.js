import { removeToken, setUserId, removeUserId, setToken } from '@/utils/auth'
import { default as api } from '../../utils/api'
import store from '../../store'
import router from '../../router'

const user = {
  state: {
    id: "",
    name: "",
    workNumber: ""
  },
  mutations: {
    SET_USER: (state, userInfo) => {
      state.id = userInfo.id;
      state.name = userInfo.name;
      state.workNumber = userInfo.workNumber;
    },
    RESET_USER: (state) => {
      state.id = "";
      state.name = "";
      state.workNumber = "";
    }
  },
  actions: {
    // 登录
    Login({ commit, state }, loginForm) {
      return new Promise((resolve, reject) => {
        api({
          url: "admin/login",
          method: "post",
          data: loginForm
        }).then(data => {
          if (data.data.code == 200) {
            //localstorage中保存token
            setToken(data.data.data.token);
            setUserId(data.data.data.admin.id);
            //储存用户信息
            commit('SET_USER', data.data.data.admin);
          } else {
            //alert(data.data.message);
            this.$message.error(data.data.message);
          }
          resolve(data);
        }).catch(err => {
          reject(err)
        })
      })
    },
    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        api({
          url: '/admin/refreshToken',
          method: 'get'
        }).then(data => {
          //储存用户信息
          // commit('SET_USER', data);
          //存储 token
          // setToken(data.data.data.token);
          //生成路由
          store.dispatch('GenerateRoutes', data).then(() => {
            //生成该用户的新路由json操作完毕之后,调用vue-router的动态新增路由方法,将新路由添加
            router.addRoutes(store.getters.addRouters)
          })
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 登出
    LogOut({ commit }) {
      commit('RESET_USER')
      removeToken();
      removeUserId();
      // return new Promise((resolve) => {
      //   api({
      //     url: "login/logout",
      //     method: "post"
      //   }).then(data => {
      //     commit('RESET_USER')
      //     removeToken()
      //     resolve(data);
      //   }).catch(() => {
      //     commit('RESET_USER')
      //     removeToken();
      //     removeUserId();
      //   })
      // })
    },
    // 前端登出,不用调后端清除token的接口
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('RESET_USER')
        removeToken()
        resolve()
      })
    }
  }
}
export default user
