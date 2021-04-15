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

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

@DocObject(description = "调试参数")
public class DebugParam {

	protected String m_ServiceName;

	protected String m_ServiceNo;

	protected String m_ScriptArgs;

	protected String m_ScriptName;

	protected String m_ScriptSource;

	public void setServiceName(String name) {
		m_ServiceName = name;
	}

	@DocAttribute(description = "服务名", necessary = true)
	public String getServiceName() {
		return m_ServiceName;
	}

	public void setServiceNo(String no) {
		m_ServiceNo = no;
	}

	@DocAttribute(description = "服务编号", necessary = true)
	public String getServiceNo() {
		return m_ServiceNo;
	}

	public void setScriptArgs(String args) {
		m_ScriptArgs = args;
	}

	@DocAttribute(description = "脚本参数")
	public String getScriptArgs() {
		return m_ScriptArgs;
	}

	public void setScriptName(String name) {
		m_ScriptName = name;
	}

	@DocAttribute(description = "脚本名（与脚本源码二选一）")
	public String getScriptName() {
		return m_ScriptName;
	}

	public void setScriptSource(String source) {
		m_ScriptSource = source;
	}

	@DocAttribute(description = "脚本源码（与脚本名二选一）")
	public String getScriptSource() {
		return m_ScriptSource;
	}

	@Override
	public String toString() {
		return "serviceName=" + m_ServiceName + ",serviceNo=" + m_ServiceNo + ",scriptSource=" + m_ScriptSource
				+ ",scriptName=" + m_ScriptName + ",scriptArgs=" + m_ScriptArgs;
	}
}
