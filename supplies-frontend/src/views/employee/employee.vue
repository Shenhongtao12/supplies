<template>
  <div class="app-container">
    <div class="filter-container">
      <el-row :gutter="10">
        <el-col  :xs="8" :sm="8" :md="8" :lg="8" :xl="8">
          <el-button type="primary" icon="plus" @click="showCreate"
            >添加员工
          </el-button>
          <el-button
            style="margin-left: 16px"
            type="primary"
            @click="showExcelDiglog()"
          >
            Excel 导入
          </el-button>
        </el-col><br/>
        <el-col  :xs="16" :sm="16" :md="16" :lg="16" :xl="16">
          <el-form
            :model="listQuery"
            :rules="rules"
            ref="listQuery"
            label-width="100px"
            class="demo-ruleForm"
          >
            <el-form-item label="" prop="name">
              <el-input
                class="input"
                placeholder="请输入姓名搜索"
                v-model.trim="listQuery.name"
                max="2"
                min="0"
              >
              </el-input>
              <el-button
                type="primary"
                icon="plus"
                @click="queryList('listQuery')"
                >查询
              </el-button>
            </el-form-item>
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
    <el-dialog title="添加员工" :visible.sync="dialogFormAdd" width="36%">
      <el-form
        :rules="dataVerify"
        class="small-space"
        :model="tempArticle"
        label-position="left"
        label-width="60px"
        style="margin: 10px"
        ref="tempArticle"
      >
        <el-form-item label="工号" prop="workNumber">
          <el-input
            v-model.trim="tempArticle.workNumber"
            maxlength="100"
          ></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
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
        :rules="dataVerify"
        ref="tempArticle"
      >
        <el-form-item label="姓名" prop="name">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.name"
            maxlength="100"
          ></el-input>
        </el-form-item>
        <el-form-item label="工号" prop="workNumber">
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

    <el-dialog title="Excel导入"
      :before-close="handleClose" :visible.sync="showExcelDig">
      <div style="display: flex; flex-direction: row;">
        <el-input style="width: 35%"
        placeholder="请上传文件"
        v-model="excelName"
        :disabled="true">
      </el-input>
      <input
        ref="excel-upload"
        class="excel-upload-input"
        type="file"
        accept=".xlsx, .xls"
        @change="handleClick"
      />
      <el-button
          style="margin-left: 16px; margin-right: 16px;"
          type="primary"
          plain
          @click="handleUpload"
          :loading="loading"
        >
          上传<i class="el-icon-upload el-icon--right"></i>
        </el-button>
        <a href="../../../static/batchUser.xlsx" style="color: #79BBFF; margin-top: 12px" download="员工信息模板.xlsx">
          下载Excel模板<i class="el-icon-download"></i>
        </a>
      </div>
      
      <el-table :data="errorResponse" max-height="320">
        <el-table-column property="workNumber" label="员工工号" width="100px">
        </el-table-column>
        <el-table-column property="name" label="姓名" width="100px">
        </el-table-column>
        <el-table-column property="errorMessage" label="错误提示信息" width="200px">
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>
<script>
import { formateDate } from "@/utils/index";
import XLSX from "xlsx";

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
      batch: 100,
      loading: false,
      errorResponse: [],
      excelName: "",
      showExcelDig: false,
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
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    handleUpload() {
      this.$refs["excel-upload"].click();
    },
    handleClick(e) {
      const files = e.target.files;
      const rawFile = files[0]; // only use files[0]
      if (!rawFile) return;
      if (!this.isExcel(rawFile)) {
        this.$message.error("请选择正确的Excel文件");
        this.loading = false;
        return;
      }
      this.upload(rawFile);
    },
    async upload(rawFile) {
      this.$refs["excel-upload"].value = null; // fix can't select the same excel
      this.excelName = rawFile.name;
      if (this.beforeUpload(rawFile)) {
        let data = await this.readerData(rawFile);
        if (data.length <= 0) {
          this.$message.error("Excel数据为空");
           this.loading = false;
          return;
        }
        let apiList = [];
        let errorList = [];
        let request = this.result(data)
        let error = request.filter(x => (!x.isSuccess));
        if (error && error.length > 0) {
          errorList = error;
        }
        
        request = request.filter((x) => x.isSuccess);
        if (request.length > 0) {
          this.batchInStore(request, apiList);
        } 
        let response = await this.batchHttp(apiList);
        for (let res of response) {
          let errorRes = res.data.data.filter(x => !x.isSuccess);
          if (errorRes && errorRes[0]) {
            errorList = [
              ...errorList, errorRes
            ];
          }
        }
        this.errorResponse = errorList;
        this.loading = false;
        this.$message({
                message: "导入成功！",
                type: "success",
              });
        this.getList();
      } 
    },
    readerData(rawFile) {
      this.loading = true;
      return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = (e) => {
          const data = e.target.result;
          const workbook = XLSX.read(data, { type: "array" });
          const firstSheet = workbook.SheetNames[0];
          const result1 = workbook.Sheets[firstSheet];
          let header = this.getHeaderRow(result1);
          if (header[0] !== "员工工号" ||
              header[1] !== "姓名") {
            this.$message.error("Excel不正确，请下载正确的模板");
            this.loading = false;
            return;
          }
          resolve(XLSX.utils.sheet_to_json(result1));
        };
        reader.readAsArrayBuffer(rawFile);
      })
    },
    getHeaderRow(sheet) {
      const headers = [];
      const range = XLSX.utils.decode_range(sheet["!ref"]);
      let C;
      const R = range.s.r;
      /* start in the first row */
      for (C = range.s.c; C <= range.e.c; ++C) {
        /* walk every column in the range */
        const cell = sheet[XLSX.utils.encode_cell({ c: C, r: R })];
        /* find the cell in the first row */
        let hdr = "UNKNOWN " + C; // <-- replace with your desired default
        if (cell && cell.t) hdr = XLSX.utils.format_cell(cell);
        headers.push(hdr);
      }
      return headers;
    },
    result(value) {
      let request = [];
      value.forEach((data) => {
        let req = {
          workNumber: data["员工工号"] ? this.strTrim(data["员工工号"].toString()) : null,
          name: data["姓名"] ? this.strTrim(data["姓名"].toString()) : null,
          isSuccess: true,
        };
        this.verify(req);
        request.push(req);
      });
      return request.filter(x => !(!x.workNumber && !x.name));
    },
    verify(data) {
      if(!data.workNumber && !data.name){
        return;
      }
      if (!data.workNumber) {
        data.isSuccess = false;
        data.errorMessage = "员工工号不能为空";
        return;
      }
      if (data.workNumber && (
        data.workNumber.length > 20 ||
        data.workNumber.length < 4)
      ) {
        data.isSuccess = false;
        data.errorMessage = "员工工号长度应4-20字符";
        return;
      }

      if (!data.name) {
        data.isSuccess = false;
        data.errorMessage = "姓名不能为空";
        return;
      }
      if (data.name.length > 10 || data.name.length < 2) {
        data.isSuccess = false;
        data.errorMessage = "姓名长度应在2-10字符";
        return;
      }
    },
    isExcel(file) {
      return /\.(xlsx|xls|csv)$/.test(file.name);
    },
    batchHttp(apiList) {
      return new Promise((resolve, reject) => {
        this.api.all(apiList).then(res => {
          resolve(res);
        })
      })
      
    },
    batchInStore(request, apiList) {
      let num = 0;
      num = Math.ceil(request.length / this.batch);
      for (let i = 0; i < num; i++) {
        apiList.push(
          this.api({
            url: "/user/batchSave",
            method: "post",
            data: request.slice(i * this.batch, (i + 1) * this.batch - 1),
          })
        )
      }
    },
    showExcelDiglog() {
      this.showExcelDig = true;
    },
    strTrim(str) {
      if (!str || str == "") {
        return null;
      }
      return str.trim();
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          this.errorResponse = [];
          this.excelName = "";
          done();
        })
        .catch(_ => {});
    },
    beforeUpload(file) {
      const isLt3M = file.size / 1024 / 1024 < 3;
      if (isLt3M) {
        return true;
      }
      this.$message({
        message: "文件过大，请删减至3M以内",
        type: "warning",
      });
      return false;
    },
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
              this.$message.error(data.data.message);
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
  height: 80px;
  line-height: 80px;
  padding-top: 20px;
  border: solid 1px rgb(243, 242, 242);
  border-radius: 3px;
}
.excel-upload-input {
  display: none;
  z-index: -9999;
}
.demo-ruleForm{
 margin-top: -14px;
}

</style>
