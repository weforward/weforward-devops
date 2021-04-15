package cn.weforward.devops.weforward.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.weforward.common.GcCleanable;
import cn.weforward.common.sys.ClockTick;
import cn.weforward.common.sys.GcCleaner;

/**
 * 统计点击进行限制，主要用于预防在设定时间内的重复点击
 * 
 * @author liangyi
 * 
 */
public class LimitHits implements GcCleanable {
	/** 秒表 */
	static protected ClockTick _Tick = ClockTick.getInstance(1);

	/** 点击项 */
	protected HashMap<String, Hit> m_Elements;
	/** 累计时间间隔（秒），若为0不检查时间间隔 */
	protected int m_MaxInterval;
	/** 最后执行GC时间 */
	protected long m_LastGC;

	/**
	 * 创建点击项（供子类覆盖）
	 * 
	 * @param token 点击标记
	 */
	protected Hit createHit(String token) {
		return new Hit(token);
	}

	/**
	 * 构造
	 * 
	 * @param intervalSeconds 点击过期时间(秒)
	 */
	public LimitHits(int intervalSeconds) {
		if (intervalSeconds < 0 || intervalSeconds > 0x7FFFFFFE) {
			throw new IllegalArgumentException("intervalSeconds is over between 0 an 0x7ffffffe");
		}
		m_MaxInterval = intervalSeconds;
		m_Elements = new HashMap<String, Hit>();
		GcCleaner.register(this);
	}

	/**
	 * 点击时间间隔
	 * 
	 * @param seconds 间隔（秒）
	 */
	public void setMaxInterval(int seconds) {
		m_MaxInterval = seconds;
	}

	public int getMaxInterval() {
		return m_MaxInterval;
	}

	/**
	 * 若计数小于指定值则计数加1
	 * 
	 * @param id    点击项ID
	 * @param count 若=0时，计数不会增加（若项未有则返回true）
	 * @return 若此次点击有效则返回true，否则false
	 */
	public boolean hitIfLess(String id, int count) {
		// synchronized (m_Elements) {
		// Hit e = m_Elements.get(id);
		// if (null == e) {
		// // 还没有此项
		// if (0 == count) {
		// // 参数count=0，直接返回true
		// return true;
		// }
		// // 创建新项
		// e = createHit();
		// m_Elements.put(id, e);
		// }
		// // 若hits小于count
		// if (e.hits < count) {
		// ++e.hits;
		// e.ticks = _Tick.getTicker();// System.currentTimeMillis();
		// return true;
		// }
		// // 若最后hit的时间超过间隔期，重置hits及最后时间
		// if (0 != m_MaxInterval && (e.ticks + m_MaxInterval) <
		// _Tick.getTicker()) {
		// e.hits = 1;
		// e.ticks = _Tick.getTicker(); // System.currentTimeMillis();
		// return true;
		// }
		// }
		// return false;
		return hitIfLess(id, count, null);
	}

	/**
	 * 若计数小于指定值则计数加1
	 * 
	 * @param id    点击项ID
	 * @param count 若=0时，计数不会增加（若项未有则返回true）
	 * @param token 点击标记
	 * @return 若此次点击有效则返回true，否则false
	 */
	public boolean hitIfLess(String id, int count, String token) {
		synchronized (m_Elements) {
			Hit e = m_Elements.get(id);
			if (null == e) {
				// 还没有此项
				if (0 == count) {
					return true;
				}
				// 创建新项
				e = createHit(token);
				// e.token = token;
				// e.hits = 1;
				// e.ticks = _Tick.getTicker();
				m_Elements.put(id, e);
				return true;
			}
			if (e.hits < count) {
				// hits还小于count，允许
				// ++e.hits;
				// e.ticks = _Tick.getTicker();
				// e.token = token;
				e.hit(token);
				return true;
			}

			if (isTimeout(e)) {
				// 若最后hit的时间超过间隔期，允许且重置hits及最后时间
				// e.hits = 1;
				// e.ticks = _Tick.getTicker();
				// e.token = token;
				e.reset(token);
				return true;
			}
		}
		// 在指定的次数内且未超过间隔期有太多次的hit
		return false;
	}

	/**
	 * 取点击项（必须是最后一次点击未超时的项）
	 * 
	 * @param id 点击项ID
	 * @return 返回相应的点击项
	 */
	public Hit get(String id) {
		synchronized (m_Elements) {
			Hit e = m_Elements.get(id);
			if (!isTimeout(e)) {
				return e;
			}
			// 顺便删除此项
			m_Elements.remove(id);
		}
		return null;
	}

	/**
	 * 提取点击项（必须是最后一次点击未超时的项），提取后该项将删除
	 * 
	 * @param id 点击项ID
	 * @return 返回相应的点击项
	 */
	public Hit take(String id) {
		synchronized (m_Elements) {
			Hit e = m_Elements.remove(id);
			if (!isTimeout(e)) {
				return e;
			}
		}
		return null;
	}

	/**
	 * 删除点击项
	 * 
	 * @param id 点击项ID
	 * @return 返回相应的点击项
	 */
	public Hit remove(String id) {
		synchronized (m_Elements) {
			Hit e = m_Elements.remove(id);
			return e;
		}
	}

	@Override
	public void onGcCleanup(int policy) {
		// 不会有过期项
		if (0 == m_MaxInterval) {
			return;
		}
		synchronized (m_Elements) {
			long now = System.currentTimeMillis();
			if (m_LastGC + (30 * 60 * 1000) > now) {
				// 至多每30分钟执行一次
				return;
			}

			// int oldSize = m_Elements.size();
			Iterator<Map.Entry<String, Hit>> it = m_Elements.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Hit> e = it.next();
				if (isTimeout(e.getValue())) {
					// 若最后hit的时间超过间隔期，删除此项
					it.remove();
				}
			}
			m_LastGC = System.currentTimeMillis();
			// Misc.getLogger().info("cleanup cache element: " +
			// m_Elements.size() + "/" + oldSize);
		}
	}

	/**
	 * 点击项是否超时
	 * 
	 * @param hit 点击项
	 * @return 是超时则返回true
	 */
	protected boolean isTimeout(Hit hit) {
		return (null != hit && 0 != m_MaxInterval && _Tick.getTicker() > (hit.ticks + m_MaxInterval));
	}

	/**
	 * 点击项
	 * 
	 * @author liangyi
	 * 
	 */
	public static class Hit {
		/** 创建的钟点 */
		protected int ticks;
		/** 点击数 */
		protected int hits;
		/** 有效点击时的标记 */
		protected String token;

		public Hit(String token) {
			this.token = token;
			this.hits = 1;
			this.ticks = _Tick.getTicker();
		}

		/**
		 * 点击（线程不安全）
		 * 
		 * @param token 标记
		 * @return 点击数
		 */
		public int hit(String token) {
			// FIXME 若需要线程安全可考虑用CAS，不建议用锁
			++this.hits;
			this.ticks = _Tick.getTicker();
			this.token = token;
			return this.hits;
		}

		/**
		 * 重罝点击（线程不安全）
		 * 
		 * @param token 标记
		 */
		public void reset(String token) {
			this.hits = 1;
			this.ticks = _Tick.getTicker();
			this.token = token;
		}

		public String getToken() {
			return token;
		}

		public int getHits() {
			return hits;
		}

		public int getTicks() {
			return ticks;
		}

		/**
		 * 是否已过期
		 * 
		 * @param expires 有效期（秒）
		 * @return 过期则返回true
		 */
		public boolean isTimeout(int expires) {
			return (_Tick.getTicker() > ticks + expires);
		}

		/**
		 * 取有效时间（秒）
		 * 
		 * @param expires
		 * @return
		 */
		public int getExpiresIn(int expires) {
			return ((ticks + expires) - _Tick.getTicker());
		}
	}
}
