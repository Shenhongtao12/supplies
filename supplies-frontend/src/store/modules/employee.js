import {default as api} from '../../utils/api'

const employee = {
  state: {
    employees:[]
  },
  mutations: {
    SET_EMPLOYE: (state, employees) => {
        employees.forEach(item => {
            state.employees.push(item);
          });
    },
    RESET_EMPLOYE: (state) => {
      state.employees=[];
    }
  },
  actions: {
    // 查询员工
    GueryUsers({commit, state}) {
      commit('RESET_EMPLOYE')
      return new Promise((resolve, reject) => {
        api({
          url: "user/select-user",
          method: "get",
        }).then(data => {
          //储存员工信息
          commit('SET_EMPLOYE', data.data.data);
          resolve(data);
        }).catch(err => {
          reject(err)
        })
      })
    }
  }
}

export default employee
