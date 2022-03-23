package cn.weforward.metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.weforward.common.util.ListUtil;
import cn.weforward.common.util.StringUtil;
import cn.weforward.trace.WeforwardTrace;

/**
 * 追踪块树结构
 * 
 * @author daibo
 *
 */
public class TracerSpanTree {
	/** 树id */
	protected String m_Id;
	/** 根节点 */
	protected SpanNode m_Root;

	protected TracerSpanTree(String id, SpanNode root) {
		m_Id = id;
		m_Root = root;
	}

	/** 树id */
	public String getId() {
		return m_Id;
	}

	/** 根节点 */
	public SpanNode getRoot() {
		return m_Root;
	}

	public String toString() {
		return getId() + "," + getRoot().getDuration() + "ms";
	}

	/**
	 * 构造树
	 * 
	 * @param spans
	 * @return
	 */
	public static TracerSpanTree valueOf(TracerSpans spans) {
		if (null == spans || ListUtil.isEmpty(spans.getSpans())) {
			return null;
		}
		List<SpanNode> nodes = new ArrayList<>();
		SpanNode root = null;
		for (TracerSpan span : spans) {
			String pid = span.getParentId();
			SpanNode pnode = null;
			if (!StringUtil.isEmpty(pid)) {
				pnode = openNode(pid, nodes);
			}
			SpanNode my = openNode(span.getSpanId(), nodes);
			if (!StringUtil.isEmpty(span.getServiceName())) {
				my.m_ServiceName = span.getServiceName();
			}
			if (!StringUtil.isEmpty(span.getServiceNo())) {
				my.m_ServiceNo = span.getServiceNo();
			}
			if (!StringUtil.isEmpty(span.getMethod())) {
				my.m_Method = span.getMethod();
			}
			if (StringUtil.eq(WeforwardMetrics.GATEWAY_TRACE_KEY, span.getName())
					|| StringUtil.eq(WeforwardTrace.KIND_GATEWAY, span.getName())) {
				my.m_GwStartTime = span.getTimestamp();
				my.m_GwEndTime = span.getTimestamp() + span.getDuration();
			} else if (StringUtil.eq(WeforwardMetrics.TRACE_KEY, span.getName())
					|| StringUtil.eq(WeforwardTrace.KIND_SERVICE, span.getName())) {
				my.m_MsStartTime = span.getTimestamp();
				my.m_MsEndTime = span.getTimestamp() + span.getDuration();
			}
			if (StringUtil.eq(WeforwardMetrics.TRACE_START_TIME, span.getName())) {
				if (StringUtil.isEmpty(span.getMethod())) {
					my.m_GwStartTime = span.getTimestamp();
				} else {
					my.m_MsStartTime = span.getTimestamp();
				}
			} else if (StringUtil.eq(WeforwardMetrics.TRACE_END_TIME, span.getName())) {
				if (StringUtil.isEmpty(span.getMethod())) {
					my.m_GwEndTime = span.getTimestamp();
				} else {
					my.m_MsEndTime = span.getTimestamp();
				}
			}
			if (null != pnode) {
				pnode.addChild(my);
			}
			if (null == root) {
				root = my;
			} else if (root.getStartTime() > my.getStartTime()) {
				root = my;
			}
		}
		return new TracerSpanTree(spans.getSpans().get(0).getId(), root);
	}

	private static SpanNode openNode(String id, List<SpanNode> nodes) {
		SpanNode match = null;
		for (SpanNode node : nodes) {
			if (StringUtil.eq(node.getId(), id)) {
				match = node;
				break;
			}
		}
		if (null == match) {
			match = new SpanNode(id);
			nodes.add(match);
		}
		return match;
	}

	public static class SpanNode {

		protected String m_Id;

		protected String m_ServiceName;

		protected String m_ServiceNo;

		protected long m_GwStartTime;

		protected long m_MsStartTime;

		protected long m_GwEndTime;

		protected long m_MsEndTime;

		protected String m_Method;

		protected SpanNode m_Parent;

		protected List<SpanNode> m_Children;

		public SpanNode(String id) {
			m_Id = id;
		}

		/**
		 * 节点id
		 * 
		 * @return
		 */
		public String getId() {
			return m_Id;
		}

		/**
		 * 服务名
		 * 
		 * @return
		 */
		public String getServiceName() {
			return m_ServiceName;
		}

		public String getServiceNo() {
			return m_ServiceNo;
		}

		/**
		 * 方法名
		 * 
		 * @return
		 */
		public String getMethod() {
			return m_Method;
		}

		/**
		 * 添加子节点
		 * 
		 * @param newNode
		 */
		public void addChild(SpanNode newNode) {
			if (null == m_Children) {
				m_Children = new ArrayList<>();
			}
			for (SpanNode node : m_Children) {
				if (StringUtil.eq(node.getId(), newNode.getId())) {
					return;
				}
			}
			newNode.m_Parent = this;
			m_Children.add(newNode);

		}

		/**
		 * 网关开始时间
		 * 
		 * @return
		 */
		public long getGwStartTime() {
			if (m_GwStartTime > 0) {
				return m_GwStartTime;
			} else {
				return 0;
			}
		}

		/**
		 * 网关开始时间
		 * 
		 * @return
		 */
		public long getGwEndTime() {
			if (m_GwEndTime > 0) {
				return m_GwEndTime;
			} else {
				return 0;
			}
		}

		/**
		 * 服务开始时间
		 * 
		 * @return
		 */
		public long getMsStartTime() {
			if (m_MsStartTime > 0) {
				return m_MsStartTime;
			} else {
				return 0;
			}
		}

		/**
		 * 服务开始时间
		 * 
		 * @return
		 */
		public long getMsEndTime() {
			if (m_MsEndTime > 0) {
				return m_MsEndTime;
			} else {
				return 0;
			}
		}

		/**
		 * 开始时间
		 * 
		 * @return
		 */
		public long getStartTime() {
			if (m_GwStartTime > 0 && m_GwEndTime > 0) {
				return m_GwStartTime;
			} else if (m_MsStartTime > 0 && m_MsEndTime > 0) {
				return m_MsStartTime;
			} else {
				return Long.MAX_VALUE;
			}
		}

		/**
		 * 结束时间
		 * 
		 * @return
		 */
		public long getEndTime() {
			if (m_GwStartTime > 0 && m_GwEndTime > 0) {
				return m_GwEndTime;
			} else if (m_MsStartTime > 0 && m_MsEndTime > 0) {
				return m_MsEndTime;
			} else {
				return Long.MAX_VALUE;
			}
		}

		/**
		 * 耗时
		 * 
		 * @return
		 */
		public long getDuration() {
			return getEndTime() - getStartTime();
		}

		/**
		 * 父结点
		 * 
		 * @return
		 */
		public SpanNode getParent() {
			return m_Parent;
		}

		/**
		 * 子结点
		 * 
		 * @return
		 */
		public List<SpanNode> getChildren() {
			return null == m_Children ? Collections.emptyList() : m_Children;
		}

	}
}
