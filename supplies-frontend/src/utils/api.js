import axios from 'axios'
import {Message} from 'element-ui'
import {getToken} from '@/utils/auth'
import store from '../store'
// 创建axios实例
const service = axios.create({
  baseURL: process.env.BASE_URL, // api的base_url
  timeout: 15000                  // 请求超时时间
})
//request拦截器
service.interceptors.request.use(config => {
  let token = getToken();
  if (token) {
    // config.headers.token = token;
    config.headers['Authorization'] ="Bearer "+ token;
  }
  return config
}, error => {
  // Do something with request error
  console.error(error) // for debug
  Promise.reject(error)
})
//respone拦截器
service.interceptors.response.use(
  (response) => {
    const code = response.cdoe || 200
    const res = response;
    const msg = res.data.message;
    if (code === 200) {
      return res;
    } else if (res.status === "20011") {
      Message({
        showClose: true,
        message: res.msg,
        type: 'error',
        duration: 500,
        onClose: () => {
          store.dispatch('FedLogOut').then(() => {
            location.reload()// 为了重新实例化vue-router对象 避免bug
          })
        }
      });
      return Promise.reject("未登录")
    } else {
      Message({
        message: res.msg,
        type: 'error',
        duration: 3 * 1000
      })
      return Promise.reject(res)
    }
  },
  error => {
    let message = error.message
    if(error.response.status === 400) {
      message = error.response.data.message
    }
    Message({
      message: message,
      type: 'error',
      duration: 3 * 1000
    })
    return Promise.reject(error)
  }
)
export default service

