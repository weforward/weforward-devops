(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2b93aeed"],{"1f7d":function(e,t,i){"use strict";i.r(t);var s=function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticClass:"rlog"},[i("div",{staticClass:"rlog-errmsg"}),i("div",{staticClass:"flexlayout"},[i("section",{staticClass:"catalog wt-scroll"},[i("ul",[i("li",{staticClass:"catalog-title "},[e._v("目录")]),e._l(e.muluList,(function(t,s){return i("li",{key:s,staticClass:"catalog-item",class:{"catalog-item-checked":e.curMuluIndex==s}},[i("div",{staticClass:"flexlayout",on:{click:function(i){return e.getThemeList(s,t.name)}}},[i("div",{staticClass:"catalog-name"},[e._v(e._s(t.name))]),i("div",{staticClass:"catalog-num"},[e._v(e._s(t.num))])]),i("div",{staticClass:"flexlayout"},[i("div",{staticClass:"catalog-item-detail",on:{click:function(i){return e.goToDetailLog(t.name)}}},[e._v("完整日志")]),i("div",{staticStyle:{flex:"1"},on:{click:function(i){return e.getThemeList(s,t.name)}}})])])}))],2),i("div",{staticClass:"rlog-input-page"},[e._v("\n\t\t\t\t\t第\n\t\t\t\t\t"),i("input",{directives:[{name:"model",rawName:"v-model",value:e.muluPage,expression:"muluPage"}],attrs:{min:"1",type:"number"},domProps:{value:e.muluPage},on:{click:e.changeMuluPage,input:function(t){t.target.composing||(e.muluPage=t.target.value)}}}),e._v("\n\t\t\t\t\t页\n\t\t\t\t")])]),i("section",{staticClass:"theme wt-scroll"},[i("ul",[i("li",{staticClass:"theme-title"},[e._v("ERROR部分")]),e._l(e.themeList,(function(t,s){return i("li",{key:s,staticClass:"theme-item",class:{"theme-item-checked":e.curThemeIndex==s},on:{click:function(i){return e.getThemeContent(s,t.id)}}},[i("div",{staticClass:"theme-content"},[i("div",{staticClass:"theme-time"},[e._v(e._s(t.create_time))]),i("div",{staticClass:"theme-name"},[e._v(e._s(t.name))])])])}))],2),i("div",{staticClass:"rlog-input-page"},[e._v("\n\t\t\t\t\t第\n\t\t\t\t\t"),i("input",{directives:[{name:"model",rawName:"v-model",value:e.themePage,expression:"themePage"}],attrs:{min:"1",max:"",type:"number"},domProps:{value:e.themePage},on:{click:e.changeThemeNum,input:function(t){t.target.composing||(e.themePage=t.target.value)}}}),e._v("\n\t\t\t\t\t页\n\t\t\t\t")])]),i("section",{staticClass:"content wt-scroll"},[i("div",[i("div",{staticClass:"content-title theme-content"},[e._v("\n\t\t\t\t\t\t"+e._s(e.themeContent.subject.name)+" "+e._s(e.themeContent.subject.create_time)+"\n\t\t\t\t\t")]),e._m(0),i("pre",{staticClass:"content-body"},[e._v("            "+e._s(e.themeContent.body))])])])])])},n=[function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticStyle:{"padding-left":"10px"}},[i("span",{staticStyle:{color:"red"}},[e._v("●")]),i("span",{staticStyle:{color:"#FFC000"}},[e._v("●")]),i("span",{staticStyle:{color:"#2E75B6"}},[e._v("●")])])}],a=(i("7f7f"),i("456d"),i("ac6a"),{name:"examplelog",data:function(){return{muluList:[],muluDirectory:"",curMuluIndex:0,muluPage:1,prePage1:1,themeList:[],curThemeIndex:0,themePage:1,prePage:1,themeContent:{body:"",subject:{create_time:"",name:""}}}},watch:{muluPage:function(e){var t=this;this.themeList=[],Object.keys(this.themeContent).forEach((function(e){t.themeContent[e]=""})),this.getMuluList()},muluList:function(){0!=this.muluList.length&&this.getThemeList(0,this.muluList[0].name)},themeList:function(){0!=this.themeList.length&&this.getThemeContent(0,this.themeList[0].id)}},methods:{changeThemeNum:function(){this.themePage>this.prePage?1==this.prePage?(this.themePage=1,this.themePage=this.prePage):(this.themePage=this.prePage-1,this.prePage=this.themePage):(this.themePage=this.prePage+1,this.prePage=this.themePage),this.getThemeList(this.curMuluIndex,this.muluDirectory)},changeMuluPage:function(){var e=this;this.muluPage>this.prePage1?1==this.prePage1?(this.muluPage1=1,this.muluPage=this.prePage1):(this.muluPage=this.prePage1-1,this.prePage1=this.themePage1):(this.muluPage=this.prePage1+1,this.prePage1=this.muluPage),this.themeList=[],Object.keys(this.themeContent).forEach((function(t){e.themeContent[t]=""})),this.getMuluList()},getMuluList:function(){var e=this,t=this.$wool.showloading("正在加载目录"),i={page:this.muluPage,server:this.$route.query.app_log};this.$wf.post("devops?method=/devops/rlog/directorys",i).then((function(i){t.hide(0),e.muluList=i.items})).catch((function(i){t.hide(0),e.$wool.showwarn(i)}))},getThemeList:function(e,t){var i=this;this.curMuluIndex=e,this.curThemeIndex=0,Object.keys(this.themeContent).forEach((function(e){i.themeContent[e]=""})),this.muluDirectory=t;var s=this.$wool.showloading("正在加载主题"),n={page:this.themePage,directory:t,server:this.$route.query.app_log};this.$wf.post("devops?method=/devops/rlog/subjects",n).then((function(e){s.hide(0),console.log(e),i.themeList=e.items;for(var t=0;t<i.themeList.length;t++){var n=new Date(i.themeList[t].create_time),a=n.getHours();a=a<10?"0"+a:a;var u=n.getMinutes();u=u<10?"0"+u:u;var h=n.getSeconds();h=h<10?"0"+h:h,i.themeList[t].create_time=a+":"+u+":"+h}})).catch((function(e){s.hide(0),i.$wool.showwarn("获取"+i.$route.query.app_log+"主题失败",e.message)}))},getThemeContent:function(e,t){var i=this;this.curThemeIndex=e;var s=this.$wool.showloading("正在加载..."),n={directory:this.muluDirectory,server:this.$route.query.app_log,subject:t};this.$wf.post("devops?method=/devops/rlog/content",n).then((function(e){s.hide(0),i.themeContent=e;var t=new Date(e.subject.create_time),n=t.getFullYear(),a=t.getMonth()+1;a=a<10?"0"+a:a;var u=t.getDate();u=u<10?"0"+u:u;var h=t.getHours();h=h<10?"0"+h:h;var o=t.getMinutes();o=o<10?"0"+o:o;var c=t.getSeconds();c=c<10?"0"+c:c,i.themeContent.subject.create_time=n+"-"+a+"-"+u+" "+h+":"+o+":"+c})).catch((function(e){s.hide(0),i.$wool.showwarn(e)}))},goToDetailLog:function(e){var t="/"+this.site_name+"/examplelogdetail?s="+this.$route.query.app_log+"&d="+e;window.open(t)}},created:function(){},mounted:function(){var e=this;this.getMuluList(),document.addEventListener("keydown",(function(t){38==t.keyCode?(0==e.themeList.length||-1!=e.curThemeIndex&&e.curThemeIndex>0&&(e.curThemeIndex--,e.getThemeContent(e.curThemeIndex,e.themeList[e.curThemeIndex].id)),-1!=e.curThemeIndex&&0!=e.themeList.length||e.curMuluIndex>0&&(e.curMuluIndex--,e.getThemeList(e.curMuluIndex,e.muluList[e.curMuluIndex].name))):40==t.keyCode?(0==e.themeList.length||-1!=e.curThemeIndex&&e.curThemeIndex<e.themeList.length-1&&(e.curThemeIndex++,e.getThemeContent(e.curThemeIndex,e.themeList[e.curThemeIndex].id)),-1!=e.curThemeIndex&&0!=e.themeList.length||e.curMuluIndex<e.muluList.length-1&&(e.curMuluIndex++,e.getThemeList(e.curMuluIndex,e.muluList[e.curMuluIndex].name))):37==t.keyCode?e.curThemeIndex=-1:39==t.keyCode&&(e.curThemeIndex=0)}))},beforeDestroy:function(){}}),u=a,h=(i("f1e5"),i("2877")),o=Object(h["a"])(u,s,n,!1,null,"0de973c5",null);t["default"]=o.exports},6557:function(e,t,i){},f1e5:function(e,t,i){"use strict";i("6557")}}]);