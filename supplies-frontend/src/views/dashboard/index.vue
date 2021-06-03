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
          <el-row :gutter="10">
            <el-col :xs="12" :sm="12" :md="12" :lg="6" :xl="6" :span="6">
              <el-form-item label="物料名称">
                <el-select
                  ref="agent1Select"
                  @hook:mounted="cancalReadOnly"
                  @visible-change="cancalReadOnly"
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
            <el-col :xs="12" :sm="12" :md="12" :lg="8" :xl="8">
              <el-form-item label="物料编码">
                <el-input
                  class="input"
                  placeholder="请输入物料编码"
                  maxlength="20"
                  v-model="listQuery.partNumber"
                >
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :xs="12" :sm="12" :md="12" :lg="6" :xl="6">
              <el-form-item label="物料类别">
                <el-select
                  ref="agent2Select"
                  @hook:mounted="cancalReadOnly"
                  @visible-change="cancalReadOnly"
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
            <el-col :xs="12" :sm="12" :md="12" :lg="2" :xl="2">
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
      <el-row :gutter="10">
        <el-col
          :xs="12"
          :sm="12"
          :md="8"
          :lg="6"
          :xl="6"
          v-for="(item, index) in list"
          :key="item.id"
        >
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
              <img v-else src="../login/log.png" class="image" />

              <div style="padding: 14px" class="cardDiv">
                <span>{{ item.title }}&nbsp;&nbsp;</span>
                <span v-if="item.partNumber">({{ item.partNumber }})</span>
                <div>
                  库存数量：
                  <span v-if="item.inventory > 0">
                    <el-tag
                      type="success"
                      v-if="parseInt(item.inventory / item.repertory) > 0"
                      size="small"
                    >
                      {{ parseInt(item.inventory / item.repertory) }}
                      {{ item.bigUnit }}
                    </el-tag>
                    <span v-if="item.inventory % item.repertory > 0">
                      <el-tag type="warning" size="small"> 零 </el-tag>
                      <el-tag type="primary" size="small">
                        {{ item.inventory % item.repertory }}
                        {{ item.smallUnit }}
                      </el-tag>
                    </span>
                  </span>
                  <span v-else>
                    <el-tag type="success" size="small">
                      0 {{ item.bigUnit }}
                    </el-tag>
                  </span>
                </div>
                <div v-if="item.smallUnit">
                  转换量： &nbsp;一{{ item.bigUnit }}有{{ item.repertory
                  }}{{ item.smallUnit }}
                </div>
                <div v-else>转换量： &nbsp;{{ item.repertory }}</div>
                <div class="bottom">
                  <el-row>
                    <el-col :span="10">
                      <el-button
                        type="primary"
                        size="mini"
                        plain
                        class="button"
                        @click="outstorage(index)"
                        >点击领用</el-button
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
        <el-table-column align="center" prop="partNumber" label="物料编码" />
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
        <el-form-item label="物料编码" prop="partNumber">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.partNumber"
            maxlength="20"
          ></el-input>
        </el-form-item>
        <el-form-item label="物料名称" prop="title">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.title"
            maxlength="50"
          ></el-input>
        </el-form-item>
        <el-form-item label="计量单位（大）" prop="bigUnit">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.bigUnit"
            maxlength="10"
          ></el-input>
        </el-form-item>
        <el-form-item label="计量单位（小）" prop="smallUnit">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.smallUnit"
            maxlength="10"
          ></el-input>
        </el-form-item>
        <el-form-item label="转换量" prop="repertory">
          <el-input-number
            style="width: 100%"
            v-model.trim="tempArticle.repertory"
            placeholder="大单位转小单位的转化量"
            :min="1"
            :max="999999"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="物料类别" prop="category">
          <el-select
            ref="agent3Select"
            @hook:mounted="cancalReadOnly"
            @visible-change="cancalReadOnly"
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
          <el-image
            v-if="imageUrl !== ''"
            style="width: 100px; height: 100px"
            :src="imageUrl"
          ></el-image>
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
        <el-form-item label="物料编码" prop="partNumber">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.partNumber"
            maxlength="20"
          ></el-input>
        </el-form-item>
        <el-form-item label="物料名称" prop="title">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.title"
            maxlength="50"
          ></el-input>
        </el-form-item>
        <el-form-item label="计量单位（大）" prop="bigUnit">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.bigUnit"
            maxlength="10"
          ></el-input>
        </el-form-item>
        <el-form-item label="计量单位（小）" prop="smallUnit">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.smallUnit"
            maxlength="10"
          ></el-input>
        </el-form-item>
        <el-form-item label="转换量" prop="repertory">
          <el-input-number
            style="width: 100%"
            v-model.trim="tempArticle.repertory"
            placeholder="大单位转小单位的转化量"
            :min="1"
            :max="999999"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="物料类别" prop="category">
          <el-select
            ref="agent4Select"
            @hook:mounted="cancalReadOnly"
            @visible-change="cancalReadOnly"
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
          <el-image
            v-if="tempArticle.image"
            style="width: 100px; height: 100px"
            :src="getImgUrl(tempArticle.image)"
          ></el-image>
        </el-form-item>
        <el-form-item class="dialog_footer">
          <el-button @click="dialogFormUpdate = false">取 消</el-button>
          <el-button type="success" @click="updateAdministrator('tempArticle')"
            >修改</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog title="物料领用" :visible.sync="dialogOutStorage" width="40%">
      <el-form
        :rules="dataVerify"
        class="small-space"
        :model="tempArticle"
        label-position="left"
        label-width="80px"
        style="margin: 10px"
        ref="tempArticle"
      >
        <el-form-item label="物料编码" prop="partNumber">
          <el-input
            style="width: 100%"
            v-model.trim="tempArticle.partNumber"
            maxlength="100"
          ></el-input>
        </el-form-item>
        <el-form-item label="物料名称" prop="goodsId">
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
              :label="item.title"
              :value="item.id"
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
        <el-form-item
          v-if="tempArticle.smallUnit"
          label="数量(小)"
          prop="smallUnit"
        >
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
        <el-form-item label="物料类别" prop="category">
          <el-select
            ref="agent7Select"
            @hook:mounted="cancalReadOnly"
            @visible-change="cancalReadOnly"
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
        size: 12, //每页条数
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
        amount: 0,
        bigAmount: "",
      },
      dataVerify: {
        partNumber: [
          { message: "请输入物料编码", trigger: "blur" },
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
            min: 0,
            max: 10,
            message: "长度在 1 到 10 个字符",
            trigger: "blur",
          },
        ],
        smallUnit: [
          { message: "请输入计量单位(小)", trigger: "blur" },
          {
            min: 0,
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
          { required: true, message: "请输入物料数量", trigger: "blur" },
        ],
        unit: [{ message: "请选择计量单位", trigger: "blur" }],
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
      imageUrl: "",
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
          this.imageUrl = this.getImgUrl(this.tempArticle.image);
          // this.$message.success(data.data.message);
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
              this.$message.error(data.data.message);
            }
          });
        }
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
    createoutstorages(tempArticle) {
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
        this.$refs[tempArticle].validate((valid) => {
          if (valid) {
            this.api({
              url: "/outStock",
              method: "post",
              data: body,
            }).then((data) => {
              if (data.data.code == 200) {
                this.$message({
                  message: "领用出库成功！",
                  type: "success",
                });
                this.getList();
                this.dialogOutStorage = false;
              } else {
                this.$message.error(data.data.message);
              }
            });
          }
        });
      }
    },
    updateAdministrator(tempArticle) {
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
              this.$message.error(data.data.message);
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
            }else {
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
.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 10px;
  margin-bottom: 10px;
  line-height: 12px;
}

.image {
  width: 100%;
  height: 200px;
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
  height: 160px;
  padding: 10px;
  line-height: 25px;
}
</style>
