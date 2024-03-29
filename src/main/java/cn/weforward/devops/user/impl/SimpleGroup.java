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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import cn.weforward.common.util.FreezedList;
import cn.weforward.common.util.StringUtil;
import cn.weforward.data.annotation.Index;
import cn.weforward.data.annotation.ResourceExt;
import cn.weforward.data.persister.Persister;
import cn.weforward.data.persister.Reloadable;
import cn.weforward.data.persister.support.AbstractPersistent;
import cn.weforward.devops.user.Group;
import cn.weforward.devops.user.Organization;
import cn.weforward.devops.user.di.GroupDi;
import cn.weforward.protocol.ops.User;

/**
 * 简单的组实现
 * 
 * @author daibo
 *
 */
public class SimpleGroup extends AbstractPersistent<GroupDi> implements Group, Reloadable<SimpleGroup> {
	/** 组名 */
	@Resource
	protected String m_Name;
	/** 备注 */
	@Resource
	protected String m_Note;
	/** 用户 */
	@ResourceExt(component = String.class)
	protected List<String> m_Users;
	/** 所在组织 */
	@Index
	@Resource
	protected String m_Organization;

	protected SimpleGroup(GroupDi di) {
		super(di);
	}

	protected SimpleGroup(GroupDi di, Organization org, String name) {
		super(di);
		genPersistenceId();
		m_Name = name;
		m_Organization = org.getId();
		markPersistenceUpdate();
	}

	@Override
	public String getId() {
		return getPersistenceId().getId();
	}

	public void setName(String name) {
		String old = m_Name;
		if (StringUtil.eq(old, name)) {
			return;
		}
		m_Name = name;
		markPersistenceUpdate();
	}

	@Override
	public String getName() {
		return m_Name;
	}

	@Override
	public void setNote(String note) {
		if (StringUtil.eq(m_Note, note)) {
			return;
		}
		m_Note = note;
		markPersistenceUpdate();
	}

	@Override
	public String getNote() {
		return m_Note;
	}

	@Override
	public synchronized void addUser(User user) {
		if (null == user || include(user)) {
			return;
		}
		String uid = user.getId();
		if (null == m_Users) {
			m_Users = Collections.singletonList(uid);
		} else {
			List<String> olds = m_Users;
			if (olds.contains(uid)) {
				return;
			}
			m_Users = FreezedList.addToFreezed(olds, olds.size(), uid);
		}
		// getBusinessDi().writeLog(getId(), "set", "添加用户", user.toString());
		markPersistenceUpdate();

	}

	@Override
	public synchronized void removeUser(User user) {
		if (null == user || null == m_Users || 0 == m_Users.size()) {
			return;
		}
		String uid = user.getId();
		List<String> olds = m_Users;
		List<String> news = new ArrayList<>();
		for (String v : olds) {
			if (StringUtil.eq(v, uid)) {
				continue;
			} else {
				news.add(v);
			}
		}
		m_Users = FreezedList.freezed(news);
		// getBusinessDi().writeLog(getId(), "set", "移除用户", user.toString());
		markPersistenceUpdate();
	}

	@Override
	public boolean include(User user) {
		if (null == m_Users) {
			return false;
		}
		String uid = user.getId();
		return m_Users.contains(uid);
	}

	@Override
	public List<User> getUsers() {
		if (null == m_Users) {
			return Collections.emptyList();
		}
		List<String> ids = m_Users;
		List<User> users = new ArrayList<>();
		for (String id : ids) {
			users.add(getBusinessDi().getUser(id));
		}
		return users;
	}

	@Override
	public boolean onReloadAccepted(Persister<SimpleGroup> persister, SimpleGroup other) {
		m_Users = other.m_Users;
		m_Name = other.m_Name;
		m_Note = other.m_Note;
		m_DriveIt = other.m_DriveIt;
		return true;
	}

	/**
	 * 是否我的组织
	 * 
	 * @param org 组织
	 * @return
	 */
	public boolean isMyOrganization(Organization org) {
		return StringUtil.eq(m_Organization, org.getId());
	}

}
