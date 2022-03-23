package cn.weforward.devops.weforward.view;

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

@DocObject(description = "网关对象")
public class GatewayVo {

	protected String m_Id;

	protected String m_Host;

	protected int m_Port;

	protected String m_Gateway;

	@DocAttribute(description = "节点标识（服务器id）")
	public String getId() {
		return m_Id;
	}

	public void setId(String id) {
		m_Id = id;
	}

	@DocAttribute(description = "节点的域名（ip地址）")
	public String getHost() {
		return m_Host;
	}

	public void setHost(String host) {
		m_Host = host;
	}

	@DocAttribute(description = "节点的端口")
	public int getPort() {
		return m_Port;
	}

	public void setPort(int port) {
		m_Port = port;
	}

	@DocAttribute(description = "网关")
	public String getGateway() {
		return m_Gateway;
	}

	public void setGateway(String gateway) {
		m_Gateway = gateway;
	}
}
