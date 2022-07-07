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

    protected ApiInvokeStatView m_Stat;

    protected ApiInvokeInfoView(ApiInvokeInfo info){
        m_Info = info;
        m_Stat = ApiInvokeStatView.valueOf(info);
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

    @DocAttribute(index = 2,type = ApiInvokeStatView.class,description = "api时间与响应统计", necessary = true)
    public ApiInvokeStatView getStatView(){
        return m_Stat;
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



}
