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
package cn.weforward;

import java.net.URI;
import java.util.Arrays;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.TaskExecutor;
import cn.weforward.data.array.LabelSetFactory;
import cn.weforward.data.log.BusinessLoggerFactory;
import cn.weforward.data.mongodb.array.MongodbLabelSetFactory;
import cn.weforward.data.mongodb.log.MongodbBusinessLoggerFactory;
import cn.weforward.data.mongodb.persister.MongodbPersisterFactory;
import cn.weforward.data.mongodb.search.MongodbSearcherFactory;
import cn.weforward.data.persister.PersisterFactory;
import cn.weforward.data.persister.PersisterSet;
import cn.weforward.data.search.SearcherFactory;
import cn.weforward.data.util.Flusher;
import cn.weforward.devops.alarm.AlarmService;
import cn.weforward.devops.alarm.impl.AlarmServiceImpl;
import cn.weforward.devops.project.ProjectService;
import cn.weforward.devops.project.impl.ProjectServiceImpl;
import cn.weforward.devops.user.AccessKeeper;
import cn.weforward.devops.user.GroupProvider;
import cn.weforward.devops.user.OrganizationProvider;
import cn.weforward.devops.user.RoleProvider;
import cn.weforward.devops.user.UserProvider;
import cn.weforward.devops.user.impl.InnerGroupProvider;
import cn.weforward.devops.user.impl.InnerOrganizationProvider;
import cn.weforward.devops.user.impl.InnerRoleProvider;
import cn.weforward.devops.user.impl.InnerUserProvider;
import cn.weforward.devops.user.impl.MicroserviceGroupProvider;
import cn.weforward.devops.user.impl.MicroserviceOrganizationProvider;
import cn.weforward.devops.user.impl.MicroserviceRoleProvider;
import cn.weforward.devops.user.impl.MicroserviceUserProvider;
import cn.weforward.devops.user.impl.MultipleGroupProvider;
import cn.weforward.devops.user.impl.MultipleOrganizationProvider;
import cn.weforward.devops.user.impl.MultipleRoleProvider;
import cn.weforward.devops.user.impl.MultipleUserProvider;
import cn.weforward.protocol.gateway.Keeper;
import cn.weforward.protocol.gateway.http.HttpKeeper;
import cn.weforward.util.HttpDevopsKeyAuth;
import cn.weforward.util.LocalKeeper;

/**
 * devops配置
 * 
 * @author daibo
 *
 */
public class DevopsConfig {
	/** 服务器id */
	@Value("${weforward.serverid}")
	protected String m_ServerId;
	/** mongodb地址 */
	@Value("${mongodb.url}")
	protected String m_MongodbUrl;
	/** mongodb数据库名 */
	@Value("${mongodb.dbname}")
	protected String m_MongodbDbname;
	/** 下载资源地址 */
	@Value("${download.url}")
	protected String m_DownloadUrl;
	/** 资源url */
	@Value("${resource.url}")
	protected String m_ResourceUrl;
	/** 日志url */
	@Value("${rlog.url}")
	protected String m_RlogUrl;
	/** 调整服务网关地址 */
	@Value("${weforward.apiUrl:}")
	protected String m_ApiUrl;
	/** 注册服务网关地址 */
	@Value("${weforward.gatewayUrl:}")
	protected String m_GatewayUrl;
	/** 服务访问id */
	@Value("${weforward.service.accessId:}")
	protected String m_ServiceAccessId;
	/** 服务访问key */
	@Value("${weforward.service.accessKey:}")
	protected String m_ServiceAccessKey;
	/** 服务名 */
	@Value("${weforward.name}")
	protected String m_DevopsName;
	/** 服务主机 */
	@Value("${weforward.host:127.0.0.1}")
	protected String m_DevopsHost;
	/** 服务端口 */
	@Value("${weforward.port:15000}")
	protected int m_DevopsPort;

	@Value("${weforward.organization.id:default}")
	protected String m_Organizationid;
	@Value("${weforward.organization.name:默认}")
	protected String m_OrganizationName;

	@Value("${weforward.organization.serviceName:}")
	protected String m_OrganizationServiceName;
	@Value("${weforward.organization.methodGroup:}")
	protected String m_OrganizationMethodGroup;
	@Value("${weforward.group.serviceName:}")
	protected String m_GroupServiceName;
	@Value("${weforward.group.methodGroup:}")
	protected String m_GroupMethodGroup;

	@Value("${weforward.role.serviceName:}")
	protected String m_RoleServiceName;
	@Value("${weforward.role.methodGroup:}")
	protected String m_RoleMethodGroup;

	@Value("${weforward.user.id:_admin}")
	protected String m_UserId;
	@Value("${weforward.user.name:admin}")
	protected String m_UserName;
	@Value("${weforward.user.password}")
	protected String m_UserPassword;
	@Value("${weforward.user.secretKey}")
	protected String m_UserSecretKey;
	@Value("${weforward.user.serviceName:}")
	protected String m_UserServiceName;
	@Value("${weforward.user.methodGroup:}")
	protected String m_UserMethodGroup;

	@Value("${weforward.devopskey.serviceName:}")
	protected String m_DevopsKeyServiceName;
	@Value("${weforward.devopskey.methodGroup:}")
	protected String m_DevopsKeyMethodGroup;

	/** 任务执行器 */
	@Resource
	protected TaskExecutor taskExecutor;
	/** 持久器集合 */
	@Resource
	protected PersisterSet persisters;

	/** 标签工厂 */
	@Bean
	LabelSetFactory labelSetFactory() {
		MongodbLabelSetFactory f = new MongodbLabelSetFactory(m_MongodbUrl);
		f.setServerId(m_ServerId);
		f.setHashSize(0);
		return f;
	}

	/** 持久类工厂 */
	@Bean
	PersisterFactory persisterFactroy(PersisterSet persister, Flusher flusher) {
		MongodbPersisterFactory ps = new MongodbPersisterFactory(m_MongodbUrl, m_MongodbDbname, persister);
		ps.setFlusher(flusher);
		ps.setServerId(m_ServerId);
		return ps;
	}

	/** 搜索器 */
	@Bean
	SearcherFactory searcherFactory() {
		MongodbSearcherFactory sf = new MongodbSearcherFactory(m_MongodbUrl, m_MongodbDbname);
		sf.setServerId(m_ServerId);
		return sf;
	}

	/** 日志工厂 */
	@Bean
	BusinessLoggerFactory loggerFactory() {
		return new MongodbBusinessLoggerFactory(m_ServerId, m_MongodbUrl, m_MongodbDbname);
	}

	@Bean
	Keeper keeper() {
		if (isLocal(m_ApiUrl, m_DevopsHost, m_DevopsPort)) {
			return new LocalKeeper(m_ServiceAccessId, m_ServiceAccessKey, m_DevopsHost, m_DevopsPort, m_DevopsName,
					m_ServerId);
		}
		return new HttpKeeper(m_ApiUrl, m_ServiceAccessId, m_ServiceAccessKey);
	}

	private static boolean isLocal(String apiUrl, String devopsHost, int devopsPort) {
		if (StringUtil.isEmpty(apiUrl)) {
			return true;
		}
		URI uri = URI.create(apiUrl);
		if (uri.getPort() == devopsPort) {
			String myHost = uri.getHost();
			if (StringUtil.eq("localhost", myHost) || StringUtil.eq("127.0.0.1", myHost)
					|| StringUtil.eq(myHost, devopsHost)) {
				return true;
			}
		}
		return false;
	}

	@Bean
	AccessKeeper accessKeeper(Keeper keeper) {
		return null == keeper ? null : new AccessKeeper(keeper);
	}

	@Bean
	HttpDevopsKeyAuth devopsKeyAuth() {
		if (StringUtil.isEmpty(m_DevopsKeyServiceName)) {
			return null;
		} else {
			return new HttpDevopsKeyAuth(m_ApiUrl, m_ServiceAccessId, m_ServiceAccessKey, m_DevopsKeyServiceName,
					m_DevopsKeyMethodGroup);
		}
	}

	/** 项目服务 */
	@Bean
	ProjectService projectService(PersisterFactory persisterFactroy, SearcherFactory searcherFactory,
			LabelSetFactory labelSetFactory, BusinessLoggerFactory loggerFactory, UserProvider userProvider,
			GroupProvider groupProvider, OrganizationProvider organizationProvider, AccessKeeper accessKeeper) {
		ProjectServiceImpl p = new ProjectServiceImpl(persisterFactroy, searcherFactory, labelSetFactory,
				loggerFactory);
		p.setDownloadUrl(m_DownloadUrl);
		p.setResourceUrl(m_ResourceUrl);
		p.setRlogUrl(m_RlogUrl);
		p.setGatewayUrl(m_GatewayUrl);
		p.setApiUrl(m_ApiUrl);
		p.setTaskExecutor(taskExecutor);
		p.setUserService(userProvider);
		p.setGroupService(groupProvider);
		p.setOrganizationService(organizationProvider);
		p.setAccessKeeper(accessKeeper);
		return p;
	}

	/** 报警服务 */
	@Bean
	AlarmService alarmService(PersisterFactory persisterFactroy) {
		AlarmServiceImpl p = new AlarmServiceImpl(persisterFactroy);
		return p;
	}

	@Bean
	OrganizationProvider organizationProvider(Keeper keeper) {
		if (StringUtil.isEmpty(m_OrganizationServiceName)) {
			return new InnerOrganizationProvider(m_Organizationid, m_OrganizationName);
		}

		MicroserviceOrganizationProvider mp = new MicroserviceOrganizationProvider(m_ApiUrl, m_ServiceAccessId,
				m_ServiceAccessKey, m_OrganizationServiceName, m_OrganizationMethodGroup);
		mp.setKeeper(keeper);
		InnerOrganizationProvider ip = new InnerOrganizationProvider(m_Organizationid, m_OrganizationName);
		return new MultipleOrganizationProvider(Arrays.asList(ip, mp));
	}

	@Bean
	GroupProvider groupProviderProvider(PersisterFactory persisterFactroy, UserProvider userProvider) {
		if (StringUtil.isEmpty(m_GroupServiceName)) {
			return new InnerGroupProvider(persisterFactroy, userProvider);
		}
		MicroserviceGroupProvider mp = new MicroserviceGroupProvider(m_ApiUrl, m_ServiceAccessId, m_ServiceAccessKey,
				m_GroupServiceName, m_GroupMethodGroup, userProvider);
		InnerGroupProvider ip = new InnerGroupProvider(persisterFactroy, userProvider);
		return new MultipleGroupProvider(ip, mp);
	}

	@Bean
	RoleProvider roleProvider() {
		if (StringUtil.isEmpty(m_RoleServiceName)) {
			return new InnerRoleProvider();
		}
		MicroserviceRoleProvider mp = new MicroserviceRoleProvider(m_ApiUrl, m_ServiceAccessId, m_ServiceAccessKey,
				m_RoleServiceName, m_RoleMethodGroup);
		InnerRoleProvider ip = new InnerRoleProvider();
		return new MultipleRoleProvider(ip, mp);
	}

	@Bean(name = "userAuth")
	UserProvider userProvider(RoleProvider roleProvider, OrganizationProvider organizationProvider) {
		if (StringUtil.isEmpty(m_UserServiceName)) {
			return new InnerUserProvider(m_UserId, m_UserName, m_UserPassword, m_UserSecretKey, roleProvider,
					organizationProvider);
		}
		MicroserviceUserProvider mp = new MicroserviceUserProvider(m_ApiUrl, m_ServiceAccessId, m_ServiceAccessKey,
				m_UserServiceName, m_UserMethodGroup, roleProvider, organizationProvider);
		InnerUserProvider ip = new InnerUserProvider(m_UserId, m_UserName, m_UserPassword, m_UserSecretKey,
				roleProvider, organizationProvider);
		return new MultipleUserProvider(ip, mp);
	}

}
