<template>
  <div class="app-container">
    <div class="filter-container">
      <el-row>
        <el-col :span="5">
          <el-button type="primary" icon="plus" @click="showCreate"
            >添加管理员
          </el-button>
        </el-col>
        <el-col :span="16">
          <el-form
            :model="listQuery"
            :rules="rules"
            ref="listQuery"
            label-width="100px"
            class="demo-ruleForm"
          >
            <el-row>
              <el-col :span="9">
                <el-form-item label="" prop="name">
                  <el-input
                    class="input"
                    placeholder="请输入姓名搜索"
                    v-model.trim="listQuery.name"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item>
                  <el-button
                    type="primary"
                    icon="plus"
                    @click="queryList('listQuery')"
                    >查询
                  </el-button>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-col>
      </el-row>
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
      <el-table-column
        align="center"
        prop="workNumber"
        label="工号"
        column-key="workNumber"
      />
      <el-table-column
        align="center"
        prop="name"
        label="姓名"
        column-key="name"
      />
      <el-table-column
        align="center"
        prop="inDate"
        label="创建时间"
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
    <el-dialog title="添加管理员" :visible.sync="dialogFormAdd" width="36%">
      <el-form
        :rules="dataVerify"
        class="small-space"
        :model="tempArticle"
        label-position="left"
        label-width="60px"
        style="margin: 10px"
        ref="tempArticle"
      >
        <el-form-item label="工号">
          <el-input
            v-model.trim="tempArticle.workNumber"
            maxlength="100"
          ></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model.trim="tempArticle.name" maxlength="100"></el-input>
        </el-form-item>
        <el-form-item class="dialog_footer">
          <el-button @click="dialogFormAdd = false">取 消</el-button>
          <el-button type="success" @click="createEmployee('tempArticle')"
            >创 建</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog title="编辑" :visible.sync="dialogFormUpdate" width="36%">
      <el-form
        class="small-space"
        :model="tempArticle"
        label-position="left"
        label-width="60px"
        style="margin: 10px"
        ref="tempArticle"
      >
        <el-form-item label="姓名">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.name"
            maxlength="100"
          ></el-input>
        </el-form-item>
        <el-form-item label="工号">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.workNumber"
            maxlength="100"
          ></el-input>
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
import { Base64 } from "js-base64";

export default {
  data() {
    return {
      totalCount: 0, //分页组件--数据总条数
      list: [], //表格的数据
      listLoading: false, //数据加载等待动画
      listQuery: {
        page: 1, //页码
        size: 10, //每页条数
        name: "",
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
        inDate: "",
        name: "",
        workNumber: "",
      },
      rules: {
        name: [
          { message: "请输入姓名", trigger: "blur" },
          {
            min: 2,
            max: 10,
            message: "长度在 2 到 10 个字符",
            trigger: "blur",
          },
        ],
      },
      dataVerify: {
        workNumber: [
          { message: "请输入工号", trigger: "blur" },
          {
            min: 4,
            max: 20,
            message: "长度在 4 到 20 个字符",
            trigger: "blur",
          },
        ],
        name: [
          { message: "请输入姓名", trigger: "blur" },
          {
            min: 2,
            max: 10,
            message: "长度在 2 到 10 个字符",
            trigger: "blur",
          },
        ],
        password: [
          { message: "请输入密码", trigger: "blur" },
          {
            min: 6,
            max: 20,
            message: "长度在 6 到 20 个字符",
            trigger: "blur",
          },
        ],
        phone: [
          { message: "请输入密码", trigger: "blur" },
          {
            min: 6,
            max: 20,
            message: "长度在 6 到 20 个字符",
            trigger: "blur",
          },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.listLoading = true;
      this.api({
        url: "/user",
        method: "get",
        params: this.listQuery,
      }).then((data) => {
        this.listLoading = false;
        this.list = data.data.data.data;
        this.totalCount = data.data.data.total;
      });
    },

    formatterTime(row, column) {
      return formateDate(row.inDate);
    },

    queryList(listQuery) {
      this.listLoading = true;
      this.$refs[listQuery].validate((valid) => {
        if (valid) {
          this.api({
            url: "/user",
            method: "get",
            params: this.listQuery,
          }).then((data) => {
            this.listLoading = false;
            this.list = data.data.data.data;
            this.totalCount = data.totalCount;
          });
        }
      });
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
    showCreate() {
      //显示新增对话框
      this.tempArticle.id = "";
      this.tempArticle.name = "";
      this.tempArticle.workNumber = "";
      this.dialogStatus = "create";
      this.dialogFormAdd = true;
    },
    showUpdate($index) {
      //显示修改对话框
      this.tempArticle.id = this.list[$index].id;
      this.tempArticle.name = this.list[$index].name;
      this.tempArticle.workNumber = this.list[$index].workNumber;
      this.dialogStatus = "update";
      this.dialogFormUpdate = true;
    },
    createEmployee(tempArticle) {
      this.$refs[tempArticle].validate((valid) => {
        console.log("valid", valid);
        if (valid) {
          this.api({
            url: "/user/save",
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
      delete this.tempArticle.inDate;
      this.$refs[tempArticle].validate((valid) => {
        console.log("valid", valid);
        if (valid) {
          this.api({
            url: "/user",
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
      this.$confirm("确定删除此用户?", "提示", {
        confirmButtonText: "确定",
        showCancelButton: false,
        type: "warning",
      }).then(() => {
        this.api({
          url: "/user",
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
/* .dialog_footer {
  margin-left: 40%;
} */
</style>
