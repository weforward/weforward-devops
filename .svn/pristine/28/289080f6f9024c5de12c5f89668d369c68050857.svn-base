package cn.weforward.util;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.weforward.common.util.TimeUtil;

/**
 * 文件清理
 * 
 * @author daibo
 *
 */
public class FileClear implements Runnable {
	/** 日志 */
	protected final Logger _Logger = LoggerFactory.getLogger(FileClear.class);
	/** 清理线程 */
	protected Thread m_Clears;

	final String m_Name;

	final File m_Path;

	final long m_MaxHistoryMs;

	public FileClear(String path, String name, long maxHistory) {
		m_Path = new File(path);
		m_Name = name;
		m_MaxHistoryMs = maxHistory * TimeUtil.DAY_MILLS;
		start();
	}

	public synchronized void start() {
		if (null != m_Clears) {
			return;
		}
		m_Clears = new Thread(this, m_Name);
		m_Clears.setDaemon(true);
		m_Clears.start();
	}

	public synchronized void stop() {
		m_Clears = null;
		synchronized (this) {
			this.notifyAll();
		}
	}

	@Override
	public void run() {
		while (null != m_Clears) {
			synchronized (this) {
				try {
					this.wait(TimeUtil.DAY_MILLS);
				} catch (InterruptedException e) {
					return;
				}
			}
			if (!m_Path.exists()) {
				continue;
			}
			for (File f : m_Path.listFiles()) {
				clearIfNeed(f);
			}
		}

	}

	private void clearIfNeed(File file) {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				clearIfNeed(f);
			}
		} else {
			long offset = System.currentTimeMillis() - file.lastModified();
			if (offset > m_MaxHistoryMs) {
				String result = file.delete() ? "成功" : "失败";
				if (_Logger.isTraceEnabled()) {
					_Logger.trace("清理" + file.getName() + result);
				}
			}
		}

	}
}
