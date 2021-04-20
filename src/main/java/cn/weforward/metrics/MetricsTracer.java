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

import java.util.Date;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.devops.user.Organization;
import cn.weforward.trace.Trace;

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
		public void collect(Organization org, Trace trace) {

		}

		@Override
		public TracerSpanTree get(Organization org, String traceId) {
			return null;
		}

		@Override
		public ResultPage<TracerSpanTree> search(Organization org, Date begin, Date end, String serviceName,
				String serviceNo, String method) {
			return ResultPageHelper.empty();
		}

		@Override
		public void clear(int maxHistory) {

		}

	};

	/**
	 * 收集
	 * 
	 * @param 组织
	 * @param trace 追踪块
	 */
	void collect(Organization org, Trace trace);

	/**
	 * 查询追踪块
	 * 
	 * @param 组织
	 * @param traceId 追踪ID
	 * @return
	 */
	TracerSpanTree get(Organization org, String traceId);

	/**
	 * 查询追踪块
	 * 
	 * @param 组织
	 * @param begin       开始时间
	 * @param end         结束时间
	 * @param serviceName 服务名
	 * @param serviceNo   服务编号
	 * @param method      方法名
	 * @return
	 */
	ResultPage<TracerSpanTree> search(Organization org, Date begin, Date end, String serviceName, String serviceNo,
			String method);

	/**
	 * 清理数据
	 * 
	 * @param maxHistory 最大要保存的天数
	 */
	void clear(int maxHistory);

}
