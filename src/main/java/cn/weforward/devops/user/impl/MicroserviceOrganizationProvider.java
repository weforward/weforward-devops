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
import cn.weforward.common.sys.GcCleaner;
import cn.weforward.common.util.LruCache;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.devops.user.Organization;
import cn.weforward.devops.user.OrganizationProvider;
import cn.weforward.protocol.Response;
import cn.weforward.protocol.client.ServiceInvoker;
import cn.weforward.protocol.client.ServiceInvokerFactory;
import cn.weforward.protocol.client.execption.GatewayException;
import cn.weforward.protocol.client.execption.MicroserviceException;
import cn.weforward.protocol.client.ext.RemoteResultPage;
import cn.weforward.protocol.client.ext.RequestInvokeParam;
import cn.weforward.protocol.gateway.Keeper;
import cn.weforward.protocol.ops.User;
import cn.weforward.protocol.support.NamingConverter;
import cn.weforward.protocol.support.datatype.FriendlyObject;
import cn.weforward.protocol.support.datatype.SimpleDtObject;

/**
 * 基于微服务实现组织提供者
 * 
 * @author daibo
 *
 */
public class MicroserviceOrganizationProvider implements OrganizationProvider {
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
	/** 组织缓存 */
	protected LruCache<String, Organization> m_Organizations;

	public MicroserviceOrganizationProvider(String apiUrl, String accessId, String accessKey, String serviceName,
			String methodGroup) {
		m_ApiUrl = apiUrl;
		m_AccessId = accessId;
		m_AccessKey = accessKey;
		m_ServiceName = serviceName;
		m_MethodGroup = StringUtil.toString(methodGroup);
		m_Organizations = new LruCache<String, Organization>("weforward-organization");
		GcCleaner.register(m_Organizations);
	}

	public void setKeeper(Keeper keeper) {
		m_Keeper = keeper;
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

	public Organization get(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		Organization g = m_Organizations.get(id);
		if (null != g) {
			return g;
		}
		g = doGet(id);
		if (null == g) {
			return null;
		}
		Organization old = m_Organizations.putIfAbsent(id, g);
		if (null != old) {
			return old;
		} else {
			return g;
		}
	}

	private Organization doGet(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		ServiceInvoker invoker = getInvoker();
		if (null == invoker) {
			return null;
		}
		String method = genMethod("getOrg");
		SimpleDtObject params = new SimpleDtObject();
		params.put("id", id);
		Response response = invoker.invoke(method, null);
		return toOrg(response);

	}

	@Override
	public ResultPage<Organization> search(String keywords) {
		ServiceInvoker invoker = getInvoker();
		if (null == invoker) {
			return ResultPageHelper.empty();
		}
		String method = genMethod("searchOrg");
		return new RemoteResultPage<Organization>(Organization.class, invoker, method,
				RequestInvokeParam.valueOf("keywords", keywords));

	}

	@Override
	public Organization get(User user) {
		if (null == user) {
			return null;
		}
		ServiceInvoker invoker = getInvoker();
		if (null == invoker) {
			return null;
		}
		String method = genMethod("getOrgByUser");
		SimpleDtObject params = new SimpleDtObject();
		params.put("user", user.getId());
		Response response = invoker.invoke(method, null);
		return toOrg(response);
	}

	@Override
	public Organization getByAccessId(String accessId) {
		// TODO Auto-generated method stub
		return null;
	}

	private Organization toOrg(Response response) {
		GatewayException.checkException(response);
		FriendlyObject result = FriendlyObject.valueOf(response.getServiceResult());
		MicroserviceException.checkException(result);
		Organization org = new Organization();
		org.setId(result.getString("id"));
		org.setName(result.getString("name"));
		return org;
	}

}
