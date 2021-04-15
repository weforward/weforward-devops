package cn.weforward.devops;

import org.junit.Test;

import cn.weforward.metrics.TracerSpanTree;
import cn.weforward.metrics.impl.MongodbMetricsTracer;

public class MongodbTracerTest {
	@Test
	public void test() {
		MongodbMetricsTracer trade = new MongodbMetricsTracer(System.getProperty("url"), "metrics");
		try {
//			devops(/devops/auth/login)|1764bc862b001x0016|2020-12-10 16:33:18|-5726ms
//			sichengrenyuan(null)|1764bbdb8bc05x00cb|2020-12-10 16:21:36|9223370429266279761ms
			TracerSpanTree tree = trade.get("1764bc862b001x0016");
			System.out.println(tree);
//			Date begin = new Date(System.currentTimeMillis() - TimeUtil.HOUR_MILLS);
//			Date end = new Date();
//			ResultPage<TracerSpanTree> rp = trade.queryTree(begin, end, "zuoche_user", null, null);
//			for (int i = 1; rp.gotoPage(i); i++) {
//				for (TracerSpanTree tree : rp) {
//					TracerSpanTree.SpanNode root = tree.getRoot();
//					if (root.getChilds().isEmpty()) {
//						continue;
//					}
//					System.out.println(root.getServiceName() + "(" + root.getMethod() + ")" + "|" + tree.getId() + "|"
//							+ TimeUtil.formatDateTime(new Date(root.getStartTime())) + "|" + root.getDuration() + "ms");
//					for (TracerSpanTree.SpanNode child : root.getChilds()) {
//						System.out.print(child.getServiceName() + "(" + child.getChilds().size() + ")");
//						System.out.print("   ");
//					}
//					System.out.println("--------");
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
