import request from '@/utils/request'

export const reqManagementList = (page, limit) => request({url:`http://192.168.43.101:5000/output/${page}/${limit}`,method:'get'})

// 处理添加信息
// http://192.168.1.86:5000/input post 携带4个参数
export const reqAddManagement = (name,number,address_x,address_y) => request({url:`http://192.168.43.101:5000/input/${name}/${number}/${address_x}/${address_y}`,method:'post'})
// export const reqAddManagement = (name,number,address_x,address_y) => request ({url:'http://192.168.1.86:5000/input',method:'post', data:Management})
// 修改信息
// http://192.168.1.86:5000/purchase put 携带5个参数 加一个id

// export const reqAddOrUpdateManagement = (Management) => {
//   if(Management.id){
//     return request({url:'http://192.168.1.86:5000/purchase',method:'put',data:Management})
//   }else{
//     // 新增
//     return request({url:'http://192.168.1.86:5000/input',method:'post', data:Management})
//   }
// }
export const reqUpdateManagement = (id) => request({url:`http://192.168.43.101:5000/purchase/${id}`,method:'put'})

//删除信息
// http://192.168.1.86:5000/delete/{id} delete
export const reqDeleteManagement = (id) => request({url:`http://192.168.43.101:5000/delete/${id}`,method:'delete'})
