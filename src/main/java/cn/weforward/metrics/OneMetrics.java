package cn.weforward.metrics;

import java.util.Date;

/**
 * 单个指标
 * 
 * @author daibo
 *
 */
public interface OneMetrics {
	/**
	 * 时间
	 * 
	 * @return
	 */
	Date getTime();

	/**
	 * 值
	 * 
	 * @return
	 */
	double getValue();
}
