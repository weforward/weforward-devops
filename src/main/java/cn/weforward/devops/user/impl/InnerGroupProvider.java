package cn.weforward.devops.user.impl;

import java.util.ArrayList;
import java.util.List;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.data.persister.Persistent;
import cn.weforward.data.persister.Persister;
import cn.weforward.data.persister.PersisterFactory;
import cn.weforward.data.search.Searcher;
import cn.weforward.devops.project.Group;
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
	/** 搜索器 */
	protected Searcher m_Searcher;
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

	@Override
	public ResultPage<? extends Group> searchGroups(Organization org, String keywords) {
//		ResultPage<? extends Group> rp = m_PsSimpleGroup
//				.search(ConditionUtil.eq(ConditionUtil.field("m_Organization"), org.getId()));
		ResultPage<? extends Group> rp = m_PsSimpleGroup.search(null);
		if (StringUtil.isEmpty(keywords)) {
			return rp;
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
