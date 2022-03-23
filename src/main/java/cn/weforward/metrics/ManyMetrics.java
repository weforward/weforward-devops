package cn.weforward.metrics;

import java.util.Date;
import java.util.List;

/**
 * 多个指标
 * 
 * @author daibo
 *
 */
public interface ManyMetrics {
	/**
	 * 开始时间
	 * 
	 * @return
	 */
	Date getBegin();

	/**
	 * 结束时间
	 * 
	 * @return
	 */
	Date getEnd();

	/**
	 * 指标项目
	 * 
	 * @return
	 */
	List<OneMetrics> getItems();
}
