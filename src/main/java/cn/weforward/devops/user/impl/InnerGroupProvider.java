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
import java.util.List;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.data.persister.Persistent;
import cn.weforward.data.persister.Persister;
import cn.weforward.data.persister.PersisterFactory;
import cn.weforward.data.persister.ext.ConditionUtil;
import cn.weforward.devops.user.Group;
import cn.weforward.devops.user.GroupProvider;
import cn.weforward.devops.user.Organization;
import cn.weforward.devops.user.UserProvider;
import cn.weforward.devops.user.di.GroupDi;
import cn.weforward.protocol.ops.User;

/**
 * 内置组实现
 * 
 * @author daibo
 *
 */
public class InnerGroupProvider implements GroupProvider, GroupDi {
	/** 持久工厂 */
	protected PersisterFactory m_PsFactroy;
	/** 组持久类 */
	protected final Persister<SimpleGroup> m_PsSimpleGroup;

	protected UserProvider m_UserProvider;

	public InnerGroupProvider(PersisterFactory psFactroy, UserProvider userProvider) {
		m_PsFactroy = psFactroy;
		m_PsSimpleGroup = m_PsFactroy.createPersister(SimpleGroup.class, this);
		m_UserProvider = userProvider;
	}

	@Override
	public Group addGroup(Organization org, String name) {
		return new SimpleGroup(this, org, name);
	}

	@Override
	public Group getGroup(Organization org, String id) {
		SimpleGroup group = m_PsSimpleGroup.get(id);
		if (null == group || !group.isMyOrganization(org)) {
			return null;
		}
		return group;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultPage<Group> search(Organization org, String keywords) {
		ResultPage<? extends Group> rp = m_PsSimpleGroup
				.search(ConditionUtil.eq(ConditionUtil.field("m_Organization"), org.getId()));
		if (StringUtil.isEmpty(keywords)) {
			return (ResultPage<Group>) rp;
		}
		List<Group> list = new ArrayList<>();
		for (Group g : ResultPageHelper.toForeach(rp)) {
			if (null == g) {
				continue;
			}
			if (!StringUtil.isEmpty(keywords) && !g.getName().contains(keywords)) {
				continue;
			}
			list.add(g);
		}
		return ResultPageHelper.toResultPage(list);
	}

	@Override
	public <E extends Persistent> Persister<E> getPersister(Class<E> clazz) {
		return m_PsFactroy.getPersister(clazz);
	}

	@Override
	public User getUser(String id) {
		return m_UserProvider.getUser(id);
	}

}
