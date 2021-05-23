<template>
  <div class="app-container">
    <upload-excel-component
      :on-success="handleSuccess"
      :before-upload="beforeUpload"
    />
    <el-table
      :data="tableData"
      border
      highlight-current-row
      style="width: 100%; margin-top: 20px"
    >
      <el-table-column
        v-for="item of tableHeader"
        :key="item"
        :prop="item"
        :label="item"
      />
    </el-table>
  </div>
</template>
<script>
import UploadExcelComponent from "./index";
export default {
  name: "import",
  components: { UploadExcelComponent },
  data() {
    return {
      tableData: [],
      tableHeader: [],
      filename: '',
       downloadLoading: false
    };
  },
  methods: {
    handleDownload() {
     
      if (this.tableData.length > 0) {
        this.downloadLoading = true;
        import("./Export2Excel").then((excel) => {
          const tHeader = ["Id", "Title", "Author", "Readings", "Date"];
          const filterVal = ['Id', 'Title', 'Author', 'Readings', 'Date'];
          const list = this.tableData;
          const data = this.formatJson(filterVal, list);
          excel.export_json_to_excel({
            header: tHeader,
            data,
            filename: this.filename,
          });
          this.downloadLoading = false;
        });
      } else {
        this.$message({
          message: "Please select at least one item",
          type: "warning",
        });
      }
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map((v) => filterVal.map((j) => v[j]));
    },
    beforeUpload(file) {
      const isLt1M = file.size / 1024 / 1024 < 1;

      if (isLt1M) {
        return true;
      }

      this.$message({
        message: "Please do not upload files larger than 1m in size.",
        type: "warning",
      });
      return false;
    },
    handleSuccess({ results, header }) {
      this.tableData = results;
      this.tableHeader = header;
    },
  },
};
</script>
