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
