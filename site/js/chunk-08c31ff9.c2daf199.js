(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-08c31ff9"],{3262:function(e,t,s){"use strict";var i=s("8aec"),a=s.n(i);a.a},8499:function(e,t,s){"use strict";s.r(t);var i=function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",{ref:"rlogdetail",staticClass:"rlogdetail wt-scroll"},[s("div",{ref:"rlogcontent",staticClass:"rlogdetail-content"},[s("pre",{staticClass:"rlogdetail-body",domProps:{innerHTML:e._s(e.content)}})]),s("div",{staticClass:"rlogdetail-btn",staticStyle:{bottom:"600px"}},[e._v(e._s(e.page.current))]),s("div",{directives:[{name:"show",rawName:"v-show",value:!e.issearch,expression:"!issearch"}],staticClass:"top rlogdetail-btn",class:{disabled:1==e.page.current},on:{click:e.topPage}},[e._v("最前")]),s("div",{directives:[{name:"show",rawName:"v-show",value:!e.issearch,expression:"!issearch"}],staticClass:"pre rlogdetail-btn",class:{disabled:1==e.page.current},on:{click:e.lastPage}},[e._v("上")]),s("div",{directives:[{name:"show",rawName:"v-show",value:!e.issearch,expression:"!issearch"}],staticClass:"next rlogdetail-btn",class:{disabled:e.page.pageCount==e.page.current},on:{click:e.nextPage}},[e._v("下")]),s("div",{directives:[{name:"show",rawName:"v-show",value:!e.issearch,expression:"!issearch"}],staticClass:"bottom rlogdetail-btn",class:{disabled:e.page.pageCount==e.page.current},on:{click:e.bottomPage}},[e._v("最后")]),s("div",{directives:[{name:"show",rawName:"v-show",value:!e.issearch,expression:"!issearch"}],staticClass:"search-keyword rlogdetail-btn",on:{click:function(t){e.issearch=!e.issearch}}},[e._v("搜索")]),s("div",{directives:[{name:"show",rawName:"v-show",value:e.issearch,expression:"issearch"}],staticClass:"flexlayout searchbar"},[s("input",{directives:[{name:"model",rawName:"v-model",value:e.keywords,expression:"keywords"}],staticClass:"searchbar-input flexitem_auto",attrs:{type:"text",placeholder:"输入关键字查找"},domProps:{value:e.keywords},on:{keydown:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.pre(t)},input:function(t){t.target.composing||(e.keywords=t.target.value)}}}),s("div",{staticClass:"searchbtn lastbtn",on:{click:e.pre}},[e._v("上一个")]),s("div",{staticClass:"searchbtn nextbtn",on:{click:e.next}},[e._v("下一个")]),s("div",{staticClass:"hide-search",on:{click:function(t){e.issearch=!e.issearch}}},[e._v("隐藏")])])])},a=[],r={name:"rlogdetail",data:function(){return{server:this.$route.query.s,catalog:this.$route.query.d,op:"",keywords:"",detailList:[],page:{current:-1,pageCount:0,totalCount:0,pageSize:0},isNext:!1,issearch:!1,searchindex:-1,content:"",originContent:"",isFirstCheck:!0}},methods:{getDetail:function(e){var t=this,s="?method=/devops/rlog/detail",i=this.$wool.showloading("正在加载"),a={server:this.server,directory:this.catalog,op:this.op,keywords:this.keywords};this.page.current>0&&(a.page=this.page.current),this.$wf.post(s,a).then((function(s){s.content?(i.hide(),t.page.pageCount=s.page_count,t.page.current=s.page,t.content=s.content,t.originContent=s.content,e&&(t.isFirstCheck=!0,t.performsearch(e,!1))):i.msgwarn("没有更多了").hide()})).catch((function(e){i.msgwarn(e.message).hide()}))},topPage:function(){this.page.current=1,this.op="first",this.keywords="",this.getDetail()},lastPage:function(){var e=this;1!=this.page.current&&(this.op="pre",this.keywords="",this.getDetail(),this.$nextTick((function(){e.$refs.rlogdetail.scrollTop=e.$refs.rlogdetail.scrollHeight})))},nextPage:function(){this.page.current!=this.page.pageCount&&(this.keywords="",this.op="next",this.getDetail(),this.$refs.rlogdetail.scrollTo(0,0))},bottomPage:function(){this.page.current=this.page.pageCount,this.keywords="",this.op="last",this.getDetail()},pre:function(){this.performsearch("pre",!0)},next:function(){this.performsearch("next",!0)},performsearch:function(e,t){var s,i=this,a=this.originContent,r=this.keywords.trim();if(t||"next"!=e||(this.searchindex=-1),"pre"==e?(this.isFirstCheck?(this.searchindex=a.length,this.isFirstCheck=!1):this.searchindex=this.searchindex-r.length,s=a.lastIndexOf(r,this.searchindex)):(this.searchindex=this.searchindex+r.length,s=a.indexOf(r,this.searchindex)),-1!=s){this.searchindex=s;var n=a.substring(0,s)+"<span id='serach-span' style='color: yellow;font-weight: bold;'>"+r+"</span>"+a.substring(this.searchindex+r.length,a.length);this.content=n,this.$nextTick((function(){var e=document.getElementById("serach-span");e&&e.offsetTop&&i.$refs.rlogdetail.scrollTo({top:e.offsetTop-500,behavior:"smooth"})}))}else{if("pre"==e){if(this.searchindex=this.searchindex+r.length,!this.keywords)return;if(1==this.page.current)return void this.$wool.showwarn("没有更多了");this.op="pre"}else{if(this.searchindex=this.searchindex-r.length,!this.keywords)return;if(this.page.current==this.page.pageCount)return void this.$wool.showwarn("没有更多了");this.op="next"}this.getDetail(e)}}},mounted:function(){this.getDetail()}},n=r,o=(s("3262"),s("2877")),c=Object(o["a"])(n,i,a,!1,null,"117b1b32",null);t["default"]=c.exports},"8aec":function(e,t,s){}}]);