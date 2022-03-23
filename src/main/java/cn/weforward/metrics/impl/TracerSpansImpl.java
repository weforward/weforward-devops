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
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import cn.weforward.common.util.StringUtil;
import cn.weforward.data.annotation.ResourceExt;
import cn.weforward.metrics.TracerSpan;
import cn.weforward.metrics.TracerSpans;
import cn.weforward.metrics.ext.SimpleTracerSpan;

/**
 * 追踪块实现
 * 
 * @author daibo
 *
 */
public class TracerSpansImpl implements TracerSpans {
	/** 唯一id */
	@Resource(name = "_id", type = String.class)
	protected String m_Id;
	/** 追踪块集合 */
	@ResourceExt(component = SimpleTracerSpan.class)
	protected List<SimpleTracerSpan> m_Spans;
	@Resource
	protected String m_Organization;

	protected TracerSpansImpl() {
	}

	public TracerSpansImpl(TracerSpan first, String org) {
		m_Id = first.getId();
		m_Organization = org;
		m_Spans = new ArrayList<>();
		add(first);
	}

	public String getId() {
		return m_Id;
	}

	public String getOrganization() {
		return m_Organization;
	}

	@Override
	public void add(TracerSpan span) {
		for (TracerSpan s : this) {
			if (StringUtil.eq(s.getName(), span.getName()) && StringUtil.eq(s.getMethod(), span.getMethod())
					&& StringUtil.eq(s.getSpanId(), span.getSpanId())) {
				return;
			}
		}
		m_Spans.add(SimpleTracerSpan.valueOf(span));
		Collections.sort(m_Spans);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TracerSpan> getSpans() {
		List<? extends TracerSpan> spans = m_Spans;
		return (List<TracerSpan>) spans;
	}

}
