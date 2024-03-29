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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.weforward.devops.user.Organization;
import cn.weforward.devops.user.OrganizationProvider;
import cn.weforward.protocol.ops.secure.RightTable;
import cn.weforward.protocol.ops.secure.RightTableItem;

/**
 * 服务权限表信息视图
 * 
 * @author daibo
 *
 */
public class RightTableInfoView {
	protected String m_Name;
	protected RightTable m_Info;
	protected OrganizationProvider m_Provider;

	public static RightTableInfoView valueOf(String name, RightTable info, OrganizationProvider provider) {
		return new RightTableInfoView(name, info, provider);
	}

	public RightTableInfoView(String name, RightTable info, OrganizationProvider provider) {
		m_Name = name;
		m_Info = info;
		m_Provider = provider;
	}

	public String getName() {
		return m_Name;

	}

	public List<RightRuleItemView> getItems() {
		List<RightTableItem> items = null == m_Info ? null : m_Info.getItems();
		if (null == items) {
			return Collections.emptyList();
		}
		List<RightRuleItemView> list = new ArrayList<>();
		for (RightTableItem item : items) {
			list.add(new RightRuleItemView(item));
		}
		return list;

	}

	public class RightRuleItemView {

		protected RightTableItem item;

		public RightRuleItemView(RightTableItem item) {
			this.item = item;
		}

		/**
		 * F使用getAccessGroupName
		 * 
		 * @return
		 */
		@Deprecated()
		public String getCompanyName() {
			// 鹏基大神说要回显公司名字 编辑时候显示accessGroup
			return getAccessGroupName();
		}

		public String getName() {
			return item.getName();
		}

		public String getAccessId() {
			return item.getAccessId();
		}

		public String getAccessKind() {
			return item.getAccessKind();
		}

		public String getAccessGroup() {
			return item.getAccessGroup();
		}

		public String getAccessGroupName() {
			Organization g = m_Provider.get(item.getAccessGroup());
			if (null != g) {
				return g.getName();
			}
			return null;
		}

		public String getDescription() {
			return item.getDescription();
		}

		public boolean isAllow() {
			return item.isAllow();
		}

	}

}
