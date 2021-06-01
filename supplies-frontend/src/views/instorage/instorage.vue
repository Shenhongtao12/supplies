<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" @click="showCreate"
            >物料入库
          </el-button>
          <el-button
            style="margin-left: 16px"
            type="primary"
            @click="showExcelDiglog()"
          >
            Excel 导入
          </el-button>
        </el-form-item>
      </el-form>
      <div class="query">
        <el-form ref="form" :model="listQuery" label-width="80px">
          <el-row :gutter="10">
            <el-col :xs="12" :sm="8" :md="8" :lg="8" :xl="8">
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
            <el-col :xs="12" :sm="8" :md="8" :lg="8" :xl="8">
              <el-form-item label="领用时间">
                <el-date-picker
                  v-model="listQuery.data"
                  style="width: 100%"
                  type="daterange"
                  range-separator=""
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  size="small"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :xs="12" :sm="8" :md="8" :lg="8" :xl="8">
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
      <el-table-column align="center" label="库存数量" >
        <template slot-scope="scope">
          <span v-if="parseInt(scope.row.inventory / scope.row.repertory) > 0">{{parseInt(scope.row.inventory / scope.row.repertory)}} {{scope.row.bigUnit}}</span>
          <span v-if="(scope.row.inventory % scope.row.repertory) > 0">{{scope.row.inventory % scope.row.repertory}} {{scope.row.smallUnit}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="入库数量" >
        <template slot-scope="scope">
          <span v-if="parseInt(scope.row.amount / scope.row.repertory) > 0">{{parseInt(scope.row.amount / scope.row.repertory)}} {{scope.row.bigUnit}}</span>
          <span v-if="(scope.row.amount % scope.row.repertory) > 0">{{scope.row.amount % scope.row.repertory}} {{scope.row.smallUnit}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="repertory" label="转换量">
        <template slot-scope="scope">
          <p v-if="scope.row.smallUnit">
            转换量：一{{ scope.row.bigUnit }}有{{
              scope.row.repertory
            }}{{ scope.row.smallUnit }}
          </p>
          <p v-else>转换量：{{ scope.row.repertory }}</p>
        </template>
      </el-table-column>
      <!-- <el-table-column align="center" prop="bigUnit" label="计量单位（大）" />
      <el-table-column align="center" prop="smallUnit" label="计量单位（小）" /> -->
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
              :label="getGoodsName(item)"
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
              :label="getGoodsName(item)"
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
                tempArticle.bigUnit
              }}
            </el-col>
          </el-row>
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
    <el-dialog title="Excel导入"
      :before-close="handleClose" :visible.sync="showExcelDig">
      <div style="display: flex; flex-direction: row;">
        <el-input style="width: 35%"
        placeholder="请上传文件"
        v-model="excelName"
        :disabled="true">
      </el-input>
      <input
        ref="excel-upload-input"
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
        <a href="../../../static/template.xlsx" style="color: #79BBFF; margin-top: 12px" download="新开货模板.xlsx">
          下载Excel模板<i class="el-icon-download"></i>
        </a>
      </div>
      
      <el-table :data="errorResponse" max-height="320">
        <el-table-column property="partNumber" label="物料编码" width="100px">
        </el-table-column>
        <el-table-column property="title" label="物料名称" width="150px">
        </el-table-column>
        <el-table-column property="errorMessage" label="错误提示信息" width="200px">
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>
<script>
import { formateDate, formateDates } from "@/utils/index";
import XLSX from "xlsx";

export default {
  data() {
    return {
      batch: 100,
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
      showExcelDig: false,
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
      errorResponse: [],
      excelTitle:["蔬菜", "食品", "底料", "冻货", "杂货"],
      excelName: ""
    };
  },
  created() {
    this.getList();
    this.queryGoods();
  },
  methods: {
    showExcelDiglog() {
      this.showExcelDig = true;
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
    getGoodsName(item) {
      return "【" + item.partNumber + "】 " + item.title;
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
      if (!this.isExcel(rawFile)) {
        this.$message.error("请选择正确的Excel文件");
        this.loading = false;
        return;
      }
      this.upload(rawFile);
    },
    async upload(rawFile) {
      this.$refs["excel-upload-input"].value = null; // fix can't select the same excel
      this.excelName = rawFile.name;
      if (this.beforeUpload(rawFile)) {
        let data = await this.readerData(rawFile);
        let apiList = [];
        let errorList = [];
        for (let i = 0; i < this.excelTitle.length; i++) {
          let request = this.result(data['result' + (i+1)], this.excelTitle[i]);
          let error = request.filter(x => (!x.isSuccess));
          if (error && error.length > 0) {
            for (let err of error) {
              errorList.push({
                isSuccess: err.isSuccess,
                errorMessage: err.errorMessage,
                partNumber: err.goods.partNumber,
                title: err.goods.title
              })
            }
          }
          request = request.filter((x) => x.isSuccess);
          if (request.length > 0) {
            this.batchInStore(request, apiList);
          }
        }
        let response = await this.batchHttp(apiList);
        for (let res of response) {
          let errorRes = res.data.data.filter(x => !x.isSuccess);
          if (errorRes && errorRes[0]) {
            errorList = [
              ...errorList, errorRes
            ];
          }
          if (res.data.code !== 200) {
             this.$message({
                message: res.data.message,
                type: "error",
                duration: 0
              });
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
      let excelData = {};
      return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = (e) => {
          const data = e.target.result;
          const workbook = XLSX.read(data, { type: "array" });
          const title = workbook.SheetNames;
          const firstSheetName = workbook.SheetNames[0];
          const result1 = workbook.Sheets[firstSheetName];
          let header = this.getHeaderRow(result1);
          if (!this.checkExcelTitle(title) && !this.checkHeader(header)) {
            this.$message.error("Excel不正确，请下载正确的模板");
            this.loading = false;
            return;
          }
          excelData.result1 = XLSX.utils.sheet_to_json(result1);
          let result2 = workbook.Sheets[workbook.SheetNames[1]];
          header = this.getHeaderRow(result2);
          if (!this.checkHeader(header)) {
            this.$message.error("Excel'食品'页格式不正确，请下载正确的模板");
            this.loading = false;
            return;
          }
          excelData.result2 = XLSX.utils.sheet_to_json(result2);

          let result3 = workbook.Sheets[workbook.SheetNames[2]];
          header = this.getHeaderRow(result3);
          if (!this.checkHeader(header)) {
            this.$message.error("Excel'底料'页格式不正确，请下载正确的模板");
            this.loading = false;
            return;
          }
          excelData.result3 = XLSX.utils.sheet_to_json(result3);

          let result4 = workbook.Sheets[workbook.SheetNames[3]];
          header = this.getHeaderRow(result4);
          if (!this.checkHeader(header)) {
            this.$message.error("Excel'冻货'页格式不正确，请下载正确的模板");
            this.loading = false;
            return;
          }
          excelData.result4 = XLSX.utils.sheet_to_json(result4);

          let result5 = workbook.Sheets[workbook.SheetNames[4]];
          header = this.getHeaderRow(result5);
          if (
              header[0] !== "物料名称" ||
              header[1] !== "单位" ||
              header[2] !== "来货" ||
              header[5] !== "小计量单位" ||
              header[6] !== "转换量") 
            {
            this.$message.error("Excel杂货页格式不正确，请下载正确的模板");
            this.loading = false;
            return;
          }
          excelData.result5 = XLSX.utils.sheet_to_json(result5);
          resolve(excelData);
        };
        reader.readAsArrayBuffer(rawFile);
      });
    },
    verify(data) {
      if(!data.goods.partNumber && !data.goods.title){
        return;
      }
      if (data.goods.partNumber && (
        data.goods.partNumber.length > 20 ||
        data.goods.partNumber.length < 4)
      ) {
        data.isSuccess = false;
        data.errorMessage = "物料编码长度应4-20字符";
        return;
      }

      if (!data.goods.title || data.goods.title === "") {
        data.isSuccess = false;
        data.errorMessage = "物料名称不能为空";
        return;
      }
      if (data.goods.title.length > 50 || data.goods.title.length < 1) {
        data.isSuccess = false;
        data.errorMessage = "物料名称长度应在1-50字符";
        return;
      }

      if (!data.goods.bigUnit || data.goods.bigUnit === "") {
        data.isSuccess = false;
        data.errorMessage = "计量单位不能为空";
        return;
      }
      if (data.goods.bigUnit.length > 10 || data.goods.bigUnit.length < 1) {
        data.isSuccess = false;
        data.errorMessage = "计量单位长度应1-10字符";
      }
    },

    result(value, category) {
      let request = [];
      value.forEach((data) => {
        let req = {
          amount: data["来货"] ? data["来货"] : 0,
          goods: {
            partNumber: data["物料编码"] ? this.strTrim(data["物料编码"].toString()) : null,
            title: data["物料名称"] ? this.strTrim(data["物料名称"].toString()) : null,
            bigUnit: this.strTrim(data["单位"]),
            smallUnit: this.strTrim(data["小计量单位"]),
            repertory: data["转换量"] ? data["转换量"] : 1,
            category: category,
          },
          isSuccess: true,
        };
        this.verify(req);
        request.push(req);
      });
      return request.filter(x => !(!x.goods.partNumber && !x.goods.title));
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
            url: "/inStock",
            method: "post",
            data: request.slice(i * this.batch, (i + 1) * this.batch - 1),
          })
        )
      }
    },
    checkExcelTitle(title) {
      if (
        title.length >= 5 &&
        title[0] == this.excelTitle[0] &&
        title[1] == this.excelTitle[1] &&
        title[2] == this.excelTitle[2] &&
        title[3] == this.excelTitle[3] &&
        title[4] == this.excelTitle[4]
      ) {
        return true;
      }
      return false;
    },
    checkHeader(header) {
      if (
        header.length >= 8 &&
        header[0] == "物料编码" &&
        header[1] == "物料名称" &&
        header[2] == "单位" &&
        header[3] == "来货" &&
        header[6] == "小计量单位" &&
        header[7] == "转换量"
      ) {
        return true;
      }
      return false;
    },
    strTrim(str) {
      if (!str || str == "") {
        return null;
      }
      return str.trim();
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
      this.tempArticle.amount = parseInt(this.list[$index].amount / this.list[$index].repertory);
      //this.tempArticle.amount = this.list[$index].amount;
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
              this.$message.error(data.data.message);
            }
          });
        }
      });
    },
    updateAdministrator(tempArticle) {
      let num = 0;
      //if (this.chooseGoods == "") {
        num = this.tempArticle.amount * this.tempArticle.repertory;
      /* } else {
        num = this.tempArticle.amount * this.chooseGoods.repertory;
      } */
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
              this.$message.error(data.data.message);
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
  height: auto;
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
