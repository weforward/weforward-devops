package cn.weforward.devops.weforward.view;

import cn.weforward.common.NameItem;
import cn.weforward.metrics.ApiInvokeInfo;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 追踪服务常量
 *
 * @author HeavyCheng
 */
@DocObject(description = "追踪服务常量值定义")
public class TracerConstantView {

    public TracerConstantView(){}

    @DocAttribute(index = 1,type = List.class,component = ConstantView.class,description = "响应耗时类型")
    public List<ConstantView> getDurationType(){
        List<ConstantView> list = new ArrayList<ConstantView>(ApiInvokeInfo.ALL_DURATION.size());
        for(NameItem ni:ApiInvokeInfo.ALL_DURATION){
            list.add(ConstantView.valueOf(ni));
        }
        return list;
    }

    @DocAttribute(index = 2,type = List.class,component = ConstantView.class,description = "快捷查询类型")
    public List<ConstantView> getSearchTimeType(){
        List<ConstantView> list = new ArrayList<ConstantView>(ApiInvokeInfo.ALL_SEARCH_TIME.size());
        for(NameItem ni:ApiInvokeInfo.ALL_SEARCH_TIME){
            list.add(ConstantView.valueOf(ni));
        }
        return list;
    }

    @DocObject(description = "常量值定义")
    public static class ConstantView{

        String name;
        int value;

        public ConstantView(String name,int value){
            this.name = name;
            this.value = value;
        }

        public static ConstantView valueOf(NameItem ni){
            return new ConstantView(ni.getName(),ni.id);
        }

        @DocAttribute(type = String.class, description = "名称")
        public String getName() {
            return name;
        }

        @DocAttribute(type = Integer.class, description = "值")
        public int getValue() {
            return value;
        }

    }

}
