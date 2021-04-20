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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
import cn.weforward.devops.user.Organization;
import cn.weforward.devops.user.OrganizationProvider;
import cn.weforward.metrics.ManyMetrics;
import cn.weforward.metrics.MetricsCollector;
import cn.weforward.metrics.MetricsService;
import cn.weforward.metrics.MetricsTracer;
import cn.weforward.metrics.OneMetrics;
import cn.weforward.metrics.TracerSpanTree;
import cn.weforward.trace.CommonTrace;
import cn.weforward.trace.Trace;
import cn.weforward.util.HttpAuth;
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

	private List<MetricsCollector> m_Collectors = Collections.emptyList();

	private List<MetricsTracer> m_Tracers = Collections.emptyList();

	protected int m_CollectorMaxHistory;

	protected int m_TracerMaxHistory;
	/** 清理线程 */
	protected Thread m_Clears;
	/** http验证 */
	protected HttpAuth m_Auth;

	protected OrganizationProvider m_Provider;

	public MetricsServiceImpl(int collectorMaxHistory, int tracerMaxHistory, OrganizationProvider provider) {
		m_Provider = provider;
		m_CollectorMaxHistory = collectorMaxHistory;
		m_TracerMaxHistory = tracerMaxHistory;
		if (m_CollectorMaxHistory > 0 || m_TracerMaxHistory > 0) {
			m_Clears = new Thread(new Runnable() {

				@Override
				public void run() {
					while (null != m_Clears) {
						synchronized (this) {
							try {
								this.wait(TimeUtil.DAY_MILLS);
							} catch (InterruptedException e) {
								return;
							}
						}
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

				}

			}, "metrics-clear");
			m_Clears.start();
		}
		Shutdown.register(this);
	}

	public void setCollectors(List<MetricsCollector> collectors) {
		m_Collectors = FreezedList.freezed(collectors);
	}

	public void setTracers(List<MetricsTracer> tracers) {
		m_Tracers = FreezedList.freezed(tracers);
	}

	public void setAuth(HttpAuth auth) {
		m_Auth = auth;
	}

	public HttpAuth getAuth() {
		return m_Auth;
	}

	@Override
	public void precheck(RestfulRequest request, RestfulResponse response) throws IOException {
		HttpAuth auth = getAuth();
		if (null != auth && null == auth.auth(request, response)) {
			response.setStatus(RestfulResponse.STATUS_UNAUTHORIZED);
			response.openOutput().close();
			return;
		}
	}

	@Override
	public void service(RestfulRequest request, RestfulResponse response) throws IOException {
		String path = request.getUri();
		if (path.length() > 1) {
			if ("metrics".equals(path.substring(1, path.length() - 2))) {
				metrics(request, response);
				return;
			}
			if ("trace".equals(path.substring(1, path.length() - 2))) {
				trace(request, response);
				return;
			}
		}
		response.setStatus(RestfulResponse.STATUS_NOT_FOUND);
		response.openOutput().close();
	}

	private void metrics(RestfulRequest request, RestfulResponse response) throws IOException {
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
		String accessId = json.optString("accessId");
		Organization org = m_Provider.getByAccessId(accessId);
		if (null == org) {
			return;
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
			collector.collect(org, mid, measureList);
		}
		response.setStatus(RestfulResponse.STATUS_OK);
		try (OutputStream out = response.openOutput()) {
			out.write("success".getBytes());
		}

	}

	private void trace(RestfulRequest request, RestfulResponse response) throws IOException {
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
		String accessId = json.optString("accessId");
		Organization org = m_Provider.getByAccessId(accessId);
		if (null == org) {
			return;
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
			tracer.collect(org, trace);
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
			String serviceNo, String method) {
		for (MetricsTracer c : m_Tracers) {
			ResultPage<TracerSpanTree> m = c.search(org, begin, end, serviceName, serviceNo, method);
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
	public void destroy() {
		Thread s = m_Clears;
		if (null != s) {
			m_Clears = null;
			s.interrupt();
		}
	}
}
