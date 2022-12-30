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
package cn.weforward.util;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 文件清理
 * 
 * @author daibo
 *
 */
public class FileClearOnNum extends FileClear implements Runnable {

	final long m_MaxHistoryNum;

	public FileClearOnNum(String path, String name, long maxHistory) {
		super(path, name);
		m_MaxHistoryNum = maxHistory;
	}

	protected void clearIfNeed(File file, AtomicInteger count) {
		long num = m_MaxHistoryNum;
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (null == files) {
				return;
			}
			if (files.length > num) {
				for (int i = 0; i < files.length - num; i++) {
					String result = files[i].delete() ? "成功" : "失败";
					if (_Logger.isTraceEnabled()) {
						_Logger.trace("清理" + file.getName() + result);
					}
				}
			}

		}

	}
}
