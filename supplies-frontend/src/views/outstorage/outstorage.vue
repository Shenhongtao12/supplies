<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" @click="showCreate"
            >物料领用
          </el-button>
        </el-form-item>
      </el-form>
      <div class="query">
        <el-form ref="form" :model="listQuery" label-width="80px">
          <el-row :gutter="10">
            <el-col :xs="12" :sm="12" :md="12" :lg="6" :xl="6">
              <el-form-item label="物料名称">
                <el-select
                  ref="agent1Select"
                  @hook:mounted="cancalReadOnly"
                  @visible-change="cancalReadOnly"
                  v-model="listQuery.goodsId"
                  placeholder="请选择物料"
                  filterable
                  clearable
                  size="small"
                >
                  <el-option
                    v-for="item in goods"
                    :key="item.id"
                    :label="item.title"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :xs="12" :sm="12" :md="12" :lg="6" :xl="6">
              <el-form-item label="员工姓名">
                <el-select
                  ref="agent2Select"
                  @hook:mounted="cancalReadOnly"
                  @visible-change="cancalReadOnly"
                  v-model="listQuery.userId"
                  placeholder="请选择员工"
                  filterable
                  clearable
                  size="small"
                >
                  <el-option
                    v-for="item in users"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :xs="12" :sm="12" :md="12" :lg="6" :xl="6">
              <el-form-item label="领用时间">
                <el-date-picker
                  v-model="listQuery.data"
                  style="width: 100%"
                  type="daterange"
                  size="small"
                  range-separator=""
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :xs="12" :sm="12" :md="12" :lg="6" :xl="6">
              <el-form-item>
                <el-button type="primary" @click="getList">查询</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
    </div>
    <br />
    <el-table
      :data="list"
      v-loading="listLoading"
      border
      fit
      :default-sort="{ prop: 'date', order: 'descending' }"
      highlight-current-row
    >
      <el-table-column align="center" label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="partNumber" label="物料编码" />
      <el-table-column align="center" prop="title" label="物料名称" />
      <el-table-column align="center" prop="userName" label="员工" />
      <el-table-column align="center" label="库存数量">
        <template slot-scope="scope">
          <span v-if="parseInt(scope.row.inventory / scope.row.repertory) > 0"
            >{{ parseInt(scope.row.inventory / scope.row.repertory) }}
            {{ scope.row.bigUnit }}</span
          >
          <span v-if="scope.row.inventory % scope.row.repertory > 0"
            >{{ scope.row.inventory % scope.row.repertory }}
            {{ scope.row.smallUnit }}</span
          >
        </template>
      </el-table-column>
      <el-table-column align="center" label="领用数量">
        <template slot-scope="scope">
          <span v-if="parseInt(scope.row.amount / scope.row.repertory) > 0"
            >{{ parseInt(scope.row.amount / scope.row.repertory) }}
            {{ scope.row.bigUnit }}</span
          >
          <span v-if="scope.row.amount % scope.row.repertory > 0"
            >{{ scope.row.amount % scope.row.repertory }}
            {{ scope.row.smallUnit }}</span
          >
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        prop="remark"
        label="备注"
      ></el-table-column>
      <el-table-column
        align="center"
        prop="inDate"
        label="领用时间"
        :formatter="formatterTime"
        column-key="inDate"
      ></el-table-column>
      <el-table-column align="center" label="管理" width="200">
        <template slot-scope="scope">
          <el-button
            type="success"
            size="small"
            icon="edit"
            @click="showUpdate(scope.$index)"
            >修改</el-button
          >
          <el-button
            type="danger"
            size="small"
            icon="edit"
            @click="deleteAdministrator(scope.$index)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="listQuery.page"
      :page-size="listQuery.size"
      :total="totalCount"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
    >
    </el-pagination>
    <el-dialog title="添加领用信息" :visible.sync="dialogFormAdd" width="40%">
      <el-form
        :rules="dataVerify"
        class="small-space"
        :model="tempArticle"
        label-position="left"
        label-width="100px"
        style="margin: 10px"
        ref="tempArticle"
      >
        <el-form-item label="物料" prop="goodsId">
          <el-select
            ref="agent3Select"
            @hook:mounted="cancalReadOnly"
            @visible-change="cancalReadOnly"
            v-model="tempArticle.goodsId"
            placeholder="请选择物料"
            style="width: 100%"
            filterable
            @change="changeGoodsName"
          >
            <el-option
              v-for="item in goods"
              :key="item.id"
              :label="getGoodsName(item)"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="员工" prop="userId">
          <el-select
            ref="agent4Select"
            @hook:mounted="cancalReadOnly"
            @visible-change="cancalReadOnly"
            v-model="tempArticle.userId"
            placeholder="请选择员工"
            style="width: 100%"
            filterable
          >
            <el-option
              v-for="item in users"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数量(大)">
          <el-input-number
            style="width: 80%"
            v-model.trim="tempArticle.bigAmount"
            maxlength="100"
            placeholder="请输入大计量单位数量"
            :min="0"
            :max="999999"
          ></el-input-number>
          &nbsp;&nbsp;{{ tempArticle.bigUnit }}
        </el-form-item>
        <el-form-item v-if="tempArticle.smallUnit" label="数量(小)">
          <el-input-number
            style="width: 80%"
            v-model.trim="tempArticle.amount"
            maxlength="100"
            placeholder="请输入小计量单位数量"
            :min="0"
            :max="999999"
          ></el-input-number>
          &nbsp;&nbsp; {{ tempArticle.smallUnit }}
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4 }"
            placeholder="请输入备注"
            maxlength="200"
            v-model="tempArticle.remark"
          >
          </el-input>
        </el-form-item>

        <el-form-item class="dialog_footer">
          <el-button @click="dialogFormAdd = false">取 消</el-button>
          <el-button type="success" @click="createEmployee('tempArticle')"
            >添 加</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog
      title="修改领用信息"
      :visible.sync="dialogFormUpdate"
      width="40%"
    >
      <el-form
        :rules="dataVerify"
        class="small-space"
        :model="tempArticle"
        label-position="left"
        label-width="80px"
        style="margin: 10px"
        ref="tempArticle"
      >
        <el-form-item label="物料" prop="goodsId">
          <el-select
            ref="agent5Select"
            @hook:mounted="cancalReadOnly"
            @visible-change="cancalReadOnly"
            v-model="tempArticle.goodsId"
            placeholder="请选择物料"
            style="width: 100%"
            filterable
            @change="changeGoodsName"
          >
            <el-option
              v-for="item in goods"
              :key="item.id"
              :label="getGoodsName(item)"
              :value="item.id"
              :disabled="true"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="员工" prop="userId">
          <el-select
            ref="agent6Select"
            @hook:mounted="cancalReadOnly"
            @visible-change="cancalReadOnly"
            v-model="tempArticle.userId"
            placeholder="请选择员工"
            style="width: 100%"
            filterable
          >
            <el-option
              v-for="item in users"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数量(大)">
          <el-input-number
            style="width: 80%"
            v-model.trim="tempArticle.bigAmount"
            maxlength="100"
            placeholder="请输入大计量单位数量"
            :min="0"
            :max="999999"
          ></el-input-number>
          &nbsp;&nbsp;{{ tempArticle.bigUnit }}
        </el-form-item>
        <el-form-item v-if="tempArticle.smallUnit" label="数量(小)">
          <el-input-number
            style="width: 80%"
            v-model.trim="tempArticle.amount"
            maxlength="100"
            placeholder="请输入小计量单位数量"
            :min="0"
            :max="999999"
          ></el-input-number>
          &nbsp;&nbsp; {{ tempArticle.smallUnit }}
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4 }"
            placeholder="请输入备注"
            maxlength="200"
            v-model="tempArticle.remark"
          >
          </el-input>
        </el-form-item>
        <el-form-item class="dialog_footer">
          <el-button @click="dialogFormUpdate = false">取 消</el-button>
          <el-button type="success" @click="updateAdministrator('tempArticle')"
            >修改</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
import { formateDate, formateDates } from "@/utils/index";

export default {
  data() {
    return {
      totalCount: 0, //分页组件--数据总条数
      list: [], //表格的数据
      listLoading: false, //数据加载等待动画
      listQuery: {
        page: 1, //页码
        size: 10, //每页条数
        goodsId: "",
        userId: "",
        data: "",
      },
      dialogStatus: "create",
      dialogFormAdd: false,
      dialogFormUpdate: false,
      textMap: {
        update: "编辑",
        create: "创建文章",
      },
      tempArticle: {
        id: "",
        amount: 0,
        partNumber: "",
        title: "",
        remark: "",
        userId: "",
        adminId: "",
        inDate: "",
        userName: "",
        goodsId: "",
        bigAmount: "",
        repertory: "",
        bigUnit: "",
        smallUnit: "",
      },
      dataVerify: {
        userId: [
          { required: true, message: "请输入员工姓名", trigger: "blur" },
        ],
        goodsId: [
          { required: true, message: "请输入物料名称", trigger: "blur" },
        ],
        amount: [{ message: "请输入物料数量", trigger: "blur" }],
        bigAmount: [
          { required: true, message: "请输入物料数量", trigger: "blur" },
        ],
      },
      goods: [],
      users: [],
    };
  },
  created() {
    this.getList();
    this.queryGoods();
    this.queryUsers();
  },
  methods: {
    cancalReadOnly(onOff) {
      this.$nextTick(() => {
        if (!onOff) {
          const Selects = this.$refs;
          if (Selects.agent1Select) {
            const input = Selects.agent1Select.$el.querySelector(
              ".el-input__inner"
            );
            input.removeAttribute("readonly");
          }
          if (Selects.agent2Select) {
            const appinput = Selects.agent2Select.$el.querySelector(
              ".el-input__inner"
            );
            appinput.removeAttribute("readonly");
          }
          if (Selects.agent3Select) {
            const gameinput = Selects.agent3Select.$el.querySelector(
              ".el-input__inner"
            );
            gameinput.removeAttribute("readonly");
          }
          if (Selects.agent4Select) {
            const gameinput = Selects.agent4Select.$el.querySelector(
              ".el-input__inner"
            );
            gameinput.removeAttribute("readonly");
          }
          if (Selects.agent5Select) {
            const gameinput = Selects.agent5Select.$el.querySelector(
              ".el-input__inner"
            );
            gameinput.removeAttribute("readonly");
          }
          if (Selects.agent6Select) {
            const gameinput = Selects.agent6Select.$el.querySelector(
              ".el-input__inner"
            );
            gameinput.removeAttribute("readonly");
          }
          if (Selects.agent7Select) {
            const gameinput = Selects.agent7Select.$el.querySelector(
              ".el-input__inner"
            );
            gameinput.removeAttribute("readonly");
          }
        }
      });
    },
    getList() {
      this.listLoading = true;
      const requestBody = {
        page: this.listQuery.page, //页码
        size: this.listQuery.size, //每页条数
        goodsId: this.listQuery.goodsId,
        userId: this.listQuery.userId,
      };

      if (this.listQuery.data != "") {
        requestBody.startDateTime = formateDates(this.listQuery.data[0]);
        requestBody.endDateTime = formateDates(this.listQuery.data[1]);
      }
      this.api({
        url: "/outStock",
        method: "get",
        params: requestBody,
      }).then((data) => {
        this.listLoading = false;
        this.list = data.data.data.data;
        this.totalCount = data.data.data.total;
      });
    },
    queryGoods() {
      this.$store.dispatch("GueryGoods").then((data) => {
        this.goods = data.data.data;
      });
    },
    queryUsers() {
      this.$store.dispatch("GueryUsers").then((data) => {
        this.users = data.data.data;
      });
    },
    changeGoodsName(value) {
      this.goods.forEach((data) => {
        if (data.id == value) {
          this.tempArticle.repertory = data.repertory;
          this.tempArticle.bigUnit = data.bigUnit;
          this.tempArticle.smallUnit = data.smallUnit;
        }
      });
    },
    formatterTime(row, column) {
      return formateDate(row.inDate);
    },
    handleSizeChange(val) {
      //改变每页数量
      this.listQuery.size = val;
      this.handleFilter();
    },
    handleCurrentChange(val) {
      //改变页码
      this.listQuery.page = val;
      this.getList();
    },
    handleFilter() {
      //改变了查询条件,从第一页开始查询
      this.listQuery.page = 1;
      this.getList();
    },
    getIndex($index) {
      //表格序号
      return (this.listQuery.page - 1) * this.listQuery.size + $index + 1;
    },
    getGoodsName(item) {
      return "【" + item.partNumber + "】 " + item.title;
    },
    showCreate() {
      if (this.$refs['tempArticle'] !== undefined) {
          this.$refs['tempArticle'].resetFields();
      }
      //显示新增对话框
      this.tempArticle.id = "";
      this.tempArticle.amount = "";
      this.tempArticle.title = "";
      this.tempArticle.remark = "";
      this.tempArticle.userId = "";
      this.tempArticle.goodsId = "";
      this.tempArticle.inDate = "";
      this.tempArticle.adminId = "";
      this.tempArticle.partNumber = "";
      this.tempArticle.userName = "";
      this.dialogFormAdd = true;
    },
    showUpdate($index) {
      //显示修改对话框
      this.tempArticle.id = this.list[$index].id;
      this.tempArticle.bigAmount = parseInt(
        this.list[$index].amount / this.list[$index].repertory
      );
      this.tempArticle.amount =
        this.list[$index].amount % this.list[$index].repertory;
      this.tempArticle.partNumber = this.list[$index].partNumber;
      this.tempArticle.title = this.list[$index].title;
      this.tempArticle.remark = this.list[$index].remark;
      this.tempArticle.userId = this.list[$index].userId;
      this.tempArticle.goodsId = this.list[$index].goodsId;
      this.tempArticle.inDate = this.list[$index].inDate;
      this.tempArticle.userName = this.list[$index].userName;
      this.tempArticle.repertory = this.list[$index].repertory;
      this.tempArticle.bigUnit = this.list[$index].bigUnit;
      this.tempArticle.smallUnit = this.list[$index].smallUnit;
      this.dialogFormUpdate = true;
    },
    createEmployee(tempArticle) {
      this.$refs[tempArticle].validate((valid) => {
        if (valid) {
          const num =
            this.tempArticle.amount +
            this.tempArticle.bigAmount * this.tempArticle.repertory;
          const body = {
            amount: num,
            goodsId: this.tempArticle.goodsId,
            remark: this.tempArticle.remark,
            userId: this.tempArticle.userId,
          };
          if (num == 0) {
            this.$message.error("请检查领取数量！");
          } else {
            this.api({
              url: "/outStock",
              method: "post",
              data: body,
            }).then((data) => {
              if (data.data.code == 200) {
                this.$message({
                  message: "添加成功！",
                  type: "success",
                });
                this.getList();
                this.dialogFormAdd = false;
              } else {
                this.$message.error(data.data.message);
              }
            });
          }
        }
      });
    },
    updateAdministrator(tempArticle) {
      this.$refs[tempArticle].validate((valid) => {
        if (valid) {
          const num =
            this.tempArticle.amount +
            this.tempArticle.bigAmount * this.tempArticle.repertory;
          const body = {
            amount: num,
            goodsId: this.tempArticle.goodsId,
            remark: this.tempArticle.remark,
            userId: this.tempArticle.userId,
            id: this.tempArticle.id,
          };
          if (num == 0) {
            this.$message.error("请检查领取数量！");
          } else {
            this.api({
              url: "/outStock",
              method: "put",
              data: body,
            }).then((data) => {
              if (data.data.code == 200) {
                this.$message({
                  message: "修改成功！",
                  type: "success",
                });
                this.getList();
                this.dialogFormUpdate = false;
              } else {
                this.$message.error(data.data.message);
              }
            });
          }
        }
      });
    },
    deleteAdministrator($index) {
      this.$confirm("确定删除这条领用记录吗?", "提示", {
        confirmButtonText: "确定",
        showCancelButton: false,
        type: "warning",
      }).then(() => {
        this.api({
          url: "/outStock",
          method: "delete",
          params: {
            id: this.list[$index].id,
          },
        })
          .then((data) => {
            this.getList();
            if (data.data.code == 200) {
              this.$message({
                message: "删除成功！",
                type: "success",
              });
            } else {
              this.$message.error(data.data.message);
            }
          })
          .catch(() => {
            this.$message.error("删除失败");
          });
      });
    },
  },
};
</script>
<style scoped>
.el-row {
  margin-bottom: 20px;
}
.el-col {
  border-radius: 4px;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
.row-bg {
  padding: 10px 0;
  background-color: #f9fafc;
}
.input {
  width: 260px;
}
.query {
  height: auto;
  line-height: 80px;
  padding-top: 20px;
  border: solid 1px rgb(243, 242, 242);
  border-radius: 3px;
}
</style>
