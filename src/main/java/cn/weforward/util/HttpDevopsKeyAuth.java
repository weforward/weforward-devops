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
package cn.weforward.util;

import cn.weforward.common.util.StringUtil;
import cn.weforward.protocol.Response;
import cn.weforward.protocol.client.ServiceInvoker;
import cn.weforward.protocol.client.ServiceInvokerFactory;
import cn.weforward.protocol.client.execption.GatewayException;
import cn.weforward.protocol.client.execption.MicroserviceException;
import cn.weforward.protocol.client.util.MappedUtil;
import cn.weforward.protocol.datatype.DtObject;
import cn.weforward.protocol.support.NamingConverter;
import cn.weforward.protocol.support.datatype.FriendlyObject;
import cn.weforward.protocol.support.datatype.SimpleDtObject;

/**
 * http开发密钥验证器
 * 
 * @author daibo
 *
 */
public class HttpDevopsKeyAuth extends AbstractHttpAuth<DevopsMember> {

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

	public HttpDevopsKeyAuth(String apiUrl, String accessId, String accessKey, String serviceName, String methodGroup) {
		m_ApiUrl = apiUrl;
		m_AccessId = accessId;
		m_AccessKey = accessKey;
		m_ServiceName = serviceName;
		m_MethodGroup = StringUtil.toString(methodGroup);
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
	protected DevopsMember check(String username, String password) {
		ServiceInvoker invoker = getInvoker();
		if (null == invoker) {
			return null;
		}
		String method = genMethod("getMemberByDevopsKey");
		SimpleDtObject params = new SimpleDtObject();
		params.put("key", password);
		Response response = invoker.invoke(method, params);
		GatewayException.checkException(response);
		FriendlyObject result = FriendlyObject.valueOf(response.getServiceResult());
		MicroserviceException.checkException(result);
		if (result.isNull()) {
			return null;
		}
		DtObject content = result.getObject("content");
		if (null == content) {
			return null;
		}
		return MappedUtil.fromBase(DevopsMember.class, content);
	}

}
