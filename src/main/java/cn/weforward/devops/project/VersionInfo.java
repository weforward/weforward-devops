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
package cn.weforward.devops.project;

import java.util.Date;

import cn.weforward.common.util.TimeUtil;

/**
 * 版本信息
 * 
 * @author daibo
 *
 */
public class VersionInfo {
	/** 版本 */
	private String m_Version;
	/** 时间 */
	private Date m_Time;

	public VersionInfo(String v) {
		m_Version = v;
	}

	public VersionInfo(String v, Date time) {
		m_Version = v;
		m_Time = time;
	}

	/**
	 * 版本
	 * 
	 * @return
	 */
	public String getVersion() {
		return m_Version;
	}

	/**
	 * 时间
	 * 
	 * @return
	 */
	public Date getTime() {
		return m_Time;
	}

	/**
	 * 获取版本信息
	 * 
	 * @param v
	 * @return
	 */
	public static String getVersion(String v) {
		int index = v.indexOf('(');
		if (index > 0) {
			return v.substring(0, index);
		} else {
			return v;
		}
	}

	/**
	 * 获取版本信息
	 * 
	 * @param v
	 * @return
	 */
	public static Date getTime(String v) {
		int index = v.indexOf('(');
		if (index > 0) {
			return TimeUtil.parseDate(v.substring(index + 1, v.length() - 1));
		} else {
			return new Date();
		}
	}

	@Override
	public String toString() {
		return null == m_Time ? m_Version : m_Version + "(" + TimeUtil.formatDateTime(m_Time) + ")";
	}
}
