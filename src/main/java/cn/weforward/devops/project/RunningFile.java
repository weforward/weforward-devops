package cn.weforward.devops.project;

import java.util.Date;

/**
 * 实例文件
 * 
 * @author daibo
 *
 */
public interface RunningFile {
	/**
	 * 文件名
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 文件大小
	 * 
	 * @return
	 */
	long length();

	/**
	 * 创建时间
	 * 
	 * @return
	 */
	Date getCreateTime();
}
