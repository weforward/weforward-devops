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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.weforward.metrics.TracerSpan;
import cn.weforward.metrics.TracerSpans;

/**
 * 追踪块集合
 * 
 * @author daibo
 *
 */
public class SimpleTracerSpans implements TracerSpans {
	/** 追踪块集合 */
	protected List<TracerSpan> m_Spans;

	public SimpleTracerSpans() {
		m_Spans = new ArrayList<>();
	}

	/**
	 * 添加追踪块
	 * 
	 * @param span
	 */
	public void add(TracerSpan span) {
		m_Spans.add(span);
		Collections.sort(m_Spans);
	}

	/**
	 * 获取追踪块
	 * 
	 * @return
	 */
	public List<TracerSpan> getSpans() {
		return m_Spans;
	}

	@Override
	public String toString() {
		return getSpans().toString();
	}

}
