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

import cn.weforward.common.crypto.Base64;
import cn.weforward.common.crypto.Hex;
import cn.weforward.devops.user.UserAccess;
import cn.weforward.framework.ApiException;

/**
 * 简单的凭证实现
 * 
 * @author daibo
 *
 */
public class SimpleUserAccess implements UserAccess {
	/** 凭证前缀 */
	private static final String ACCESS_PREFIX = cn.weforward.protocol.Access.KIND_USER
			+ cn.weforward.protocol.Access.SPEARATOR_STR;
	/** 会话id */
	protected String m_SessionId;
	/** 凭证id */
	protected String m_AccessId;
	/** 凭证key */
	protected byte[] m_AccessKey;
	/** 凭证有效期 */
	protected long m_Expire;
	/** 会话 */
	private SimpleSession m_Session;

	public SimpleUserAccess() {
	}

	public SimpleUserAccess(SimpleSession session, byte[] secretKey) throws ApiException {
		long expire = session.getExpireTime().getTime();
		if (expire <= System.currentTimeMillis()) {
			throw new ApiException(ApiException.CODE_ILLEGAL_ARGUMENT, "会话无效或已过期");
		}
		m_SessionId = session.getId();
		m_AccessId = genAccessId(session.getId(), expire);
		m_AccessKey = genAccessKey(m_AccessId, secretKey);
		m_Expire = expire;
		m_Session = session;
	}

	/**
	 * 根据生成session生成accessId
	 * 
	 * @param sessionId
	 * @param expire    access的过期时间
	 */
	private static String genAccessId(String sessionId, long expire) {
		if (null == sessionId || 0 == sessionId.length()) {
			throw new IllegalArgumentException("'sessionId'、'expire'不能为空");
		}
		if (expire <= 0 || expire >= 1L << 48) {
			throw new IllegalArgumentException("'expire'应大于0，小于2^48");
		}
		StringBuilder sb = new StringBuilder(cn.weforward.protocol.Access.KIND_USER.length() + sessionId.length() + 14);
		sb.append(cn.weforward.protocol.Access.KIND_USER);
		sb.append(cn.weforward.protocol.Access.SPEARATOR);
		sb.append(sessionId);
		sb.append(cn.weforward.protocol.Access.SPEARATOR);
		Hex.toHexFixed((short) (expire >> 32 & 0xFFFF), sb);
		Hex.toHexFixed((int) (expire & 0xFFFFFFFF), sb);
		return sb.toString();
	}

	/**
	 * 生成accessId对应的密钥
	 * 
	 * @param accessId
	 * @param masterKey 128/192/256位的主密钥
	 * @return
	 */
	private static byte[] genAccessKey(String accessId, byte[] secretKey) {
		try {
			java.security.MessageDigest md;
			md = java.security.MessageDigest.getInstance("SHA-256");
			md.update(accessId.getBytes("utf-8"));
			md.update(secretKey);
			return md.digest();
		} catch (Exception e) {
			// 应该不会发生
			throw new RuntimeException("生成AccessKey失败", e);
		}
	}

	/**
	 * 从accessId中获取sessionId
	 * 
	 * @param accessId
	 * @return 失败时，返回null
	 */
	public static String getSessionId(String accessId) {
		if (null == accessId || accessId.length() < 4 || !accessId.startsWith(ACCESS_PREFIX)) {
			return null;
		}
		int offset = 3;
		int pos = accessId.indexOf(cn.weforward.protocol.Access.SPEARATOR, offset);
		if (-1 == pos) {
			return null;
		}
		if (12 == (pos - offset)) {
			// 旧格式
			offset = pos + 1;
			pos = accessId.length();
		}
		return accessId.substring(offset, pos);
	}

	public void setSessionId(String id) {
		m_SessionId = id;
	}

	public String getSessionId() {
		return m_SessionId;
	}

	public void setAccessId(String id) {
		m_AccessId = id;
	}

	public String getAccessId() {
		return m_AccessId;
	}

	public void setAccessKey(byte[] hex) {
		m_AccessKey = hex;
	}

	public byte[] getAccessKey() {
		return m_AccessKey;
	}

	public String getAccessKeyHex() {
		return Hex.encode(getAccessKey());
	}

	public String getAccessKeyBase64() {
		return Base64.encode(getAccessKey());
	}

	public void setAccessExpire(long v) {
		m_Expire = v;
	}

	public long getAccessExpire() {
		return m_Expire;
	}

	protected SimpleSession getSession() {
		return m_Session;
	}

}
