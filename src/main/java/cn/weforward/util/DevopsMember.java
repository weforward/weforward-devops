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
package cn.weforward.util;

/**
 * 开发成员
 * 
 * @author daibo
 *
 */
public class DevopsMember {
	/** id */
	protected String m_Id;
	/** 名称 */
	protected String m_Name;
	/** 用户id */
	protected String m_UserId;
	/** 组织id */
	protected String m_OrganizationId;
	/** 组织名称 */
	protected String m_OrganizationName;

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

	public void setUserId(String userId) {
		m_UserId = userId;
	}

	public String getUserId() {
		return m_UserId;
	}

	public void setOrganizationId(String id) {
		m_OrganizationId = id;
	}

	public String getOrganizationId() {
		return m_OrganizationId;
	}

	public void setOrganizationName(String name) {
		m_OrganizationName = name;
	}

	public String getOrganizationName() {
		return m_OrganizationName;
	}

}
