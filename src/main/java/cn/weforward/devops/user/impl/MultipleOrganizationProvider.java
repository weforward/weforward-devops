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
import java.util.Arrays;
import java.util.List;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.UnionResultPage;
import cn.weforward.devops.user.Organization;
import cn.weforward.devops.user.OrganizationProvider;
import cn.weforward.protocol.ops.User;

/**
 * 多个组织供应商合并
 * 
 * @author daibo
 *
 */
public class MultipleOrganizationProvider implements OrganizationProvider {

	protected List<OrganizationProvider> m_Providers;

	public MultipleOrganizationProvider(OrganizationProvider... providers) {
		this(Arrays.asList(providers));
	}

	public MultipleOrganizationProvider(List<OrganizationProvider> providers) {
		m_Providers = providers;
	}

	@Override
	public ResultPage<Organization> search(String keywords) {
		List<ResultPage<Organization>> pages = new ArrayList<>();
		for (OrganizationProvider p : m_Providers) {
			ResultPage<Organization> rp = p.search(keywords);
			pages.add(rp);
		}
		return UnionResultPage.union(pages, null);
	}

	@Override
	public Organization get(String id) {
		for (OrganizationProvider p : m_Providers) {
			Organization org = p.get(id);
			if (null != org) {
				return org;
			}
		}
		return null;
	}

	@Override
	public Organization get(User user) {
		for (OrganizationProvider p : m_Providers) {
			Organization org = p.get(user);
			if (null != org) {
				return org;
			}
		}
		return null;
	}

	@Override
	public Organization getByAccessId(String accessId) {
		for (OrganizationProvider p : m_Providers) {
			Organization org = p.get(accessId);
			if (null != org) {
				return org;
			}
		}
		return null;
	}

}
