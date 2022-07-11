package cn.weforward.devops.weforward.view;

import java.util.Date;

import cn.weforward.common.NameItem;
import cn.weforward.framework.doc.DocPageParams;
import cn.weforward.metrics.ApiInvokeInfo;
import cn.weforward.protocol.doc.annotation.DocAttribute;

public class TracerParam extends DocPageParams {

	protected String m_Id;
	protected Date m_Begin;
	protected Date m_End;
	protected String m_ServiceName;
	protected String m_ServiceNo;
	protected String m_Method;
	protected int m_DurationType;

	public TracerParam() {

	}

	@DocAttribute(description = "追踪id")
	public void setId(String id) {
		m_Id = id;
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

	@DocAttribute(description = "方法名")
	public void setMethod(String v) {
		m_Method = v;
	}

	@DocAttribute(type = Integer.class,description = "持续时间类型")
	public void setDurationType(int v) {
		m_DurationType = v;
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

	public String getMethod() {
		return m_Method;
	}

	public String getId() {
		return m_Id;
	}

	public NameItem getDurationType(){
		return NameItem.findById(m_DurationType,ApiInvokeInfo.ALL_DURATION);
	}

	public int getMinDuration(){
		if(m_DurationType==ApiInvokeInfo.DURATION_100MS.id)
			return 0;
		if(m_DurationType==ApiInvokeInfo.DURATION_100MS_500MS.id)
			return 100;
		if(m_DurationType==ApiInvokeInfo.DURATION_500MS_1S.id)
			return 500;
		if(m_DurationType==ApiInvokeInfo.DURATION_1S_5S.id)
			return 1000;
		if(m_DurationType==ApiInvokeInfo.DURATION_5S_10S.id)
			return 5000;
		if(m_DurationType==ApiInvokeInfo.DURATION_10S.id)
			return 10000;
		return 0;
	}

	public int getMaxDuration(){
		if(m_DurationType==ApiInvokeInfo.DURATION_100MS.id)
			return 100;
		if(m_DurationType==ApiInvokeInfo.DURATION_100MS_500MS.id)
			return 500;
		if(m_DurationType==ApiInvokeInfo.DURATION_500MS_1S.id)
			return 1000;
		if(m_DurationType==ApiInvokeInfo.DURATION_1S_5S.id)
			return 5000;
		if(m_DurationType==ApiInvokeInfo.DURATION_5S_10S.id)
			return 10000;
//		if(m_DurationType==ApiInvokeInfo.DURATION_10S.id)
//			return 0;
		return 0;
	}

}
