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
package cn.weforward.rlog.impl;

import java.util.Date;

import javax.annotation.Resource;

import cn.weforward.data.array.LabelElement;

/***
 * 远程日志主题
 * 
 * @author daibo
 *
 */
public class RemoteLogSubjectElement implements LabelElement {

	@Resource
	protected String m_Timestamp;

	@Resource
	protected String m_Subject;

	@Resource
	protected long m_Offset;

	@Resource
	protected int m_Length;

	@Resource
	protected Date m_CreateTime;

	protected RemoteLogSubjectElement() {

	}

	public RemoteLogSubjectElement(String timestamp, String subject, long offset, int length) {
		m_Timestamp = timestamp;
		m_Subject = subject;
		m_Offset = offset;
		m_Length = length;
		m_CreateTime = new Date();
	}

	public String getId() {
		return m_Timestamp;
	}

	public long getOffset() {
		return m_Offset;
	}

	public int getLength() {
		return m_Length;
	}

	public String getName() {
		return getSubject();
	}

	public String getSubject() {
		return m_Subject;
	}

	public Date getCreateTime() {
		return m_CreateTime;
	}

	@Override
	public String getIdForLabel() {
		return m_Timestamp;
	}

	/**
	 * 生成标签
	 * 
	 * @param server
	 * @param directory
	 * @return
	 */
	public static String genLabel(String org, String server, String directory) {
		return org + "-" + server + "-" + directory;
	}

}
