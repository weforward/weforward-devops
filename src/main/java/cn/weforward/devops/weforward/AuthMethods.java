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
package cn.weforward.devops.weforward;

import javax.annotation.Resource;

import cn.weforward.common.util.StringUtil;
import cn.weforward.devops.user.UserAccess;
import cn.weforward.devops.user.UserProvider;
import cn.weforward.framework.ApiException;
import cn.weforward.framework.WeforwardMethod;
import cn.weforward.framework.WeforwardMethods;
import cn.weforward.protocol.datatype.DtBase;
import cn.weforward.protocol.datatype.DtObject;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocMethod;
import cn.weforward.protocol.doc.annotation.DocParameter;
import cn.weforward.protocol.support.CommonServiceCodes;
import cn.weforward.protocol.support.datatype.FriendlyObject;
import cn.weforward.protocol.support.datatype.SimpleDtObject;

@WeforwardMethods
public class AuthMethods {
	@Resource
	protected UserProvider m_UserProvider;

	@DocMethod(description = "登录")
	@DocParameter({ @DocAttribute(name = "user_name", type = String.class),
			@DocAttribute(name = "password", type = String.class) })
	@WeforwardMethod
	public DtBase login(FriendlyObject params) throws ApiException {
		String userName = params.getString("user_name");
		String password = params.getString("password");
		if (StringUtil.isEmpty(userName) || StringUtil.isEmpty(password)) {
			throw new ApiException(CommonServiceCodes.ILLEGAL_ARGUMENT.code, "用户名、密码不能为空");
		}
		UserAccess access = m_UserProvider.login(userName, password);
		if (null == access) {
			throw new ApiException(CommonServiceCodes.ILLEGAL_ARGUMENT.code, "登陆后access为空");
		}
		return toBase(access);
	}

	@DocMethod(description = "登出")
	@DocParameter({ @DocAttribute(name = "access_id", type = String.class) })
	@WeforwardMethod
	public void logout(FriendlyObject params) {
		String accessId = params.getString("access_id");
		if (!StringUtil.isEmpty(accessId)) {
			m_UserProvider.logout(accessId);
		}
	}

	@DocMethod(description = "刷新凭证")
	@DocParameter({ @DocAttribute(name = "access_id", type = String.class),
			@DocAttribute(name = "access_key", type = String.class) })
	@WeforwardMethod
	public DtBase refreshAccess(FriendlyObject params) throws ApiException {
		String accessId = params.getString("access_id");
		String accessKey = params.getString("access_key");
		UserAccess access = m_UserProvider.refreshAccess(accessId, accessKey);
		if (null == access) {
			throw new ApiException(CommonServiceCodes.ILLEGAL_ARGUMENT.code, "刷新后access为空");
		}
		return toBase(access);
	}

	private static DtObject toBase(UserAccess access) {
		SimpleDtObject content = new SimpleDtObject();
		content.put("access_id", access.getAccessId());
		content.put("access_key", access.getAccessKeyBase64());
		content.put("access_expire", access.getAccessExpire());
		content.put("session_id", access.getSessionId());
		return content;
	}

}
