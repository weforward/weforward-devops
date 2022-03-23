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

/**
 * 追踪块
 * 
 * @author daibo
 *
 */
public interface TracerSpan extends Comparable<TracerSpan> {
	/**
	 * 追踪id
	 * 
	 * @return
	 */
	String getId();

	/**
	 * 追踪块id
	 * 
	 * @return
	 */
	String getSpanId();

	/**
	 * 追踪块id
	 * 
	 * @param traceSpanId
	 */
	void setSpanId(String traceSpanId);

	/**
	 * 追踪父id
	 * 
	 * @return
	 */
	String getParentId();

	/**
	 * 追踪父id
	 * 
	 * @param traceParentId
	 */
	void setParentId(String traceParentId);

	/**
	 * 名称
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 名称
	 * 
	 * @param name
	 */
	void setName(String name);

	/**
	 * 服务名
	 * 
	 * @return
	 */
	String getServiceName();

	/**
	 * 服务名
	 * 
	 * @param serviceName
	 */
	void setServiceName(String serviceName);

	/**
	 * 服务编号
	 * 
	 * @return
	 */
	String getServiceNo();

	/**
	 * 服务编号
	 * 
	 * @param serviceNo
	 */
	void setServiceNo(String serviceNo);

	/**
	 * 方法名
	 * 
	 * @return
	 */
	String getMethod();

	/**
	 * 方法名
	 * 
	 * @param method
	 */
	void setMethod(String method);

	/**
	 * 时间戳
	 * 
	 * @return
	 */
	long getTimestamp();

	/**
	 * 耗时
	 * 
	 * @return
	 */
	long getDuration();

	default public int compareTo(TracerSpan o) {
		long v = getTimestamp() - o.getTimestamp();
		if (v > 0) {
			return 1;
		} else if (v < 0) {
			return -1;
		} else {
			return 0;
		}
	}

}
