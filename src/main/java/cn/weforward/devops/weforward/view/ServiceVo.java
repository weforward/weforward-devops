package cn.weforward.devops.weforward.view;

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

@DocObject(description = "服务对象")
public class ServiceVo {

	protected String m_No;

	protected String m_Name;

	protected String m_BuildVersion;

	protected String m_Note;

	protected String m_RunningId;

	protected String m_Gateway;

	@DocAttribute(description = "服务编号")
	public String getNo() {
		return m_No;
	}

	public void setNo(String no) {
		m_No = no;
	}

	@DocAttribute(description = "服务名称")
	public String getName() {
		return m_Name;
	}

	public void setName(String name) {
		m_Name = name;
	}

	@DocAttribute(description = "服务构建版本")
	public String getBuildVersion() {
		return m_BuildVersion;
	}

	public void setBuildVersion(String buildVersion) {
		m_BuildVersion = buildVersion;
	}

	@DocAttribute(description = "服务备注")
	public String getNote() {
		return m_Note;
	}

	public void setNote(String note) {
		m_Note = note;
	}

	@DocAttribute(description = "实例id")
	public String getRunningId() {
		return m_RunningId;
	}

	public void setRunningId(String id) {
		m_RunningId = id;
	}

	@DocAttribute(description = "网关")
	public String getGateway() {
		return m_Gateway;
	}

	public void setGateway(String gateway) {
		m_Gateway = gateway;
	}

}
