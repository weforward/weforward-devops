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
import cn.weforward.devops.user.GroupProvider;
import cn.weforward.devops.user.OrganizationProvider;
import cn.weforward.devops.user.UserProvider;
import cn.weforward.devops.user.impl.InnerGroupProvider;
import cn.weforward.devops.user.impl.InnerOrganizationProvider;
import cn.weforward.devops.user.impl.InnerUserProvider;
import cn.weforward.devops.user.impl.MicroserviceOrganizationProvider;
import cn.weforward.devops.user.impl.MicroserviceUserProvider;
import cn.weforward.devops.user.impl.MultipleOrganizationProvider;
import cn.weforward.devops.user.impl.MultipleUserProvider;
import cn.weforward.protocol.gateway.Keeper;
import cn.weforward.protocol.gateway.http.HttpKeeper;
import cn.weforward.util.nexus.NexusRepositoryClear;

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
	/** proxy机器svndist地址 */
	@Value("${proxyDistUrl}")
	protected String m_ProxyDistUrl;
	/** docker机器svndist地址 */
	@Value("${dockerDistUrl}")
	protected String m_DockerDistUrl;
	/** 日志url */
	@Value("${rlog.url}")
	protected String m_RlogUrl;
	/** 资源url */
	@Value("${resource.url}")
	protected String m_ResourceUrl;

	/** 服务网关地址 */
	@Value("${weforward.apiUrl:}")
	protected String m_ApiUrl;
	/** 服务访问id */
	@Value("${weforward.service.accessId:}")
	protected String m_ServiceAccessId;
	/** 服务访问key */
	@Value("${weforward.service.accessKey:}")
	protected String m_ServiceAccessKey;
	/** 访问凭证id */
	@Value("${weforward.keeper.accessId:}")
	protected String m_KeeperAccessId;
	/** 访问凭证key */
	@Value("${weforward.keeper.accessKey:}")
	protected String m_KeeperAccessKey;

	/** dockerHub地址 */
	@Value("${dockerHubUrl}")
	protected String m_DockerHubUrl;
	/** dockerHub https地址 */
	@Value("${dockerHubHttpsUrl}")
	protected String m_DockerHubHttpsUrl;
	/** dockerHub用户名 */
	@Value("${dockerHubUsername}")
	protected String m_DockerHubUsername;
	/** dockerHub密码 */
	@Value("${dockerHubPassword}")
	protected String m_DockerHubPassword;
	/** dockerHub邮箱 */
	@Value("${dockerHubEmail:}")
	protected String m_DockerHubEmail;

	@Value("${nexus.url:}")
	protected String m_NexusUrl;
	@Value("${nexus.username:}")
	protected String m_NexusUsername;
	@Value("${nexus.password:}")
	protected String m_NexusPassword;
	@Value("${nexus.repository:}")
	protected String m_NexusRepository;
	@Value("${nexus.repository.keepnum:10}")
	protected int m_NexusRepositoryKeepnum;

	@Value("${weforward.organization.id:default}")
	protected String m_Organizationid;
	@Value("${weforward.organization.name:默认}")
	protected String m_OrganizationName;

	@Value("${weforward.organization.serviceName:}")
	protected String m_OrganizationServiceName;
	@Value("${weforward.organization.methodGroup:}")
	protected String m_OrganizationMethodGroup;

	@Value("${weforward.user.id:_admin}")
	protected String m_UserId;
	@Value("${weforward.user.name:admin}")
	protected String m_UserName;
	@Value("${weforward.user.serviceName:}")
	protected String m_UserServiceName;
	@Value("${weforward.user.methodGroup:}")
	protected String m_UserMethodGroup;

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
		if (StringUtil.isEmpty(m_KeeperAccessId) || StringUtil.isEmpty(m_KeeperAccessKey)) {
			return null;
		}
		return new HttpKeeper(m_ApiUrl, m_KeeperAccessId, m_KeeperAccessKey);
	}

	/** 项目服务 */
	@Bean
	ProjectService projectService(PersisterFactory persisterFactroy, SearcherFactory searcherFactory,
			LabelSetFactory labelSetFactory, BusinessLoggerFactory loggerFactory, UserProvider userProvider,
			GroupProvider groupProvider, OrganizationProvider organizationProvider) {
		ProjectServiceImpl p = new ProjectServiceImpl(persisterFactroy, searcherFactory, labelSetFactory,
				loggerFactory);
		p.setDockerHubUrl(m_DockerHubUrl);
		p.setDockerHubHttpsUrl(m_DockerHubHttpsUrl);
		p.setDockerHubUsername(m_DockerHubUsername);
		p.setDockerHubPassword(m_DockerHubPassword);
		p.setDockerHubEmail(m_DockerHubEmail);
		p.setProxyDistUrl(m_ProxyDistUrl);
		p.setDockerDistUrl(m_DockerDistUrl);
		p.setResourceUrl(m_ResourceUrl);
		p.setRlogUrl(m_RlogUrl);
		p.setApiUrl(m_ApiUrl);
		p.setTaskExecutor(taskExecutor);
		p.setDistUserName(m_DockerHubUsername);
		p.setDistPassword(m_DockerHubPassword);
		p.setUserService(userProvider);
		p.setGroupService(groupProvider);
		p.setOrganizationProvider(organizationProvider);
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
		return new MultipleOrganizationProvider(Arrays.asList(mp, ip));
	}

	@Bean
	GroupProvider groupProviderProvider(PersisterFactory persisterFactroy, UserProvider userProvider) {
		return new InnerGroupProvider(persisterFactroy, userProvider);
	}

	@Bean(name = "userAuth")
	UserProvider userProvider(@Value("${weforward.user.password}") String password,
			@Value("${weforward.user.secretKey}") String secretKey, OrganizationProvider organizationProvider) {
		if (StringUtil.isEmpty(m_UserServiceName)) {
			return new InnerUserProvider(m_UserId, m_UserName, password, secretKey, organizationProvider);
		}
		MicroserviceUserProvider mp = new MicroserviceUserProvider(m_ApiUrl, m_ServiceAccessId, m_ServiceAccessKey,
				m_UserServiceName, m_UserMethodGroup, organizationProvider);
		InnerUserProvider ip = new InnerUserProvider(m_UserId, m_UserName, password, secretKey, organizationProvider);
		return new MultipleUserProvider(mp, ip);
	}

	@Bean
	NexusRepositoryClear repositoryClear(TaskExecutor taskExecutor) {
		if (StringUtil.isEmpty(m_NexusUrl) || StringUtil.isEmpty(m_NexusRepository)) {
			return null;
		}
		NexusRepositoryClear c = new NexusRepositoryClear(m_NexusUrl, m_NexusUsername, m_NexusPassword,
				m_NexusRepository);
		c.setKeepNum(m_NexusRepositoryKeepnum);
		c.setTaskExecutor(taskExecutor);
		return c;
	}

}
