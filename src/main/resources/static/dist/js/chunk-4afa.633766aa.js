(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-4afa"],{"01f1":function(t,e,a){"use strict";var i=a("2908"),s=a.n(i);s.a},2908:function(t,e,a){},"874e":function(t,e,a){"use strict";a.r(e);var i=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",[a("Row",{attrs:{gutter:20}},[a("i-col",{attrs:{span:"6"}},[a("Card",{attrs:{title:"主机列表",shadow:""}},[a("Tree",{attrs:{data:t.data1,"load-data":t.loadData},on:{"on-select-change":t.onclickTreeNode}})],1)],1),a("i-col",{attrs:{span:"18"}},[a("Card",{attrs:{title:"服务信息列表",shadow:""}},[a("Table",{attrs:{border:"","highlight-row":"","row-class-name":t.rowClassName,columns:t.serverColumns,data:t.serverData},on:{"on-row-click":t.onclickRow}}),a("Page",{staticStyle:{"margin-top":"20px"},attrs:{total:t.serverTotal,size:"small","show-total":"","show-elevator":""},on:{"on-change":t.onclickServerPage}})],1),a("Card",{staticStyle:{"margin-top":"20px"},attrs:{title:"服务方法列表",shadow:""}},[a("Table",{attrs:{border:"",columns:t.methodColumns,data:t.methodData}}),a("Page",{staticStyle:{"margin-top":"20px"},attrs:{total:t.methodTotal,size:"small","show-total":"","show-elevator":""},on:{"on-change":t.onclickMethodPage}})],1)],1)],1),t.isLoading?a("Spin",{staticStyle:{background:"rgba(255,255,255,0)"},attrs:{fix:""}},[a("Icon",{staticClass:"demo-spin-icon-load",attrs:{type:"ios-loading",size:"50"}}),a("div",{staticStyle:{"font-size":"24px"}},[t._v("Loading")])],1):t._e()],1)},s=[],o=(a("7f7f"),a("cadf"),a("551c"),a("097d"),{name:"join_page",data:function(){return{methodData:[],methodColumns:[{title:"方法名",key:"serviceName"},{title:"方法ID",key:"serviceId"},{title:"描述",key:"description"},{title:"地址",key:"url"},{title:"黑名单",key:"inBlack"}],methodTmpList:[],methodPage:{pageSize:10,pageNum:1},isLoading:!0,methodTotal:0,serverColumns:[{title:"服务名称",key:"appName"},{title:"服务描述",key:"description"},{title:"服务状态",key:"runLanguage"}],serverData:[],data1:[],serverTmpList:[],serverPage:{pageSize:10,pageNum:1},serverTotal:0}},mounted:function(){this.onloadeVirtualMachine()},methods:{onloadeVirtualMachine:function(){var t=this;this.axios.get(this.ajaxUrl.path+"/mic/manager/host/hostName").then(function(e){for(var a=e.data.result,i=0;i<a.length;i++){var s={};s.title=a[i].name,0==i&&(s.selected=!0),s.loading=!1,s.type="ip",s.children=[],t.data1.push(s)}t.isLoading=!1}).catch(function(t){console.log(t)})},loadData:function(t,e){var a=this;setTimeout(function(){var i=[],s=t.title;a.axios.get(a.ajaxUrl.path+"/mic/manager/host/vm?hostName="+s).then(function(t){for(var a=t.data.result,s=0;s<a.length;s++){var o={};o.title=a[s].name,o.ip=a[s].ip,o.expand=!0,o.type="vm",o.children=[],i.push(o)}e(i)}).catch(function(t){console.log(t)})},1e3)},onclickTreeNode:function(t){var e=this,a=(t[0].title,t[0].type);if("ip"==a);else if("vm"==a){this.isLoading=!0,this.serverData=[];var i=t[0].ip;this.axios.get(this.ajaxUrl.path+"/mic/manager/service/apps/ip?ip="+i).then(function(t){var a=t.data.result;e.serverTotal=a.length,e.serverTmpList=a,e.onclickChangeListData(),e.isLoading=!1}).catch(function(t){console.log(t)})}},onclickChangeListData:function(){Math.ceil(this.serverTotal/this.serverPage.pageSize);var t=this.serverPage.pageSize*(this.serverPage.pageNum-1),e=this.serverPage.pageSize*this.serverPage.pageNum;e>=this.serverTotal&&(e=this.serverTotal);for(var a=t;a<e;a++)"normal"==this.serverTmpList[a].status?this.serverTmpList[a].status="正常":"alarm"==this.serverTmpList[a].status?this.serverTmpList[a].status="告警":"offline"==this.serverTmpList[a].status&&(this.serverTmpList[a].status="离线"),this.serverData.push(this.serverTmpList[a]);this.methodData=[],this.methodTotal=0,this.serverData.length>0&&this.onclickRow(this.serverData[0])},rowClassName:function(t,e){if(0==e)return"table-info-row"},onclickServerPage:function(t){this.serverData=[],this.serverPage.pageNum=t,this.onclickChangeListData()},onclickRow:function(t){var e=this;this.axios.get(this.ajaxUrl.path+"/mic/manager/service/apps/appName?appName="+t.appName).then(function(t){var a=t.data.result;e.methodTotal=a.length,e.methodTmpList=a,e.onclickChangeMethodList()}).catch(function(t){console.log(t)})},onclickChangeMethodList:function(){Math.ceil(this.methodTotal/this.methodPage.pageSize);var t=this.methodPage.pageSize*(this.methodPage.pageNum-1),e=this.methodPage.pageSize*this.methodPage.pageNum;e>=this.methodTotal&&(e=this.methodTotal);for(var a=t;a<e;a++)this.methodTmpList[a].inBlack?this.methodTmpList[a].inBlack="是":this.methodTmpList[a].inBlack="否",this.methodData.push(this.methodTmpList[a])},onclickMethodPage:function(t){this.methodData=[],this.methodPage.pageNum=t,this.onclickChangeMethodList()}}}),r=o,n=(a("01f1"),a("2877")),h=Object(n["a"])(r,i,s,!1,null,null,null);h.options.__file="service-preview.vue";e["default"]=h.exports}}]);