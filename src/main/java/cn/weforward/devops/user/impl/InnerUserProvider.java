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

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.weforward.common.ResultPage;
import cn.weforward.common.crypto.Hex;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.devops.user.OrganizationProvider;
import cn.weforward.devops.user.RoleProvider;
import cn.weforward.devops.user.UserAccess;
import cn.weforward.devops.user.UserProvider;
import cn.weforward.framework.ApiException;
import cn.weforward.protocol.Access;
import cn.weforward.protocol.AccessLoader;
import cn.weforward.protocol.ops.User;
import cn.weforward.util.UserAuth;

/**
 * 内置用户实现
 * 
 * @author daibo
 *
 */
public class InnerUserProvider implements UserProvider, UserAuth, AccessLoader {
	/** 会话缓存 */
	private Map<String, SimpleUserAccess> m_Access;
	/** 超级管理员 */
	private SimpleOrganizationUser m_Sa;
	/** 用于生成AccessKey的密钥。 */
	private byte[] m_SecretKey;

	public InnerUserProvider(String id, String name, String password, String secretKey, RoleProvider roleProvider,
			OrganizationProvider organizationProvider) {
		m_Access = new ConcurrentHashMap<String, SimpleUserAccess>();
		m_Sa = new SimpleOrganizationUser(id, name);
		m_Sa.setOrganizationProvider(organizationProvider);
		m_Sa.setRoleProvider(roleProvider);
		m_Sa.setPassword(password);
		m_Sa.setInner(true);
		setSecretKeyHex(secretKey);
	}

	public void setSecretKeyHex(String key) {
		if (StringUtil.isEmpty(key)) {
			m_SecretKey = null;
			return;
		}
		if (key.length() != 64) {
			try {
				m_SecretKey = Access.Helper.secretToAccessKey(key);
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException("算法异常", e);
			}
		} else {
			m_SecretKey = Hex.decode(key);
		}

	}

	public byte[] getSecretKey() {
		return m_SecretKey;
	}

	@Override
	public UserAccess login(String userName, String password) throws ApiException {
		byte[] masterKey = getSecretKey();
		if (null == masterKey) {
			throw new ApiException(ApiException.CODE_INTERNAL_ERROR, "未设置weforward.user.secretKey");
		}
		if (StringUtil.eq(m_Sa.getName(), userName) && m_Sa.checkPassword(password)) {
			SimpleSession session = new SimpleSession(m_Sa);
			SimpleUserAccess access = openAccess(session);
			m_Access.put(access.getAccessId(), access);
			return access;
		} else {
			throw new ApiException(LOGIN_FAILED.code, "帐号或密码错误");
		}

	}

	@Override
	public void logout(String accessId) {
		m_Access.remove(accessId);
	}

	@Override
	public User getUser(String id) {
		if (StringUtil.eq(id, m_Sa.getId())) {
			return m_Sa;
		}
		return null;
	}

	@Override
	public User getUserByAccess(String accessId) {
		SimpleUserAccess access = m_Access.get(accessId);
		if (null == access) {
			return null;
		}
		SimpleSession session = access.getSession();
		if (null == session) {
			return null;
		}
		if (System.currentTimeMillis() > session.getExpireTime().getTime()) {
			m_Access.remove(accessId);
			return null;
		}
		return session.getUser();
	}

	@Override
	public UserAccess refreshAccess(String accessId, String accessKey) {
		SimpleUserAccess access = m_Access.get(accessId);
		if (null == access) {
			return null;
		}
		if ((!StringUtil.eq(accessKey, access.getAccessKeyBase64())
				&& !StringUtil.eq(accessKey, access.getAccessKeyHex()))) {
			return null;
		}
		SimpleSession session = access.getSession();
		if (null == session) {
			return null;
		}
		if (System.currentTimeMillis() > session.getExpireTime().getTime()) {
			m_Access.remove(accessId);
			return null;
		}
		session.refresh();
		return access;
	}

	@Override
	public ResultPage<User> searchUser(String keywords) {
		return ResultPageHelper.singleton((User) m_Sa);
	}

	@Override
	public User checkPassword(String userName, String password) {
		if (StringUtil.eq(m_Sa.getName(), userName) && m_Sa.checkPassword(password)) {
			return m_Sa;
		}
		return null;
	}

	// @Override
	public User checkAccess(String accessId, String accessKey) {
		SimpleUserAccess access = m_Access.get(accessId);
		if (null == access) {
			return null;
		}
		if ((!StringUtil.eq(accessKey, access.getAccessKeyBase64())
				&& !StringUtil.eq(accessKey, access.getAccessKeyHex()))) {
			return null;
		}
		SimpleSession session = access.getSession();
		if (null == session) {
			return null;
		}
		if (System.currentTimeMillis() > session.getExpireTime().getTime()) {
			m_Access.remove(accessId);
			return null;
		}
		return session.getUser();
	}

	/* 开始凭证对象 */
	private SimpleUserAccess openAccess(SimpleSession session) throws ApiException {
		return new SimpleUserAccess(session, m_SecretKey);
	}

	@Override
	public cn.weforward.protocol.Access getValidAccess(String accessId) {
		SimpleUserAccess access = m_Access.get(accessId);
		if (null == access) {
			return null;
		}
		cn.weforward.protocol.support.SimpleAccess a = new cn.weforward.protocol.support.SimpleAccess();
		a.setAccessId(access.getAccessId());
		a.setAccessKey(access.getAccessKey());
		a.setValid(true);
		return a;
	}

}
