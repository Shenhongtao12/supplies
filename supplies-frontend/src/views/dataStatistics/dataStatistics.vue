<template>
  <div class="app-container">
    <div class="filter-container">
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
                  style="width: 100%"
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
              <el-form-item label="物料编码">
                <el-input
                  class="input"
                  placeholder="请输入物料编码"
                  v-model="listQuery.partNumber"
                  maxlength="20"
                  style="width: 100%"
                >
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :xs="12" :sm="12" :md="12" :lg="6" :xl="6">
              <el-form-item label="时间">
                <el-date-picker
                  v-model="listQuery.data"
                  type="month"
                  placeholder="选择日期"
                  style="width: 100%"
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
      <el-table-column
        align="center"
        prop="partNumber"
        label="物料编码"
        column-key="partNumber"
      />
      <el-table-column
        align="center"
        prop="title"
        label="物料名称"
        column-key="title"
      />
      <el-table-column align="center" label="领用数量">
        <template slot-scope="scope">
          <span v-if="parseInt(scope.row.outStock / scope.row.repertory) > 0"
            >{{ parseInt(scope.row.outStock / scope.row.repertory) }}
            {{ scope.row.bigUnit }}</span
          >
          <span v-if="scope.row.outStock % scope.row.repertory > 0"
            >{{ scope.row.outStock % scope.row.repertory }}
            {{ scope.row.smallUnit }}</span
          >
        </template>
      </el-table-column>
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
      <el-table-column
        align="center"
        prop="date"
        label="时间"
        column-key="date"
      ></el-table-column>
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
  </div>
</template>
<script>
import { formateDates } from "@/utils/index";
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
        partNumber: "",
        title: "",
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
        inDate: "",
        name: "",
        workNumber: "",
      },
      goods: [],
    };
  },
  created() {
    this.getList();
    this.queryGoods();
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
        }
      });
    },
    getList() {
      let query = { ...this.listQuery };
      if (this.listQuery.data) {
        query.date = formateDates(this.listQuery.data);
      }
      this.listLoading = true;
      this.api({
        url: "/statistical",
        method: "get",
        params: query,
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
    queryList(listQuery) {
      this.listLoading = true;
      this.$refs[listQuery].validate((valid) => {
        if (valid) {
          this.api({
            url: "/statistical",
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
/* .dialog_footer {
  margin-left: 40%;
} */
</style>
