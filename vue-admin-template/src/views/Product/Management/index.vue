<template>
  <div>
    <!-- 按钮 -->
    <el-button type="primary" icon="el-icon-plus" style="margin:10px 0" @click="showDialog">添加</el-button>
    <!--
      表格组件
      data:表格组件将来需要显示的数据------数组类型
    -->
    <el-table style="width: 100%;" :data="list">
      <el-table-column type="index" label="序号" width="80px" align="center"></el-table-column>
      <el-table-column prop="name" label="姓名" width="width"></el-table-column>
      <el-table-column prop="number" label="电话" width="width"></el-table-column>
      <el-table-column prop="address_x" label="x" width="width"></el-table-column>
      <el-table-column prop="address_y" label="y" width="width"></el-table-column>
      <el-table-column prop="prop" label="操作" width="width">
        <template slot-scope="{row, $index}">
          <el-button type="warning" icon="el-icon-edit" size="mini" @click="updateInfo(row)">修改</el-button>
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteInfo(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--
      分页器
      当前页、数据总条数、
    -->
    <el-pagination style="margin-top: 20px;text-align: center;" :current-page="page" :total="total" :page-size="limit" :page-sizes="[3,5,10]" layout="prev,pager,next,jumper,->,sizes,total" @current-change="handleCurrentChange" @size-change="handleSizeChange" ></el-pagination>
    <!-- 对话框
      :visible.sync 控制对话框的显示与隐藏
    -->
    <el-dialog title="添加信息" :visible.sync="dialogFormVisible">
      <!-- form表单  model属性作用是将表单数据收集到对象上，后期进行表单验证-->
      <el-form style="width:85%" :model="mForm" ref="mForm">
        <el-form-item label="姓名" label-width=100px>
          <el-input autocomplete="off" v-model="mForm.name"></el-input>
        </el-form-item>
        <el-form-item label="电话" label-width=100px>
          <el-input autocomplete="off" v-model="mForm.number" ></el-input>
        </el-form-item>
        <el-form-item label="x" label-width=100px>
          <el-input autocomplete="off" v-model="mForm.address_x" ></el-input>
        </el-form-item>
        <el-form-item label="y" label-width=100px>
          <el-input autocomplete="off" v-model="mForm.address_y" ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addOrUpdate">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Axios from 'axios'
export default {
  name:"management",
  data(){
    return{
      page:1,
      limit:3,
      // 总数据条数
      total:0,
      list:[],
      // 对话框显示与隐藏
      dialogFormVisible:false,
      dialogUpdateFormVisible:false,
      // 收集品牌信息
      mForm:{
        name:"",
        number:"",
        address_x:"",
        address_y:""
      }
    }
  },
  // 组件挂载完毕发送请求
  mounted(){
    // 获取列表数据方法
    console.log(this.$API)
    this.getPageList()
  },
  methods:{
    async getPageList(){
      // this.page = pager
      // 解构出参数
      const {page, limit} = this;
      // 获取快递列表的接口
      let result = await this.$API.management.reqManagementList(page, limit)
      console.log(result)
      this.total = result.count;
      this.list = result.data;
      console.log(this.list)
    },
    handleCurrentChange(pager){
      console.log(pager)
      // 修改参数
      this.page = pager;
      this.getPageList();
    },
    // 当分页器某一页需要展示数据条数回传
    handleSizeChange(limit){
      console.log(limit);
      // 整理参数
      this.limit = limit;
      this.getPageList();
    },
    // 点击添加
    showDialog(){
      this.dialogFormVisible = true;
      // 清除数据
      this.mForm = {name:'',number:'',address_x:'',address_y:''}

    },
    // 点击更新
    async updateInfo(row){
      this.dialogFormVisible = true;
      // 将已有信息赋值给form
      console.log(row)
      this.mForm = row;
      let result = await this.$API.management.reqUpdateManagement(row.id);
      let resulta = await this.$API.management.reqDeleteManagement(row.id);
      console.log(resulta)
      console.log(result)
      this.getPageList();
    },

    //添加按钮
    async addOrUpdate(){
      this.dialogFormVisible = false;
        var a = this.mForm
        console.log(a)
        var parms = new URLSearchParams()
        parms.append('name', a.name)
        parms.append('number', a.number)
        parms.append('address_x', a.address_x)
        parms.append('address_y', a.address_y)
        console.log(parms)
        Axios.post('http://192.168.53.101:5000/input', parms).then(function (res) {
        if (res.data.isInput == '0') {
          sessionStorage.setItem('Info', JSON.stringify(res.data))
          console.log(JSON.stringify(res.data.isInput))
          this.$message('添加信息成功');
        }
      })
      this.getPageList();
    },
    // 删除
    deleteInfo(row){
      console.log(row)
      this.$confirm(`此操作将永久删除${row.name}, 是否继续?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async() => {
          let result = await this.$API.management.reqDeleteManagement(row.id);
          console.log(result)
          if (result.isDelete == 0){
            this.$message({
              type: 'success',
              message: '删除成功!'
            });
          }
          this.getPageList();

        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
    }
  }
}
</script>

<style>

</style>
