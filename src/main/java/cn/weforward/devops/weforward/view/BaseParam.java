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

import java.util.List;

import cn.weforward.protocol.doc.annotation.DocAttribute;

/**
 * 基本参数
 * 
 * @author daibo
 *
 */
public class BaseParam {
	/** 操作 */
	protected String m_Op;
	/** 环境变量 */
	protected List<String> m_Envs;
	/** 绑定 */
	protected List<String> m_Binds;
	/** 群组 */
	protected List<String> m_Groups;
	/** 群组 */
	protected List<IdAndRightView> m_GroupList;

	public BaseParam() {

	}

	@DocAttribute(description = "操作标识，如:add|delete", example = "add")
	public String getOp() {
		return m_Op;
	}

	public void setOp(String op) {
		m_Op = op;
	}

	@DocAttribute(description = "环境变量", example = "{\"111=222\",\"222\"=\"333\"}")
	public List<String> getEnvs() {
		return m_Envs;
	}

	public void setEnvs(List<String> envs) {
		m_Envs = envs;
	}

	@DocAttribute(description = "绑定", example = "{\"/data/=/data/\",\"/var/log/\"=\"/home/log/\"}")
	public List<String> getBinds() {
		return m_Binds;
	}

	public void setBinds(List<String> binds) {
		m_Binds = binds;
	}

	@DocAttribute(description = "组", example = "{\"Group$1\",\"Group$2\"}")
	public List<String> getGroups() {
		return m_Groups;
	}

	public void setGroups(List<String> groups) {
		m_Groups = groups;
	}

	@DocAttribute(description = "组")
	public List<IdAndRightView> getGroupList() {
		return m_GroupList;
	}

	public void setGroupList(List<IdAndRightView> list) {
		m_GroupList = list;
	}
}
