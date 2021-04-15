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

import cn.weforward.data.annotation.ResourceExt;

/**
 * 项目参数
 * 
 * @author daibo
 *
 */
public class ProjectParam extends BaseParam {
	/** id */
	protected String m_Id;
	/** 类型 */
	protected int m_Type;
	/** 名称 */
	protected String m_Name;
	/** 描述 */
	protected String m_Desc;
	/** 所属者 */
	protected String m_Owner;
	/** 属性 */
	protected List<String> m_Props;
	/* java 项目 start */
	/** 服务端口 */
	protected List<Integer> m_ServerPorts;
	/* java 项目 end */

	/* html 项目 start */
	/** 访问地址 */
	protected List<String> m_AccessUrls;
	/* html 项目 end */

	public ProjectParam() {

	}

	public String getId() {
		return m_Id;
	}

	public void setId(String id) {
		m_Id = id;
	}

	public int getType() {
		return m_Type;
	}

	public void setType(int type) {
		m_Type = type;
	}

	public String getName() {
		return m_Name;
	}

	public void setName(String name) {
		m_Name = name;
	}

	public String getDesc() {
		return m_Desc;
	}

	public void setDesc(String desc) {
		m_Desc = desc;
	}

	public String getOwner() {
		return m_Owner;
	}

	public void setOwner(String owner) {
		m_Owner = owner;
	}

	public List<String> getProps() {
		return m_Props;
	}

	public void setProps(List<String> props) {
		m_Props = props;
	}

	public List<Integer> getServerPorts() {
		return m_ServerPorts;
	}

	@ResourceExt(component = Integer.class)
	public void setServerPorts(List<Integer> serverPorts) {
		m_ServerPorts = serverPorts;
	}

	public List<String> getAccessUrls() {
		return m_AccessUrls;
	}

	public void setAccessUrls(List<String> accessUrls) {
		m_AccessUrls = accessUrls;
	}

}
