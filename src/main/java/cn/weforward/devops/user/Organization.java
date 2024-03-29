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
package cn.weforward.devops.user;

import cn.weforward.common.util.StringUtil;

/**
 * 组织
 * 
 * @author daibo
 *
 */
public class Organization {

	protected String m_Id;

	protected String m_Name;

	public Organization() {
	}

	public Organization(String id, String name) {
		m_Id = id;
		m_Name = name;
	}

	public void setId(String id) {
		m_Id = id;
	}

	public String getId() {
		return m_Id;
	}

	public void setName(String name) {
		m_Name = name;
	}

	public String getName() {
		return m_Name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Organization) {
			return StringUtil.eq(m_Id, ((Organization) obj).getId());
		}
		return false;
	}

	@Override
	public String toString() {
		return m_Id + "=" + m_Name;
	}

	public static Organization DEFAULT = new Organization("default", "默认");

	public static Organization valueOf(String id, String name) {
		if (DEFAULT.m_Id.equals(id) && DEFAULT.m_Name.equals(name)) {
			return DEFAULT;
		}
		return new Organization(id, name);
	}

}
