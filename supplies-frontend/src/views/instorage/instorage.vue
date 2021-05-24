<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" @click="showCreate"
            >物料入库
          </el-button>
          <input
            ref="excel-upload-input"
            class="excel-upload-input"
            type="file"
            accept=".xlsx, .xls"
            @change="handleClick"
          />
          <el-button
            :loading="loading"
            style="margin-left: 16px"
            size="medium"
            type="primary"
            @click="handleUpload"
          >
            Excel 导入
          </el-button>
        </el-form-item>
      </el-form>
      <div class="query">
        <el-form ref="form" :model="listQuery" label-width="80px">
          <el-row>
            <el-col :span="6">
              <el-form-item label="物料名称">
                <el-select
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
            <el-col :span="9">
              <el-form-item label="出库时间">
                <el-date-picker
                  v-model="listQuery.data"
                  style="width: 100%"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  size="small"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="4">
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
      <el-table-column align="center" prop="partNumber" label="物料编号" />
      <el-table-column align="center" prop="title" label="物料名称" />
      <el-table-column align="center" prop="amount" label="入库数量" />
      <el-table-column align="center" prop="inventory" label="总库存" />
      <el-table-column align="center" prop="repertory" label="转换量" />
      <el-table-column align="center" prop="bigUnit" label="计量单位（大）" />
      <el-table-column align="center" prop="smallUnit" label="计量单位（小）" />
      <el-table-column align="center" prop="remark" label="备注" />
      <el-table-column
        align="center"
        prop="inDate"
        label="入库时间"
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
    <el-dialog title="添加入库信息" :visible.sync="dialogFormAdd" width="40%">
      <el-form
        :rules="dataVerify"
        class="small-space"
        :model="tempArticle"
        label-position="left"
        label-width="80px"
        style="margin: 10px"
        ref="tempArticle"
      >
        <el-form-item label="物料名称" prop="goodsId">
          <el-select
            v-model="tempArticle.goodsId"
            placeholder="请选择物料名称"
            style="width: 100%"
            filterable
            @change="goodsNameChange"
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
        <el-form-item label="数量" class="num" prop="amount">
          <el-row>
            <el-col :span="18">
              <el-input-number
                style="width: 100%"
                v-model.trim="tempArticle.amount"
                placeholder="请输入数量"
                maxlength="100"
                :min="1"
                :max="999999"
              ></el-input-number
            ></el-col>
            <el-col :span="5"> &nbsp; &nbsp;{{ chooseGoods.bigUnit }} </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4 }"
            placeholder="请输入备注"
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
      title="修改入库信息"
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
        <el-form-item label="物料名称" prop="goodsId">
          <el-select
            v-model="tempArticle.goodsId"
            placeholder="请选择物料"
            style="width: 100%"
            filterable
            @change="goodsNameChange"
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
        <el-form-item label="数量" class="num" prop="amount">
          <el-row>
            <el-col :span="18">
              <el-input-number
                style="width: 100%"
                v-model.trim="tempArticle.amount"
                placeholder="请输入数量"
                maxlength="100"
                :min="1"
                :max="999999"
              ></el-input-number
            ></el-col>
            <el-col :span="5">
              &nbsp; &nbsp;{{
                chooseGoods != "" ? chooseGoods.bigUnit : tempArticle.bigUnit
              }}
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4 }"
            placeholder="请输入备注"
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
import XLSX from "xlsx";

export default {
  props: {
    beforeUpload: Function, // eslint-disable-line
    onSuccess: Function, // eslint-disable-line
  },
  data() {
    return {
      totalCount: 0, //分页组件--数据总条数
      list: [], //表格的数据
      listLoading: false, //数据加载等待动画
      listQuery: {
        page: 1, //页码
        size: 10, //每页条数
        goodsId: "",
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
        amount: "",
        partNumber: "",
        title: "",
        remark: "",
        adminId: "",
        inDate: "",
        bigUnit: "",
        inventory: "",
        repertory: "",
        smallUnit: "",
        goodsId: "",
      },
      materials: {
        bigUnit: "",
        category: "",
        inventory: 0,
        partNumber: "",
        repertory: 0,
        smallUnit: "",
        title: "",
      },
      dataVerify: {
        goodsId: [
          { required: true, message: "请输入物料名称", trigger: "blur" },
        ],
        amount: [
          { required: true, message: "请输入物料名称", trigger: "blur" },
        ],
      },
      goods: [],
      users: [],
      chooseGoods: "",
      loading: false,
      vegetables: [],
    };
  },
  created() {
    this.getList();
    this.queryGoods();
  },
  methods: {
    getList() {
      this.listLoading = true;
      const requestBody = {
        page: this.listQuery.page, //页码
        size: this.listQuery.size, //每页条数
        goodsId: this.listQuery.goodsId,
      };
      if (this.listQuery.data != "") {
        requestBody.startDateTime = formateDates(this.listQuery.data[0]);
        requestBody.endDateTime = formateDates(this.listQuery.data[1]);
      }
      this.api({
        url: "/inStock",
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
    goodsNameChange(value) {
      this.goods.forEach((data) => {
        if (data.id == value) {
          this.chooseGoods = data;
        }
      });
    },
    formatterTime(row, column) {
      return formateDate(row.inDate);
    },
    handleUpload() {
      this.$refs["excel-upload-input"].click();
    },
    handleClick(e) {
      const files = e.target.files;
      const rawFile = files[0]; // only use files[0]
      if (!rawFile) return;
      this.upload(rawFile);
    },
    upload(rawFile) {
      this.$refs["excel-upload-input"].value = null; // fix can't select the same excel
      if (!this.beforeUpload) {
        this.readerData(rawFile);
        return;
      }
      const before = this.beforeUpload(rawFile);
      if (before) {
        this.readerData(rawFile);
      }
    },
    readerData(rawFile) {
      this.loading = true;
      return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = (e) => {
          const data = e.target.result;
          const workbook = XLSX.read(data, { type: "array" });
          const firstSheetName = workbook.SheetNames[0];
          const worksheet = workbook.Sheets[firstSheetName];
          const header = this.getHeaderRow(worksheet);
          const result1 = XLSX.utils.sheet_to_json(
            workbook.Sheets[workbook.SheetNames[0]]
          );
          this.result1(result1);

          const result2 = XLSX.utils.sheet_to_json(
            workbook.Sheets[workbook.SheetNames[1]]
          );
          this.result2(result2);

          const result3 = XLSX.utils.sheet_to_json(
            workbook.Sheets[workbook.SheetNames[2]]
          );
          this.result3(result3);

          const result4 = XLSX.utils.sheet_to_json(
            workbook.Sheets[workbook.SheetNames[4]]
          );
          this.result4(result4);

          const result5 = XLSX.utils.sheet_to_json(
            workbook.Sheets[workbook.SheetNames[5]]
          );
          this.result5(result5);
          // this.generateData({ header, results });
          this.loading = false;
          resolve();
        };
        reader.readAsArrayBuffer(rawFile);
      });
    },
    verify(data) {
      // console.log("length",data.goods.bigUnit.length);
      // if (
      //   data.goods.partNumber.length > 20
      // ) {
      //   return false;
      // } else {
      return true;
      // }
    },
    result1(value) {
      value.forEach((data) => {
        const goods = {
          amount: data.来货 ? data.来货 : 10,
          goods: {
            partNumber: data.物料编码,
            title: data.物料名称,
            bigUnit: data.单位,
            smallUnit: data.小计量单位 ? data.小计量单位 : "个",
            repertory: data.转换量 ? data.转换量 : 10,
            category: "蔬菜",
          },
        };
        if (this.verify(goods)) {
          this.vegetables.push(goods);
        } else {
          this.$message({
            message: "字段太长",
            type: "warning",
          });
          return;
        }
      });
      if (this.vegetables.length > 0) {
        this.batchInStore();
      }
    },
    result2(value) {
      value.forEach((data) => {
        const goods = {
          amount: data.来货 ? data.来货 : 10,
          goods: {
            partNumber: data.物料编码,
            title: data.物料名称,
            bigUnit: data.单位,
            smallUnit: data.小计量单位 ? data.小计量单位 : "个",
            repertory: data.转换量 ? data.转换量 : 10,
            category: "食品",
          },
        };
        if (this.verify(goods)) {
          this.vegetables.push(goods);
        } else {
          this.$message({
            message: "字段太长",
            type: "warning",
          });
          return;
        }
      });
      if (this.vegetables.length > 0) {
        this.batchInStore();
      }
    },
    result3(value) {
      value.forEach((data) => {
        const goods = {
          amount: data.来货 ? data.来货 : 10,
          goods: {
            partNumber: data.物料编码,
            title: data.物料名称,
            bigUnit: data.单位,
            smallUnit: data.小计量单位 ? data.小计量单位 : "个",
            repertory: data.转换量 ? data.转换量 : 10,
            category: "底料",
          },
        };
        if (this.verify(goods)) {
          this.vegetables.push(goods);
        } else {
          this.$message({
            message: "字段太长",
            type: "warning",
          });
          return;
        }
      });
      if (this.vegetables.length > 0) {
        this.batchInStore();
      }
    },
    result4(value) {
      value.forEach((data) => {
        const goods = {
          amount: data.来货 ? data.来货 : 10,
          goods: {
            partNumber: data.物料编码,
            title: data.物料名称,
            bigUnit: data.单位,
            smallUnit: data.小计量单位 ? data.小计量单位 : "个",
            repertory: data.转换量 ? data.转换量 : 10,
            category: "冻货",
          },
        };
        if (this.verify(goods)) {
          this.vegetables.push(goods);
        } else {
          this.$message({
            message: "字段太长",
            type: "warning",
          });
          return;
        }
      });
      if (this.vegetables.length > 0) {
        this.batchInStore();
      }
    },
    result5(value) {
      value.forEach((data) => {
        const goods = {
          amount: data.来货 ? data.来货 : 10,
          goods: {
            partNumber: data.物料编码,
            title: data.物料名称,
            bigUnit: data.单位,
            smallUnit: data.小计量单位 ? data.小计量单位 : "个",
            repertory: data.转换量 ? data.转换量 : 10,
            category: "杂货",
          },
        };
        if (this.verify(goods)) {
          this.vegetables.push(goods);
        } else {
          this.$message({
            message: "字段太长",
            type: "warning",
          });
          return;
        }
      });
      if (this.vegetables.length > 0) {
        this.batchInStore();
      }
    },
    batchInStore() {
      const body = this.vegetables.slice(0, 30);
      let num = 0;
      if (this.vegetables.length > 100) {
        num = this.vegetables.length / 100;
      }
      let start = 0;
      let end = 100;
      for (let i = 0; i < num; i++) {
        this.api({
          url: "/inStock",
          method: "post",
          // data: this.vegetables.slice(start, end),
          data: body,
        }).then((data) => {
          if (data.data.code == 200) {
            this.$message({
              message: "批量入库成功！",
              type: "success",
            });
            this.getList();
            this.dialogFormAdd = false;
            start = end;
            end = this.vegetables.length;
          } else {
            this.$message.error(data.data.toString());
          }
        });
      }
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
    isExcel(file) {
      return /\.(xlsx|xls|csv)$/.test(file.name);
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
      this.tempArticle.amount = "";
      this.tempArticle.title = "";
      this.tempArticle.remark = "";
      this.tempArticle.goodsId = "";
      this.tempArticle.inDate = "";
      this.tempArticle.adminId = "";
      this.tempArticle.partNumber = "";
      this.tempArticle.bigUnit = "";
      this.tempArticle.inventory = "";
      this.tempArticle.repertory = "";
      this.tempArticle.smallUnit = "";
      this.dialogFormAdd = true;
    },
    showUpdate($index) {
      //显示修改对话框
      this.tempArticle.id = this.list[$index].id;
      this.tempArticle.amount = this.list[$index].amount;
      this.tempArticle.partNumber = this.list[$index].partNumber;
      this.tempArticle.title = this.list[$index].title;
      this.tempArticle.remark = this.list[$index].remark;
      this.tempArticle.goodsId = this.list[$index].goodsId;
      this.tempArticle.inDate = this.list[$index].inDate;
      this.tempArticle.bigUnit = this.list[$index].bigUnit;
      this.tempArticle.inventory = this.list[$index].inventory;
      this.tempArticle.repertory = this.list[$index].repertory;
      this.tempArticle.smallUnit = this.list[$index].smallUnit;
      this.dialogFormUpdate = true;
    },
    createEmployee(tempArticle) {
      const num = this.tempArticle.amount * this.chooseGoods.repertory;
      const body = [
        {
          amount: num,
          goodsId: this.tempArticle.goodsId,
          remark: this.tempArticle.remark,
        },
      ];
      this.$refs[tempArticle].validate((valid) => {
        if (valid) {
          this.api({
            url: "/inStock",
            method: "post",
            data: body,
          }).then((data) => {
            if (data.data.code == 200) {
              this.$message({
                message: "成功入库！",
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
      let num = 0;
      if (this.chooseGoods == "") {
        num = this.tempArticle.amount * this.tempArticle.repertory;
      } else {
        num = this.tempArticle.amount * this.chooseGoods.repertory;
      }
      const body = {
        id: this.tempArticle.id,
        amount: num,
        goodsId: this.tempArticle.goodsId,
        remark: this.tempArticle.remark,
      };
      this.$refs[tempArticle].validate((valid) => {
        if (valid) {
          this.api({
            url: "/inStock",
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
              this.$message.error(data.data);
            }
          });
        }
      });
    },
    deleteAdministrator($index) {
      this.$confirm("确定删除这条入库信息吗?", "提示", {
        confirmButtonText: "确定",
        showCancelButton: false,
        type: "warning",
      }).then(() => {
        this.api({
          url: "/inStock",
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
.num {
  margin-top: 25px;
  height: 50px;
}
.excel-upload-input {
  display: none;
  z-index: -9999;
}
</style>
