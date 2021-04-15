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
package cn.weforward.devops.weforward.view;

import cn.weforward.common.util.TimeUtil;
import cn.weforward.data.log.BusinessLog;
import cn.weforward.protocol.doc.annotation.DocAttribute;

/**
 * 日志视图
 * 
 * @author daibo
 *
 */
public class LogView {
	/** 日志 */
	protected BusinessLog m_Log;

	/**
	 * 构造视图
	 * 
	 * @param log
	 * @return
	 */
	public static LogView valueOf(BusinessLog log) {
		return null == log ? null : new LogView(log);
	}

	/**
	 * 构造
	 * 
	 * @param log
	 */
	public LogView(BusinessLog log) {
		m_Log = log;
	}

	/**
	 * 作者
	 * 
	 * @return
	 */
	@DocAttribute(description = "作者")
	public String getAuthor() {
		return m_Log.getAuthor();
	}

	/**
	 * 行为
	 * 
	 * @return
	 */
	@DocAttribute(description = "行为")
	public String getAction() {
		return m_Log.getAction();
	}

	/**
	 * 什么
	 * 
	 * @return
	 */
	@DocAttribute(description = "做了什么")
	public String getWhat() {
		return m_Log.getWhat();
	}

	/**
	 * 备注
	 * 
	 * @return
	 */
	@DocAttribute(description = "备注")
	public String getNote() {
		return m_Log.getNote();
	}

	/**
	 * 时间
	 * 
	 * @return
	 */
	@DocAttribute(description = "时间，格式为：yyyy-MM-dd HH:mm:ss")
	public String getTime() {
		return TimeUtil.formatDateTime(m_Log.getTime());
	}
}
