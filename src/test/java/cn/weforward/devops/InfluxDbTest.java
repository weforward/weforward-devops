package cn.weforward.devops;

import org.junit.Test;

import cn.weforward.metrics.impl.InfluxdbMetricsCollector;

public class InfluxDbTest {
	@Test
	public void getlately() {
		InfluxdbMetricsCollector c = new InfluxdbMetricsCollector(System.getProperty("url"), System.getProperty("u"),
				System.getProperty("p"), "metrics");
		c.clear(1);
	}
}
