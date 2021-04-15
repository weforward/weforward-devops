package cn.weforward.devops.weforward.view;

import static cn.weforward.devops.weforward.view.TracerTreeView.formatMs;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.TransList;
import cn.weforward.framework.doc.DocPageParams;
import cn.weforward.metrics.TracerSpanTree;
import cn.weforward.metrics.TracerSpanTree.SpanNode;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

@DocObject(description = "追踪树详细视图")
public class TracerTreeDetailView extends DocPageParams {

	protected TracerSpanTree m_Tree;

	protected int m_Services;

	protected int m_Depth;

	protected TracerTreeDetailView(TracerSpanTree tree) {
		m_Tree = tree;
		Set<String> services = new HashSet<>();
		m_Depth = calDepth(services, 0, tree.getRoot());
		m_Services = services.size();

	}

	private int calDepth(Set<String> services, int depth, SpanNode root) {
		depth++;
		String n = root.getServiceName();
		if (!StringUtil.isEmpty(n)) {
			services.add(n);
		}
		for (SpanNode node : root.getChildren()) {
			int temp = calDepth(services, depth, node);
			depth = Math.max(temp, depth);
		}
		return depth;

	}

	public static TracerTreeDetailView valueOf(TracerSpanTree tree) {
		return null == tree ? null : new TracerTreeDetailView(tree);
	}

	@DocAttribute(description = "唯一id")
	public String getId() {
		return m_Tree.getId();
	}

	@DocAttribute(description = "根服务名")
	public String getServiceName() {
		return m_Tree.getRoot().getServiceName();
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

	@DocAttribute(description = "涉及服务数")
	public int getServices() {
		return m_Services;
	}

	@DocAttribute(description = "调用栈深度")
	public int getDepth() {
		return m_Depth;
	}

	@DocAttribute(description = "根节点")
	public Node getRoot() {
		return Node.valueOf(m_Tree.getRoot());
	}

	@DocObject(description = "节点")
	public static class Node {

		protected SpanNode m_Node;

		public Node(SpanNode node) {
			m_Node = node;
		}

		public static Node valueOf(SpanNode root) {
			return null == root ? null : new Node(root);
		}

		@DocAttribute(description = "节点id")
		public String getId() {
			return m_Node.getId();
		}

		@DocAttribute(description = "父节点id")
		public String getParentId() {
			SpanNode n = m_Node.getParent();
			return null == n ? null : n.getId();
		}

		@DocAttribute(description = "服务名")
		public String getServiceName() {
			return m_Node.getServiceName();
		}

		@DocAttribute(description = "服务编号")
		public String getServiceNo() {
			return m_Node.getServiceName();
		}

		@DocAttribute(description = "方法")
		public String getMethod() {
			return m_Node.getMethod();
		}

		@DocAttribute(description = "网关开始时间")
		public Date getGwStartTime() {
			long v = m_Node.getGwStartTime();
			if (v <= 0) {
				return null;
			} else {
				return new Date(v);
			}
		}

		@DocAttribute(description = "网关开始基准时间")
		public String getGwStartRelativeTime() {
			return "--";
		}

		@DocAttribute(description = "服务开始时间")
		public Date getMsStartTime() {
			long v = m_Node.getMsStartTime();
			if (v <= 0) {
				return null;
			} else {
				return new Date(v);
			}
		}

		@DocAttribute(description = "服务开始基准时间")
		public String getMsStartRelativeTime() {
			long before = m_Node.getGwStartTime();
			if (before <= 0) {
				return "--";
			}
			long now = m_Node.getMsStartTime();
			if (now <= 0) {
				return "--";
			}
			long v = now - before;
			return formatMs(v);
		}

		@DocAttribute(description = "服务结束时间")
		public Date getMsEndTime() {
			long v = m_Node.getMsEndTime();
			if (v <= 0) {
				return null;
			} else {
				return new Date(v);
			}
		}

		@DocAttribute(description = "服务结束基准时间")
		public String getMsEndRelativeTime() {
			long before = m_Node.getMsStartTime();
			if (before <= 0) {
				return "--";
			}
			long now = m_Node.getMsEndTime();
			if (now <= 0) {
				return "--";
			}
			long v = now - before;
			return formatMs(v);
		}

		@DocAttribute(description = "网关结束时间")
		public Date getGwEndTime() {
			long v = m_Node.getGwEndTime();
			if (v <= 0) {
				return null;
			} else {
				return new Date(v);
			}
		}

		@DocAttribute(description = "网关结束基准时间")
		public String getGwEndRelativeTime() {
			long before = m_Node.getMsEndTime();
			if (before <= 0) {
				before = m_Node.getGwStartTime();
			}
			long now = m_Node.getGwEndTime();
			if (now <= 0) {
				return "--";
			}
			long v = now - before;
			return formatMs(v);
		}

		private Date getStartTime() {
			long v = m_Node.getStartTime();
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
			long v = m_Node.getDuration();
			return formatMs(v);
		}

		@DocAttribute(description = "持续时间数值")
		public long getDurationNum() {
			return m_Node.getDuration();
		}

		@DocAttribute(description = "标签")
		public List<Tag> getTags() {
			return Collections.emptyList();
		}

		@DocAttribute(description = "子对象")
		public List<Node> getChildren() {
			return TransList.valueOf(m_Node.getChildren(), (e) -> Node.valueOf(e));
		}
	}

	@DocObject(description = "标签")
	public static class Tag {

		protected String m_Name;

		protected String m_Value;

		public Tag(String name, String value) {
			m_Name = name;
			m_Value = value;
		}

		@DocAttribute(description = "名称")
		public String getName() {
			return m_Name;
		}

		@DocAttribute(description = "值")
		public String getValue() {
			return m_Value;
		}
	}
}
