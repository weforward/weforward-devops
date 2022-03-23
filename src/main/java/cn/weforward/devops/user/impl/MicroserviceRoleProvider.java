package cn.weforward.devops.user.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.weforward.common.util.StringUtil;
import cn.weforward.devops.user.OrganizationUser;
import cn.weforward.devops.user.RoleProvider;
import cn.weforward.protocol.Response;
import cn.weforward.protocol.client.ServiceInvoker;
import cn.weforward.protocol.client.ServiceInvokerFactory;
import cn.weforward.protocol.client.execption.GatewayException;
import cn.weforward.protocol.client.execption.MicroserviceException;
import cn.weforward.protocol.gateway.Keeper;
import cn.weforward.protocol.ops.Right;
import cn.weforward.protocol.ops.Role;
import cn.weforward.protocol.ops.User;
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
public class MicroserviceRoleProvider implements RoleProvider {
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

	public MicroserviceRoleProvider(String apiUrl, String accessId, String accessKey, String serviceName,
			String methodGroup) {
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
	public List<Role> getRoles(User user) {
		if (null == user) {
			return Collections.emptyList();
		}
		ServiceInvoker invoker = getInvoker();
		if (null == invoker) {
			return Collections.emptyList();
		}
		String orgId = null;
		if (user instanceof OrganizationUser) {
			orgId = ((OrganizationUser) user).getOrganization().getId();
		}
		String method = genMethod("getRoles");
		SimpleDtObject params = new SimpleDtObject();
		params.put("user", user.getId());
		params.put("org", orgId);
		Response response = invoker.invoke(method, params);
		GatewayException.checkException(response);
		FriendlyObject result = FriendlyObject.valueOf(response.getServiceResult());
		MicroserviceException.checkException(result);
		if (result.isNull()) {
			return Collections.emptyList();
		}
		FriendlyList content = result.getFriendlyList("content");
		if (content.size() == 0) {
			return Collections.emptyList();
		}
		List<Role> roles = new ArrayList<>(content.size());
		for (int i = 0; i < content.size(); i++) {
			FriendlyObject value = content.getFriendlyObject(i);
			roles.add(new SimpleRole(value.getInt("id"), value.getString("name")));
		}
		return roles;
	}

	@Override
	public List<Right> getRights(User user) {
		if (null == user) {
			return Collections.emptyList();
		}
		ServiceInvoker invoker = getInvoker();
		if (null == invoker) {
			return Collections.emptyList();
		}
		String orgId = null;
		if (user instanceof OrganizationUser) {
			orgId = ((OrganizationUser) user).getOrganization().getId();
		}
		String method = genMethod("getRights");
		SimpleDtObject params = new SimpleDtObject();
		params.put("user", user.getId());
		params.put("org", orgId);
		Response response = invoker.invoke(method, params);
		GatewayException.checkException(response);
		FriendlyObject result = FriendlyObject.valueOf(response.getServiceResult());
		MicroserviceException.checkException(result);
		if (result.isNull()) {
			return Collections.emptyList();
		}
		FriendlyList content = result.getFriendlyList("content");
		if (content.size() == 0) {
			return Collections.emptyList();
		}
		List<Right> rights = new ArrayList<>(content.size());
		for (int i = 0; i < content.size(); i++) {
			FriendlyObject value = content.getFriendlyObject(i);
			rights.add(new SimpleRight((short) value.getInt("rule"), value.getString("uriPattern")));
		}
		return rights;
	}

}
