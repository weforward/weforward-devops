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
package cn.weforward.metrics.ext;

import javax.annotation.Resource;

import cn.weforward.metrics.TracerSpan;

/**
 * 追踪块
 * 
 * @author daibo
 *
 */
public class SimpleTracerSpan implements TracerSpan {
	@Resource
	protected String m_TraceId;
	@Resource
	protected String m_TraceSpanId;
	@Resource
	protected String m_TraceParentId;
	@Resource
	protected String m_Name;
	@Resource
	protected String m_ServiceName;
	@Resource
	protected String m_ServiceNo;
	@Resource
	protected String m_Method;
	@Resource
	protected long m_Timestamp;
	@Resource
	protected long m_Duration;

	public SimpleTracerSpan() {

	}

	public SimpleTracerSpan(String traceId, long ts) {
		m_TraceId = traceId;
		m_Timestamp = ts;
	}

	@Override
	public String getId() {
		return m_TraceId;
	}

	@Override
	public String getSpanId() {
		return m_TraceSpanId;
	}

	@Override
	public void setSpanId(String traceSpanId) {
		m_TraceSpanId = traceSpanId;
	}

	@Override
	public String getParentId() {
		return m_TraceParentId;
	}

	@Override
	public void setParentId(String traceParentId) {
		m_TraceParentId = traceParentId;
	}

	@Override
	public String getName() {
		return m_Name;
	}

	@Override
	public void setName(String name) {
		m_Name = name;
	}

	@Override
	public String getServiceName() {
		return m_ServiceName;
	}

	@Override
	public void setServiceName(String serviceName) {
		m_ServiceName = serviceName;
	}

	@Override
	public String getServiceNo() {
		return m_ServiceNo;
	}

	@Override
	public void setServiceNo(String serviceNo) {
		m_ServiceNo = serviceNo;
	}

	@Override
	public String getMethod() {
		return m_Method;
	}

	@Override
	public void setMethod(String method) {
		m_Method = method;
	}

	@Override
	public long getTimestamp() {
		return m_Timestamp;
	}

	public void setDuration(long duration) {
		m_Duration = duration;
	}

	@Override
	public long getDuration() {
		return m_Duration;
	}

	public static SimpleTracerSpan valueOf(TracerSpan span) {
		if (span instanceof SimpleTracerSpan) {
			return (SimpleTracerSpan) span;
		}
		SimpleTracerSpan s = new SimpleTracerSpan(span.getId(), span.getTimestamp());
		s.setMethod(span.getMethod());
		s.setName(s.getName());
		s.setServiceNo(span.getServiceNo());
		s.setServiceName(span.getServiceName());
		s.setSpanId(span.getSpanId());
		s.setParentId(span.getParentId());
		return s;
	}

	@Override
	public String toString() {
		return getSpanId() + ":" + getTimestamp();
	}

}
