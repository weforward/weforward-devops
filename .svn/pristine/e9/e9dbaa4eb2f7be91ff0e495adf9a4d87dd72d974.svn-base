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
package cn.weforward.metrics;

import java.io.IOException;
import java.util.Date;

import cn.weforward.common.ResultPage;
import cn.weforward.trace.Trace;
import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Meter.Id;

/**
 * 追踪者
 * 
 * @author daibo
 *
 */
public interface MetricsTracer {
	/** 空实现 */
	MetricsTracer EMPTY = new MetricsTracer() {

		@Override
		public void collect(Id id, Iterable<Measurement> measure) {

		}

		@Override
		public void clear(int maxHistory) {

		}

		@Override
		public void collect(Trace trace) {

		}

	};

	/**
	 * 收集
	 * 
	 * @param id
	 * @param measure
	 */
	void collect(Meter.Id id, Iterable<Measurement> measure);

	/**
	 * 收集
	 * 
	 * @param trace
	 */
	void collect(Trace trace);

	/**
	 * 清理数据
	 * 
	 * @param maxHistory 最大要保存的天数
	 */
	void clear(int maxHistory);

	/**
	 * 查询追踪块
	 * 
	 * @param traceId
	 * @return
	 * @throws IOException
	 */
	default TracerSpanTree get(String traceId) {
		return null;
	}

	/**
	 * 查询 追踪块
	 * 
	 * @param begin
	 * @param end
	 * @param serviceName
	 * @param serviceNo
	 * @param method
	 * @return
	 * @throws IOException
	 */
	default ResultPage<TracerSpanTree> search(Date begin, Date end, String serviceName, String serviceNo,
			String method) {
		return null;
	}

}
