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
package cn.weforward;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.mongodb.client.MongoDatabase;

import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.ThreadPool;
import cn.weforward.data.mongodb.counter.MongodbCounterFactory;
import cn.weforward.data.mongodb.util.MongodbUtil;
import cn.weforward.data.util.Flusher;
import cn.weforward.devops.user.AccessKeeper;
import cn.weforward.metrics.MetricsCollector;
import cn.weforward.metrics.MetricsTracer;
import cn.weforward.metrics.impl.InfluxdbMetricsCollector;
import cn.weforward.metrics.impl.MetricsServiceImpl;
import cn.weforward.metrics.impl.MongodbMetricsTracer;
import cn.weforward.protocol.aio.http.RestfulServer;
import cn.weforward.protocol.aio.netty.NettyHttpServer;
import cn.weforward.util.HttpAccessAuth;

/**
 * 指标配置
 * 
 * @author daibo
 *
 */
public class MetricsConfig {
	/** 端口 */
	@Value("${metrics.port}")
	protected int m_Port;
	/** 收集器类型 */
	@Value("${metrics.collector}")
	protected String m_Collector;
	/** 追踪器类型 */
	@Value("${metrics.tracer}")
	protected String m_Tracer;

	@Value("${metrics.prometus.url:}")
	protected String m_PrometusUrl;

	@Value("${metrics.mongodb.url:}")
	protected String m_MongodbUrl;

	@Value("${metrics.mongodb.dbname:metrics}")
	protected String m_MongodbDbName;

	@Value("${metrics.influxdb.url:}")
	protected String InfluxdbUrl;

	@Value("${metrics.influxdb.username:}")
	protected String InfluxdbUsername;

	@Value("${metrics.influxdb.password:}")
	protected String InfluxdbPassword;

	@Value("${metrics.influxdb.dbname:metrics}")
	protected String m_InfluxdbDbName;

	@Value("${metrics.collector.maxHistory}")
	protected int m_CollectorMaxHistory;

	@Value("${metrics.tracer.maxHistory}")
	protected int m_TracerMaxHistory;
	/** 凭证管理者 */
	@Resource
	protected AccessKeeper m_AccessKeeper;
	/** 服务器id */
	@Value("${weforward.serverid}")
	protected String m_ServerId;
	@Value("${metrics.apiInvokeAnalyze:false}")
	protected boolean m_ApiInvokeAnalyze;
	@Resource
	protected Flusher m_Flusher;
	protected MongoDatabase m_MongoDb;

	@Bean
	List<MetricsCollector> collectors() {
		List<MetricsCollector> list = new ArrayList<>();
		if (!StringUtil.isEmpty(m_Collector)) {
			String[] arr = m_Collector.split(";");
			for (String v : arr) {
				if ("influxdb".equalsIgnoreCase(v)) {
					list.add(new InfluxdbMetricsCollector(InfluxdbUrl, InfluxdbUsername, InfluxdbPassword,
							m_InfluxdbDbName));
				}
			}
		}
		return list;
	}

	MongoDatabase getMongoDb() {
		MongoDatabase db = m_MongoDb;
		if (null == db) {
			db = MongodbUtil.create(m_MongodbUrl).getDatabase(m_MongodbDbName);
			m_MongoDb = db;
		}
		return db;
	}

	@Bean
	List<MetricsTracer> tracers() {
		List<MetricsTracer> list = new ArrayList<>();
		if (!StringUtil.isEmpty(m_Tracer)) {
			String[] arr = m_Tracer.split(";");
			for (String v : arr) {
				if ("Mongodb".equalsIgnoreCase(v)) {
					list.add(new MongodbMetricsTracer(getMongoDb()));
				}
			}
		}
		return list;

	}

	@Bean
	MetricsServiceImpl metricsService(List<MetricsCollector> collectors, List<MetricsTracer> tracers) {
		MetricsServiceImpl s = new MetricsServiceImpl(m_CollectorMaxHistory, m_TracerMaxHistory,
				new HttpAccessAuth(m_AccessKeeper), new MongodbCounterFactory(m_ServerId, getMongoDb(), m_Flusher));
		s.setCollectors(collectors);
		s.setTracers(tracers);
		s.setApiInvokeAnalyze(m_ApiInvokeAnalyze);
		return s;
	}

	@Bean
	NettyHttpServer metricsApiServer(MetricsServiceImpl metricsService, ThreadPool threadPool) throws Exception {
		if (m_Port <= 0) {
			return null;
		}
		NettyHttpServer s = new NettyHttpServer(m_Port);
		s.setName("metrics");
		s.setGzipEnabled(true);
		s.setIdle(10);
		RestfulServer server = new RestfulServer(metricsService);
		ThreadPool mypool = new ThreadPool(threadPool);
		mypool.setName("metrics");
		server.setExecutor(mypool);
		s.setHandlerFactory(server);
		s.start();
		return s;
	}
}
