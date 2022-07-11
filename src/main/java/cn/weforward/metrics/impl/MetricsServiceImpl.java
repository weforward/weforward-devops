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
package cn.weforward.metrics.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.weforward.common.Destroyable;
import cn.weforward.common.ResultPage;
import cn.weforward.common.restful.RestfulRequest;
import cn.weforward.common.restful.RestfulResponse;
import cn.weforward.common.restful.RestfulService;
import cn.weforward.common.sys.Shutdown;
import cn.weforward.common.util.FreezedList;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.TimeUtil;
import cn.weforward.data.counter.Counter;
import cn.weforward.data.counter.CounterFactory;
import cn.weforward.devops.user.Organization;
import cn.weforward.devops.user.OrganizationProvider;
import cn.weforward.metrics.ApiInvokeInfo;
import cn.weforward.metrics.ManyMetrics;
import cn.weforward.metrics.MetricsCollector;
import cn.weforward.metrics.MetricsService;
import cn.weforward.metrics.MetricsTracer;
import cn.weforward.metrics.OneMetrics;
import cn.weforward.metrics.TracerSpanTree;
import cn.weforward.metrics.TracerSpanTree.SpanNode;
import cn.weforward.protocol.ops.AccessExt;
import cn.weforward.trace.CommonTrace;
import cn.weforward.trace.Trace;
import cn.weforward.util.HttpAccessAuth;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Meter.Id;
import io.micrometer.core.instrument.Statistic;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;

/**
 * 指标实现
 * 
 * @author daibo
 *
 */
public class MetricsServiceImpl implements RestfulService, MetricsService, Destroyable {
	/** 日志记录 */
	private final static Logger _Logger = LoggerFactory.getLogger(MetricsService.class);

	final static DateFormat DTF = TimeUtil.getDateFormatInstance("yyyyMMddHHmm");

	private List<MetricsCollector> m_Collectors = Collections.emptyList();

	private List<MetricsTracer> m_Tracers = Collections.emptyList();

	protected int m_CollectorMaxHistory;
	protected int m_TracerMaxHistory;
	/** http验证 */
	protected HttpAccessAuth m_Auth;
	protected OrganizationProvider m_Provider;
	protected Counter m_ApiInvokeCounter;
	protected Timer m_Timer;

	public MetricsServiceImpl(int collectorMaxHistory, int tracerMaxHistory, HttpAccessAuth auth,
			CounterFactory factory) {
		m_Auth = auth;
		m_CollectorMaxHistory = collectorMaxHistory;
		m_TracerMaxHistory = tracerMaxHistory;
		m_ApiInvokeCounter = factory.openCounter("metrics");
		m_Timer = new Timer("metrics-timer", true);
		if (m_CollectorMaxHistory > 0 || m_TracerMaxHistory > 0) {
			m_Timer.schedule(new TimerTask() {
				@Override
				public void run() {
					clear();
				}
			}, TimeUtil.DAY_MILLS, TimeUtil.DAY_MILLS);
		}
		m_Timer.schedule(new ApiInvokeAnalyzer(), 60 * 1000, 60 * 1000);
		Shutdown.register(this);
	}

	public void setCollectors(List<MetricsCollector> collectors) {
		m_Collectors = FreezedList.freezed(collectors);
	}

	public void setTracers(List<MetricsTracer> tracers) {
		m_Tracers = FreezedList.freezed(tracers);
	}

	@Override
	public void precheck(RestfulRequest request, RestfulResponse response) throws IOException {
	}

	@Override
	public void service(RestfulRequest request, RestfulResponse response) throws IOException {
		response.setHeader("WF-Biz", "metrics");
		AccessExt access;
		try {
			access = m_Auth.auth(request, response);
		} catch (Throwable e) {
			response.setStatus(RestfulResponse.STATUS_SERVICE_UNAVAILABLE);
			response.openOutput().close();
			return;
		}
		if (null == access) {
			response.setStatus(RestfulResponse.STATUS_UNAUTHORIZED);
			response.openOutput().close();
			return;
		}
		String path = request.getUri();
		if (path.length() > 1) {
			if ("metrics".equals(path.substring(1, path.length() - 2))) {
				metrics(access, request, response);
				return;
			}
			if ("trace".equals(path.substring(1, path.length() - 2))) {
				trace(access, request, response);
				return;
			}
		}
		response.setStatus(RestfulResponse.STATUS_NOT_FOUND);
		response.openOutput().close();
	}

	private void metrics(AccessExt access, RestfulRequest request, RestfulResponse response) throws IOException {
		String content = readRequest(request);
		if (StringUtil.isEmpty(content)) {
			response.setStatus(RestfulResponse.STATUS_OK);
			try (OutputStream out = response.openOutput()) {
				out.write("empty metrics".getBytes());
			}
			return;
		}
		JSONObject json = new JSONObject(content);
		if (_Logger.isTraceEnabled()) {
			_Logger.trace(json.toString());
		}
		JSONObject id = json.getJSONObject("id");
		String name = id.optString("name");
		String type = id.optString("type");
		String description = id.optString("description");
		String baseUnit = id.optString("baseUnit");
		JSONObject tags = json.optJSONObject("tags");
		List<Tag> tagList = new ArrayList<>();
		for (String key : tags.keySet()) {
			tagList.add(new ImmutableTag(key, tags.optString(key)));
		}
		List<Measurement> measureList = new ArrayList<>();
		JSONObject measure = json.optJSONObject("measure");
		for (String key : measure.keySet()) {
			final double d = measure.optDouble(key);
			Measurement m = new Measurement(() -> d, Statistic.valueOf(key));
			measureList.add(m);
		}
		Meter.Id mid = new Meter.Id(name, Tags.concat(tagList), StringUtil.isEmpty(baseUnit) ? null : baseUnit,
				StringUtil.isEmpty(description) ? null : description, Meter.Type.valueOf(type));
		for (MetricsCollector collector : m_Collectors) {
			collector.collect(access.getGroupId(), mid, measureList);
		}
		response.setStatus(RestfulResponse.STATUS_OK);
		try (OutputStream out = response.openOutput()) {
			out.write("success".getBytes());
		}

	}

	private void trace(AccessExt access, RestfulRequest request, RestfulResponse response) throws IOException {
		String content = readRequest(request);
		if (StringUtil.isEmpty(content)) {
			response.setStatus(RestfulResponse.STATUS_OK);
			try (OutputStream out = response.openOutput()) {
				out.write("empty trace".getBytes());
			}
			return;
		}
		JSONObject json = new JSONObject(content);
		if (_Logger.isTraceEnabled()) {
			_Logger.trace(json.toString());
		}
		String id = json.optString("id");
		String parentId = json.optString("parentId");
		String traceId = json.optString("traceId");
		String kind = json.optString("kind");
		long timestamp = json.optLong("timestamp");
		long duration = json.optLong("duration");
		JSONObject tags = json.optJSONObject("tags");
		List<Tag> tagList = new ArrayList<>();
		for (String key : tags.keySet()) {
			tagList.add(new ImmutableTag(key, tags.optString(key)));
		}
		Trace trace = CommonTrace.newTrace(id, parentId, traceId, timestamp, duration, kind, tagList);
		for (MetricsTracer tracer : m_Tracers) {
			tracer.collect(access.getGroupId(), trace);
		}
		response.setStatus(RestfulResponse.STATUS_OK);
		try (OutputStream out = response.openOutput()) {
			out.write("success".getBytes());
		}

	}

	class OutAppendable implements Appendable {

		private OutputStream m_Out;

		public OutAppendable(OutputStream out) {
			m_Out = out;
		}

		@Override
		public Appendable append(CharSequence csq) throws IOException {
			m_Out.write(csq.toString().getBytes());
			return this;
		}

		@Override
		public Appendable append(CharSequence csq, int start, int end) throws IOException {
			return append(csq.subSequence(start, end));
		}

		@Override
		public Appendable append(char c) throws IOException {
			m_Out.write(c);
			return this;
		}

	}

	@Override
	public void timeout(RestfulRequest request, RestfulResponse response) throws IOException {

	}

	private static String readRequest(RestfulRequest request) throws IOException {
		InputStream in = null;
		ByteArrayOutputStream bos = null;
		try {
			String json;
			in = request.getContent();
			bos = new ByteArrayOutputStream(4 * 1024);
			byte[] buf = new byte[1024];
			int len;
			while (-1 != (len = in.read(buf))) {
				bos.write(buf, 0, len);
			}
			buf = null;
			json = bos.toString("UTF-8");
			return json;
		} finally {
			if (null != in) {
				in.close();
			}
			if (null != bos) {
				bos.close();
			}
		}
	}

	@Override
	public OneMetrics getLately(Organization org, Id id) {
		for (MetricsCollector c : m_Collectors) {
			OneMetrics m = c.getLately(org, id, Statistic.VALUE);
			if (null != m) {
				return m;
			}
		}
		return null;
	}

	@Override
	public ManyMetrics search(Organization org, Meter.Id id, String name, Tags exclude, Date begin, Date end,
			int interval) {
		for (MetricsCollector c : m_Collectors) {
			ManyMetrics m = c.search(org, id, name, exclude, begin, end, interval);
			if (null != m) {
				return m;
			}
		}
		return null;
	}

	@Override
	public List<String> showTags(Organization org, Id id) {
		for (MetricsCollector c : m_Collectors) {
			List<String> m = c.showTags(org, id);
			if (null != m) {
				return m;
			}
		}
		return Collections.emptyList();
	}

	@Override
	public ResultPage<TracerSpanTree> searchTracer(Organization org, Date begin, Date end, String serviceName,
			String serviceNo, String method,int minDuration,int maxDuration) {
		for (MetricsTracer c : m_Tracers) {
			ResultPage<TracerSpanTree> m = c.search(org, begin, end, serviceName, serviceNo, method,minDuration,maxDuration);
			if (null != m) {
				return m;
			}
		}
		return ResultPageHelper.empty();
	}

	@Override
	public TracerSpanTree getTracer(Organization org, String id) {
		for (MetricsTracer c : m_Tracers) {
			TracerSpanTree m = c.get(org, id);
			if (null != m) {
				return m;
			}
		}
		return null;
	}

	@Override
	public ApiInvokeInfo getApiInvokeInfo(Organization org, Date begin, Date end, String serviceName, String serviceNo,
			String methodName) {
		ResultPage<String> rp = m_ApiInvokeCounter
				.startsWith(StringUtil.toString(serviceNo) + "." + StringUtil.toString(serviceName) + ".");
		return null;
	}

	@Override
	public void destroy() {
		Timer timer = m_Timer;
		timer.cancel();
	}

	public void clear() {
		{
			int maxHistory = m_CollectorMaxHistory;
			List<MetricsCollector> cs = m_Collectors;
			if (maxHistory > 0 && null != cs) {
				for (MetricsCollector c : cs) {
					try {
						c.clear(maxHistory);
					} catch (Throwable e) {
						_Logger.warn("忽略清理异常", e);
					}
				}
			}
		}
		{
			int maxHistory = m_TracerMaxHistory;
			List<MetricsTracer> ts = m_Tracers;
			if (maxHistory > 0 && null != m_Tracers) {
				for (MetricsTracer t : ts) {
					try {
						t.clear(maxHistory);
					} catch (Throwable e) {
						_Logger.warn("忽略清理异常", e);
					}
				}
			}
		}
	}

	/**
	 * 分析API调用情况
	 * 
	 * @author liangcha
	 */
	class ApiInvokeAnalyzer extends TimerTask implements Runnable {

		@Override
		public void run() {
			byMinutes(System.currentTimeMillis(), null);
		}

		/**
		 * 按分钟统计（上一分钟的），生成计数器项为： <br>
		 * 实例编号.服务.方法.yyyyMMddHHmm<br>
		 * 实例编号.服务..yyyyMMddHHmm<br>
		 * 实例编号.服务.方法.yyyyMMddHHmm.t0_01<br>
		 * 实例编号.服务.方法.yyyyMMddHHmm.t01_05<br>
		 * 实例编号.服务.方法.yyyyMMddHHmm.t05_1<br>
		 * 实例编号.服务.方法.yyyyMMddHHmm.t1_5<br>
		 * 实例编号.服务.方法.yyyyMMddHHmm.t5_10<br>
		 * 实例编号.服务.方法.yyyyMMddHHmm.t10<br>
		 * 
		 * @param timeMs 要统计的所在分钟时间点（自GMT1970零时的毫秒数）
		 */
		void byMinutes(long timeMs, Organization org) {
			// 把时间修整为分钟
			long minutes = timeMs / (60 * 1000L);
			timeMs = minutes * 60 * 1000L;
			Date begin = new Date(timeMs - (60 * 1000L));
			DTF.setTimeZone(TimeUtil.TZ_GMT);
			String dtf = DTF.format(begin);
			Date end = new Date(timeMs);
			StringBuilder builder = new StringBuilder(64);
			Map<String, HitItem> counter = new HashMap<>();
			for (MetricsTracer c : m_Tracers) {
				ResultPage<TracerSpanTree> rp = c.search(org, begin, end, null, null, null,0,0);
				if (null == rp) {
					continue;
				}
				for (int i = 1; rp.gotoPage(i); i++) {
					for (TracerSpanTree t : rp) {
						SpanNode node = t.getRoot();
						count(node, counter, dtf, builder);
					}
				}
			}
			// 更新到计数器
			for (Map.Entry<String, HitItem> item : counter.entrySet()) {
				long v = item.getValue().v;
				m_ApiInvokeCounter.set(item.getKey(), v);
			}
		}

//		/**
//		 * 按小时统计
//		 * 
//		 * @param timeMs 要统计的所在小时时间点（自GMT1970零时的毫秒数）
//		 * @param org
//		 */
//		void byHours(long timeMs, Organization org) {
//			// 把时间修整为小时
//			long hours = timeMs / (60* 60 * 1000L);
//			long begin=hours*60;
//			long end=begin+60;
//			String suffix = ".h" + Hex.toHex32((int) minutes);
//			StringBuilder builder = new StringBuilder(64);
//			Map<String, HitItem> counter = new HashMap<>();
//			
//			
//			for (MetricsTracer c : m_Tracers) {
//				ResultPage<TracerSpanTree> rp = c.search(org, begin, end, null, null, null);
//				if (null == rp) {
//					continue;
//				}
//				for (int i = 1; rp.gotoPage(i); i++) {
//					for (TracerSpanTree t : rp) {
//						SpanNode node = t.getRoot();
//						count(node, counter, suffix, builder);
//					}
//				}
//			}
//			// 更新到计数器
//			for (Map.Entry<String, HitItem> item : counter.entrySet()) {
//				long v=item.getValue().v;
//				if(v>0) {
//				m_ApiInvokeCounter.set(item.getKey(), item.getValue().v);
//				}
//			}
//		}

		void count(SpanNode node, Map<String, HitItem> counter, String dtf, StringBuilder builder) {
			// 实例编号.服务.方法.suffix
			if (!StringUtil.isEmpty(node.getMethod())) {
				builder.setLength(0);
				builder.append(StringUtil.toString(node.getServiceNo())).append('.')
						.append(StringUtil.toString(node.getServiceName())).append('.')
						.append(StringUtil.toString(node.getMethod())).append('.').append(dtf);
//				m_ApiInvokeCounter.inc(builder.toString());
				HitItem hit = counter.computeIfAbsent(builder.toString(), HitItem._creater);
				++hit.v;
			}
			{
				// 实例编号.服务..suffix
				builder.setLength(0);
				builder.append(StringUtil.toString(node.getServiceNo())).append('.')
						.append(StringUtil.toString(node.getServiceName())).append('.').append(dtf);
				HitItem hit = counter.computeIfAbsent(builder.toString(), HitItem._creater);
				++hit.v;
			}

			// 实例编号.服务.方法.t0_01<br>
			long duration = node.getDuration();
			if (duration >= 0 && duration < 100) {
				builder.setLength(0);
				builder.append(StringUtil.toString(node.getServiceNo())).append('.')
						.append(StringUtil.toString(node.getServiceName())).append('.')
						.append(StringUtil.toString(node.getMethod())).append('.').append(dtf).append(".t0_01");
				HitItem hit = counter.computeIfAbsent(builder.toString(), HitItem._creater);
				++hit.v;
			}
			// 实例编号.服务.方法.t01_05<br>
			else if (duration >= 100 && duration < 500) {
				builder.setLength(0);
				builder.append(StringUtil.toString(node.getServiceNo())).append('.')
						.append(StringUtil.toString(node.getServiceName())).append('.')
						.append(StringUtil.toString(node.getMethod())).append('.').append(dtf).append(".t01_05");
				HitItem hit = counter.computeIfAbsent(builder.toString(), HitItem._creater);
				++hit.v;
			}
			// 实例编号.服务.方法.t05_1<br>
			else if (duration >= 500 && duration < 1000) {
				builder.setLength(0);
				builder.append(StringUtil.toString(node.getServiceNo())).append('.')
						.append(StringUtil.toString(node.getServiceName())).append('.')
						.append(StringUtil.toString(node.getMethod())).append('.').append(dtf).append(".t05_1");
				HitItem hit = counter.computeIfAbsent(builder.toString(), HitItem._creater);
				++hit.v;
			}
			// 实例编号.服务.方法.t1_5<br>
			else if (duration >= 1000 && duration < 5000) {
				builder.setLength(0);
				builder.append(StringUtil.toString(node.getServiceNo())).append('.')
						.append(StringUtil.toString(node.getServiceName())).append('.')
						.append(StringUtil.toString(node.getMethod())).append('.').append(dtf).append(".t1_5");
				HitItem hit = counter.computeIfAbsent(builder.toString(), HitItem._creater);
				++hit.v;
			}
			// 实例编号.服务.方法.t5_10<br>
			else if (duration >= 5000 && duration < 1000) {
				builder.setLength(0);
				builder.append(StringUtil.toString(node.getServiceNo())).append('.')
						.append(StringUtil.toString(node.getServiceName())).append('.')
						.append(StringUtil.toString(node.getMethod())).append(dtf).append(".t5_10");
				HitItem hit = counter.computeIfAbsent(builder.toString(), HitItem._creater);
				++hit.v;
			}
			// 实例编号.服务.方法.t10<br>
			else if (duration >= 10000) {
				builder.setLength(0);
				builder.append(StringUtil.toString(node.getServiceNo())).append('.')
						.append(StringUtil.toString(node.getServiceName())).append('.')
						.append(StringUtil.toString(node.getMethod())).append(dtf).append(".t10");
				HitItem hit = counter.computeIfAbsent(builder.toString(), HitItem._creater);
				++hit.v;
			}

			// 遍历子节点
			List<SpanNode> ls = node.getChildren();
			if (null != ls && ls.size() > 0) {
				for (SpanNode n : ls) {
					count(n, counter, dtf, builder);
				}
			}
		}
	}

	static class HitItem {
		public long v;

		public static Function<String, HitItem> _creater = new Function<String, HitItem>() {
			@Override
			public HitItem apply(String t) {
				return new HitItem();
			}
		};
	}
}
