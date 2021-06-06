package cn.weforward.devops.user.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.weforward.devops.user.RoleProvider;
import cn.weforward.protocol.ops.Right;
import cn.weforward.protocol.ops.Role;
import cn.weforward.protocol.ops.User;

/**
 * 内部角色实现
 * 
 * @author daibo
 *
 */
public class InnerRoleProvider implements RoleProvider {
	/** 所有权限 */
	final static List<Right> ALLRIGHTS = Arrays.asList(new SimpleRight(Right.RULE_ALLOW, "/devops/**"),
			new SimpleRight(Right.RULE_ALLOW, "/user/**"));

	@Override
	public List<Right> getRights(User user) {
		if (user instanceof SimpleOrganizationUser) {
			if (((SimpleOrganizationUser) user).isInner()) {
				return ALLRIGHTS;
			}
		}
		return Collections.emptyList();
	}

	@Override
	public List<Role> getRoles(User user) {
		return Collections.emptyList();
	}

}
