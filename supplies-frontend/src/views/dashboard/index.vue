<template>
  <div class="app-container">首页</div>
</template>
<script>
// import { fetchList } from "@/api/article";
// import { parseTime } from "@/utils";
export default {
  name: "dashboard",
  data() {
    return {
      list: null,
      listLoading: true,
      multipleSelection: [],
      downloadLoading: false,
      filename: "",
    };
  },
  created() {
    this.fetchData();
    // this.queryGoods();
    // this.queryUsers();
  },
  methods: {
    fetchData() {
      this.listLoading = true;
      // fetchList(this.listQuery).then(response => {
      //   this.list = response.data.items
      //   this.listLoading = false
      // })
    },
    // queryGoods() {
    //   this.$store.dispatch("GueryGoods");
    // },
    // queryUsers() {
    //   this.$store.dispatch("GueryUsers");
    // },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleDownload() {
      if (this.multipleSelection.length) {
        this.downloadLoading = true;
        import("./Export2Excel").then((excel) => {
          const tHeader = ["Id", "Title", "Author", "Readings", "Date"];
          const filterVal = ["Id", "Title", "Author", "Readings", "Date"];
          const list = this.multipleSelection;
          const data = this.formatJson(filterVal, list);

          excel.export_json_to_excel({
            header: tHeader,
            data,
            filename: this.filename,
          });
          this.$refs.multipleTable.clearSelection();
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
  },
};
</script>
