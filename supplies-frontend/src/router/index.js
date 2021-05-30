import Vue from 'vue'
import Router from 'vue-router'
// in development env not use Lazy Loading,because Lazy Loading too many pages will cause webpack hot update too slow.so only in production use Lazy Loading
/* layout */
import Layout from '../views/layout/Layout'

const _import = require('./_import_' + process.env.NODE_ENV)
Vue.use(Router)
export const constantRouterMap = [
  {path: '/login', component: _import('login/index'), hidden: true},
  {path: '/404', component: _import('404'), hidden: true},
  {
    path: '',
    component: Layout,
    redirect: 'dashboard',
    name: '首页',
    hidden: false,
    children: [{
      path: 'dashboard', component: _import('dashboard/index'),
      meta: {title: '首页', icon: 'table'},
    }]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '/system/outstorage',
    name: '功能模块',
    meta: {title: '功能模块', icon: 'tree'},
    children: [
      {
        path: 'outstorage',
        name: '出库管理',
        component: _import('outstorage/outstorage'),
        meta: {title: '出库管理', icon: 'example'},
        menu: 'outstorage'
      },
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '/system/instorage',
    name: '功能模块',
    meta: {title: '功能模块', icon: 'tree'},
    children: [
      {
        path: 'instorage',
        name: '入库管理',
        component: _import('instorage/instorage'),
        meta: {title: '入库管理', icon: 'form'},
        menu: 'instorage'
      },
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '/system/dataStatistics',
    name: '功能模块',
    meta: {title: '功能模块', icon: 'tree'},
    children: [
      {
        path: 'dataStatistics',
        name: '数据统计',
        component: _import('dataStatistics/dataStatistics'),
        meta: {title: '数据统计', icon: 'tree'},
        menu: 'dataStatistics'
      },
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '/system/employee',
    name: '功能模块',
    meta: {title: '功能模块', icon: 'tree'},
    children: [
      {
        path: 'employee',
        name: '员工管理',
        component: _import('employee/employee'),
        meta: {title: '员工管理', icon: 'user'},
        menu: 'employee'
      },
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '/system/administrator',
    name: '功能模块',
    meta: {title: '功能模块', icon: 'tree'},
    children: [
      {
        path: 'administrator',
        name: '管理员',
        component: _import('administrator/administrator'),
        meta: {title: '管理员', icon: 'user'},
        menu: 'administrator'
      },
    ]
  },
]
export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({y: 0}),
  routes: constantRouterMap
})
export const asyncRouterMap = [
  // {
  //   path: '/system',
  //   component: Layout,
  //   redirect: '/system/article',
  //   name: '功能模块',
  //   meta: {title: '功能模块', icon: 'tree'},
  //   children: [
  //     {
  //       path: 'article',
  //       name: '文章',
  //       component: _import('article/article'),
  //       meta: {title: '文章', icon: 'eye'},
  //       menu: 'article'
  //     },
  //   ]
  // },
  {
    path: '/user',
    component: Layout,
    redirect: '/user/',
    name: '',
    meta: {title: '用户权限', icon: 'table'},
    children: [
      {
        path: '', name: '用户列表', component: _import('user/user'), meta: {title: '用户列表', icon: 'user'}, menu: 'user'
      },
      {
        path: 'role',
        name: '权限管理',
        component: _import('user/role'),
        meta: {title: '权限管理', icon: 'password'},
        menu: 'role'
      },
    ]
  },
  {path: '*', redirect: '/404', hidden: true}
]
