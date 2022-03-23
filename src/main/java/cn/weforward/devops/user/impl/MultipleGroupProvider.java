package cn.weforward.devops.user.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.UnionResultPage;
import cn.weforward.devops.user.Group;
import cn.weforward.devops.user.GroupProvider;
import cn.weforward.devops.user.Organization;

public class MultipleGroupProvider implements GroupProvider {

	protected List<GroupProvider> m_Providers;

	public MultipleGroupProvider(GroupProvider... providers) {
		this(Arrays.asList(providers));
	}

	public MultipleGroupProvider(List<GroupProvider> providers) {
		m_Providers = providers;
	}

	@Override
	public Group addGroup(Organization org, String name) {
		for (GroupProvider p : m_Providers) {
			try {
				return p.addGroup(org, name);
			} catch (UnsupportedOperationException e) {

			}
		}
		return null;
	}

	@Override
	public Group getGroup(Organization org, String id) {
		for (GroupProvider p : m_Providers) {
			Group g = p.getGroup(org, id);
			if (null != g) {
				return g;
			}
		}
		return null;
	}

	@Override
	public ResultPage<Group> search(Organization org, String keywords) {
		List<ResultPage<Group>> pages = new ArrayList<>();
		for (GroupProvider p : m_Providers) {
			ResultPage<Group> rp = p.search(org, keywords);
			pages.add(rp);
		}
		return UnionResultPage.union(pages, null);
	}

}
