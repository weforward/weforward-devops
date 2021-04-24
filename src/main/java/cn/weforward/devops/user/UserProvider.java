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
package cn.weforward.devops.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.weforward.common.ResultPage;
import cn.weforward.devops.weforward.AuthMethods;
import cn.weforward.framework.ApiException;
import cn.weforward.protocol.StatusCode;
import cn.weforward.protocol.ops.User;
import cn.weforward.protocol.ops.UserService;

/**
 * 用户供应商
 * 
 * @author daibo
 *
 */
public interface UserProvider extends UserService {
	/** 日志 */
	final static Logger _Logger = LoggerFactory.getLogger(AuthMethods.class);
	/** 状态码 - 无法不明确的错误，根据错误描述判断 */
	public static final StatusCode INDEFINITE = new StatusCode(100001, "");
	/** 状态码 - （OAuth）认证失败 */
	public static final StatusCode AUTH_FAIL = new StatusCode(100002, "OAuth认证失败");
	/** 状态码 - 尝试太频繁 */
	public static final StatusCode FREQUENT = new StatusCode(100003, "尝试太频繁");
	/** 状态码 - 用户登录失败 */
	public static final StatusCode LOGIN_FAILED = new StatusCode(100004, "登录失败");
	/** 状态码 - 用户会话冲突 */
	public static final StatusCode SESSION_CONFLICT = new StatusCode(100005, "会话冲突");
	/** 状态码 - （角色/权限不适合）阻止用户会话 */
	public static final StatusCode SESSION_FORBID = new StatusCode(100006, "会话被阻止");
	/** 状态码 - 用户重复 */
	public static final StatusCode USER_EXIST = new StatusCode(100007, "用户重复");
	/** 状态码 - 没有相关用户 */
	public static final StatusCode USER_NOT_FOUND = new StatusCode(100008, "没有相关用户");
	/** 状态码 - 刷新Access失败 */
	public static final StatusCode REFRESH_ACCESS_FAIL = new StatusCode(100009, "刷新Access失败");

	/**
	 * 登陆
	 * 
	 * @param userName
	 * @param password
	 * @return
	 * @throws ApiException
	 */
	UserAccess login(String userName, String password) throws ApiException;

	/**
	 * 登出
	 * 
	 * @param accessId
	 */
	void logout(String accessId);

	/**
	 * 搜索用户
	 * 
	 * @param keywords
	 * @return
	 */
	ResultPage<User> searchUser(String keywords);

	/**
	 * 刷新凭证
	 * 
	 * @param accessId
	 * @param accessKey
	 * @return
	 * @throws ApiException
	 */
	UserAccess refreshAccess(String accessId, String accessKey) throws ApiException;

}
