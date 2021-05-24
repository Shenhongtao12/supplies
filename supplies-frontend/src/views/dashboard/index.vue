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
            <el-col :span="6">
              <el-form-item label="物料名称">
                <el-select
                  v-model="listQuery.title"
                  placeholder="请选择物料"
                  filterable
                  clearable
                >
                  <el-option
                    v-for="item in goods"
                    :key="item.id"
                    :label="item.title"
                    :value="item.title"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="物料编号">
                <el-input
                  class="input"
                  placeholder="请输入物料编号"
                  v-model="listQuery.partNumber"
                  style="width: 80%"
                >
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="物料类别">
                <el-select
                  v-model="listQuery.category"
                  placeholder="请选择物料类别"
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
    <div>
      <el-row>
        <el-col :span="6" v-for="(item, index) in list" :key="item.id">
          <el-card
            :body-style="{ padding: '0px' }"
            class="card"
            :data="list"
            :default-sort="{ prop: 'date', order: 'descending' }"
          >
            <template slot-scope="scope">
              <img
                v-if="item.image"
                :src="getImgUrl(item.image)"
                class="image"
                width="100%"
                height="100%"
              />
              <img
                v-if="!item.image"
                src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"
                class="image"
              />

              <div style="padding: 14px" class="cardDiv">
                <span>{{ item.title }}&nbsp;&nbsp;({{ item.partNumber }})</span>
                <p>
                  总库存：
                  <el-tag type="success" size="small">{{
                    item.inventory
                  }}</el-tag>
                  &nbsp;&nbsp;&nbsp;{{ item.smallUnit }}
                </p>
                <p>
                  转换量： &nbsp;&nbsp;&nbsp;一{{ item.bigUnit }}有{{
                    item.repertory
                  }}{{ item.smallUnit }}
                </p>
                <div class="bottom clearfix">
                  <time class="time"></time>
                  <el-row>
                    <el-col :span="10">
                      <el-button
                        type="primary"
                        size="mini"
                        plain
                        class="button"
                        @click="outstorage(index)"
                        >点击出库</el-button
                      >
                    </el-col>
                    <el-col :span="7">
                      <el-button
                        type="warning"
                        size="mini"
                        plain
                        class="button1"
                        @click="showUpdate(index)"
                        >修改</el-button
                      >
                    </el-col>
                    <el-col :span="7">
                      <el-button
                        type="danger"
                        size="mini"
                        plain
                        class="button1"
                        @click="deleteAdministrator(index)"
                        >删除</el-button
                      >
                    </el-col>
                  </el-row>
                </div>
              </div>
            </template>
          </el-card>
        </el-col>
      </el-row>
    </div>
    <div>
      <!-- <el-table
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
        <el-table-column align="center" prop="bigUnit" label="计量单位（大）" />
        <el-table-column
          align="center"
          prop="smallUnit"
          label="计量单位（小）"
        />
        <el-table-column align="center" prop="repertory" label="转换量" />
        <el-table-column align="center" prop="category" label="物料类别" />
        <el-table-column align="center" prop="inventory" label="总库存" />
        <el-table-column
          align="center"
          prop="inDate"
          label="创建时间"
          :formatter="formatterTime"
          column-key="inDate"
        />
        <el-table-column align="center" label="出入库管理">
          <template slot-scope="scope">
            <el-button
              type="primary"
              size="mini"
              icon="edit"
              plain
              @click="showUpdate(scope.$index)"
              >入库</el-button
            >
            <el-button
              type="primary"
              size="mini"
              icon="edit"
              plain
              @click="outstorage(scope.$index)"
              >出库</el-button
            >
          </template>
        </el-table-column>
        <el-table-column align="center" label="管理">
          <template slot-scope="scope">
            <el-button
              type="success"
              size="mini"
              icon="edit"
              @click="showUpdate(scope.$index)"
              >修改</el-button
            >
            <el-button
              type="danger"
              size="mini"
              icon="edit"
              @click="deleteAdministrator(scope.$index)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table> -->
    </div>
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
    <el-dialog title="添加物料" :visible.sync="dialogFormAdd" width="40%">
      <el-form
        :rules="dataVerify"
        class="small-space"
        :model="tempArticle"
        label-position="left"
        label-width="120px"
        style="margin: 10px"
        ref="tempArticle"
      >
        <el-form-item label="物料编号" prop="partNumber">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.partNumber"
            maxlength="100"
          ></el-input>
        </el-form-item>
        <el-form-item label="物料名称" prop="title">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.title"
            maxlength="100"
          ></el-input>
        </el-form-item>
        <el-form-item label="计量单位（大）" prop="bigUnit">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.bigUnit"
            maxlength="100"
          ></el-input>
        </el-form-item>
        <el-form-item label="计量单位（小）" prop="smallUnit">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.smallUnit"
            maxlength="100"
          ></el-input>
        </el-form-item>
        <el-form-item label="转换量" prop="repertory">
          <el-input-number
            style="width: 100%"
            v-model.trim="tempArticle.repertory"
            placeholder="大单位转小单位的转化量"
            maxlength="100"
            :min="1"
            :max="999999"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="物料类别" prop="category">
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
        <el-form-item label="物料图片">
          <el-upload
            class="upload-demo"
            action=""
            multiple
            :on-change="handleChange"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">
              只能上传jpg/png文件，且不超过500kb
            </div>
          </el-upload>
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
        label-width="120px"
        style="margin: 10px"
        ref="tempArticle"
      >
        <el-form-item label="物料编号" prop="partNumber">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.partNumber"
            maxlength="100"
          ></el-input>
        </el-form-item>
        <el-form-item label="物料名称" prop="title">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.title"
            maxlength="100"
          ></el-input>
        </el-form-item>
        <el-form-item label="计量单位（大）" prop="bigUnit">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.bigUnit"
            maxlength="100"
          ></el-input>
        </el-form-item>
        <el-form-item label="计量单位（小）" prop="smallUnit">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.smallUnit"
            maxlength="100"
          ></el-input>
        </el-form-item>
        <el-form-item label="转换量" prop="repertory">
          <el-input-number
            style="width: 100%"
            v-model.trim="tempArticle.repertory"
            placeholder="大单位转小单位的转化量"
            maxlength="100"
            :min="1"
            :max="999999"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="物料类别" prop="category">
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
        <el-form-item label="物料图片">
          <el-upload
            class="upload-demo"
            action=""
            multiple
            :on-change="handleChangeUpdate"
          >
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">
              只能上传jpg/png文件，且不超过500kb
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item class="dialog_footer">
          <el-button @click="dialogFormUpdate = false">取 消</el-button>
          <el-button type="success" @click="updateAdministrator('tempArticle')"
            >修改</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog title="物料出库" :visible.sync="dialogOutStorage" width="40%">
      <el-form
        :rules="dataVerify"
        class="small-space"
        :model="tempArticle"
        label-position="left"
        label-width="80px"
        style="margin: 10px"
        ref="tempArticle"
      >
        <el-form-item label="物料编号" prop="partNumber">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.partNumber"
            maxlength="100"
          ></el-input>
        </el-form-item>
        <el-form-item label="物料名称" prop="goodsId">
          <el-select
            v-model="tempArticle.goodsId"
            placeholder="请选择物料"
            style="width: 100%"
            filterable
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
        <el-form-item label="员工" prop="userId">
          <el-select
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

        <el-form-item label="数量" prop="amount">
          <el-row>
            <el-col :span="18">
              <el-input-number
                style="width: 100%"
                v-model.trim="tempArticle.amount"
                placeholder="请输入数量（小计量单位）"
                maxlength="100"
                :min="1"
                :max="999999"
              ></el-input-number
            ></el-col>
            <el-col :span="5">
              &nbsp; &nbsp;{{ tempArticle.smallUnit }}
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="物料类别" prop="category">
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
          <el-button @click="dialogOutStorage = false">取 消</el-button>
          <el-button type="success" @click="createoutstorages('tempArticle')"
            >确认</el-button
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
        page: 1, //页码
        size: 10, //每页条数
        partNumber: "",
        title: "",
        category: "",
      },
      dialogStatus: "create",
      dialogFormAdd: false,
      dialogOutStorage: false,
      dialogFormUpdate: false,
      textMap: {
        update: "编辑",
        create: "创建文章",
      },
      tempArticle: {
        id: "",
        partNumber: "",
        title: "",
        bigUnit: "",
        smallUnit: "",
        repertory: 0,
        inventory: "",
        category: "",
        adminId: "",
        image: "",
        inDate: "",
        goodsId: "",
      },
      dataVerify: {
        partNumber: [
          { required: true, message: "请输入物料编号", trigger: "blur" },
          {
            min: 4,
            max: 20,
            message: "长度在 4 到 20 个字符",
            trigger: "blur",
          },
        ],
        title: [
          {
            required: true,
            required: true,
            message: "请输入物料名称",
            trigger: "blur",
          },
          {
            min: 1,
            max: 50,
            message: "长度在 1 到 50 个字符",
            trigger: "blur",
          },
        ],
        bigUnit: [
          { required: true, message: "请输入计量单位(大)", trigger: "blur" },
          {
            min: 1,
            max: 10,
            message: "长度在 1 到 10 个字符",
            trigger: "blur",
          },
        ],
        smallUnit: [
          { required: true, message: "请输入计量单位(小)", trigger: "blur" },
          {
            min: 1,
            max: 10,
            message: "长度在 1 到 10 个字符",
            trigger: "blur",
          },
        ],
        repertory: [{ required: true, message: "转换量", trigger: "blur" }],
        userId: [
          { required: true, message: "请输入员工姓名", trigger: "blur" },
        ],
        goodsId: [
          { required: true, message: "请输入物料名称", trigger: "blur" },
        ],
        amount: [
          { required: true, message: "请输入物料名称", trigger: "blur" },
        ],
        category: [
          { required: true, message: "物料类别", trigger: "blur" },
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
      fileList: [],
      fileList1: [],
      goods: [],
      users: [],
      outstorageData: {},
    };
  },
  created() {
    this.getList();
    this.queryGoods();
    this.queryUsers();
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
    //上传图片
    handleChange(file, fileList1) {
      let fd = new FormData();
      fd.append("name", file.name);
      fd.append("type", "类型一");
      fd.append("file", file.raw);
      const name1 = {
        name: file.name,
      };
      this.uploadImage(fd);
    },
    handleChangeUpdate(file, fileList1) {
      let fd = new FormData();
      fd.append("name", file.name);
      fd.append("type", "类型一");
      fd.append("file", file.raw);
      const name = {
        name: file.name,
      };
      this.uploadImage(fd);
    },
    uploadImage(fileData) {
      this.api({
        url: `/upload/image`,
        method: "post",
        data: fileData,
        headers: {
          "Content-Type": "multipart/form-data; ",
        },
      }).then((data) => {
        if (data.data.code == 200) {
          this.tempArticle.image = data.data.data.thumbnailName;
          this.$message.success(data.data.message);
        } else {
          this.$message.error(data.data.message);
        }
      });
    },
    getImgUrl(i) {
      const url = "http://47.98.128.88:8888/supplies/images/" + i;
      return url;
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
      this.tempArticle.bigUnit = "";
      this.tempArticle.image = "";

      this.tempArticle.smallUnit = "";
      this.tempArticle.repertory = 0;
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
      this.tempArticle.bigUnit = this.list[$index].bigUnit;
      this.tempArticle.smallUnit = this.list[$index].smallUnit;
      this.tempArticle.repertory = this.list[$index].repertory;
      this.tempArticle.image = this.list[$index].image;
      this.dialogFormUpdate = true;
    },

    outstorage($index) {
      // 显示出库对话框
      this.tempArticle.id = this.list[$index].id;
      this.tempArticle.partNumber = this.list[$index].partNumber;
      this.tempArticle.goodsId = this.list[$index].id;
      this.tempArticle.title = this.list[$index].title;
      this.tempArticle.inventory = this.list[$index].inventory;
      this.tempArticle.category = this.list[$index].category;
      this.tempArticle.inDate = this.list[$index].inDate;
      this.tempArticle.bigUnit = this.list[$index].bigUnit;
      this.tempArticle.smallUnit = this.list[$index].smallUnit;
      this.tempArticle.repertory = this.list[$index].repertory;
      this.dialogOutStorage = true;
    },
    createEmployee(tempArticle) {
      delete this.tempArticle.adminId;
      delete this.tempArticle.inDate;
      delete this.tempArticle.id;
      delete this.tempArticle.inventory;
      this.$refs[tempArticle].validate((valid) => {
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
              this.fileList = [];
            } else {
              this.$message.error(data.data);
            }
          });
        }
      });
    },
    createoutstorages(tempArticle) {
      delete this.tempArticle.adminId;
      delete this.tempArticle.inDate;
      delete this.tempArticle.id;
      delete this.tempArticle.partNumber;
      delete this.tempArticle.title;
      this.$refs[tempArticle].validate((valid) => {
        if (valid) {
          this.api({
            url: "/outStock",
            method: "post",
            data: this.tempArticle,
          }).then((data) => {
            if (data.data.code == 200) {
              this.$message({
                message: "出库成功！",
                type: "success",
              });
              this.getList();
              this.dialogOutStorage = false;
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
              this.fileList = [];
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
.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.image {
  width: 100%;
  display: block;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
}
.card {
  width: 90%;
  height: 20%;
  margin-top: 10%;
  margin-left: 6%;
  font-size: 13px;
}
.cardDiv {
  height: 180px;
  padding: 10px;
}
</style>
