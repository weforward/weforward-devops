package cn.weforward.metrics;

import cn.weforward.common.NameItem;
import cn.weforward.common.NameItems;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * api调用情况汇总
 *
 * @author HeavyCheng
 */
public class ApiInvokeInfo {

    public static NameItem DURATION_100MS = NameItem.valueOf("<100ms",1);
    public static NameItem DURATION_100MS_500MS = NameItem.valueOf("<500ms",2);
    public static NameItem DURATION_500MS_1S = NameItem.valueOf("<1s",3);
    public static NameItem DURATION_1S_5S = NameItem.valueOf("<5s",4);
    public static NameItem DURATION_5S_10S = NameItem.valueOf("<10s",5);
    public static NameItem DURATION_10S = NameItem.valueOf("≥10s",6);
    public static NameItems ALL_DURATION = NameItems.valueOf(DURATION_100MS,DURATION_100MS_500MS,DURATION_500MS_1S,DURATION_1S_5S,DURATION_5S_10S,DURATION_10S);

    public static NameItem SEARCH_TIME_1HOURS = NameItem.valueOf("近1小时",1);
    public static NameItem SEARCH_TIME_6HOURS = NameItem.valueOf("近6小时",2);
    public static NameItem SEARCH_TIME_12HOURS = NameItem.valueOf("近12小时",3);
    public static NameItem SEARCH_TIME_24HOURS = NameItem.valueOf("近24小时",4);
    public static NameItems ALL_SEARCH_TIME = NameItems.valueOf(SEARCH_TIME_1HOURS,SEARCH_TIME_6HOURS,SEARCH_TIME_12HOURS,SEARCH_TIME_24HOURS);

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
