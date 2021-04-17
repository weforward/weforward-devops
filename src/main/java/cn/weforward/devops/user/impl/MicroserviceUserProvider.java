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

import cn.weforward.common.ResultPage;
import cn.weforward.common.crypto.Base64;
import cn.weforward.devops.user.OrganizationProvider;
import cn.weforward.devops.user.UserProvider;
import cn.weforward.framework.ApiException;
import cn.weforward.framework.support.MicroserviceUserService;
import cn.weforward.protocol.Response;
import cn.weforward.protocol.client.ServiceInvoker;
import cn.weforward.protocol.client.ext.RemoteResultPage;
import cn.weforward.protocol.client.ext.RequestInvokeParam;
import cn.weforward.protocol.datatype.DtObject;
import cn.weforward.protocol.exception.DataTypeCastExecption;
import cn.weforward.protocol.exception.ObjectMappingException;
import cn.weforward.protocol.ext.ObjectMapper;
import cn.weforward.protocol.ops.User;
import cn.weforward.protocol.support.datatype.FriendlyObject;
import cn.weforward.protocol.support.datatype.SimpleDtObject;
import cn.weforward.protocol.support.datatype.SimpleDtString;
import cn.weforward.util.UserAuth;

/**
 * 基于微服务实现
 * 
 * @author daibo
 *
 */
public class MicroserviceUserProvider extends MicroserviceUserService implements UserProvider, UserAuth {
	/** 用户映射表 */
	final ObjectMapper<User> _User = new ObjectMapper<User>() {

		@Override
		public String getName() {
			return User.class.getSimpleName();
		}

		@Override
		public DtObject toDtObject(User object) throws ObjectMappingException {
			throw new UnsupportedOperationException();
		}

		@Override
		public User fromDtObject(DtObject obj) throws ObjectMappingException {
			return getUser(FriendlyObject.valueOf(obj));
		}
	};

	protected OrganizationProvider m_OrganizationProvider;

	public MicroserviceUserProvider(String apiUrl, String accessId, String accessKey, String serviceName,
			String methodGroup, OrganizationProvider provider) {
		super(apiUrl, accessId, accessKey, serviceName, methodGroup);
		m_OrganizationProvider = provider;
	}

	@Override
	public SimpleUserAccess login(String userName, String password) throws ApiException {
		ServiceInvoker invoker = getInvoker();
		SimpleDtObject params = new SimpleDtObject();
		params.put("user_name", SimpleDtString.valueOf(userName));
		params.put("password", SimpleDtString.valueOf(password));
		Response response = invoker.invoke(genMethod("login"), params);
		FriendlyObject content = getContentWithApiException(response);
		SimpleUserAccess access = new SimpleUserAccess();
		access.setSessionId(content.getString("session_id"));
		access.setAccessId(content.getString("access_id"));
		access.setAccessKey(Base64.decode(content.getString("access_key")));
		access.setAccessExpire(content.getLong("access_expire"));
		return access;
	}

	/* 获取内容 */
	protected FriendlyObject getContentWithApiException(Response response) throws DataTypeCastExecption, ApiException {
		if (response.getResponseCode() != 0) {
			throw new RuntimeException("网关异常:" + response.getResponseCode() + "/" + response.getResponseMsg());
		}
		FriendlyObject result = FriendlyObject.valueOf(response.getServiceResult());
		if (0 != result.getInt("code", -1)) {
			throw new ApiException(result.getInt("code", -1), result.getString("msg"));
		}
		return result.getFriendlyObject("content");
	}

	@Override
	public void logout(String accessId) {
		ServiceInvoker invoker = getInvoker();
		SimpleDtObject params = new SimpleDtObject();
		params.put("access_id", SimpleDtString.valueOf(accessId));
		Response response = invoker.invoke(genMethod("logout"), params);
		getContent(response);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultPage<User> searchUser(String keywords) {
		ServiceInvoker invoker = getInvoker();
		String method = genMethod("searchUser");
		RemoteResultPage<? extends User> rp = new RemoteResultPage<SimpleUser>(SimpleUser.class, invoker, method,
				RequestInvokeParam.valueOf("keywords", keywords)) {
		};
		return (RemoteResultPage<User>) rp;
	}

	@Override
	public boolean check(String userName, String password) {
		ServiceInvoker invoker = getInvoker();
		SimpleDtObject params = new SimpleDtObject();
		params.put("user_name", SimpleDtString.valueOf(userName));
		params.put("password", SimpleDtString.valueOf(password));
		Response response = invoker.invoke(genMethod("login"), params);
		if (response.getResponseCode() != 0) {
			throw new RuntimeException("网关异常:" + response.getResponseCode() + "/" + response.getResponseMsg());
		}
		FriendlyObject result = FriendlyObject.valueOf(response.getServiceResult());
		if (0 != result.getInt("code", -1)) {
			return false;
		}
		return true;
	}

	/* 获取用户 */
	protected User getUser(FriendlyObject content) {
		if (content.isNull()) {
			return null;
		}
		SimpleOrganizationUser user = new SimpleOrganizationUser(content.getString("id"), content.getString("name"),
				getRight(content.getFriendlyList("right")));
		user.setOrganizationProvider(m_OrganizationProvider);
		return user;
	}

}
