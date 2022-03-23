package cn.weforward.devops.user.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.weforward.devops.user.RoleProvider;
import cn.weforward.protocol.ops.Right;
import cn.weforward.protocol.ops.Role;
import cn.weforward.protocol.ops.User;

/**
 * 多个角色供应商实现
 * 
 * @author daibo
 *
 */
public class MultipleRoleProvider implements RoleProvider {

	protected List<RoleProvider> m_Providers;

	public MultipleRoleProvider(RoleProvider... providers) {
		this(Arrays.asList(providers));
	}

	public MultipleRoleProvider(List<RoleProvider> providers) {
		m_Providers = providers;
	}

	@Override
	public List<Role> getRoles(User user) {
		List<Role> list = new ArrayList<>();
		for (RoleProvider p : m_Providers) {
			list.addAll(p.getRoles(user));
		}
		return list;
	}

	@Override
	public List<Right> getRights(User user) {
		List<Right> list = new ArrayList<>();
		for (RoleProvider p : m_Providers) {
			list.addAll(p.getRights(user));
		}
		return list;
	}

}
