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

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.weforward.common.sys.GcCleaner;
import cn.weforward.common.util.LruCache;
import cn.weforward.common.util.LruCache.CacheNode;
import cn.weforward.common.util.NumberUtil;
import cn.weforward.common.util.StringUtil;
import cn.weforward.rlog.LogPage;

/**
 * 日志显示分页结果集
 * 
 * @author bo
 *
 */
public class RlogResultImpl implements LogPage {
	/** 日志 */
	private static final Logger _Logger = LoggerFactory.getLogger(RlogResultImpl.class);
	/** 字符集 */
	static Charset CHARSET;
	/** 缓存大小 */
	private static final int BUFFER_LENGTH;
	/** 最大偏移量 */
	private static final long MAX_OFFSET;

	static {
		String l = System.getProperty("cn.weforward.rlog.LogResultPage.BufferLength");
		if (StringUtil.isEmpty(l)) {
			BUFFER_LENGTH = 1024 * 10;
		} else {
			BUFFER_LENGTH = NumberUtil.toInt(l, 2014 * 10);
		}
		String c = System.getProperty("cn.weforward.rlog.LogResultPage.Charset");
		if (StringUtil.isEmpty(c)) {
			CHARSET = Charset.forName("UTF-8");
		} else {
			CHARSET = Charset.forName(c);
		}
		MAX_OFFSET = Long.MAX_VALUE / BUFFER_LENGTH;
	}
	/** 日志 */
	RandomAccessFile log;
	/** 日志名 */
	String logname;

	protected RlogResultImpl(String logname) throws IOException {
		this.logname = logname;
		log = new RandomAccessFile(logname, "r");
	}

	public String getLogname() {
		return logname;
	}

	private long calCount() {
		long length;
		try {
			length = log.length();
		} catch (IOException e) {
			throw new RuntimeException("读取长度出错", e);
		}
		long l = length / BUFFER_LENGTH;
		if ((l * BUFFER_LENGTH) < length) {
			l++;
		}
		return l;
	}

	public long getPageCount() {
		return calCount();
	}

	@Override
	public boolean gotoPage(long i) {
		return i > 0 && i <= getPageCount();
	}

	public String get(long idx) {
		long offset = idx - 1l;
		if (offset > MAX_OFFSET) {
			throw new UnsupportedOperationException("文件过大，无法查看");
		}
		long position = offset * BUFFER_LENGTH;
		try {
			FileChannel channel = log.getChannel();
			ByteBuffer dst = ByteBuffer.allocate(BUFFER_LENGTH);
			int v = channel.read(dst, position);
			if (v <= 0) {
				return null;
			}
			dst.flip();
			byte[] bs = new byte[v];
			dst = dst.get(bs);
			return new String(bs, CHARSET);
		} catch (IOException e) {
			throw new RuntimeException("读取数据出错", e);
		}
	}

	public void close() {
		if (null != log) {
			try {
				log.close();
			} catch (IOException e) {
				_Logger.warn("忽略关闭出错", e);
			}
		}
	}

	/**
	 * 转换成字符串
	 * 
	 * @param bs
	 * @return
	 */
	public static String toString(byte[] bs) {
		return new String(bs, CHARSET);
	}

	/**
	 * 是否包含关键字
	 * 
	 * @param bs
	 * @param k
	 * @return
	 */
	public static boolean contains(byte[] bs, String k) {
		if (StringUtil.isEmpty(k)) {
			return true;
		}
		if (null == bs || bs.length == 0) {
			return false;
		}
		return toString(bs).contains(k);
	}

	/* 缓存数据 */
	private static LruCache<String, RlogResultImpl> CACHE = new LruCache<>("RlogResultImplpage");
	/* 加载值 */
	private static final LruCache.Loader<String, RlogResultImpl> LOADER = new LruCache.Loader<String, RlogResultImpl>() {

		@Override
		public RlogResultImpl load(String key, CacheNode<String, RlogResultImpl> node) {
			RlogResultImpl result;
			try {
				result = new RlogResultImpl(key);
			} catch (IOException e) {
				throw new RuntimeException("初始化数据出错", e);
			}
			return result;
		}
	};

	static {
		GcCleaner.register(CACHE);
	}

	/**
	 * 构造值
	 * 
	 * @param date
	 * @return
	 * @throws IOException
	 */
	public static RlogResultImpl valueOf(String ln) throws IOException {
		return CACHE.getAndLoad(ln, LOADER, 10);
	}

	@Override
	public void finalize() {
		if (null != log) {
			try {
				_Logger.info("缓存过期关闭" + log);
				log.close();
			} catch (IOException e) {
				_Logger.warn("忽略关闭出错", e);
			}
		}
	}

}
