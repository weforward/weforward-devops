package cn.weforward.devops.weforward.view;

import java.util.List;

import cn.weforward.protocol.doc.annotation.DocAttribute;

public class IdAndRightView {

	protected String m_Id;

	protected List<String> m_Rights;

	public IdAndRightView() {

	}

	public void setId(String id) {
		m_Id = id;
	}

	@DocAttribute(description = "id")
	public String getId() {
		return m_Id;
	}

	public void setRights(List<String> rights) {
		m_Rights = rights;
	}

	@DocAttribute(description = "权限列表,如:READ,OPERATOR,UPDATE")
	public List<String> getRights() {
		return m_Rights;
	}
}
