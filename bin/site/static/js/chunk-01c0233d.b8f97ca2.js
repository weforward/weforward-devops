(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-01c0233d"],{4366:function(e,t,s){"use strict";s.r(t);var i=function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("WoolShowmodal",{attrs:{isshow:e.isshow,isshowcloser:!0},on:{tapCloser:function(t){return e.$emit("toggle")}}},[i("div",{staticClass:"rightTable"},[i("div",[i("div",{staticClass:"titlebar flexlayout"},[i("div",[e._v("\n          "+e._s(e.servicename)+" 服务权限表\n        ")]),i("div",{staticClass:"explain-box"},[i("div",{staticClass:"explain-icon"}),i("div",{staticClass:"explain-context"},[e._v("\n            权限表描述微服务的访问权限规则。\n            "),i("p",[e._v("规则项描述了微服务对调用方Access的访问控制，默认情况（未配置规则是）禁止所有访问。")]),e._v("\n              - 规则项的名称与描述只是帮助记忆，无特殊意义；"),i("br"),e._v("\n              - 每个规则项的id、kind、group依次匹配调用方Access的id、kind、group；"),i("br"),e._v("\n              - 顺序匹配各个规则项，当匹配时，应用规则项的访问策略；"),i("br"),i("br"),e._v("\n              !! 特殊：空规则项（未指定id、kind、group）仅匹配无调用方Access的情况\n          ")])])]),i("div",{staticClass:"flexlayout flexlayout_middle",staticStyle:{"margin-top":"10px","font-size":"14px"}},[i("span",{staticClass:"warn-note"},[e._v("提示：拖动可调换位置")]),i("p",{staticStyle:{"padding-left":"10px"}},[e._v("是否打开拖动排序模式：")]),i("CheckBox",{attrs:{allowcheck:!0},model:{value:e.isAllowSort,callback:function(t){e.isAllowSort=t},expression:"isAllowSort"}})],1)]),i("div",{staticClass:"rightInfo"},[i("table",{staticClass:"tableDatil"},[i("thead",[i("th",[i("div",{staticClass:"flex"},[i("div",[e._v("名称")]),i("div",{staticClass:"explain-box"},[i("div",{staticClass:"explain-icon"}),i("div",{staticClass:"explain-context"},[e._v("无特殊意义，不重复即可")])])])]),i("th",[i("div",{staticClass:"flex"},[i("div",[e._v("访问id(access id)")]),i("div",{staticClass:"explain-box"},[i("div",{staticClass:"explain-icon"}),i("div",{staticClass:"explain-context"},[e._v("匹配调用方的'access id'，为空表示匹配任意'access id'")])])])]),i("th",[i("div",{staticClass:"flex"},[i("div",[e._v("访问组(access group)")]),i("div",{staticClass:"explain-box"},[i("div",{staticClass:"explain-icon"}),i("div",{staticClass:"explain-context"},[e._v("匹配调用方的'access group'，为空表示匹配任意'access group'")])])])]),i("th",[i("div",{staticClass:"flex"},[i("div",[e._v("描述")]),i("div",{staticClass:"explain-box"},[i("div",{staticClass:"explain-icon"}),i("div",{staticClass:"explain-context"},[e._v("规则项的说明性描述")])])])]),i("th",[i("div",{staticClass:"flex"},[i("div",[e._v("访问类型")]),i("div",{staticClass:"explain-box"},[i("div",{staticClass:"explain-icon"}),i("div",{staticClass:"explain-context"},[e._v("匹配调用方的'access kind'，不指定表示匹配任意'access kind'")])])])]),i("th",[i("div",{staticClass:"flex"},[i("div",[e._v("策略")]),i("div",{staticClass:"explain-box"},[i("div",{staticClass:"explain-icon"}),i("div",{staticClass:"explain-context"},[e._v("当调用方Access匹配此项时，将应用的访问策略")])])])]),i("th",[e._v("操作")]),i("th",{directives:[{name:"show",rawName:"v-show",value:e.isAllowSort,expression:"isAllowSort"}]},[i("div",{staticClass:"flex"},[e._v("排序")])])]),i("WoolSortable",{attrs:{tag:"tbody",handle:".right-item-handle"},on:{change:e.sortIndex},model:{value:e.rightItems,callback:function(t){e.rightItems=t},expression:"rightItems"}},e._l(e.rightItems,(function(t,a){return i("tr",{key:a},[i("td",[i("input",{directives:[{name:"model",rawName:"v-model",value:t.name,expression:"item.name"}],staticClass:"smallTable-input",staticStyle:{width:"100px"},attrs:{type:"text",title:t.name,disabled:!(e.selectIdx==a)},domProps:{value:t.name},on:{input:function(s){s.target.composing||e.$set(t,"name",s.target.value)}}})]),i("td",{attrs:{title:t.access_id+"[双击复制]"},on:{dblclick:function(s){return e.copy(t.access_id)}}},[i("div",[i("input",{directives:[{name:"model",rawName:"v-model",value:t.access_id,expression:"item.access_id"}],staticClass:"smallTable-input",staticStyle:{"min-width":"200px"},attrs:{type:"text",disabled:!(e.selectIdx==a)},domProps:{value:t.access_id},on:{input:function(s){s.target.composing||e.$set(t,"access_id",s.target.value)}}})])]),i("td",{attrs:{title:t.access_group+"[双击复制]"},on:{dblclick:function(s){return e.copy(t.access_group)},click:function(t){e.showAssociate=!e.showAssociate}}},[i("div",{staticStyle:{width:"250px",position:"relative"}},[i("span",{staticStyle:{color:"#757575"}},[e._v(e._s(t.access_group_name))]),i("div",{staticClass:"flexlayout"},[e.selectIdx==a?i("input",{directives:[{name:"model",rawName:"v-model",value:t.access_group,expression:"item.access_group"}],staticClass:"smallTable-input",staticStyle:{"min-width":"200px"},attrs:{type:"text",placeholder:"回车搜索",disabled:!(e.selectIdx==a)},domProps:{value:t.access_group},on:{keydown:function(s){return!s.type.indexOf("key")&&e._k(s.keyCode,"enter",13,s.key,"Enter")?null:e.associateMerchant(t)},input:function(s){s.target.composing||e.$set(t,"access_group",s.target.value)}}}):e._e(),i("div",{directives:[{name:"show",rawName:"v-show",value:e.selectIdx==a,expression:"selectIdx == indx"}],staticStyle:{"padding-left":"5px"}},[i("WoolShowLoading",{attrs:{isshow:e.isloading,color:"#095280"}})],1)]),e.selectIdx!=a?i("div",[i("span",[e._v(e._s(t.access_group))])]):e._e(),i("div",{directives:[{name:"show",rawName:"v-show",value:e.selectIdx==a&&e.showAssociate&&e.merchantsList.length,expression:"selectIdx == indx && showAssociate && merchantsList.length"}],staticClass:"merchants-associate wt-scroll"},[i("ul",e._l(e.merchantsList,(function(s,a){return i("li",{key:a,staticClass:"associate-item",on:{click:function(i){return e.selectMerchant(t,s)}}},[e._v(e._s(s.id)+e._s(s.name))])})),0)])])]),i("td",[i("input",{directives:[{name:"model",rawName:"v-model",value:t.description,expression:"item.description"}],staticClass:"smallTable-input",attrs:{type:"text",title:t.description,disabled:!(e.selectIdx==a)},domProps:{value:t.description},on:{input:function(s){s.target.composing||e.$set(t,"description",s.target.value)}}})]),i("td",e._l(e.accessKindTypes,(function(s,n){return i("label",{key:n,staticClass:"radio"},[i("input",{directives:[{name:"model",rawName:"v-model",value:t.access_kind,expression:"item.access_kind"}],attrs:{type:"radio",name:a,disabled:!(e.selectIdx==a)},domProps:{value:s.value,checked:e._q(t.access_kind,s.value)},on:{change:function(i){return e.$set(t,"access_kind",s.value)}}}),e._v("\n                  "+e._s(s.name)+"\n                ")])})),0),i("td",[i("label",{staticClass:"radio"},[i("input",{directives:[{name:"model",rawName:"v-model",value:t.allow,expression:"item.allow"}],attrs:{type:"radio",name:"策略"+a,disabled:!(e.selectIdx==a)},domProps:{value:!0,checked:e._q(t.allow,!0)},on:{change:function(s){return e.$set(t,"allow",!0)}}}),e._v("\n                允许\n              ")]),i("label",{staticClass:"radio"},[i("input",{directives:[{name:"model",rawName:"v-model",value:t.allow,expression:"item.allow"}],attrs:{type:"radio",name:"策略"+a,disabled:!(e.selectIdx==a)},domProps:{value:!1,checked:e._q(t.allow,!1)},on:{change:function(s){return e.$set(t,"allow",!1)}}}),e._v("\n                拒绝\n              ")])]),i("td",[i("div",{staticClass:"table-handle-box"},[t.new?i("span",{staticClass:"smallTable-handle",on:{click:function(s){return e.save(t,a,"append")}}},[e._v("保存")]):e._e(),t.new?i("span",{staticClass:"smallTable-handle",on:{click:function(t){return e.deleteRow(a)}}},[e._v("取消")]):e._e(),t.new?e._e():i("span",{staticClass:"smallTable-handle",on:{click:function(s){return e.showRemoveModel(t,a)}}},[e._v("移除")]),t.new?e._e():i("span",{staticClass:"smallTable-handle"},[e.isshowSave&&e.selectIdx==a?e._e():i("span",{on:{click:function(s){return e.replace(t,a)}}},[e._v("编辑")]),e.isshowSave&&e.selectIdx==a?i("span",{on:{click:function(s){return e.save(t,a,"replace")}}},[e._v("保存")]):e._e()]),e.isshowSave&&e.selectIdx==a&&!t.new?i("span",{staticClass:"smallTable-handle",on:{click:function(t){return e.cancelSave(a)}}},[e._v("取消")]):e._e()])]),i("td",{directives:[{name:"show",rawName:"v-show",value:e.isAllowSort,expression:"isAllowSort"}],staticClass:"right-item-handle"},[i("img",{staticStyle:{width:"20px","margin-bottom":"-5px"},attrs:{src:s("90ad")}})])])})),0),0==e.rightItems.length?i("tfoot",[i("tr",[i("td",{staticClass:"table-warnning",attrs:{colspan:"8"}},[e._v("暂无数据")])])]):e._e()],1),i("div",{staticClass:"addrowBar",on:{click:e.addRow}},[e._v("+")])])]),i("DeleteConfirmModel",{attrs:{isshow:e.isShowRemove},on:{toggle:function(t){e.isShowRemove=!1},handle:e.remove}})],1)},a=[],n=(s("7f7f"),s("ac6a"),{name:"RightTableInfo",props:{isshow:{type:Boolean},servicename:{type:String}},components:{WoolShowmodal:function(){return s.e("chunk-41c82f40").then(s.bind(null,"59c5"))},WoolSortable:function(){return s.e("chunk-56ca3884").then(s.bind(null,"8ec4"))},CheckBox:function(){return s.e("chunk-2965e772").then(s.bind(null,"751e"))},DeleteConfirmModel:function(){return s.e("chunk-8418382e").then(s.bind(null,"7f61"))},WoolShowLoading:function(){return s.e("chunk-889b52a0").then(s.bind(null,"9b16"))}},data:function(){return{isAllowSort:!1,rightRuleItem:{},selectIdx:-1,rightTableOp:"",rightItems:[],isshowSave:!1,accessKindTypes:[{name:"服务",value:"H"},{name:"用户",value:"US"},{name:"不指定",value:""}],selectValue:0,merchantsList:[],showAssociate:!1,copyRightItems:[],isShowRemove:!1,needRemoveObj:null,isloading:!1}},methods:{showRemoveModel:function(e,t){this.needRemoveObj=e,this.needRemoveObj.index=t,this.isShowRemove=!0},sortIndex:function(e){if(this.rightTableOp="move",this.$set(this.rightRuleItem,"from",e.oldIndex),this.$set(this.rightRuleItem,"to",e.newIndex),this.rightItems[e.oldIndex].new||this.rightItems[e.newIndex].new)return this.selectIdx=e.newIndex,void this.$wool.showwarn("存在新的一列未保存，不支持调用方法",1e3);var t="移动";this.submitFrom(t)},resetData:function(){this.rightRuleItem={},this.selectIdx=-1,this.rightTableOp=""},deleteRow:function(e){this.rightItems.splice(e,1),this.selectIdx=-1},addRow:function(){var e=!1;if(this.rightItems.forEach((function(t){t.new&&(e=!0)})),e)this.$wool.showwarn("已存在新的一列！");else{var t={access_group:"",access_id:"",access_kind:"",description:"",marks:"",name:"",new:!0,allow:!0};this.rightItems.push(t),this.selectIdx=this.rightItems.length-1}},remove:function(){var e=this.needRemoveObj;this.rightTableOp="remove",this.$set(this.rightRuleItem,"remove_name",e.name),this.$set(this.rightRuleItem,"index",e.index),this.isShowRemove=!1;var t="移除";this.submitFrom(t)},replace:function(e,t){this.selectIdx=t,this.rightRuleItem.replaceName=e.name,this.$set(this.rightRuleItem,"index",t),this.isshowSave=!0},save:function(e,t,s){this.rightTableOp=s,this.$set(this.rightRuleItem,"name",e.name),this.$set(this.rightRuleItem,"access_id",e.access_id),this.$set(this.rightRuleItem,"access_group",e.access_group),this.$set(this.rightRuleItem,"description",e.description),this.$set(this.rightRuleItem,"access_kind",e.access_kind),this.$set(this.rightRuleItem,"allow",e.allow);var i="保存";this.submitFrom(i)},cancelSave:function(e){this.rightItems[e]=Object.assign({},this.copyRightItems[e]),this.selectIdx=-1},submitFrom:function(e){var t=this,s=this.$wool.showloading(e+"中"),i="?method=/devops/gateway/right",a={op:this.rightTableOp,name:this.servicename,item:this.rightRuleItem};this.$wf.post(i,a).then((function(i){s.msgsucc(e+"成功").hide((function(){t.requestRightList(),t.resetData()}))})).catch((function(e){s.msgwarn(e.message).hide()}))},requestRightList:function(){var e=this;this.copyRightItems=[],this.rightItems=[];var t=this.$wool.showloading("加载中");this.$wf.post("?method=/devops/gateway/right",{name:this.servicename}).then((function(s){t.hide(0),e.rightItems=s.items||[],e.copyRightItems=e.rightItems.map((function(e){var t=Object.assign({},e);return t}))})).catch((function(s){t.hide(0),e.$wool.showwarn(s.message)}))},associateMerchant:function(e){var t=this;this.isloading=!0,this.$wf.post("?method=/devops/gateway/merchants",{keywords:e.access_group}).then((function(e){t.showAssociate=!0,t.merchantsList=e.items,t.isloading=!1})).catch((function(e){t.isloading=!1,t.showAssociate=!1,t.$wool.showwarn("联想商家出错了")}))},selectMerchant:function(e,t){this.$forceUpdate(),e.access_group=t.id,e.access_group_name=t.name},copy:function(e){null!=e&&0!=e.length&&(this.$wool.copyText(e),this.$wool.showsucc("复制"))}},watch:{isshow:function(e){e&&this.requestRightList()}}}),o=n,c=(s("d489"),s("2877")),l=Object(c["a"])(o,i,a,!1,null,"53ba82bb",null);t["default"]=l.exports},7569:function(e,t,s){},"90ad":function(e,t){e.exports="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAgCAYAAABzenr0AAADqUlEQVRYR8WXX4gbVRTGv3P3jyhW7UzSIra1mVkWsSj1QcU+iH1QCkppFeqT+lD/0SLoamUzWTBqc1Mt2FYUFNuXPlrqbgutWqG7oIjog0WxYEluFq2wmNzZWgVlu7lHJk4ghN3NnU2weRmSOfd8v/nOPedOCFf5Qzb6N64bXdnfLz4CcAeAdQD+BPArg6dCVXzZJsdiMR0B0kOjdxkjvgJwHUDHmVkRYQUAH8CDYJ7SleLm5UIsCeD4ubXE/EuUnFjcVqvs/blVyPGCPAGvMWN/WJGvLgdiSQDXD06AsZWZdoSVwrFIIOVlh/sFZmdKxWrju597lpk/ZOD1UMl8UoilAbzgMhE+q5XljhXD+dRA/Yok5mcikVbBlJerMvFpXZZP9QxgVSZYXSfMMPilUBUPprzcYQbvjIQJSAPY1XTGzQRfg/CXVvKhngG0J3K94HcCnaypwtPRPdcLSgA+10ruTiraGt+xC5rBrhdwm+2nGPyPVvKxqwLg+tmjMFjbTQs2usuWvt0B1wuOg9khQSViMV5Ve0/b5upJCVwvOAPgbgA3RZPREB7vY3Lm5gY+vXwxH9rCLN+BTDChK3JbvCFnAKyORcsg2qPLhXEbiIQA/E5z9qe83PM1VfggBjhLQryB+XmwEJNxbaPxva+m5KmlQBIBgHB0oWHjesG4VnJ7DHOQjJlgIUYADBvU751Vb/2R+DByhrK3CyNGGLwKhC/AeJcIx6Kp2J4svT7YWJ2W56Lfo3VhqXg+tX70gcgNMmZzbXrfVCKA9Ib89ebvK98AvIGBcwRsbNi6CMBCybsCaF+c8nOvMPP+/x2AgZFQyQP/nYJjj4DMkwuVoOcOxJvpCAj31euDmy5N5y81IDLZrbVK8aRNe3VVgkgg7Y8OGaYvQXREl+WYjWhrTNcADRf84AUw3haib1O19Ob3SSB6AnDDmrwzcM3cj2Cc1Uo+kQTAyWR3EtFh9NMafaHwW6I2bA12vOyLBDoAQdt0qXDCFsLNZA+B6GGt5FDXkzCadABu1tcO3o+f8nM2EG4mO8mgC2FFPtc1QDTpjMAkA4dsXjzd4dwtmOeLIN6uy8WJrgHitvwkumolH+3kQMoP9jBjrG4Gb2228LL3QHOh6wXvAdjSqaYx7BkQjC7LLZ1gE5yGuV0Avw/QbjL184slNtSXJuKPbf8nWAPET/Zt/BbU6cG+00re0ymoccDZBLXGrMwEd/axcRZbVycRzlbkD7Z5EwPYJraN+xd5EM0wc9Z34wAAAABJRU5ErkJggg=="},d489:function(e,t,s){"use strict";s("7569")}}]);