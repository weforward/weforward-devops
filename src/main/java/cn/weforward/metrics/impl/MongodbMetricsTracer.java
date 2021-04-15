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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.UpdateResult;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.TimeUtil;
import cn.weforward.data.mongodb.util.MongodbResultPage;
import cn.weforward.data.mongodb.util.MongodbUtil;
import cn.weforward.data.util.FieldMapper;
import cn.weforward.metrics.MetricsTracer;
import cn.weforward.metrics.TracerSpanTree;
import cn.weforward.metrics.WeforwardMetrics;
import cn.weforward.metrics.ext.SimpleTracerSpan;
import cn.weforward.protocol.datatype.DtObject;
import cn.weforward.protocol.ext.ObjectMapper;
import cn.weforward.protocol.support.datatype.SimpleDtObject;
import cn.weforward.trace.Trace;
import cn.weforward.trace.WeforwardTrace;
import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter.Id;

/**
 * 基于mongodb的收集器
 * 
 * @author daibo
 *
 */
public class MongodbMetricsTracer implements MetricsTracer {

	static final Logger _Logger = LoggerFactory.getLogger(MongodbMetricsTracer.class);

	protected MongoDatabase m_Db;
	protected MongoCollection<Document> m_Connection;

	private static ObjectMapper<TracerSpansImpl> MAPPER = FieldMapper.valueOf(TracerSpansImpl.class);

	private static String ID = "_id";

	private static String LASTMODIFIED = "_lastmodified";

	private static ReplaceOptions UPSERT_OPTIONS = new ReplaceOptions().upsert(true);

	public MongodbMetricsTracer(String url, String dbname) {
		m_Db = MongodbUtil.create(url).getDatabase(dbname);
	}

	@Override
	public void collect(Trace trace) {
		String traceId = trace.getTraceId();
		if (StringUtil.isEmpty(traceId)) {
			return;
		}
		long ts = trace.getTimestamp();
		long duration = trace.getDuration();
		SimpleTracerSpan span = new SimpleTracerSpan(traceId, ts);
		span.setName(trace.getKind());
		span.setServiceNo(trace.getTag(WeforwardTrace.LABEL_SERVICE_NO));
		span.setServiceName(trace.getTag(WeforwardTrace.LABEL_SERVICE_NAME));
		span.setMethod(trace.getTag(WeforwardTrace.LABEL_METHOD_NAME));
		span.setSpanId(trace.getId());
		span.setParentId(trace.getParentId());
		span.setDuration(duration);
		synchronized (this) {
			TracerSpansImpl spans;
			spans = getSpans(traceId);
			if (null == spans) {
				spans = new TracerSpansImpl(span);
			} else {
				spans.add(span);
			}
			saveSpans(spans);
		}

	}

	@Override
	public void collect(Id id, Iterable<Measurement> measure) {
		String traceId = id.getTag(WeforwardMetrics.LABEL_TRACE_ID);
		if (StringUtil.isEmpty(traceId)) {
			return;
		}
		long ts = 0;
		long duration = 0;
		for (Measurement m : measure) {
			String name = m.getStatistic().name();
			if (name.equals("VALUE")) {
				ts = (long) (m.getValue() * 1000);
			} else if (name.equals("TOTAL_TIME")) {
				duration = (long) (m.getValue() * 1000);
				try {
					ts = Long.parseLong(id.getTag(WeforwardMetrics.LABEL_START_TIME_MS));
				} catch (NumberFormatException e) {
					ts = System.currentTimeMillis();
				}

			}
		}
		SimpleTracerSpan span = new SimpleTracerSpan(traceId, ts);
		span.setName(id.getName());
		span.setServiceNo(id.getTag(WeforwardMetrics.LABEL_SERVICE_NO));
		span.setServiceName(id.getTag(WeforwardMetrics.LABEL_SERVICE_NAME));
		span.setMethod(id.getTag(WeforwardMetrics.LABEL_METHOD_NAME));
		span.setSpanId(id.getTag(WeforwardMetrics.LABEL_TRACE_SPAN_ID));
		span.setParentId(id.getTag(WeforwardMetrics.LABEL_TRACE_PARENT_ID));
		span.setDuration(duration);
		synchronized (this) {
			TracerSpansImpl spans;
			spans = getSpans(traceId);
			if (null == spans) {
				spans = new TracerSpansImpl(span);
			} else {
				spans.add(span);
			}
			saveSpans(spans);
		}
	}

	private MongoCollection<Document> getCollection() {
		if (null == m_Connection) {
			MongoCollection<Document> c = m_Db.getCollection("tracerspans");
			IndexOptions options = new IndexOptions();
			options.name(LASTMODIFIED);
			c.createIndex(Filters.eq(LASTMODIFIED, 1), options);
			m_Connection = c;
		}
		return m_Connection;
	}

	private TracerSpansImpl getSpans(String traceId) {
		FindIterable<Document> it = getCollection().find(Filters.eq(ID, traceId));
		Document doc = it.first();
		if (null == doc) {
			return null;
		}
		SimpleDtObject dt = new SimpleDtObject();
		dt = MongodbUtil.docToDt(dt, doc);
		return MAPPER.fromDtObject(dt);
	}

	private void saveSpans(TracerSpansImpl spans) {
		DtObject dt = MAPPER.toDtObject(spans);
		Document doc = new Document();
		doc = MongodbUtil.dtToDoc(doc, dt);
		doc.put(LASTMODIFIED, System.currentTimeMillis());
		MongoCollection<Document> c = getCollection();
		Bson filter = Filters.eq(ID, spans.getId());
		UpdateResult result = c.replaceOne(filter, doc, UPSERT_OPTIONS);
		if (_Logger.isDebugEnabled()) {
			_Logger.debug("upsertedId:" + result.getUpsertedId());
			_Logger.debug("matchedCount:" + result.getMatchedCount());
			_Logger.debug("modifiedCount:" + result.getModifiedCount());
		}
	}

	@Override
	public TracerSpanTree get(String traceId) {
		return TracerSpanTree.valueOf(getSpans(traceId));
	}

	@Override
	public ResultPage<TracerSpanTree> search(Date begin, Date end, String serviceName, String serviceNo,
			String method) {
		List<Bson> list = new ArrayList<>();
		list.add(Filters.gte(LASTMODIFIED, null == begin ? 0 : begin.getTime()));
		list.add(Filters.lte(LASTMODIFIED, null == end ? System.currentTimeMillis() : end.getTime()));
		if (!StringUtil.isEmpty(serviceName)) {
			list.add(Filters.eq("spans.service_name", serviceName));
		}
		if (!StringUtil.isEmpty(serviceNo)) {
			list.add(Filters.eq("spans.service_no", serviceNo));
		}
		if (!StringUtil.isEmpty(method)) {
			list.add(Filters.eq("spans.method", method));
		}
		MongodbResultPage<TracerSpanTree> rp = new MongodbResultPage<TracerSpanTree>(getCollection(), Filters.and(list),
				Filters.eq(LASTMODIFIED, -1)) {

			@Override
			protected TracerSpanTree to(Document doc) {
				TracerSpansImpl src = MAPPER.fromDtObject(MongodbUtil.docToDt(null, doc));
				return TracerSpanTree.valueOf(src);
			}
		};
		return rp;

	}

	@Override
	public void clear(int maxHistory) {
		long end = (System.currentTimeMillis() - maxHistory * 1l * TimeUtil.DAY_MILLS);
		getCollection().deleteMany(Filters.lte(LASTMODIFIED, end));

	}

}
