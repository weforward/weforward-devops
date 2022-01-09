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

import java.net.ConnectException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.InfluxDBIOException;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Result;
import org.influxdb.dto.QueryResult.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.weforward.common.util.NumberUtil;
import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.TimeUtil;
import cn.weforward.common.util.TransList;
import cn.weforward.devops.user.Organization;
import cn.weforward.metrics.ManyMetrics;
import cn.weforward.metrics.MetricsCollector;
import cn.weforward.metrics.OneMetrics;
import cn.weforward.protocol.datatype.DtDate;
import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Meter.Id;
import io.micrometer.core.instrument.Statistic;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.config.NamingConvention;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;

/**
 * 基于Influxdb的指标收集
 * 
 * @author daibo
 *
 */
public class InfluxdbMetricsCollector implements MetricsCollector {

	private static Logger _Logger = LoggerFactory.getLogger(InfluxdbMetricsCollector.class);

	private String m_ServerUrl;
	private String m_Username;
	private String m_Password;
	private String m_DBName;
	private String m_RetentionPolicy;
	private InfluxDB m_DB;
	private static final String INTERVAL = "2m";

	public InfluxdbMetricsCollector(String serverUrl, String username, String password, String dbname) {
		m_ServerUrl = serverUrl;
		m_Username = username;
		m_Password = password;
		m_DBName = dbname;
	}

	public void setRetentionPolicy(String v) {
		m_RetentionPolicy = v;
	}

	private InfluxDB getDb() {
		if (null == m_DB) {
			Builder client = new OkHttpClient.Builder();
			InfluxDB db;
			if (StringUtil.isEmpty(m_Username) || StringUtil.isEmpty(m_Password)) {
				db = InfluxDBFactory.connect(m_ServerUrl, client);
			} else {
				db = InfluxDBFactory.connect(m_ServerUrl, m_Username, m_Password, client);
			}
			try {
				db.query(new Query("create database " + m_DBName));
			} catch (Throwable e) {
				_Logger.warn("忽略创建数据库出错", e);
			}
			m_DB = db;
		}
		return m_DB;

	}

	@Override
	public void collect(String org, Id id, Iterable<Measurement> measure) {
		String conventionName = getConventionName(org, id);
		Point.Builder build = Point.measurement(conventionName);
		// String help = StringUtil.toString(id.getDescription());
		for (Tag t : id.getTags()) {
			build.tag(t.getKey(), t.getValue());
		}
		for (Measurement m : measure) {
			Statistic stat = m.getStatistic();
			build.addField(stat.name(), m.getValue());
		}
		try {
			getDb().write(m_DBName, m_RetentionPolicy, build.build());
		} catch (InfluxDBIOException e) {
			if (e.getCause() instanceof ConnectException) {
				if (_Logger.isTraceEnabled()) {
					_Logger.trace("忽略异常", e);
				}
			} else {
				throw e;
			}
		}
	}

	@Override
	public OneMetrics getLately(Organization org, Id id, Statistic statistic) {
		String conventionName = getConventionName(org.getId(), id);
		StringJoiner joiner = new StringJoiner(" AND ");
		for (Tag t : id.getTags()) {
			joiner.add("\"" + escape(t.getKey()) + "\"=~ /^" + escape(t.getValue()) + "$/");
		}
		String name = escape(statistic.name().toUpperCase());
		String command = "SELECT " + name + " FROM \"" + conventionName + "\" WHERE time > now() - 5m AND "
				+ joiner.toString();
		Series s = query(command);
		if (null == s) {
			return null;
		}
		List<List<Object>> values = s.getValues();
		if (null == values || values.isEmpty()) {
			return null;
		}
		return new MetricsWrap(s, values.get(values.size() - 1));
	}

	@Override
	public ManyMetrics search(Organization org, Id id, String name, Tags exclude, Date begin, Date end, int interval) {
		String conventionName = getConventionName(org.getId(), id);
		StringJoiner joiner = new StringJoiner(" AND ");
		for (Tag t : id.getTags()) {
			joiner.add("\"" + escape(t.getKey()) + "\"=~ /^" + escape(t.getValue()) + "$/");
		}
		if (null != exclude) {
			for (Tag t : exclude) {
				joiner.add("\"" + escape(t.getKey()) + "\"!='" + escape(t.getValue()) + "'");
			}
		}
		String time;
		if (null == begin && null == end) {
			time = "time > now() - 5m";
		} else if (null == begin) {
			time = "time <=" + end.getTime() + "ms";
		} else if (null == end) {
			time = "time >= " + begin.getTime() + "ms";
		} else {
			time = "time <=" + end.getTime() + "ms AND " + "time >= " + begin.getTime() + "ms";
		}
		name = name.toUpperCase();
		String intervalTime = interval > 0 ? interval + "m" : INTERVAL;
		String command = "SELECT " + name + " as VALUE FROM \"" + conventionName + "\" WHERE " + time + " AND ("
				+ joiner.toString() + ") GROUP BY time(" + intervalTime + ") ";// +fill(null);
		Series s = query(command);
		if (null == s) {
			return null;
		}
		List<List<Object>> values = s.getValues();
		if (null == values || values.isEmpty()) {
			return null;
		}
		return new ManyMetricsWrap(begin, end, s, values);
	}

	@Override
	public List<String> showTags(Organization org, Id id) {
		Meter.Type type = id.getType();
		String conventionName = org.getId() + "_"
				+ escape(NamingConvention.snakeCase.name(id.getName(), type, id.getBaseUnit()));
		StringJoiner joiner = new StringJoiner(" AND ");
		for (Tag t : id.getTags()) {
			joiner.add("key=\"" + escape(t.getKey()) + "\"");
		}
		String command = "SHOW tag values from  " + conventionName + " with " + joiner.toString();
		Series s = query(command);
		if (null == s) {
			return Collections.emptyList();
		}
		List<List<Object>> values = s.getValues();
		if (null == values || values.isEmpty()) {
			return null;
		}
		int valueIndex = -1;
		List<String> cols = s.getColumns();
		for (int i = 0; i < cols.size(); i++) {
			if (StringUtil.eq(cols.get(i), "value")) {
				valueIndex = i;
			}
		}
		if (valueIndex >= 0) {
			List<String> list = new ArrayList<>();
			for (List<Object> v : values) {
				list.add(StringUtil.toString(v.get(valueIndex)));
			}
			return list;
		}
		return Collections.emptyList();
	}

	@Override
	public void clear(int maxHistory) {
		Series s = query("show measurements");
		long end = System.currentTimeMillis() - maxHistory * 1l * TimeUtil.DAY_MILLS;
		for (List<Object> list : s.getValues()) {
			for (Object v : list) {
				String command = "DELETE FROM \"" + v + "\" WHERE time <" + end + "ms";
				query(command);
			}
		}

	}

	private static String getConventionName(String org, Id id) {
		return org + "_" + escape(NamingConvention.snakeCase.name(id.getName(), id.getType(), id.getBaseUnit()));
	}

	static final void appendEscape(char ch, StringBuilder sb) {
		if ('\'' == ch) {
			sb.append("''");
		}
		if ('/' == ch) {
			sb.append("\\/");
		} else {
			sb.append(ch);
		}
	}

	static public final String escape(String str) {
		if (null == str || 0 == str.length()) {
			return "";
		}

		StringBuilder sql = new StringBuilder(str.length());
		int len = str.length();
		for (int i = 0; i < len; i++) {
			appendEscape(str.charAt(i), sql);
		}
		return sql.toString();
	}

	private Series query(String command) {
		QueryResult result;
		try {
			result = getDb().query(new Query(command, m_DBName));
		} catch (InfluxDBIOException e) {
			if (e.getCause() instanceof ConnectException) {
				if (_Logger.isTraceEnabled()) {
					_Logger.trace("忽略异常", e);
				}
				return null;
			} else {
				throw e;
			}
		}
		String error = result.getError();
		if (!StringUtil.isEmpty(error)) {
			throw new IllegalArgumentException("查询出错:" + error);
		}
		List<Result> rs = result.getResults();
		if (null == rs || rs.isEmpty()) {
			return null;
		}
		Result r = rs.get(0);
		error = r.getError();
		if (!StringUtil.isEmpty(error)) {
			throw new IllegalArgumentException("查询出错:" + error);
		}
		List<Series> ss = r.getSeries();
		if (null == ss || ss.isEmpty()) {
			return null;
		}
		Series s = ss.get(0);
		return s;
	}

	class ManyMetricsWrap implements ManyMetrics {

		protected Date m_Begin;

		protected Date m_End;

		protected Series m_Series;

		protected List<List<Object>> m_Values;

		public ManyMetricsWrap(Date begin, Date end, Series series, List<List<Object>> values) {
			m_Begin = begin;
			m_End = end;
			m_Series = series;
			m_Values = values;
		}

		@Override
		public Date getBegin() {
			return m_Begin;
		}

		@Override
		public Date getEnd() {
			return m_End;
		}

		@Override
		public List<OneMetrics> getItems() {
			return TransList.valueOf(m_Values, (v) -> new MetricsWrap(m_Series, v));
		}

	}

	class MetricsWrap implements OneMetrics {

		protected Date m_Time;

		protected double m_Value;

		public MetricsWrap(Series r, List<Object> list) {
			int timeIndex = -1;
			int valueIndex = -1;
			List<String> cols = r.getColumns();
			for (int i = 0; i < cols.size(); i++) {
				if (StringUtil.eq(cols.get(i), "time")) {
					timeIndex = i;
				} else if (StringUtil.eq(cols.get(i), "VALUE")) {
					valueIndex = i;
				}
			}
			if (timeIndex >= 0) {
				String format = StringUtil.toString(list.get(timeIndex));
				if (format.length() > 24) {
					format = format.substring(0, 23) + 'Z';
				} else if (format.length() == 20) {
					format = format.substring(0, 19) + ".000Z";
				}
				try {
					m_Time = DtDate.Formater.parse(format);
				} catch (ParseException e) {
					_Logger.warn("格式化异常:" + format, e);
				}
			}
			if (valueIndex >= 0) {
				m_Value = NumberUtil.toDouble(StringUtil.toString(list.get(valueIndex)), 0);
			}
		}

		@Override
		public Date getTime() {
			return m_Time;
		}

		@Override
		public double getValue() {
			return m_Value;
		}
	}

}
