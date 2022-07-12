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
import java.util.List;

import cn.weforward.common.ResultPage;
import cn.weforward.devops.user.Organization;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Meter.Id;
import io.micrometer.core.instrument.Tags;

/**
 * 指标服务
 * 
 * @author daibo
 *
 */
public interface MetricsService {
	/**
	 * 显示标签
	 * 
	 * @param org 组织
	 * @param id
	 * @return
	 */
	List<String> showTags(Organization org, Id id);

	/**
	 * 获取最近一个指标(5分钟内)
	 * 
	 * @param org 组织
	 * @param id
	 * @return
	 */
	OneMetrics getLately(Organization org, Meter.Id id);

	/**
	 * 搜索指标
	 * 
	 * @param org      组织
	 * @param id       统计项id
	 * @param name     统计项名
	 * @param exclude  排除的标签
	 * @param begin    开始时间
	 * @param end      结束时间
	 * @param interval 间隔，分钟
	 * @return
	 */
	ManyMetrics search(Organization org, Id id, String name, Tags exclude, Date begin, Date end, int interval);

	/**
	 * 搜索追踪树
	 * 
	 * @param org         组织
	 * @param begin
	 * @param end
	 * @param serviceName
	 * @param serviceNo
	 * @param method
	 * @param minDuration 持续时间范围，开始，单位毫秒 0
	 * @param maxDuration 持续时间范围，结束，单位毫秒 100ms
	 * @return
	 */
	ResultPage<TracerSpanTree> searchTracer(Organization org, Date begin, Date end, String serviceName,
			String serviceNo, String method,int minDuration,int maxDuration);

	/**
	 * 获取追踪树
	 * 
	 * @param org 组织
	 * @param id  唯一id
	 * @return
	 */
	TracerSpanTree getTracer(Organization org, String id);

	/**
	 * 获取api调用情况
	 * @param org 组织
	 * @param begin
	 * @param end
	 * @param serviceName
	 * @param serviceNo
	 * @param methodName
	 * @return
	 */
	ApiInvokeInfo getApiInvokeInfo(Organization org,Date begin, Date end, String serviceName,
								   String serviceNo,String methodName);

}
