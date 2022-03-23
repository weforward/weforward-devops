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
import cn.weforward.devops.user.Organization;
import cn.weforward.metrics.MetricsTracer;
import cn.weforward.metrics.TracerSpanTree;
import cn.weforward.metrics.ext.SimpleTracerSpan;
import cn.weforward.protocol.datatype.DtObject;
import cn.weforward.protocol.ext.ObjectMapper;
import cn.weforward.protocol.support.datatype.SimpleDtObject;
import cn.weforward.trace.Trace;
import cn.weforward.trace.WeforwardTrace;

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

	private static final ObjectMapper<TracerSpansImpl> MAPPER = FieldMapper.valueOf(TracerSpansImpl.class);

	private static final String ID = "_id";

	private static final String LASTMODIFIED = "_lastmodified";

	private static final String ORGANIZATION = "organization";

	private static final ReplaceOptions UPSERT_OPTIONS = new ReplaceOptions().upsert(true);

	public MongodbMetricsTracer(String url, String dbname) {
		m_Db = MongodbUtil.create(url).getDatabase(dbname);
	}

	@Override
	public void collect(String org, Trace trace) {
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
			spans = getSpans(org, traceId);
			if (null == spans) {
				spans = new TracerSpansImpl(span, org);
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

	private TracerSpansImpl getSpans(String org, String traceId) {
		FindIterable<Document> it = getCollection()
				.find(Filters.and(Filters.eq(ID, traceId), Filters.eq(ORGANIZATION, org)));
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
	public TracerSpanTree get(Organization org, String traceId) {
		return TracerSpanTree.valueOf(getSpans(org.getId(), traceId));
	}

	@Override
	public ResultPage<TracerSpanTree> search(Organization org, Date begin, Date end, String serviceName,
			String serviceNo, String method) {
		List<Bson> list = new ArrayList<>();
		list.add(Filters.eq(ORGANIZATION, org.getId()));
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
