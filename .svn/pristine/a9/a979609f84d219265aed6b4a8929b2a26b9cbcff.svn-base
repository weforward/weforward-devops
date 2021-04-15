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
package cn.weforward.util.docker;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * docker日志
 * 
 * @author daibo
 *
 */
public class DockerLog {

	protected String m_Type;

	protected ZonedDateTime m_Time;

	protected String m_Content;

	public DockerLog(String type, ZonedDateTime time, String content) {
		m_Type = type;
		m_Time = time;
		m_Content = content;
	}

	public String getType() {
		return m_Type;
	}

	public ZonedDateTime getTime() {
		return m_Time;
	}

	public String getContent() {
		return m_Content;
	}

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM-dd HH:mm:ss");

	public String toString() {
		return (null == m_Time ? "" : FORMATTER.format(m_Time)) + "[" + getType() + "]" + getContent();
	}
}
