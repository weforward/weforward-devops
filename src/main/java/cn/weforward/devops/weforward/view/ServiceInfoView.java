/**
 * Copyright (c) 2019,2020 honintech
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */
package cn.weforward.devops.weforward.view;

import java.util.Date;
import java.util.List;

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;
import cn.weforward.protocol.ops.ServiceExt;

/**
 * （微）服务信息视图
 * 
 * @author daibo
 *
 */
@DocObject
public class ServiceInfoView {
	/** 信息类 */
	protected ServiceExt m_Info;

	public ServiceInfoView(ServiceExt info) {
		m_Info = info;
	}

	/** 编号 */
	@DocAttribute(description = "编号", necessary = true, index = 2)
	public String getNo() {
		return m_Info.getNo();
	}

	/** 服务名 */
	@DocAttribute(description = "服务名", necessary = true, index = 1)
	public String getName() {
		return m_Info.getName();
	}

	/** 域名 */
	@Deprecated
	public String getDomain() {
		return m_Info.getDomain();
	}

	/** 端口号 */
	@Deprecated
	public int getPort() {
		return m_Info.getPort();
	}

	@DocAttribute(type = List.class, component = String.class, description = "服务访问链接", necessary = true, index = 10)
	public List<String> getUrl() {
		return m_Info.getUrls();
	}

	/** 版本号 */
	@DocAttribute(description = "版本号", necessary = true, index = 3)
	public String getVersion() {
		return m_Info.getVersion();
	}

	/** 向下兼容的最低版本号 */
	@DocAttribute(description = "向下兼容的最低版本号", index = 4)
	public String getCompatibleVersion() {
		return m_Info.getCompatibleVersion();
	}

	/** 构建版本号 */
	@DocAttribute(description = "构建版本号", index = 5)
	public String getBuildVersion() {
		return m_Info.getBuildVersion();
	}

	/** 运行实例标识 */
	@DocAttribute(description = "运行实例标识", index = 20)
	public String getRunningId() {
		return m_Info.getRunningId();
	}

	/** 心跳时间 */
	@DocAttribute(description = "心跳时间", index = 30)
	public Date getHeartbeat() {
		return m_Info.getHeartbeat();
	}

	/** 是否不可达 */
	@DocAttribute(description = "是否不可达", necessary = true, index = 50)
	public boolean isInaccessible() {
		return m_Info.isInaccessible();
	}

	/** 是否超时 */
	@DocAttribute(description = "是否超时", necessary = true, index = 51)
	public boolean isTimeout() {
		return m_Info.isTimeout();
	}

	/** 是否不可用 */
	@DocAttribute(description = "是否不可用", necessary = true, index = 52)
	public boolean isUnavailable() {
		return m_Info.isUnavailable();
	}

	/**
	 * 是否过载
	 */
	@DocAttribute(description = "是否过载", necessary = true, index = 53)
	public boolean isOverload() {
		return m_Info.isOverload();
	}

	/** 备注 */
	@DocAttribute(description = "备注", index = 1000)
	public String getNote() {
		return m_Info.getNote();
	}

}
