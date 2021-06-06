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

import cn.weforward.common.UniteId;
import cn.weforward.common.util.TransList;
import cn.weforward.devops.user.Group;
import cn.weforward.devops.user.UserProvider;
import cn.weforward.protocol.ops.User;

public class GroupVo implements Group {

	protected String m_Id;

	protected String m_Name;

	protected String m_Note;

	protected List<String> m_Users = Collections.emptyList();

	protected UserProvider m_UserProvider;

	@Override
	public String getId() {
		return m_Id;
	}

	public void setId(String id) {
		m_Id = id;
	}

	@Override
	public void setName(String name) {
		m_Name = name;
	}

	@Override
	public String getName() {
		return m_Name;
	}

	@Override
	public void setNote(String note) {
		m_Note = note;
	}

	@Override
	public String getNote() {
		return m_Note;
	}

	@Override
	public void addUser(User user) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeUser(User user) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean include(User user) {
		for (String id : m_Users) {
			if (eq(id, user.getId())) {
				return true;
			}
		}
		return false;
	}

	private boolean eq(String id, String id2) {
		return UniteId.getOrdinal(id).equals(UniteId.getOrdinal(id2));
	}

	public void setUsers(List<String> ids) {
		m_Users = ids;
	}

	public void setUserProvider(UserProvider provider) {
		m_UserProvider = provider;
	}

	@Override
	public List<User> getUsers() {
		return TransList.valueOf(m_Users, (id) -> getUser(id));
	}

	private User getUser(String id) {
		return null == m_UserProvider ? null : m_UserProvider.getUser(id);
	}

}
