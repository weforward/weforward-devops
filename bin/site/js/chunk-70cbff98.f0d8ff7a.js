(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-70cbff98"],{"2a60":function(e,t,i){},"65bf":function(e,t,i){"use strict";i.r(t);var s=function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticClass:"wangguan_area"},[i("div",{staticClass:"facts_boxs"},[i("div",{staticClass:"selbox"},[e._v("\n        网关："),i("SingleSelect",{attrs:{items:e.wangguanList,selectname:e.id},on:{select:e.changeNo}}),i("div",{staticClass:"time_box"},[e._v("\n           时间段："),i("WoolPCDateTimePicker",{staticClass:"time_sel",attrs:{format:"yyyy-MM-dd HH:mm",autopickdate:!1},model:{value:e.kaishiTime,callback:function(t){e.kaishiTime=t},expression:"kaishiTime"}}),e._v(" 至\n          "),i("WoolPCDateTimePicker",{staticClass:"time_sel",attrs:{format:"yyyy-MM-dd HH:mm",autopickdate:!1},model:{value:e.jieshuTime,callback:function(t){e.jieshuTime=t},expression:"jieshuTime"}})],1),i("div",{staticClass:"query_button",staticStyle:{"margin-left":"1%"},on:{click:e.freshEcharts}},[e._v("查询")]),i("div",{staticClass:"query_button",on:{click:e.freshEcharts}},[e._v("刷新")]),i("div",{staticClass:"query_button",staticStyle:{width:"80px","background-color":"#563d7c",color:"#ffe484"},on:{click:e.goToRlog}},[e._v("查看日志")])],1),i("div",{staticClass:"item_box"},[i("div",{class:["query_button",{is_sel:!e.isOnTime}],on:{click:function(t){return e.clickTime("onTime")}}},[e._v("实时")]),i("div",{class:["query_button",{is_sel:!e.isRecentHour}],staticStyle:{"padding-left":"10px","padding-right":"10px"},on:{click:function(t){return e.clickTime("recentHour")}}},[e._v("近24小时")]),i("div",{class:["query_button",{is_sel:!e.isRecentDay}],on:{click:function(t){return e.clickTime("recentDay")}}},[e._v("近7天")]),i("div",{staticClass:"item",staticStyle:{width:"180px"}},[i("span",[e._v("启动时间")]),i("br"),i("span",[e._v(e._s(e.startTime||"--"))])]),i("div",{staticClass:"item",staticStyle:{width:"180px"}},[i("span",[e._v("持续时间")]),i("br"),i("span",[e._v(e._s(e.loadTime||"--"))])]),i("div",{staticClass:"item"},[i("span",[e._v("内存上限")]),i("br"),i("span",[e._v(e._s(e.neicunMax||"--"))])]),i("div",{staticClass:"item"},[i("span",[e._v("已用内存")]),i("br"),i("span",[e._v(e._s(e.usedNeicun||"--"))])]),i("div",{staticClass:"item"},[i("span",[e._v("已分配内存")]),i("br"),i("span",[e._v(e._s(e.fenpeiNeicun||"--"))])])])]),e._m(0),i("div",{staticClass:"JVM_box",staticStyle:{width:"98%","background-color":"#fff","border-radius":"5px",border:"#e3ebf6 1px solid"}}),e._m(1),e._m(2),e._m(3),e._m(4)])},n=[function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticClass:"item_title",staticStyle:{"margin-bottom":"5px","margin-top":"5px"}},[i("span",[e._v("JVM Memory")])])},function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticClass:"item_title"},[i("span",[e._v("JVM Misc")])])},function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticClass:"JVM_box"},[i("div",{attrs:{id:"cpu_box"}}),i("div",{attrs:{id:"thread_box"}}),i("div",{attrs:{id:"GC_box"}})])},function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticClass:"item_title"},[i("span",[e._v("Overload")])])},function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticClass:"JVM_box",staticStyle:{height:"550px"}},[i("div",{attrs:{id:"RPC_pl"}}),i("div",{attrs:{id:"RPC_num"}}),i("div",{attrs:{id:"stream_pl"}}),i("div",{attrs:{id:"stream_num"}})])}],r=(i("96cf"),i("1da1")),a=i("313e"),o={components:{SingleSelect:function(){return i.e("chunk-90e745ba").then(i.bind(null,"4e15"))},WoolPCDateTimePicker:function(){return i.e("chunk-55326304").then(i.bind(null,"2d87"))}},data:function(){return{id:"",wangguanList:[],startTime:"",loadTime:"",neicunMax:"",usedNeicun:"",fenpeiNeicun:"",timerList:[],kaishiTime:"",jieshuTime:"",queryKsTime:"",queryJsTime:"",isOnTime:!1,isRecentHour:!1,isRecentDay:!1}},methods:{freshEcharts:function(){this.id?(this.kaishiTime||this.jieshuTime)&&(this.isOnTime=!1,this.isRecentHour=!1,this.isRecentDay=!1,new Date(this.kaishiTime)>=new Date(this.jieshuTime)?this.$wool.showwarn("时间选择错误！"):new Date(this.jieshuTime)-new Date(this.kaishiTime)<=6e4?this.$wool.showwarn("时间间隔必须大于1分钟"):(this.queryKsTime=new Date(this.kaishiTime),this.queryJsTime=new Date(this.jieshuTime),this.drawEcharts())):this.$wool.showwarn("请选择网关！")},goToRlog:function(){window.open("/devops/examplelog?app_log=weforward-gateway_"+this.id)},clickTime:function(e){this.id&&(this.kaishiTime="",this.jieshuTime="",this.queryJsTime=new Date,"onTime"==e?(this.isOnTime=!0,this.isRecentHour=!1,this.isRecentDay=!1,this.queryKsTime=new Date(this.queryJsTime.getTime()-18e5),this.drawEcharts()):"recentHour"==e?(this.isOnTime=!1,this.isRecentHour=!0,this.isRecentDay=!1,this.queryKsTime=new Date(this.queryJsTime.getTime()-864e5),this.kaishiTime=this.queryKsTime.Format("y-M-d h:m"),this.jieshuTime=this.queryJsTime.Format("y-M-d h:m"),this.drawEcharts()):"recentDay"==e&&(this.isOnTime=!1,this.isRecentHour=!1,this.isRecentDay=!0,this.queryKsTime=new Date(this.queryJsTime.getTime()-6048e5),this.kaishiTime=this.queryKsTime.Format("y-M-d h:m"),this.jieshuTime=this.queryJsTime.Format("y-M-d h:m"),this.drawEcharts()))},changeNo:function(){var e=Object(r["a"])(regeneratorRuntime.mark((function e(t){var i,s,n;return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return this.id=t,i=this.$wool.showloading("加载中......"),e.next=4,this.$wf.post("devops?method=/devops/metrics/gw_start_time",{id:t});case 4:return s=e.sent,s&&(n=new Date(s),this.startTime=n.Format()),e.next=8,this.$wf.post("devops?method=/devops/metrics/gw_up_time",{id:t});case 8:return this.loadTime=e.sent,e.next=11,this.$wf.post("devops?method=/devops/metrics/gw_memory_max",{id:t});case 11:return this.neicunMax=e.sent,e.next=14,this.$wf.post("devops?method=/devops/metrics/gw_memory_used",{id:t});case 14:return this.usedNeicun=e.sent,e.next=17,this.$wf.post("devops?method=/devops/metrics/gw_memory_alloc",{id:t});case 17:this.fenpeiNeicun=e.sent,i.hide(0),this.drawEcharts();case 20:case"end":return e.stop()}}),e,this)})));function t(t){return e.apply(this,arguments)}return t}(),getServiceList:function(){var e=this,t=this.$wool.showloading("加载中...");this.$wf.post("devops?method=/devops/metrics/gw_tags").then((function(i){e.wangguanList=i.map((function(e,t){return{name:e,id:e}})),t.hide(0),e.changeNo(i[0])})).catch((function(i){t.hide(0),e.$wool.showwarn(i)}))},drawMemory:function(){var e=Object(r["a"])(regeneratorRuntime.mark((function e(){var t,i,s,n,o,c,m,u,h,d,l,p,_;return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return l=function(){return l=Object(r["a"])(regeneratorRuntime.mark((function e(){var i,s,n,r;return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return i=new Date,s=new Date(i.getTime()-18e5),e.next=4,t.$wf.post("devops?method=/devops/metrics/gw_memory_alloc_range",{end:i,begin:s,id:t.id});case 4:return n=e.sent,e.next=7,t.$wf.post("devops?method=/devops/metrics/gw_memory_used_range",{end:i,begin:s,id:t.id});case 7:r=e.sent,h(n,"s1"),h(r,"s2");case 10:case"end":return e.stop()}}),e)}))),l.apply(this,arguments)},d=function(){return l.apply(this,arguments)},h=function(e,r){if(e){i=[],"s1"==r&&(s=[]),"s2"==r&&(n=[]);var a=[];t.queryJsTime-t.queryKsTime>2592e5&&t.queryJsTime-t.queryKsTime<6048e5?o("MM-dd hh"):t.queryJsTime-t.queryKsTime>=6048e5?o("YYYY-MM-DD hh:mm"):o("hh:mm")}function o(t){for(var o=1048576,c=0;c<e.length;c++)i.push(e[c].time.format(t)),a.push(Math.floor(e[c].value/o*100)/100);"s1"==r?s=a:n=a}},t=this,i=[],s=[],n=[],o=a.init(document.getElementsByClassName("JVM_box")[0]),c={begin:this.queryKsTime,end:this.queryJsTime,id:this.id},e.next=11,this.$wf.post("devops?method=/devops/metrics/gw_memory_alloc_range",c);case 11:return m=e.sent,e.next=14,this.$wf.post("devops?method=/devops/metrics/gw_memory_used_range",c);case 14:u=e.sent,h(m,"s1"),h(u,"s2"),p={title:{text:"已用内存",x:"center",textStyle:{fontSize:15}},tooltip:{trigger:"axis",axisPointer:{animation:!1}},xAxis:{type:"category",splitLine:{show:!1},data:i,boundaryGap:!1},yAxis:{type:"value",boundaryGap:[0,"100%"],splitLine:{show:!1},axisLabel:{formatter:"{value}M"}},series:[{name:"已用内存",type:"line",showSymbol:!1,hoverAnimation:!1,areaStyle:{},data:n},{name:"已分配内存",type:"line",showSymbol:!1,hoverAnimation:!1,areaStyle:{},data:s}],grid:{x:60,y:40,x2:25,y2:50},color:["#E062AE","#3398DB"]},o.setOption(p),this.kaishiTime||(_=window.setInterval((function(){d(),p.xAxis.data=i,p.series[0].data=n,p.series[1].data=s,o.setOption(p)}),1e4),this.timerList.push(_));case 20:case"end":return e.stop()}}),e,this)})));function t(){return e.apply(this,arguments)}return t}(),drawJvm:function(){var e=Object(r["a"])(regeneratorRuntime.mark((function e(t){var i,s,n,o,c,m,u,h,d,l,p,_,v,w,f,g,x,y,b,T,k,C,q,D;return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:if(q=function(e){e.setOption(C)},k=function(){return k=Object(r["a"])(regeneratorRuntime.mark((function e(){var t,i,s,r,a,o,c,h,l,_;return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:if(t=new Date,i=new Date(t.getTime()-18e5),s={end:t,begin:i,id:n.id},!m){e.next=10;break}return e.next=6,n.$wf.post("devops?method=/devops/metrics/gw_cpu_usage_rate_range",s);case 6:r=e.sent,b(r),e.next=50;break;case 10:if(!u){e.next=17;break}return e.next=13,n.$wf.post("devops?method=/devops/metrics/gw_thread_count_range",s);case 13:a=e.sent,b(a),e.next=50;break;case 17:if(!d){e.next=24;break}return e.next=20,n.$wf.post("devops?method=/devops/metrics/gw_gc_full_count_range",s);case 20:o=e.sent,b(o),e.next=50;break;case 24:if(!p){e.next=31;break}return e.next=27,n.$wf.post("devops?method=/devops/metrics/gw_rpc_count_range",s);case 27:c=e.sent,b(c),e.next=50;break;case 31:if(!v){e.next=38;break}return e.next=34,n.$wf.post("devops?method=/devops/metrics/gw_rpc_concurrent_range",s);case 34:h=e.sent,b(h),e.next=50;break;case 38:if(!f){e.next=45;break}return e.next=41,n.$wf.post("devops?method=/devops/metrics/gw_streamc_count_range",s);case 41:l=e.sent,b(l),e.next=50;break;case 45:if(!x){e.next=50;break}return e.next=48,n.$wf.post("devops?method=/devops/metrics/gw_stream_concurrent_range",s);case 48:_=e.sent,b(_);case 50:case"end":return e.stop()}}),e)}))),k.apply(this,arguments)},T=function(){return k.apply(this,arguments)},b=function(e){function t(t){for(var n=0;n<e.length;n++)i.push(e[n].time.format(t)),s.push(e[n].value)}e&&(i=[],s=[],n.queryJsTime-n.queryKsTime>2592e5&&n.queryJsTime-n.queryKsTime<6048e5?t("MM-dd hh"):n.queryJsTime-n.queryKsTime>=6048e5?t("YYYY-MM-DD hh:mm"):t("hh:mm"))},i=[],s=[],n=this,o={begin:this.queryKsTime,end:this.queryJsTime,id:this.id},"cpu_box"!=t){e.next=16;break}return e.next=11,this.$wf.post("devops?method=/devops/metrics/gw_cpu_usage_rate_range",o);case 11:c=e.sent,b(c),m=a.init(document.getElementById("cpu_box")),e.next=62;break;case 16:if("thread_box"!=t){e.next=24;break}return u=a.init(document.getElementById("thread_box")),e.next=20,this.$wf.post("devops?method=/devops/metrics/gw_thread_count_range",o);case 20:h=e.sent,b(h),e.next=62;break;case 24:if("GC_box"!=t){e.next=32;break}return d=a.init(document.getElementById("GC_box")),e.next=28,this.$wf.post("devops?method=/devops/metrics/gw_gc_full_count_range",o);case 28:l=e.sent,b(l),e.next=62;break;case 32:if("RPC_pl"!=t){e.next=40;break}return p=a.init(document.getElementById("RPC_pl")),e.next=36,this.$wf.post("devops?method=/devops/metrics/gw_rpc_count_range",o);case 36:_=e.sent,b(_),e.next=62;break;case 40:if("RPC_num"!=t){e.next=48;break}return v=a.init(document.getElementById("RPC_num")),e.next=44,this.$wf.post("devops?method=/devops/metrics/gw_rpc_concurrent_range",o);case 44:w=e.sent,b(w),e.next=62;break;case 48:if("stream_pl"!=t){e.next=56;break}return f=a.init(document.getElementById("stream_pl")),e.next=52,this.$wf.post("devops?method=/devops/metrics/gw_streamc_count_range",o);case 52:g=e.sent,b(g),e.next=62;break;case 56:if("stream_num"!=t){e.next=62;break}return x=a.init(document.getElementById("stream_num")),e.next=60,this.$wf.post("devops?method=/devops/metrics/gw_stream_concurrent_range",o);case 60:y=e.sent,b(y);case 62:C={title:{text:"动态数据 + 时间坐标轴",x:"center",textStyle:{fontSize:15}},tooltip:{trigger:"axis",axisPointer:{animation:!1}},xAxis:{type:"category",splitLine:{show:!1},data:i,boundaryGap:!1},yAxis:{type:"value",boundaryGap:[0,"100%"],splitLine:{show:!1}},series:[{name:"线程数",type:"line",showSymbol:!1,hoverAnimation:!1,areaStyle:{},data:s}],grid:{x:50,y:40,x2:25,y2:50},color:["#3398DB"]},m?(C.title.text="CPU使用率",C.series[0].name="CPU使用率",C.yAxis.axisLabel={formatter:"{value}%"}):u?(C.title.text="线程数",C.grid.x=30,C.grid.y=25):d?(C.title.text="GC次数",C.series[0].name="GC次数",C.grid.x=30,C.grid.y=25):p?(C.title.text="RPC次数/分钟",C.series[0].name="RPC次数"):v?(C.title.text="RPC并发数",C.series[0].name="RPC并发数"):f?(C.title.text="Stream次数/分钟",C.series[0].name="Stream次数"):x&&(C.title.text="Stream并发数",C.series[0].name="Stream并发数"),m&&q(m),u&&q(u),d&&q(d),p&&q(p),v&&q(v),f&&q(f),x&&q(x),this.kaishiTime||(D=window.setInterval((function(){T(),C.xAxis.data=i,C.series[0].data=s,m?q(m):u?q(u):d?q(d):p?q(p):v?q(v):f?q(f):x&&q(x)}),1e4),this.timerList.push(D));case 72:case"end":return e.stop()}}),e,this)})));function t(t){return e.apply(this,arguments)}return t}(),drawEcharts:function(){for(var e=0;e<this.timerList.length;e++)window.clearInterval(this.timerList[e]);this.drawMemory(),this.drawJvm("cpu_box"),this.drawJvm("thread_box"),this.drawJvm("GC_box"),this.drawJvm("RPC_pl"),this.drawJvm("RPC_num"),this.drawJvm("stream_pl"),this.drawJvm("stream_num")}},created:function(){Date.prototype.Format=function(e){var t=this.getFullYear(),i=this.getMonth()+1;i=i<10?"0"+i:i;var s=this.getDate();s=s<10?"0"+s:s;var n=this.getHours();n=n<10?"0"+n:n;var r=this.getMinutes();r=r<10?"0"+r:r;var a=this.getSeconds();return a=a<10?"0"+a:a,e?t+"-"+i+"-"+s+" "+n+":"+r:t+"-"+i+"-"+s+" "+n+":"+r+":"+a},this.queryJsTime=new Date,this.queryKsTime=new Date(this.queryJsTime.getTime()-18e5),this.isOnTime=!0,this.getServiceList()},destroyed:function(){for(var e=0;e<this.timerList.length;e++)window.clearInterval(this.timerList[e])}},c=o,m=(i("b966"),i("2877")),u=Object(m["a"])(c,s,n,!1,null,"2016ad2c",null);t["default"]=u.exports},b966:function(e,t,i){"use strict";i("2a60")}}]);