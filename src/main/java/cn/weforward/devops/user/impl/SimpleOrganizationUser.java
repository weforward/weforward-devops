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
package cn.weforward.devops.user.impl;

import java.util.Collections;
import java.util.List;

import cn.weforward.common.util.StringUtil;
import cn.weforward.devops.user.Organization;
import cn.weforward.framework.ApiException;
import cn.weforward.protocol.ops.Right;
import cn.weforward.protocol.ops.Role;
import cn.weforward.protocol.ops.User;

/**
 * 简单的组织用户实现
 * 
 * @author daibo
 *
 */
public class SimpleOrganizationUser implements User {

	protected String m_Id;

	protected String m_Name;

	protected String m_Password;

	protected List<Right> m_Right;

	protected Organization m_Organization;

	public SimpleOrganizationUser(String id, String name, List<Right> right) {
		m_Id = id;
		m_Name = name;
		m_Right = right;
	}

	/**
	 * 设置密码
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		m_Password = password;
	}

	/**
	 * 检查密码是否正确
	 * 
	 * @param password
	 * @return
	 * @throws ApiException
	 */
	public boolean checkPassword(String password) {
		if (StringUtil.isEmpty(password)) {
			return false;
		}
		return StringUtil.eq(m_Password, password);
	}

	@Override
	public String getId() {
		return m_Id;
	}

	@Override
	public String getName() {
		return m_Name;
	}

	@Override
	public List<Right> getRight() {
		return getRights();
	}

	@Override
	public List<Right> getRights() {
		return m_Right;
	}

	@Override
	public List<Role> getRoles() {
		return Collections.emptyList();
	}

	public void setOrganization(Organization org) {
		m_Organization = org;
	}

	public Organization getOrganization() {
		return m_Organization;
	}

}