package cn.weforward.devops.weforward.view;

import cn.weforward.protocol.doc.annotation.DocAttribute;

import java.util.Date;

public class ApiInvokeInfoParam {

    protected Date m_Begin;
    protected Date m_End;
    protected String m_ServiceName;
    protected String m_ServiceNo;

    public ApiInvokeInfoParam(){

    }

    @DocAttribute(description = "开始时间", necessary = true)
    public void setBegin(Date v) {
        m_Begin = v;
    }

    @DocAttribute(description = "结束时间", necessary = true)
    public void setEnd(Date v) {
        m_End = v;
    }

    @DocAttribute(description = "服务名")
    public void setServiceName(String v) {
        m_ServiceName = v;
    }

    @DocAttribute(description = "服务编号")
    public void setServiceNo(String v) {
        m_ServiceNo = v;
    }

    public Date getBegin() {
        return m_Begin;
    }

    public Date getEnd() {
        return m_End;
    }

    public String getServiceName() {
        return m_ServiceName;
    }

    public String getServiceNo() {
        return m_ServiceNo;
    }

}
