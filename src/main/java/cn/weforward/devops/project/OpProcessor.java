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

import javax.annotation.Resource;

/**
 * 升级进度条
 * 
 * @author daibo
 *
 */
public interface OpProcessor {

	/**
	 * 状态处理
	 * 
	 * @param status
	 */
	void progesser(OpProcessor.Status status);

	/**
	 * 状态
	 * 
	 * @author daibo
	 *
	 */
	class Status {
		@Resource
		protected String m_Id;
		@Resource
		protected String m_Desc;
		@Resource
		protected Date m_Time;

		public Status() {

		}

		public Status(String desc) {
			this(null, desc);
		}

		public Status(String id, String desc) {
			m_Id = id;
			m_Desc = desc;
			m_Time = new Date();
		}

		public String getId() {
			return m_Id;
		}

		public String getDesc() {
			return m_Desc;
		}

		public Date getTime() {
			return m_Time;
		}

	}
}
