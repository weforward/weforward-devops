(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2a0b3c90"],{"5ef9":function(e,t,s){"use strict";s.r(t);var a=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"fwpz_area"},[a("div",{staticClass:"query"},[a("div",{staticClass:"input_box",staticStyle:{"margin-left":"0"}},[e._v("类型："),a("SingleSelect",{attrs:{items:e.selectOption,selectname:e.selectOption[0].name},on:{select:e.changeNX},model:{value:e.selectValue,callback:function(t){e.selectValue=t},expression:"selectValue"}})],1),a("div",{staticClass:"AcGroup",staticStyle:{"margin-left":"7%"}},[e._v("访问组：\n        "),a("SingleSelect",{attrs:{selectname:e.serachDefalt.name,items:e.merchantitems},on:{serachbykeyword:e.getMerchants},model:{value:e.searchMachant,callback:function(t){e.searchMachant=t},expression:"searchMachant"}})],1),a("div",{staticClass:"input_box"},[e._v("关键字："),a("input",{directives:[{name:"model",rawName:"v-model",value:e.keyword,expression:"keyword"}],attrs:{type:"text"},domProps:{value:e.keyword},on:{keydown:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.query(t)},input:function(t){t.target.composing||(e.keyword=t.target.value)}}})]),a("div",{staticClass:"query_button",on:{click:e.query}},[e._v("查询")]),a("div",{staticClass:"query_button",on:{click:function(t){e.showcreate=!e.showcreate}}},[e._v("\n        +添加\n      ")])]),a("div",[a("table",{staticClass:"fwpz_table"},[e._m(0),a("tbody",e._l(e.accessesList,(function(t,s){return a("tr",{key:s},[a("td",{attrs:{title:t.id+"[双击复制]"},on:{dblclick:function(s){return e.copy(t.id)}}},[e._v(e._s(t.id))]),a("td",[a("p",{directives:[{name:"show",rawName:"v-show",value:t.group,expression:"item.group"}],attrs:{title:t.group}},[e._v(e._s(t.group))]),a("p",{directives:[{name:"show",rawName:"v-show",value:t.group_name,expression:"item.group_name"}],staticStyle:{color:"#757575"}},[e._v(e._s(t.group_name))])]),a("td",{attrs:{title:t.summary}},[e._v(e._s(t.summary||"--"))]),a("td",[e._v(e._s(t.valid?"是":"否"))]),a("td",[a("span",{staticStyle:{cursor:"pointer",color:"#3D83E5"},on:{click:function(s){return e.seeAccesskey(t)}}},[e._v("查看accesskey")])])])})),0),e.warnningMsg?a("tfoot",[a("tr",[a("td",{staticClass:"table-warnning",attrs:{colspan:"3"}},[e._v(e._s(e.warnningMsg))])])]):e._e()])]),e.warnningMsg?e._e():a("PagerBar",{attrs:{page:e.page},on:{change:e.searchByPage}}),a("WoolShowmodal",{attrs:{isshow:e.showcreate,isshowcloser:!0},on:{tapCloser:function(t){e.showcreate=!e.showcreate}}},[a("div",{staticClass:"add_fwpz"},[a("div",{staticClass:"titlebar flexlayout"},[e._v("添加凭证")]),a("div",{staticClass:"kind_items"},[a("label",{staticClass:"kind_item"},[a("span",[e._v("种类(access kind)：")])]),a("WoolSelect",{staticClass:"kindselect",attrs:{items:e.cSelectOption},model:{value:e.createOptionVal,callback:function(t){e.createOptionVal=t},expression:"createOptionVal"}},[e._v(e._s(e.cSelectOption[e.createOptionVal].name))])],1),a("div",{staticClass:"visit_groups"},[a("label",{staticClass:"visit_group"},[a("span",[e._v("访问组(access group)：")])]),a("SingleSelect",{attrs:{selectname:e.createMerchant.name,items:e.merchantitems},on:{serachbykeyword:e.getMerchants},model:{value:e.createMerchant.id,callback:function(t){e.$set(e.createMerchant,"id",t)},expression:"createMerchant.id"}})],1),a("div",{staticClass:"zhuaiyao_items"},[a("label",{staticClass:"zhuaiyao_item"},[a("span",[e._v("摘要：")])]),a("textarea",{directives:[{name:"model",rawName:"v-model",value:e.summary,expression:"summary"}],staticClass:"summary-input zhaiyao",staticStyle:{width:"550px"},attrs:{rows:"5",placeholder:"请输入凭证摘要"},domProps:{value:e.summary},on:{input:function(t){t.target.composing||(e.summary=t.target.value)}}})]),a("div",{staticClass:"btn-box"},[a("button",{staticClass:"add-btn",on:{click:e.createAccess}},[a("span",[e._v("添  加")])])])])]),a("WoolShowmodal",{attrs:{isshow:e.showaccesskey,isshowcloser:!0},on:{tapCloser:function(t){e.showaccesskey=!e.showaccesskey}}},[a("div",{staticClass:"accesskey"},[a("div",{staticClass:"titlebar flexlayout"},[e._v("凭证信息")]),a("div",[a("span",{staticClass:"accesskey-item"},[e._v("accessId = "+e._s(e.accessId))]),a("img",{attrs:{src:s("9b6d")},on:{click:function(t){return e.copy(e.accessId)}}})]),a("div",[a("span",{staticClass:"accesskey-item"},[e._v("accesskey = "+e._s(e.accesskey))]),a("img",{attrs:{src:s("9b6d")},on:{click:function(t){return e.copy(e.accesskey)}}})])])])],1)},c=[function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("tr",{staticClass:"fwpz_title"},[s("td",{staticStyle:{width:"23%"}},[e._v("access id")]),s("td",{staticStyle:{width:"27%"}},[e._v("访问组(access group)")]),s("td",{staticStyle:{width:"20%"}},[e._v("摘要")]),s("td",{staticStyle:{width:"10%"}},[e._v("是否有效")]),s("td",{staticStyle:{width:"20%"}},[e._v("access key")])])}],n=(s("7f7f"),s("ac6a"),{name:"devopsaccesses",components:{WoolSelect:function(){return s.e("chunk-3f7fc98c").then(s.bind(null,"8445"))},PagerBar:function(){return s.e("chunk-22e86add").then(s.bind(null,"2f3e"))},WoolShowmodal:function(){return s.e("chunk-41c82f40").then(s.bind(null,"59c5"))},SingleSelect:function(){return s.e("chunk-90e745ba").then(s.bind(null,"4e15"))}},data:function(){return{selectOption:[{name:"服务",id:"H"},{name:"用户",id:"US"}],cSelectOption:[{name:"服务",value:"H"}],selectValue:"H",searchMachant:"",page:{current:1,pagecount:1,totalCount:0,pagesize:10},warnningMsg:"",showcreate:!1,createOptionVal:0,createMerchant:{},summary:"",accessesList:[],showaccesskey:!1,accesskey:"",accessId:"",serachDefalt:{},merchantitems:[],searchMachantCount:0,keyword:""}},methods:{changeNX:function(e){e&&this.requestList()},query:function(){this.page.current=1,this.requestList()},searchByPage:function(e){this.page.current=e.page,this.requestList()},createAccess:function(){var e=this;if(""!=this.createMerchant)if(""!=this.summary){var t={op:"create",kind:this.cSelectOption[this.createOptionVal].value,group:this.createMerchant.id,summary:this.summary},s=this.$wool.showloading("保存中"),a="?method=/devops/gateway/access";this.$wf.post(a,t).then((function(t){t.id&&(s.msgsucc("创建成功").hide(),e.showcreate=!e.showcreate,e.showaccesskey=!0,e.accesskey=t.key,e.accessId=t.id,e.requestList())})).catch((function(e){s.msgwarn(e.message).hide(0)}))}else this.$wool.showwarn("请输入凭证摘要");else this.$wool.showwarn("请选择一个商家")},requestList:function(){var e=this;this.accessesList=[],this.warnningMsg="";var t=this.$wool.showloading("加载中..."),s="?method=/devops/gateway/accesses",a={kind:this.selectValue,group:this.searchMachant,keyword:this.keyword,page:this.page.current,page_size:this.page.pagesize};this.$wf.post(s,a).then((function(s){t.hide(0),e.accessesList=s.items,0==e.accessesList.length?e.warnningMsg="没有更多数据,试试其他的吧":(e.page.totalCount=s.count,e.page.current=s.page,e.page.pagecount=s.page_count)})).catch((function(s){t.hide(0),e.warnningMsg=s.message}))},seeAccesskey:function(e){this.showaccesskey=!0,this.accessId=e.id,this.accesskey=e.key},searchMerchants:function(e,t){var s=this,a={p:1,pagesize:500,keywords:e};this.searchMachantCount>2||this.$wf.post("?method=/devops/gateway/merchants",a).then((function(e){if(0==e.items.length)t&&(s.searchMachantCount=s.searchMachantCount+1,s.searchMerchants(s.searchMachant,!1));else{if(e.count>1){var a=[];e.items.forEach((function(e){"CommonCompany$06d651f0-0173"!=e.id&&"CommonCompany$08cf445c-062a"!=e.id||a.push(e)})),0==a.length?(s.searchMachantCount=s.searchMachantCount+1,s.searchMerchants(s.searchMachant,!1)):s.serachDefalt=a[0]}else s.serachDefalt=e.items[0];s.searchMachant=s.serachDefalt.id}})).catch((function(e){s.$wool.showwarn("搜索商家异常")}))},getMerchants:function(e){var t=this;this.$wf.post("?method=/devops/gateway/merchants",{page:1,page_size:100,keywords:e}).then((function(e){t.merchantitems=e.items.map((function(e){return e.name=e.name?e.name+"("+e.id+")":e.id,e}))})).catch((function(e){return t.$wool.showwarn("获取商家列表失败"+e.message)}))},copy:function(e){this.$wool.copyText(e),this.$wool.showsucc("复制")}},watch:{searchMachant:function(e){"undefined"!=typeof e&&this.requestList()}},mounted:function(){this.getMerchants(),this.requestList()}}),i=n,o=(s("815e"),s("2877")),r=Object(o["a"])(i,a,c,!1,null,"4d6d13b4",null);t["default"]=r.exports},"815e":function(e,t,s){"use strict";s("e17b")},"9b6d":function(e,t){e.exports="data:image/png;base64,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"},e17b:function(e,t,s){}}]);