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
package cn.weforward.devops.project.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.TaskExecutor;
import cn.weforward.common.util.TimeUtil;
import cn.weforward.common.util.TransResultPage;
import cn.weforward.data.UniteId;
import cn.weforward.data.array.LabelSet;
import cn.weforward.data.array.LabelSetFactory;
import cn.weforward.data.log.BusinessLog;
import cn.weforward.data.log.BusinessLogger;
import cn.weforward.data.log.BusinessLoggerFactory;
import cn.weforward.data.persister.Persistent;
import cn.weforward.data.persister.Persister;
import cn.weforward.data.persister.PersisterFactory;
import cn.weforward.data.search.IndexKeyword;
import cn.weforward.data.search.IndexResults;
import cn.weforward.data.search.Searcher;
import cn.weforward.data.search.SearcherFactory;
import cn.weforward.data.search.util.IndexKeywordHelper;
import cn.weforward.data.util.FieldMapper;
import cn.weforward.devops.project.Group;
import cn.weforward.devops.project.Machine;
import cn.weforward.devops.project.Project;
import cn.weforward.devops.project.di.ProjectDi;
import cn.weforward.devops.project.ext.AbstractMachine;
import cn.weforward.devops.project.ext.AbstractProject;
import cn.weforward.devops.user.GroupProvider;
import cn.weforward.devops.user.Organization;
import cn.weforward.devops.user.OrganizationProvider;
import cn.weforward.devops.user.UserProvider;
import cn.weforward.protocol.ops.User;
import cn.weforward.util.OperatorUtils;

/**
 * 项目依赖di
 * 
 * @author daibo
 *
 */
public abstract class ProjectDiImpl implements ProjectDi {
	/** 日志 */
	static final Logger _Logger = LoggerFactory.getLogger(ProjectDiImpl.class);
	/** 持久工厂 */
	protected PersisterFactory m_PsFactroy;
	/** 标签工厂 */
	protected LabelSet<ServiceProperties> m_PropLabel;
	/** dockerHub地址 */
	protected String m_DockerHubUrl;
	/** dockerHub地址 */
	protected String m_DockerHubHttpsUrl;
	/** dockerHub用户名 */
	protected String m_DockerHubUsername;
	/** dockerHub用户密码 */
	protected String m_DockerHubPassword;
	/** dockerHub用户邮箱 */
	protected String m_DockerHubEmail;
	/** Proxy发行版本地址 */
	protected String m_ProxyDistUrl;
	/** Docker发行版本地址 */
	protected String m_DockerDistUrl;
	/** svn用户名 */
	protected String m_SvnUsername;
	/** svn密码 */
	protected String m_SvnPassword;
	/** 远程日志 */
	protected String m_RlogUrl;
	/** 资源链接 */
	protected String m_ResourceUrl;
	/** 云链接 */
	protected String m_ApiUrl;
	/** docker机器 */
	protected final Persister<DockerMachine> m_PsDockerMachine;
	/** nginx机器 */
	protected final Persister<ProxyMachine> m_PsProxyMachine;
	/** 机器持久类 */
	protected final List<Persister<? extends AbstractMachine>> m_Machines;
	/** 项目持久类 */
	protected final Persister<JavaProject> m_PsJavaProject;
	/** 项目持久类 */
	protected final Persister<HtmlProject> m_PsHtmlProject;
	/** 机器持久类 */
	protected final List<Persister<? extends AbstractProject>> m_Projects;
	/** 运行项目持久类 */
	protected final Persister<SimpleRunning> m_PsSimpleRunning;
	/** 操作任务 */
	protected final Persister<SimpleOpTask> m_PsSimpleOpTask;

	/** 日志记录器 */
	protected final BusinessLogger m_DevopsLoggers;
	/** 搜索器 */
	protected Searcher m_RunningSearcher;
	/** 搜索器 */
	protected Searcher m_ProjectSearcher;
	/** 用户服务 */
	protected UserProvider m_UserService;
	/** 用户组服务 */
	protected GroupProvider m_GroupService;
	/** 组织服务 */
	protected OrganizationProvider m_OrganizationProvider;
	/** 任务执行器 */
	protected TaskExecutor m_TaskExecutor;
	/** dist帐号 */
	protected String m_DistUserName;
	/** dist密码 */
	protected String m_DistPassword;
	/** 系统配置 */
	@Resource(name = "globalProperties")
	protected Properties m_GlobalProperties;

	@Value("${devops.datapath}")
	protected String m_DataPath;

	@Value("${devops.maxHistory}")
	protected int m_MaxHistory;

	protected int m_DefaultMachineRight;

	protected int m_DefaultProjectRight;

	protected TaskExecutor.Task m_ClearTask;

	public ProjectDiImpl(PersisterFactory psFactroy, SearcherFactory searcherFactory, LabelSetFactory lsFactory,
			BusinessLoggerFactory loggerFactory) {
		m_PsFactroy = psFactroy;
		m_RunningSearcher = searcherFactory.createSearcher("running_doc");
		m_ProjectSearcher = searcherFactory.createSearcher("project_doc");
		m_PropLabel = lsFactory.createLabelSet("devops_serviceprops", FieldMapper.valueOf(ServiceProperties.class));
		m_Machines = new ArrayList<>();
		m_Projects = new ArrayList<>();
		m_PsDockerMachine = registerMachine(DockerMachine.class);
		m_PsProxyMachine = registerMachine(ProxyMachine.class);
		m_PsJavaProject = registerProject(JavaProject.class);
		m_PsHtmlProject = registerProject(HtmlProject.class);
		m_PsSimpleRunning = m_PsFactroy.createPersister(SimpleRunning.class, this);
		m_PsSimpleOpTask = m_PsFactroy.createPersister(SimpleOpTask.class, this);
		m_DevopsLoggers = loggerFactory.createLogger("devops_log");
	}

	@Override
	public AbstractProject getProject(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		Object v = m_PsFactroy.get(id);
		if (v instanceof Project) {
			AbstractProject p = ((AbstractProject) v);
			return p;
		}
		return null;
	}

	@Override
	public AbstractMachine getMachine(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		Object v = m_PsFactroy.get(id);
		if (v instanceof Machine) {
			AbstractMachine m = ((AbstractMachine) v);
			return m;
		}
		return null;
	}

	@Override
	public File getStackDir(Machine machine, Project project) {
		File dir = new File(m_DataPath,
				"stack" + File.separator + machine.getName() + File.separator + project.getName());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	@Override
	public File getMermoryMapDir(Machine machine, Project project) {
		File dir = new File(m_DataPath,
				"mermorymap" + File.separator + machine.getName() + File.separator + project.getName());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	private <E extends AbstractMachine> Persister<E> registerMachine(Class<E> clazz) {
		Persister<E> ps = m_PsFactroy.createPersister(clazz, this);
		m_Machines.add(ps);
		return ps;
	}

	private <E extends AbstractProject> Persister<E> registerProject(Class<E> clazz) {
		Persister<E> ps = m_PsFactroy.createPersister(clazz, this);
		m_Projects.add(ps);
		return ps;
	}

	@Override
	public <E extends Persistent> Persister<E> getPersister(Class<E> clazz) {
		return m_PsFactroy.getPersister(clazz);
	}

	public void setUserService(UserProvider us) {
		m_UserService = us;
	}

	public void setGroupService(GroupProvider gs) {
		m_GroupService = gs;
	}

	public void setOrganizationProvider(OrganizationProvider os) {
		m_OrganizationProvider = os;
	}

	public void setDockerHubUrl(String v) {
		m_DockerHubUrl = v;
	}

	@Override
	public ServiceProperties getServiceProperties(String label, String sid) {
		ServiceProperties s = m_PropLabel.get(label, sid);
		return null == s ? ServiceProperties.EMPTY : s;
	}

	@Override
	public void saveServiceProperties(String label, ServiceProperties ele) {
		m_PropLabel.put(label, ele);
	}

	@Override
	public String getDockerHubUrl() {
		return m_DockerHubUrl;
	}

	public void setDockerHubHttpsUrl(String url) {
		m_DockerHubHttpsUrl = url;
	}

	@Override
	public String getDockerHubHttpsUrl() {
		return m_DockerHubHttpsUrl;
	}

	public void setDockerHubUsername(String v) {
		m_DockerHubUsername = v;
	}

	@Override
	public String getDockerHubUsername() {
		return m_DockerHubUsername;
	}

	public void setDockerHubPassword(String v) {
		m_DockerHubPassword = v;
	}

	@Override
	public String getDockerHubPassword() {
		return m_DockerHubPassword;
	}

	public void setDockerHubEmail(String v) {
		m_DockerHubEmail = v;
	}

	@Override
	public String getDockerHubEmail() {
		return m_DockerHubEmail;
	}

	public void setProxyDistUrl(String v) {
		m_ProxyDistUrl = v;
	}

	public void setDockerDistUrl(String v) {
		m_DockerDistUrl = v;
	}

	@Override
	public String getProxyDistUrl() {
		return m_ProxyDistUrl;
	}

	@Override
	public String getDockerDistUrl() {
		return m_DockerDistUrl;
	}

	public void setRlogUrl(String url) {
		m_RlogUrl = url;
	}

	@Override
	public String getRlogUrl() {
		return m_RlogUrl;
	}

	public void setResourceUrl(String url) {
		m_ResourceUrl = url;
	}

	@Override
	public String getResourceUrl() {
		return m_ResourceUrl;
	}

	public void setApiUrl(String url) {
		m_ApiUrl = url;
	}

	@Override
	public String getApiUrl() {
		return m_ApiUrl;
	}

	public synchronized void setTaskExecutor(TaskExecutor exe) {
		m_TaskExecutor = exe;
		if (null == m_ClearTask) {
			m_ClearTask = m_TaskExecutor.execute(new Runnable() {

				@Override
				public void run() {
					clear();
				}
			}, TaskExecutor.OPTION_NONE, 5 * 60 * 1000, TimeUtil.HOUR_MILLS);
		}
	}

	@Override
	public TaskExecutor getTaskExecutor() {
		return m_TaskExecutor;
	}

	@Override
	public void writeLog(UniteId id, String action, String what, String note) {
		String author;
		UniteId user = OperatorUtils.getOperator();
		if (null == user) {
			author = "System";
		} else {
			author = user.toString();
		}
		m_DevopsLoggers.writeLog(id.getId().replace('$', '_'), author, action, what, note);
	}

	@Override
	public ResultPage<BusinessLog> getLogs(UniteId id) {
		return ResultPageHelper.reverseResultPage(m_DevopsLoggers.getLogs(id.getId().replace('$', '_')));
	}

	@Override
	public User getUser(String id) {
		try {
			return m_UserService.getUser(id);
		} catch (Exception e) {
			_Logger.warn("忽略获取用户异常", e);
			return null;
		}
	}

	@Override
	public Group getGroups(Organization org, String id) {
		return m_GroupService.getGroup(org, id);
	}

	@Override
	public Organization getOrganization(String id) {
		return m_OrganizationProvider.get(id);
	}

	@Override
	public ResultPage<JavaProject> getJavaProjects(Organization org) {
		List<IndexKeyword> ks = Arrays.asList(IndexKeywordHelper.prefixKeyword(UniteId.getType(JavaProject.class)),
				IndexKeywordHelper.newKeyword(org.getId()));
		IndexResults irs = getProjectSearcher().search(ks, null);
		return TransResultPage.valueOf(irs, (id) -> m_PsJavaProject.get(id.getKey()));
	}

	public void setDistUserName(String name) {
		m_DistUserName = name;
	}

	@Override
	public String getDistUserName() {
		return StringUtil.toString(m_DistUserName);
	}

	public void setDistPassword(String password) {
		m_DistPassword = password;
	}

	@Override
	public String getDistPassword() {
		return StringUtil.toString(m_DistPassword);
	}

	@Override
	public Searcher getRunningSearcher() {
		return m_RunningSearcher;
	}

	@Override
	public Searcher getProjectSearcher() {
		return m_ProjectSearcher;
	}

	protected void clear() {
		int h = m_MaxHistory;
		if (h <= 0) {
			return;
		}
		ResultPage<SimpleRunning> rp = m_PsSimpleRunning.startsWith(null);
		for (int i = 1; rp.gotoPage(i); i++) {
			for (SimpleRunning r : rp) {
				try {
					r.getMachine().clear(r.getProject(), h);
				} catch (Throwable e) {
					_Logger.warn("忽略清理出错", e);
				}
			}
		}
	}

	@Override
	public int getDefaultMachineRight() {
		return m_DefaultMachineRight;
	}

	@Override
	public int getDefaultProjectRight() {
		return m_DefaultProjectRight;
	}

}
