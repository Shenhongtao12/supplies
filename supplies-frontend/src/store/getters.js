const getters = {
  sidebar: state => state.app.sidebar,
  visitedViews: state => state.app.visitedViews,

  id: state => state.user.id,
  name: state => state.user.name,
  workNumber: state => state.user.workNumber,

  permission_routers: state => state.permission.routers,
  addRouters: state => state.permission.addRouters
}
export default getters
