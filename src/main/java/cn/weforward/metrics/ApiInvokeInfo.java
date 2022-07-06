package cn.weforward.metrics;

import cn.weforward.common.NameItem;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * api调用情况汇总
 *
 * @author HeavyCheng
 */
public class ApiInvokeInfo {

    /** api调用次数列表 */
    protected List<InvokeItem> m_InvokeItems;

    /** 时间调用次数分布统计项 */
    protected List<TimeInvokeItem> m_TimeInvokeItems;

    /** 响应时间分类统计项 */
    protected List<ResponseTimeItem> m_ResponseTimeItems;

    public ApiInvokeInfo(){
        
    }

    /**
     * 获取api调用次数列表
     * @return
     */
    public List<InvokeItem> getInvokeItems(){
        if(null==m_InvokeItems) return Collections.emptyList();
        return m_InvokeItems;
    }

    /**
     * 获取api的时间调用次数分布统计项
     * @return
     */
    public List<TimeInvokeItem> getTimeInvokeItems(){
        if(null==m_TimeInvokeItems) return Collections.emptyList();
        return m_TimeInvokeItems;
    }

    /**
     * 获取api的响应时间分类统计项
     * @return
     */
    public List<ResponseTimeItem> getResponseTimeItems(){
        if(null==m_ResponseTimeItems) return Collections.emptyList();
        return m_ResponseTimeItems;
    }

    public static class InvokeItem {

        /**api名称*/
        protected String m_Name;

        /**调用次数*/
        protected int m_Count;

        public InvokeItem(String name,int count) {
            m_Name = name;
            m_Count = count;
        }

        /**
         * api名称
         *
         * @return
         */
        public String getName() {
            return m_Name;
        }

        /**
         * 调用次数
         * @return
         */
        public int getCount() {
            return m_Count;
        }
    }

    public static class TimeInvokeItem {

        /**调用时间*/
        protected Date m_Time;

        /**调用次数*/
        protected int m_Count;

        public TimeInvokeItem(Date time,int count) {
            m_Time = time;
            m_Count = count;
        }

        /**
         * 调用时间
         *
         * @return
         */
        public Date getTime() {
            return m_Time;
        }

        /**
         * 调用次数
         * @return
         */
        public int getCount() {
            return m_Count;
        }
    }

    public static class ResponseTimeItem {

        /**类型*/
        protected NameItem m_Type;

        /**调用次数*/
        protected int m_Count;

        /**百分比，92.33% */
        protected double m_Percent;

        public ResponseTimeItem(NameItem type,int count,double percent) {
            m_Type = type;
            m_Count = count;
            m_Percent = percent;
        }

        /**
         * 类型
         *
         * @return
         */
        public NameItem getType() {
            return m_Type;
        }

        /**
         * 调用次数
         * @return
         */
        public int getCount() {
            return m_Count;
        }

        /**
         * 百分比
         * @return
         */
        public double getPercent() {
            return m_Percent;
        }
    }
}
