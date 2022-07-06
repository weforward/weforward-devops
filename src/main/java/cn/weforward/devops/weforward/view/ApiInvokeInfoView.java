package cn.weforward.devops.weforward.view;

import cn.weforward.metrics.ApiInvokeInfo;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 接口调用情况视图
 *
 * @author HeavyCheng
 */
@DocObject(description = "接口调用情况视图")
public class ApiInvokeInfoView {

    protected ApiInvokeInfo m_Info;

    protected ApiInvokeInfoView(ApiInvokeInfo info){
        m_Info = info;
    }

    public static ApiInvokeInfoView valueOf(ApiInvokeInfo info) {
        return null == info ? null : new ApiInvokeInfoView(info);
    }

    @DocAttribute(index = 1,type = List.class,component = InvokeItemView.class,description = "获取api调用次数列表", necessary = true)
    public List<InvokeItemView> getInvokeItems(){
        List<InvokeItemView> list = new ArrayList<InvokeItemView>();
       for(ApiInvokeInfo.InvokeItem item: m_Info.getInvokeItems()){
           list.add(new InvokeItemView(item));
       }
       return list;
    }

    @DocAttribute(index = 2,type = List.class,component = TimeInvokeItemView.class,description = "api的时间调用次数分布统计项列表", necessary = true)
    public List<TimeInvokeItemView> getTimeInvokeItems(){
        List<TimeInvokeItemView> list = new ArrayList<TimeInvokeItemView>();
        for(ApiInvokeInfo.TimeInvokeItem item: m_Info.getTimeInvokeItems()){
            list.add(new TimeInvokeItemView(item));
        }
        return list;
    }

    @DocAttribute(index = 3,type = List.class,component = ResponseTimeItemView.class,description = "api的响应时间分类统计项列表", necessary = true)
    public List<ResponseTimeItemView> getResponseTimeItems(){
        List<ResponseTimeItemView> list = new ArrayList<ResponseTimeItemView>();
        for(ApiInvokeInfo.ResponseTimeItem item: m_Info.getResponseTimeItems()){
            list.add(new ResponseTimeItemView(item));
        }
        return list;
    }

    @DocObject(description = "api调用次数项")
    public static class InvokeItemView {

        protected ApiInvokeInfo.InvokeItem m_Item;

        public InvokeItemView(ApiInvokeInfo.InvokeItem item) {
            m_Item = item;
        }

        @DocAttribute(description = "api名称")
        public String getName() {
            return m_Item.getName();
        }

        @DocAttribute(description = "调用次数")
        public int getCount() {
            return m_Item.getCount();
        }
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
            return m_Item.getType().getName();
        }

        @DocAttribute(description = "调用次数")
        public int getCount() {
            return m_Item.getCount();
        }

        @DocAttribute(description = "百分比")
        public double getPercent() {
            return m_Item.getPercent();
        }

    }

}
