package cn.weforward.devops.weforward.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.weforward.common.util.StringUtil;
import cn.weforward.metrics.TracerSpanTree;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

@DocObject(description = "追踪树视图")
public class TracerTreeView {

	protected TracerSpanTree m_Tree;

	protected List<NameAndNum> m_Children;

	protected TracerTreeView(TracerSpanTree tree) {
		m_Tree = tree;
		Map<String, Integer> map = new HashMap<>();
		cal(tree.getRoot(), map);
		List<NameAndNum> list = new ArrayList<>();
		for (Entry<String, Integer> e : map.entrySet()) {
			list.add(new NameAndNum(e.getKey(), e.getValue()));
		}
		m_Children = list;

	}

	private void cal(TracerSpanTree.SpanNode node, Map<String, Integer> map) {
		String n = node.getServiceName();
		if (!StringUtil.isEmpty(n)) {
			Integer v = map.get(n);
			if (null == v) {
				v = 1;
			} else {
				v++;
			}
			map.put(n, v);
		}
		for (TracerSpanTree.SpanNode child : node.getChildren()) {
			cal(child, map);
		}

	}

	public static TracerTreeView valueOf(TracerSpanTree tree) {
		return null == tree ? null : new TracerTreeView(tree);
	}

	@DocAttribute(description = "唯一id")
	public String getId() {
		return m_Tree.getId();
	}

	@DocAttribute(description = "根服务名")
	public String getServiceName() {
		return m_Tree.getRoot().getServiceName();
	}

	@DocAttribute(description = "根服务编号")
	public String getServiceNo() {
		return m_Tree.getRoot().getServiceNo();
	}

	@DocAttribute(description = "根方法")
	public String getMethod() {
		return m_Tree.getRoot().getMethod();
	}

	@DocAttribute(description = "开始时间，为null显示--")
	public Date getStartTime() {
		long v = m_Tree.getRoot().getStartTime();
		if (v == Long.MAX_VALUE) {
			return null;
		} else {
			return new Date(v);
		}
	}

	@DocAttribute(description = "持续时间")
	public String getDuration() {
		if (null == getStartTime()) {
			return "--";
		}
		long v = m_Tree.getRoot().getDuration();
		return formatMs(v);
	}

	@DocAttribute(description = "包含服务和出现次数")
	public List<NameAndNum> getChildren() {
		return m_Children;
	}

	public static String formatMs(long v) {
		String symbols;
		if (v < 0) {
			v = -v;
			symbols = "-";
		} else {
			symbols = "";
		}
		long s = v / 1000;
		long ms = v % 1000;
		if (ms < 10) {
			return symbols + s + ".00" + ms + "秒";
		} else if (ms < 100) {
			return symbols + s + ".0" + ms + "秒";
		} else {
			return symbols + s + "." + ms + "秒";
		}
	}

	@DocObject(description = "服务和出现次数")
	public static class NameAndNum {

		protected String m_Name;
		protected int m_Num;

		public NameAndNum(String name, int num) {
			m_Name = name;
			m_Num = num;
		}

		@DocAttribute(description = "服务名")
		public String getName() {
			return m_Name;
		}

		@DocAttribute(description = "出现次数")
		public int getNum() {
			return m_Num;
		}
	}
}
