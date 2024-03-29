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

import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.weforward.devops.user.Organization;
import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Meter.Id;
import io.micrometer.core.instrument.Statistic;
import io.micrometer.core.instrument.Tags;

/**
 * 指标收集
 * 
 * @author daibo
 *
 */
public interface MetricsCollector {
	/** 空实现 */
	MetricsCollector EMPTY = new MetricsCollector() {

		@Override
		public void collect(String org, Id id, Iterable<Measurement> measure) {

		}

		@Override
		public OneMetrics getLately(Organization org, Id id, Statistic statistic) {
			return null;
		}

		@Override
		public ManyMetrics search(Organization org, Id id, String name, Tags exclude, Date begin, Date end,
				int interval) {
			return null;
		}

		@Override
		public List<String> showTags(Organization org, Id id) {
			return Collections.emptyList();
		}

		@Override
		public void clear(int maxHistory) {

		}

	};

	/**
	 * 收集
	 * 
	 * @param org
	 * @param id
	 * @param measure
	 */
	void collect(String org, Meter.Id id, Iterable<Measurement> measure);

	/**
	 * 获取最近一个指标(5分钟内)
	 * 
	 * @param org
	 * @param id
	 * @return
	 */
	OneMetrics getLately(Organization org, Meter.Id id, Statistic statistic);

	/**
	 * 搜索指标
	 * 
	 * @param org
	 * @param id
	 * @param statistic
	 * @param exclude
	 * @param begin
	 * @param end
	 * @return
	 */
	ManyMetrics search(Organization org, Meter.Id id, String name, Tags exclude, Date begin, Date end, int interval);

	/**
	 * 显示标签
	 * 
	 * @param org
	 * @param id
	 * @return
	 */
	List<String> showTags(Organization org, Meter.Id id);

	/**
	 * 清理数据
	 * 
	 * @param maxHistory 最大要保存的天数
	 */
	void clear(int maxHistory);

}
