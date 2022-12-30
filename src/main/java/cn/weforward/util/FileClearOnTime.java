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

import cn.weforward.common.util.TimeUtil;

/**
 * 文件清理
 * 
 * @author daibo
 *
 */
public class FileClearOnTime extends FileClear implements Runnable {

	final long m_MaxHistoryMs;

	public FileClearOnTime(String path, String name, long maxHistory) {
		super(path, name);
		m_MaxHistoryMs = maxHistory * TimeUtil.DAY_MILLS;
	}

	protected void clearIfNeed(File file, AtomicInteger count) {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				clearIfNeed(f, count);
			}
			if (null == file.listFiles() || file.listFiles().length == 0) {
				file.delete();// 目录也清除了
			}
		} else {
			long offset = System.currentTimeMillis() - file.lastModified();
			if (offset > m_MaxHistoryMs) {
				String result = file.delete() ? "成功" : "失败";
				if (_Logger.isTraceEnabled()) {
					_Logger.trace("清理" + file.getName() + result);
				}
				count.incrementAndGet();
			}
		}

	}
}
