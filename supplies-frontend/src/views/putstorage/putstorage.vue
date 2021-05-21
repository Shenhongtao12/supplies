<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" @click="showCreate"
            >添加物料
          </el-button>
        </el-form-item>
      </el-form>
      <div class="query">
        <el-form ref="form" :model="listQuery" label-width="80px">
          <el-row>
            <el-col :span="7">
              <el-form-item label="物料名称">
                <el-input
                  class="input"
                  placeholder="请输入物料名称"
                  v-model="listQuery.title"
                >
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="物料编号">
                <el-input
                  class="input"
                  placeholder="请输入物料编号"
                  v-model="listQuery.partNumber"
                >
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="物料类别">
                <el-select
                  v-model="listQuery.category"
                  placeholder="请选择物料类别"
                  style="width: 100%"
                  clearable
                >
                  <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.value"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="3">
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
      <el-table-column align="center" sortable label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        prop="partNumber"
        label="物料编号"
        sortable
      />
      <el-table-column
        align="center"
        prop="title"
        label="物料名称"
        width="170"
      />
      <el-table-column
        align="center"
        prop="unit"
        label="计量单位"
      ></el-table-column>
      <el-table-column
        align="center"
        prop="category"
        label="物料类别"
      ></el-table-column>
      <el-table-column
        align="center"
        prop="inventory"
        label="总库存"
      ></el-table-column>
      <el-table-column
        align="center"
        prop="inDate"
        label="创建时间"
        :formatter="formatterTime"
        sortable
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
      :current-page="listQuery.pageNum"
      :page-size="listQuery.pageRow"
      :total="totalCount"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
    >
    </el-pagination>
    <el-dialog title="添加物料" :visible.sync="dialogFormAdd" width="40%">
      <el-form
        :rules="dataVerify"
        class="small-space"
        :model="tempArticle"
        label-position="left"
        label-width="80px"
        style="margin: 10px"
        ref="tempArticle"
      >
        <el-form-item label="物料编号">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.partNumber"
            maxlength="100"
          ></el-input>
        </el-form-item>
        <el-form-item label="物料名称">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.title"
            maxlength="100"
          ></el-input>
        </el-form-item>
        <el-form-item label="计量单位">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.unit"
            maxlength="100"
          ></el-input>
        </el-form-item>
        <el-form-item label="物料类别">
          <el-select
            v-model="tempArticle.category"
            placeholder="请选择物料类别"
            style="width: 100%"
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.value"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item class="dialog_footer">
          <el-button @click="dialogFormAdd = false">取 消</el-button>
          <el-button type="success" @click="createEmployee('tempArticle')"
            >添 加</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog title="更新物料" :visible.sync="dialogFormUpdate" width="40%">
      <el-form
        :rules="dataVerify"
        class="small-space"
        :model="tempArticle"
        label-position="left"
        label-width="80px"
        style="margin: 10px"
        ref="tempArticle"
      >
        <el-form-item label="物料编号">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.partNumber"
            maxlength="100"
          ></el-input>
        </el-form-item>
        <el-form-item label="物料名称">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.title"
            maxlength="100"
          ></el-input>
        </el-form-item>
        <el-form-item label="计量单位">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.unit"
            maxlength="100"
          ></el-input>
        </el-form-item>
        <el-form-item label="物料类别">
          <el-select
            v-model="tempArticle.category"
            placeholder="请选择物料类别"
            style="width: 100%"
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.value"
              :value="item.value"
            >
            </el-option>
          </el-select>
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
import { formateDate } from "@/utils/index";

export default {
  data() {
    return {
      totalCount: 0, //分页组件--数据总条数
      list: [], //表格的数据
      listLoading: false, //数据加载等待动画
      listQuery: {
        pageNum: 1, //页码
        pageRow: 10, //每页条数
        partNumber: "",
        title:"",
        category:""
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
        partNumber: "",
        title: "",
        unit: "",
        inventory: "",
        category: "",
        adminId: "",
        inDate: "",
      },
      dataVerify: {
        partNumber: [
          { message: "请输入物料编号", trigger: "blur" },
          {
            min: 4,
            max: 20,
            message: "长度在 4 到 20 个字符",
            trigger: "blur",
          },
        ],
        title: [
          { required: true, message: "请输入物料名称", trigger: "blur" },
          {
            min: 1,
            max: 50,
            message: "长度在 1 到 50 个字符",
            trigger: "blur",
          },
        ],
        unit: [
          { required: true, message: "请输入计量单位", trigger: "blur" },
          {
            min: 1,
            max: 10,
            message: "长度在 1 到 10 个字符",
            trigger: "blur",
          },
        ],
        inventory: [
          { required: true, message: "请输入总库存", trigger: "blur" },
          {
            min: 1,
            max: 10,
            message: "长度在 1 到 10 个字符",
            trigger: "blur",
          },
        ],
      },
      options: [
        {
          value: "蔬菜",
        },
        {
          value: "食品",
        },
        {
          value: "底料",
        },
        {
          value: "冻货",
        },
        {
          value: "杂货",
        },
      ],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.listLoading = true;
      this.api({
        url: "/goods",
        method: "get",
        params: this.listQuery,
      }).then((data) => {
        this.listLoading = false;
        this.list = data.data.data.data;
        this.totalCount = data.totalCount;
      });
    },
    formatterTime(row, column) {
      return formateDate(row.inDate);
    },
    handleSizeChange(val) {
      //改变每页数量
      this.listQuery.pageRow = val;
      this.handleFilter();
    },
    handleCurrentChange(val) {
      //改变页码
      this.listQuery.pageNum = val;
      this.getList();
    },
    handleFilter() {
      //改变了查询条件,从第一页开始查询
      this.listQuery.pageNum = 1;
      this.getList();
    },
    getIndex($index) {
      //表格序号
      return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1;
    },
    showCreate() {
      //显示新增对话框
      this.tempArticle.id = "";
      this.tempArticle.partNumber = "";
      this.tempArticle.title = "";
      this.tempArticle.inventory = "";
      this.tempArticle.category = "";
      this.tempArticle.inDate = "";
      this.tempArticle.adminId = "";
      this.tempArticle.unit = "";

      this.dialogStatus = "create";
      this.dialogFormAdd = true;
    },
    showUpdate($index) {
      //显示修改对话框
      this.tempArticle.id = this.list[$index].id;
      this.tempArticle.partNumber = this.list[$index].partNumber;
      this.tempArticle.title = this.list[$index].title;
      this.tempArticle.inventory = this.list[$index].inventory;
      this.tempArticle.category = this.list[$index].category;
      this.tempArticle.inDate = this.list[$index].inDate;
      // this.tempArticle.adminId = this.list[$index].adminId;
      this.tempArticle.unit = this.list[$index].unit;
      this.dialogFormUpdate = true;
      this.dialogStatus = "update";
    },
    createEmployee(tempArticle) {
      delete this.tempArticle.adminId;
      delete this.tempArticle.inDate;
      delete this.tempArticle.id;
      delete this.tempArticle.inventory;

      this.$refs[tempArticle].validate((valid) => {
        console.log("valid", valid);
        if (valid) {
          this.api({
            url: "/goods",
            method: "post",
            data: this.tempArticle,
          }).then((data) => {
            if (data.data.code == 200) {
              this.$message({
                message: "添加成功！",
                type: "success",
              });
              this.getList();
              this.dialogFormAdd = false;
            } else {
              this.$message.error(data.data);
            }
          });
        }
      });
    },
    updateAdministrator(tempArticle) {
      delete this.tempArticle.adminId;
      delete this.tempArticle.inDate;
      this.$refs[tempArticle].validate((valid) => {
        console.log("valid", valid);
        if (valid) {
          this.api({
            url: "/goods",
            method: "put",
            data: this.tempArticle,
          }).then((data) => {
            if (data.data.code == 200) {
              this.$message({
                message: "修改成功！",
                type: "success",
              });
              this.getList();
              this.dialogFormUpdate = false;
            } else {
              this.$message.error(data.data);
            }
          });
        }
      });
    },
    deleteAdministrator($index) {
      this.$confirm("确定删除此物料吗?", "提示", {
        confirmButtonText: "确定",
        showCancelButton: false,
        type: "warning",
      }).then(() => {
        this.api({
          url: "/goods",
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
  height: 80px;
  line-height: 80px;
  padding-top: 20px;
  border: solid 1px rgb(243, 242, 242);
  border-radius: 3px;
}
</style>
