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

import cn.weforward.protocol.ops.traffic.TrafficTable;
import cn.weforward.protocol.ops.traffic.TrafficTableItem;

/**
 * 服务流量表信息视图
 * 
 * @author daibo
 *
 */
public class TrafficTableInfoView {

	protected String m_Name;
	protected TrafficTable m_Info;

	public static TrafficTableInfoView valueOf(String name, TrafficTable info) {
		return new TrafficTableInfoView(name, info);
	}

	public TrafficTableInfoView(String name, TrafficTable info) {
		m_Name = name;
		m_Info = info;
	}

	public String getName() {
		return m_Name;
	}

	public List<TrafficRuleItemView> getItems() {
		List<TrafficTableItem> items = null == m_Info ? null : m_Info.getItems();
		if (null == items) {
			return Collections.emptyList();
		}
		List<TrafficRuleItemView> list = new ArrayList<>();
		for (TrafficTableItem item : items) {
			list.add(new TrafficRuleItemView(item));
		}
		return list;
	}

	public class TrafficRuleItemView {

		protected TrafficTableItem item;

		public TrafficRuleItemView(TrafficTableItem item) {
			this.item = item;
		}

		/** @see #m_Name */
		public String getName() {
			return item.getName();
		}

		/** @see #m_Weight */
		public int getWeight() {
			return item.getWeight();
		}

		/** @see #m_MaxFails */
		public int getMaxFails() {
			return item.getMaxFails();
		}

		/** @see #m_FailTimeout */
		public int getFailTimeout() {
			return item.getFailTimeout();
		}

		/** @see #m_MaxConcurrent */
		public int getMaxConcurrent() {
			return item.getMaxConcurrent();
		}

		/** @see #m_ConnectTimeout */
		public int getConnectTimeout() {
			return item.getConnectTimeout();
		}

		/** @see #m_ReadTimeout */
		public int getReadTimeout() {
			return item.getReadTimeout();
		}

		public String getServiceNo() {
			return item.getServiceNo();
		}

		public String getServiceVersion() {
			return item.getServiceVersion();
		}

	}

}
