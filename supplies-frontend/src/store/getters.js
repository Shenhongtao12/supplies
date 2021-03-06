const getters = {
  sidebar: state => state.app.sidebar,
  visitedViews: state => state.app.visitedViews,

  id: state => state.user.id,
  name: state => state.user.name,
  workNumber: state => state.user.workNumber,

  goodData: state => state.goods.goods,
  employeeData:state=>state.employee.employees,
  
  permission_routers: state => state.permission.routers,
  addRouters: state => state.permission.addRouters
}
export default getters
