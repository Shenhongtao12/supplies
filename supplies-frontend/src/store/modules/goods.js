import {default as api} from '../../utils/api'

const goods = {
  state: {
   goods:[]
  },
  mutations: {
    SET_GOODS: (state, goods) => {
        goods.forEach(item => {
            state.goods.push(item);
          });
    },
    RESET_GOODS: (state) => {
      state.goods=[];
    }
  },
  actions: {
    // 查询物料
    GueryGoods({commit, state}) {
      commit('RESET_GOODS')
      return new Promise((resolve, reject) => {
        api({
          url: "goods/select-supplies",
          method: "get",
        }).then(data => {
          //储存物料信息
          commit('SET_GOODS', data.data.data);
          resolve(data);
        }).catch(err => {
          reject(err)
        })
      })
    }
  }
}

export default goods
