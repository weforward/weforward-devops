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
package cn.weforward.devops.weforward.view;

import cn.weforward.devops.user.Organization;
import cn.weforward.devops.user.OrganizationProvider;
import cn.weforward.protocol.doc.annotation.DocObject;
import cn.weforward.protocol.ops.AccessExt;

@DocObject(description = "访问凭证")
public class AccessView {

	protected AccessExt m_Access;

	protected OrganizationProvider m_Provider;

	public AccessView(AccessExt access, OrganizationProvider provider) {
		m_Access = access;
		m_Provider = provider;
	}

	public static AccessView valueOf(AccessExt info, OrganizationProvider provider) {
		return new AccessView(info, provider);
	}

	public String getId() {
		return m_Access.getAccessId();
	}

	public String getKey() {
		return m_Access.getAccessKeyHex();
	}

	public String getKeyBase64() {
		return m_Access.getAccessKeyBase64();
	}

	public String getSummary() {
		return m_Access.getSummary();
	}

	public String getKind() {
		return m_Access.getKind();
	}

	public String getGroup() {
		return m_Access.getGroupId();
	}

	public boolean isValid() {
		return m_Access.isValid();
	}

	public String getGroupName() {
		if (null == m_Provider) {
			return null;
		}
		Organization g = m_Provider.get(getGroup());
		if (null != g) {
			return g.getName();
		}
		return null;
	}

}
