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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.UnionResultPage;
import cn.weforward.devops.user.UserAccess;
import cn.weforward.devops.user.UserProvider;
import cn.weforward.framework.ApiException;
import cn.weforward.protocol.Access;
import cn.weforward.protocol.AccessLoader;
import cn.weforward.protocol.ops.User;
import cn.weforward.util.UserAuth;

/**
 * 多个用户供应商合并
 * 
 * @author daibo
 *
 */
public class MultipleUserProvider implements UserProvider, UserAuth, AccessLoader {

	protected List<UserProvider> m_Providers;

	public MultipleUserProvider(UserProvider... providers) {
		this(Arrays.asList(providers));
	}

	public MultipleUserProvider(List<UserProvider> providers) {
		m_Providers = providers;
	}

	@Override
	public User getUser(String id) {
		for (UserProvider p : m_Providers) {
			User user = p.getUser(id);
			if (null != user) {
				return user;
			}
		}
		return null;
	}

	@Override
	public User getUserByAccess(String accessId) {
		for (UserProvider p : m_Providers) {
			User user = p.getUserByAccess(accessId);
			if (null != user) {
				return user;
			}
		}
		return null;
	}

	@Override
	public UserAccess login(String userName, String password) throws ApiException {
		Throwable error = null;
		for (UserProvider p : m_Providers) {
			try {
				return p.login(userName, password);
			} catch (ApiException e) {
				_Logger.warn("登陆异常", e);
				error = e;
			}
		}
		if (null == error) {
			throw new NullPointerException("没有合适的用户供应商");
		}
		if (error instanceof ApiException) {
			throw (ApiException) error;
		} else if (error instanceof RuntimeException) {
			throw (RuntimeException) error;
		} else {
			throw new RuntimeException(error);
		}
	}

	@Override
	public void logout(String accessId) {
		RuntimeException error = null;
		for (UserProvider p : m_Providers) {
			try {
				p.logout(accessId);
			} catch (RuntimeException e) {
				error = e;
			}
		}
		if (null == error) {
			throw new NullPointerException("没有合适的用户供应商");
		}
		throw error;
	}

	@Override
	public ResultPage<User> searchUser(String keywords) {
		List<ResultPage<User>> pages = new ArrayList<>();
		for (UserProvider p : m_Providers) {
			pages.add(p.searchUser(keywords));
		}
		return UnionResultPage.union(pages, null);
	}

	@Override
	public User check(String userName, String password) {
		for (UserProvider p : m_Providers) {
			if (p instanceof UserAuth) {
				User user = ((UserAuth) p).check(userName, password);
				if (null != user) {
					return user;
				}
			}
		}
		return null;
	}

	@Override
	public Access getValidAccess(String accessId) {
		for (UserProvider p : m_Providers) {
			if (p instanceof AccessLoader) {
				Access access = ((AccessLoader) p).getValidAccess(accessId);
				if (null != access) {
					return access;
				}
			}
		}
		return null;
	}

	@Override
	public UserAccess refreshAccess(String accessId, String accessKey) throws ApiException {
		Throwable error = null;
		for (UserProvider p : m_Providers) {
			try {
				return p.refreshAccess(accessId, accessKey);
			} catch (ApiException e) {
				_Logger.warn("刷新异常", e);
				error = e;
			}
		}
		if (null == error) {
			throw new NullPointerException("没有合适的用户供应商");
		}
		if (error instanceof ApiException) {
			throw (ApiException) error;
		} else if (error instanceof RuntimeException) {
			throw (RuntimeException) error;
		} else {
			throw new RuntimeException(error);
		}
	}

}
