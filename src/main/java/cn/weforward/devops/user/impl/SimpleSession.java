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
package cn.weforward.devops.user.impl;

import java.util.Date;

import cn.weforward.common.crypto.Hasher;
import cn.weforward.common.crypto.Hex;
import cn.weforward.common.sys.Timestamp;
import cn.weforward.common.util.TimeUtil;
import cn.weforward.protocol.ops.User;

/**
 * 会话
 * 
 * @author daibo
 *
 */
public class SimpleSession {
	/** 关联用户 */
	protected User m_User;
	/** 唯一id */
	protected String m_Id;
	/** 过期日期 */
	protected Date m_Expire;
	/** 时间戳生成器 */
	protected final static Timestamp _Timestamp = Timestamp.getInstance(Timestamp.POLICY_DEFAULT);

	public SimpleSession(User user) {
		m_User = user;
		m_Id = genSessionId(user.getName());
		refresh();
	}

	/**
	 * 生成会话ID，由hast32及时间戳组成，共8+16=24个字符
	 * 
	 * @param name 名称/登录名
	 * @return 会话ID串
	 */
	protected String genSessionId(String name) {
		long t = _Timestamp.next(name.hashCode() % 255);
		int hash = Hasher.hashInt32(name, (int) (0x1516FFFF & Timestamp.getTime(t)));
		StringBuilder sb = new StringBuilder(24);
		Hex.toHexFixed(hash, sb);
		Hex.toHexFixed(t, sb);
		return sb.toString();
	}

	public String getId() {
		return m_Id;
	}

	public Date getExpireTime() {
		return m_Expire;
	}

	public User getUser() {
		return m_User;
	}

	public void refresh() {
		m_Expire = new Date(System.currentTimeMillis() + TimeUtil.DAY_MILLS);
	}

}
