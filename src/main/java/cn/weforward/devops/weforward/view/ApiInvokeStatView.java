package cn.weforward.devops.weforward.view;

import cn.weforward.common.NameItem;
import cn.weforward.metrics.ApiInvokeInfo;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 接口调用统计视图
 * 主要按时间调用情况&响应耗时分类的统计项
 * @author HeavyCheng
 */
@DocObject(description = "api时间调用情况&响应耗时分类")
public class ApiInvokeStatView {

    protected ApiInvokeInfo m_Info;

    protected ApiInvokeStatView(ApiInvokeInfo info){
        m_Info = info;
    }

    public static ApiInvokeStatView valueOf(ApiInvokeInfo info) {
        return null == info ? null : new ApiInvokeStatView(info);
    }

    @DocAttribute(index = 1,type = List.class,component = TimeInvokeItemView.class,description = "时间调用次数分布统计项", necessary = true)
    public List<TimeInvokeItemView> getTimeInvokeItems(){
        List<TimeInvokeItemView> list = new ArrayList<TimeInvokeItemView>();
        for(ApiInvokeInfo.TimeInvokeItem item: m_Info.getTimeInvokeItems()){
            list.add(new TimeInvokeItemView(item));
        }
        return list;
    }

    @DocAttribute(index = 2,type = List.class,component = ResponseTimeItemView.class,description = "响应时间分类统计项", necessary = true)
    public List<ResponseTimeItemView> getResponseTimeItems(){
        List<ResponseTimeItemView> list = new ArrayList<ResponseTimeItemView>();
        for(NameItem ni:ApiInvokeInfo.ALL_DURATION){
            ApiInvokeInfo.ResponseTimeItem item = getResponseTimeItem(ni.getId());
            if(null!=item){
                list.add(new ResponseTimeItemView(item));
                continue;
            }
            item = new ApiInvokeInfo.ResponseTimeItem(ni);
            list.add(new ResponseTimeItemView(item));
        }
        return list;
    }

    private ApiInvokeInfo.ResponseTimeItem getResponseTimeItem(int id){
        for(ApiInvokeInfo.ResponseTimeItem item: m_Info.getResponseTimeItems()){
            if(id==item.getTypeId()){
                return item;
            }
        }
        return null;
    }

    @DocObject(description = "api的时间调用次数分布统计项")
    public static class TimeInvokeItemView {

        protected ApiInvokeInfo.TimeInvokeItem m_Item;

        public TimeInvokeItemView(ApiInvokeInfo.TimeInvokeItem item) {
            m_Item = item;
        }

        @DocAttribute(description = "统计时间")
        public Date getTime() {
            return m_Item.getTime();
        }

        @DocAttribute(description = "调用次数")
        public int getCount() {
            return m_Item.getCount();
        }

    }

    @DocObject(description = "api的响应时间分类统计项")
    public static class ResponseTimeItemView {

        protected ApiInvokeInfo.ResponseTimeItem m_Item;

        public ResponseTimeItemView(ApiInvokeInfo.ResponseTimeItem item) {
            m_Item = item;
        }

        @DocAttribute(description = "类型名称")
        public String getType() {
            if(m_Item.getTypeId()==ApiInvokeInfo.DURATION_100MS.id)
                return "<100ms";
            if(m_Item.getTypeId()==ApiInvokeInfo.DURATION_100MS_500MS.id)
                return "<500ms";
            if(m_Item.getTypeId()==ApiInvokeInfo.DURATION_500MS_1S.id)
                return "<1s";
            if(m_Item.getTypeId()==ApiInvokeInfo.DURATION_1S_5S.id)
                return "<5s";
            if(m_Item.getTypeId()==ApiInvokeInfo.DURATION_5S_10S.id)
                return "<10s";
            if(m_Item.getTypeId()==ApiInvokeInfo.DURATION_10S.id)
                return "≥10s";
            return "未知分类";
        }

        @DocAttribute(description = "调用次数")
        public int getCount() {
            return m_Item.getCount();
        }

        @DocAttribute(description = "百分比")
        public String getPercent() {
            if(1==m_Item.getPercent()){
                return "100%";
            }
            if(0==m_Item.getPercent()){
                return "0%";
            }
            return String.format("%.2f", m_Item.getPercent()*100)+"%";
        }

    }
}
