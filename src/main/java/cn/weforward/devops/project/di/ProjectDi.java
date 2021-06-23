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
package cn.weforward.devops.project.di;

import java.io.File;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.TaskExecutor;
import cn.weforward.data.UniteId;
import cn.weforward.data.log.BusinessLog;
import cn.weforward.data.persister.BusinessDi;
import cn.weforward.data.search.Searcher;
import cn.weforward.devops.project.Machine;
import cn.weforward.devops.project.Project;
import cn.weforward.devops.project.ext.AbstractProject;
import cn.weforward.devops.project.impl.JavaProject;
import cn.weforward.devops.project.impl.ServiceProperties;
import cn.weforward.devops.project.impl.SimpleRunning;
import cn.weforward.devops.user.Group;
import cn.weforward.devops.user.Organization;
import cn.weforward.protocol.ops.User;

/**
 * 项目依赖di
 * 
 * @author daibo
 *
 */
public interface ProjectDi extends BusinessDi {

	/** docker仓库地址 */
	String getDockerHubUrl();

	/** docker仓库地址 */
	String getDockerHubHttpsUrl();

	/** dockerHub用户名 */
	String getDockerHubUsername();

	/** dockerHub用户密码 */
	String getDockerHubPassword();

	/** dockerHub用户邮箱 */
	String getDockerHubEmail();

	/** 任务执行器 */
	TaskExecutor getTaskExecutor();

	/** 下载地址 */
	String getDownloadUrl();

	/** 获取用户 */
	User getUser(String id);

	/**
	 * 写日志
	 * 
	 * @param id
	 * @param action
	 * @param what
	 * @param note
	 */
	void writeLog(UniteId id, String action, String what, String note);

	/**
	 * 获取日志
	 * 
	 * @param id
	 * @return
	 */
	ResultPage<BusinessLog> getLogs(UniteId id);

	/**
	 * 远程日志url
	 * 
	 * @return
	 */
	String getRlogUrl();

	/**
	 * 资源地址
	 * 
	 * @return
	 */
	String getResourceUrl();

	/**
	 * 获取java项目
	 * 
	 * @return
	 */
	ResultPage<JavaProject> getJavaProjects(Organization org);

	/**
	 * 调用网关链接
	 * 
	 * @return
	 */
	String getApiUrl();

	/**
	 * 网关链接
	 * 
	 * @return
	 */
	String getGatewayUrl();

	/**
	 * 获取服务配置
	 * 
	 * @param label
	 * @param sid
	 * @return
	 */
	ServiceProperties getServiceProperties(String label, String sid);

	/**
	 * 保存服务配置
	 * 
	 * @param label
	 * @param ele
	 */
	void saveServiceProperties(String label, ServiceProperties ele);

	/**
	 * 获取机器
	 * 
	 * @param id
	 * @return
	 */
	Machine getMachine(String id);

	/**
	 * 获取项目
	 * 
	 * @param id
	 * @return
	 */
	Project getProject(String id);

	/**
	 * 获取实例搜索器
	 * 
	 * @return
	 */
	Searcher getRunningSearcher();

	/**
	 * 获取项目搜索器
	 * 
	 * @return
	 */
	Searcher getProjectSearcher();

	/**
	 * 获取项目所有实例
	 * 
	 * @param projectid
	 * @return
	 */
	ResultPage<SimpleRunning> getProjectRunnings(String projectid);

	File getStackDir(Machine machine, Project project);

	File getMermoryMapDir(Machine machine, Project project);

	/**
	 * 默认机器权限
	 * 
	 * @return
	 */
	int getDefaultMachineRight();

	/**
	 * 默认项目权限
	 * 
	 * @return
	 */
	int getDefaultProjectRight();

	/**
	 * 获取组
	 * 
	 * @param org
	 * @param id
	 * @return
	 */
	Group getGroups(Organization org, String id);

	/**
	 * 获取组织
	 * 
	 * @param id
	 * @return
	 */
	Organization getOrganization(String id);

	void onGroupsChange(AbstractProject project);

}
