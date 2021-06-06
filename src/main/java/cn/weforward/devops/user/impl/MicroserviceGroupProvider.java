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
import java.util.List;

import cn.weforward.common.ResultPage;
import cn.weforward.common.sys.GcCleaner;
import cn.weforward.common.util.LruCache;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.devops.user.Group;
import cn.weforward.devops.user.GroupProvider;
import cn.weforward.devops.user.Organization;
import cn.weforward.devops.user.UserProvider;
import cn.weforward.protocol.Response;
import cn.weforward.protocol.client.ServiceInvoker;
import cn.weforward.protocol.client.ServiceInvokerFactory;
import cn.weforward.protocol.client.execption.GatewayException;
import cn.weforward.protocol.client.execption.MicroserviceException;
import cn.weforward.protocol.client.ext.TransRemoteResultPage;
import cn.weforward.protocol.datatype.DtBase;
import cn.weforward.protocol.datatype.DtObject;
import cn.weforward.protocol.gateway.Keeper;
import cn.weforward.protocol.support.NamingConverter;
import cn.weforward.protocol.support.datatype.FriendlyList;
import cn.weforward.protocol.support.datatype.FriendlyObject;
import cn.weforward.protocol.support.datatype.SimpleDtObject;

/**
 * 基于微服务的角色供应商实现
 * 
 * @author daibo
 *
 */
public class MicroserviceGroupProvider implements GroupProvider {
	/** 服务地址 */
	protected String m_ApiUrl;
	/** 服务访问id */
	protected String m_AccessId;
	/** 服务访问key */
	protected String m_AccessKey;
	/** 服务名 */
	protected String m_ServiceName;
	/** 方法名 */
	protected String m_MethodGroup;
	/** 服务调用器 */
	protected ServiceInvoker m_Invoker;
	/** （网关）管理接口 */
	protected Keeper m_Keeper;
	/** 缓存 */
	protected LruCache<String, Group> m_Groups;

	protected UserProvider m_UserProvider;

	public MicroserviceGroupProvider(String apiUrl, String accessId, String accessKey, String serviceName,
			String methodGroup, UserProvider userProvder) {
		m_ApiUrl = apiUrl;
		m_AccessId = accessId;
		m_AccessKey = accessKey;
		m_ServiceName = serviceName;
		m_MethodGroup = StringUtil.toString(methodGroup);
		m_UserProvider = userProvder;
		m_Groups = new LruCache<String, Group>("weforward-groups");
		GcCleaner.register(m_Groups);
	}

	/* 生成方法名 */
	protected String genMethod(String method) {
		return m_MethodGroup + NamingConverter.camelToWf(method);
	}

	public ServiceInvoker getInvoker() {
		if (StringUtil.isEmpty(m_ServiceName)) {
			return null;
		}
		if (null == m_Invoker) {
			m_Invoker = ServiceInvokerFactory.create(m_ServiceName, m_ApiUrl, m_AccessId, m_AccessKey);
		}
		return m_Invoker;
	}

	@Override
	public Group getGroup(Organization org, String id) {
		if (null == org) {
			return null;
		}
		ServiceInvoker invoker = getInvoker();
		if (null == invoker) {
			return null;
		}
		String orgId = org.getId();
		String method = genMethod("getGroup");
		SimpleDtObject params = new SimpleDtObject();
		params.put("org", orgId);
		params.put("id", id);
		Response response = invoker.invoke(method, params);
		GatewayException.checkException(response);
		FriendlyObject result = FriendlyObject.valueOf(response.getServiceResult());
		MicroserviceException.checkException(result);
		if (result.isNull()) {
			return null;
		}
		return toGroup(result.getFriendlyObject("content"));
	}

	@Override
	public ResultPage<Group> search(Organization org, String keywords) {
		ServiceInvoker invoker = getInvoker();
		if (null == invoker) {
			return ResultPageHelper.empty();
		}
		String method = genMethod("searchGroup");
		SimpleDtObject params = new SimpleDtObject();
		params.put("org", org.getId());
		params.put("keywords", keywords);
		return new TransRemoteResultPage<Group>(invoker, method, params) {

			@Override
			protected Group trans(DtBase item) {
				return toGroup(FriendlyObject.valueOf((DtObject) item));
			}
		};
	}

	private Group toGroup(FriendlyObject params) {
		GroupVo vo = new GroupVo();
		vo.setId(params.getString("id"));
		vo.setName(params.getString("name"));
		vo.setNote(params.getString("note"));
		vo.setUserProvider(m_UserProvider);
		FriendlyList list = params.getFriendlyList("users");
		List<String> users = new ArrayList<>(list.size());
		for (int i = 0; i < list.size(); i++) {
			users.add(list.getString(i));
		}
		vo.setUsers(users);
		return vo;
	}

	@Override
	public Group addGroup(Organization org, String name) {
		throw new UnsupportedOperationException();
	}

}
