package cn.weforward.devops.weforward.view;

import cn.weforward.protocol.doc.annotation.DocAttribute;

import java.util.Calendar;
import java.util.Date;

public class ApiInvokeInfoParam {

//    protected Date m_Begin;
//    protected Date m_End;
    protected int m_TimeType;
    protected String m_ServiceName;
    protected String m_ServiceNo;
    protected String m_MethodName;

    public ApiInvokeInfoParam(){

    }

//    @DocAttribute(index = 5,description = "开始时间")
//    public void setBegin(Date v) {
//        m_Begin = v;
//    }
//
//    @DocAttribute(index = 6,description = "结束时间")
//    public void setEnd(Date v) {
//        m_End = v;
//    }

    @DocAttribute(index = 3,type = Integer.class,description = "时间快捷类型，参考“追踪分析常量值”", necessary = true)
    public void setTimeType(int v) {
        m_TimeType = v;
    }

    @DocAttribute(index = 1,type = String.class,description = "服务名", necessary = true)
    public void setServiceName(String v) {
        m_ServiceName = v;
    }

    @DocAttribute(index = 2,type = String.class,description = "服务编号", necessary = true)
    public void setServiceNo(String v) {
        m_ServiceNo = v;
    }

    @DocAttribute(index = 4,type = String.class,description = "方法名，按方法获取调用统计视图时必传")
    public void setMethodName(String v) {
        m_MethodName = v;
    }

    public Date getBegin() {
        Calendar cal = Calendar.getInstance();
        if(3==m_TimeType){
            cal.add(Calendar.MINUTE,-60*24);
            return cal.getTime();
        }
        if(2==m_TimeType){
            cal.add(Calendar.MINUTE,-60*12);
            return cal.getTime();
        }
        cal.add(Calendar.MINUTE,-60);
        return cal.getTime();
    }

    public Date getEnd() {
        return new Date();
    }

    public int getTimeType() {
        return m_TimeType;
    }

    public String getServiceName() {
        return m_ServiceName;
    }

    public String getServiceNo() {
        return m_ServiceNo;
    }

    public String getMethodName() {
        return m_MethodName;
    }

}
