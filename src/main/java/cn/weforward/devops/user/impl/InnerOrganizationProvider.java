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

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.devops.user.Organization;
import cn.weforward.devops.user.OrganizationProvider;
import cn.weforward.protocol.ops.User;

/**
 * 组织提供者
 * 
 * @author daibo
 *
 */
public class InnerOrganizationProvider implements OrganizationProvider {

	protected Organization m_Org;

	public InnerOrganizationProvider(String id, String name) {
		m_Org = new Organization(id, name);
	}

	@Override
	public ResultPage<Organization> search(String keywords) {
		Organization org = m_Org;
		if (StringUtil.isEmpty(keywords) || org.getName().contains(keywords)) {
			return ResultPageHelper.singleton(org);
		}
		return ResultPageHelper.empty();
	}

	@Override
	public Organization get(String id) {
		if (StringUtil.eq(id, m_Org.getId())) {
			return m_Org;
		}
		return null;
	}

	@Override
	public Organization get(User user) {
		if (user instanceof SimpleOrganizationUser) {
			if (((SimpleOrganizationUser) user).isInner()) {
				return m_Org;
			}
		}
		return null;
	}

	@Override
	public Organization getByAccessId(String accessId) {
		return null;
	}
}
