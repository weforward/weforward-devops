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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import cn.weforward.common.NameItem;
import cn.weforward.common.ResultPage;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.TransResultPage;
import cn.weforward.data.array.LabelSetFactory;
import cn.weforward.data.log.BusinessLoggerFactory;
import cn.weforward.data.persister.Persister;
import cn.weforward.data.persister.PersisterFactory;
import cn.weforward.data.persister.ext.ConditionUtil;
import cn.weforward.data.search.IndexResult;
import cn.weforward.data.search.IndexResults;
import cn.weforward.data.search.SearcherFactory;
import cn.weforward.devops.project.Group;
import cn.weforward.devops.project.Machine;
import cn.weforward.devops.project.OpTask;
import cn.weforward.devops.project.Project;
import cn.weforward.devops.project.ProjectService;
import cn.weforward.devops.project.Prop;
import cn.weforward.devops.project.Running;
import cn.weforward.devops.project.RunningProp;
import cn.weforward.devops.project.ext.AbstractMachine;
import cn.weforward.devops.project.ext.AbstractProject;
import cn.weforward.devops.user.Organization;
import cn.weforward.framework.ApiException;
import cn.weforward.protocol.ops.AccessExt;
import cn.weforward.util.OperatorUtils;

/**
 * 项目服务实现
 * 
 * @author daibo
 *
 */
public class ProjectServiceImpl extends ProjectDiImpl implements ProjectService {

	public ProjectServiceImpl(PersisterFactory psFactroy, SearcherFactory searcherFactory, LabelSetFactory lsFactory,
			BusinessLoggerFactory loggerHub) {
		super(psFactroy, searcherFactory, lsFactory, loggerHub);
	}

	@Override
	public Machine addDockerMachine(Organization org, String name, String url) {
		return new DockerMachine(this, org, name, url);
	}

	@Override
	public Machine addProxyMachine(Organization org, String name, String url, String root, String password) {
		return new ProxyMachine(this, org, name, url, root, password);
	}

	@Override
	public Machine getMachine(Organization org, String id) {
		AbstractMachine m = getMachine(id);
		if (null == m) {
			return null;
		}
		if (!m.isRight(Project.RIGHT_READ) || !m.isMyOrganization(org)) {
			return null;
		}
		return m;
	}

	@Override
	public void delete(Machine m) throws ApiException {
		if (!m.isRight(Machine.RIGHT_UPDATE)) {
			return;
		}
		_Logger.info(OperatorUtils.getOperator() + "删除" + m.getName());
		IndexResults irs = getRunningSearcher().search(null, m.getPersistenceId().getId());
		for (IndexResult ir : ResultPageHelper.toForeach(irs)) {
			if (null != m_PsSimpleRunning.get(ir.getKey())) {
				throw new ApiException(ApiException.CODE_ILLEGAL_ARGUMENT, "存在运行的实例，无法删除本容器");
			}
		}
		if (m instanceof DockerMachine) {
			m_PsDockerMachine.remove(m.getPersistenceId());
		} else if (m instanceof ProxyMachine) {
			m_PsProxyMachine.remove(m.getPersistenceId());
		} else {
			throw new ApiException(ApiException.CODE_ILLEGAL_ARGUMENT, "不支持删除本容器");
		}

	}

	@Override
	public ResultPage<? extends Machine> searchMachines(Organization org, Project project, String keywords) {
		List<Persister<? extends AbstractMachine>> pss;
		if (project instanceof JavaProject) {
			pss = Arrays.asList((Persister<? extends AbstractMachine>) m_PsDockerMachine);
		} else if (project instanceof HtmlProject) {
			pss = Arrays.asList((Persister<? extends AbstractMachine>) m_PsProxyMachine);
		} else {
			pss = m_Machines;
		}
		List<Machine> list = new ArrayList<>();
		for (Persister<? extends AbstractMachine> ps : pss) {
			ResultPage<? extends AbstractMachine> rp = ps
					.search(ConditionUtil.eq(ConditionUtil.field("m_Organization"), org.getId()));
			for (AbstractMachine p : ResultPageHelper.toForeach(rp)) {
				if (null == p || !p.isRight(Machine.RIGHT_READ)) {
					continue;
				}
				if (!isMatch(keywords, p)) {
					continue;
				}
				list.add(p);
			}
		}
		return ResultPageHelper.toResultPage(list);
	}

	@Override
	public Project addProject(Organization org, String name, NameItem type) {
		if (type.id == Project.TYPE_JAVA.id) {
			return new JavaProject(this, org, name);
		} else if (type.id == Project.TYPE_HTML.id) {
			return new HtmlProject(this, org, name);
		} else {
			throw new UnsupportedOperationException("不支持的类型" + type);
		}
	}

	@Override
	public Project getProject(Organization org, String id) {
		AbstractProject p = getProject(id);
		if (null == p) {
			return null;
		}
		if (!p.isRight(Project.RIGHT_READ) || !p.isMyOrganization(org)) {
			return null;
		}
		return p;
	}

	@Override
	public synchronized void delete(Project p) throws ApiException {
		if (!p.isRight(Project.RIGHT_UPDATE)) {
			return;
		}
		IndexResults irs = getRunningSearcher().search(null, p.getPersistenceId().getId());
		for (IndexResult ir : ResultPageHelper.toForeach(irs)) {
			if (null != m_PsSimpleRunning.get(ir.getKey())) {
				throw new ApiException(ApiException.CODE_ILLEGAL_ARGUMENT, "存在运行的实例，无法删除本项目");
			}
		}
		_Logger.info(OperatorUtils.getOperator() + "删除" + p.getName());
		if (p instanceof JavaProject) {
			m_PsJavaProject.remove(p.getPersistenceId());
		} else if (p instanceof HtmlProject) {
			m_PsHtmlProject.remove(p.getPersistenceId());
		} else {
			throw new ApiException(ApiException.CODE_ILLEGAL_ARGUMENT, "不支持删除本项目");
		}
	}

	@Override
	public ResultPage<? extends Project> searchProjects(Organization org, Group group, String keywords, NameItem type) {
		ResultPage<? extends Project> rp;
		if (null != group) {
			IndexResults irs = getProjectSearcher().search(null, group.getId());
			rp = TransResultPage.valueOf(irs, (e) -> getProject(e.getKey()));
		} else if (null != org) {
			IndexResults irs = getProjectSearcher().search(null, org.getId());
			rp = TransResultPage.valueOf(irs, (e) -> getProject(e.getKey()));
		} else {
			return ResultPageHelper.empty();
		}
		List<Project> list = new ArrayList<>();
		for (Project p : ResultPageHelper.toForeach(rp)) {
			if (null == p || !p.isRight(Project.RIGHT_READ)) {
				continue;
			}
			if (!isMatch(type, p)) {
				continue;
			}
			if (!isMatch(keywords, p)) {
				continue;
			}
			list.add(p);
		}
		return ResultPageHelper.toResultPage(list);
	}

	@Override
	public OpTask getOpTask(String id) {
		return m_PsSimpleOpTask.get(id);
	}

	@Override
	public synchronized Running addRunning(Organization org, Project project, Machine machine) {
		IndexResults irs = getRunningSearcher().search(null, machine.getPersistenceId().getId());
		for (IndexResult ir : ResultPageHelper.toForeach(irs)) {
			SimpleRunning run = m_PsSimpleRunning.get(ir.getKey());
			if (null != run && run.getProject().equals(project)) {
				throw new UnsupportedOperationException("同一机器不能存在两个相同实例");
			}
		}
		return new SimpleRunning(this, org, project, machine);
	}

	@Override
	public Running getRunning(Organization org, String id) {
		SimpleRunning r = m_PsSimpleRunning.get(id);
		if (null == r || !r.isMyOrganization(org)) {
			return null;
		}
		return r.isRight(Running.RIGHT_SHOW) ? r : null;
	}

	@Override
	public void delete(Running p) {
		if (!p.isRight(Running.RIGHT_UPGRADE)) {
			return;
		}
		_Logger.info(OperatorUtils.getOperator() + "删除" + p.getId());
		m_PsSimpleRunning.remove(p.getId());
	}

	@Override
	public ResultPage<? extends Running> searchRunnings(Organization org, Group group, String keywords, NameItem type) {
		ResultPage<? extends Running> rp;
		if (null != group) {
			IndexResults irs = getRunningSearcher().search(null, group.getId());
			rp = TransResultPage.valueOf(irs, (e) -> m_PsSimpleRunning.get(e.getKey()));
		} else if (null != org) {
			IndexResults irs = getRunningSearcher().search(null, org.getId());
			rp = TransResultPage.valueOf(irs, (e) -> m_PsSimpleRunning.get(e.getKey()));
		} else {
			return ResultPageHelper.empty();
		}
		List<Running> list = new ArrayList<>();
		for (Running r : ResultPageHelper.toForeach(rp)) {
			if (null == r || !r.isRight(Running.RIGHT_SHOW)) {
				continue;
			}
			// 匹配关键字
			if (!isMatch(keywords, r)) {
				continue;
			}
			if (!isMatch(type, r)) {
				continue;
			}
			list.add(r);
		}
		Collections.sort(list, SimpleRunning._BY_SERVERID);
		rp = ResultPageHelper.toResultPage(list);
		return rp;
	}

	@Override
	public int recommendedServerPort(Organization org) {
		int start = 16000;
		int i = start;
		for (; i < 65535; i++) {
			ResultPage<JavaProject> rp = getJavaProjects(org);
			boolean use = false;
			for (JavaProject project : ResultPageHelper.toForeach(rp)) {
				for (Integer v : project.getServerPorts()) {
					if (v.equals(i)) {
						use = true;
						break;
					}
				}
				if (use) {
					break;
				}
			}
			if (use) {
				continue;
			} else {
				return i;
			}
		}
		return i;
	}

	@Override
	public List<Prop> loadServiceProperties(String projectName, String serverId, String accessId) {
		List<Prop> list = new ArrayList<>();
		AccessExt access = m_AccessKeeper.getAccess(accessId);
		if (null == access) {
			_Logger.warn(projectName + "," + serverId + "," + accessId + "未找到对应组织返回空集!");
			return Collections.emptyList();
		}
		String label = SimpleRunning.genLabel(access.getGroupId(), projectName);
		ServiceProperties runningProps = getServiceProperties(label, serverId);
		boolean matchAccess = false;
		for (Prop p : runningProps.getProps()) {
			if (StringUtil.eq(p.getKey(), RunningProp.WEFORWARD_SERVICE_ACCESS_ID)
					&& StringUtil.eq(accessId, p.getValue())) {
				matchAccess = true;
			} else if (RunningProp.isBootStartProp(p.getKey())) {
				continue;
			}
			list.add(p);
		}

		ServiceProperties projectProps = getServiceProperties(label, SimpleRunning.PROJECT_SERVICE_ID);
		for (Prop prop : projectProps.getProps()) {
			if (StringUtil.eq(prop.getKey(), RunningProp.WEFORWARD_SERVICE_ACCESS_ID)
					&& StringUtil.eq(accessId, prop.getValue())) {
				matchAccess = true;
			} else if (RunningProp.isBootStartProp(prop.getKey())) {
				continue;
			}
			if (containsKey(list, prop)) {
				continue;
			}
			list.add(prop);
		}
		if (null != m_GlobalProperties) {
			m_GlobalProperties.forEach((key, value) -> list.add(new Prop(String.valueOf(key), String.valueOf(value))));
		}
		if (matchAccess) {
			return list;
		} else {
			_Logger.warn(projectName + "," + serverId + "," + accessId + "越权获取配置返回空集!");
			return Collections.emptyList();
		}

	}

	private static boolean containsKey(List<Prop> list, Prop prop) {
		for (Prop p : list) {
			if (StringUtil.eq(p.getKey(), prop.getKey())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Machine findMachine(String accessId, String name) {
		AccessExt access = m_AccessKeeper.getAccess(accessId);
		if (null == access) {
			return null;
		}
		for (Persister<? extends AbstractMachine> ps : m_Machines) {
			ResultPage<? extends AbstractMachine> rp = ps.startsWith(null);
			for (AbstractMachine p : ResultPageHelper.toForeach(rp)) {
				if (null == p) {
					continue;
				}
				if (StringUtil.eq(name, p.getName()) && p.isMyOrganization(access)) {
					return p;
				}
			}
		}
		return null;
	}

	@Override
	public ResultPage<SimpleRunning> getProjectRunnings(String projectid) {
		IndexResults irs = m_RunningSearcher.search(null, projectid);
		if (irs.getCount() > 0) {
			return new TransResultPage<SimpleRunning, IndexResult>(irs) {
				@Override
				protected SimpleRunning trans(IndexResult src) {
					return m_PsSimpleRunning.get(src.getKey());
				}
			};
		}
		return ResultPageHelper.empty();
	}

	@Value("${devops.defaultMachineRight:}")
	public void setDefaultMachineRightFormat(String format) {
		if (StringUtil.isEmpty(format)) {
			m_DefaultMachineRight = 0;
			return;
		}
		String arr[] = format.split(";");
		for (String v : arr) {
			if (RIGHT_READ.equalsIgnoreCase(v)) {
				m_DefaultMachineRight |= Machine.RIGHT_UPDATE;
			} else if (RIGHT_OPERATOR.equalsIgnoreCase(v)) {
				m_DefaultMachineRight |= Machine.RIGHT_OPERATOR;
			} else if (RIGHT_READ.equalsIgnoreCase(v)) {
				m_DefaultMachineRight |= Machine.RIGHT_READ;
			}
		}
	}

	@Value("${devops.defaultProjectRight:}")
	public void setDefaultProjectRightFormat(String format) {
		if (StringUtil.isEmpty(format)) {
			m_DefaultMachineRight = 0;
			return;
		}
		String arr[] = format.split(";");
		for (String v : arr) {
			if (RIGHT_READ.equalsIgnoreCase(v)) {
				m_DefaultProjectRight |= Project.RIGHT_UPDATE;
			} else if (RIGHT_OPERATOR.equalsIgnoreCase(v)) {
				m_DefaultProjectRight |= Project.RIGHT_OPERATOR;
			} else if (RIGHT_READ.equalsIgnoreCase(v)) {
				m_DefaultProjectRight |= Project.RIGHT_READ;
			}
		}
	}

	@Override
	public void onGroupsChange(AbstractProject project) {
		IndexResults irs = getRunningSearcher().search(null, project.getId().getId());
		for (IndexResult ir : ResultPageHelper.toForeach(irs)) {
			SimpleRunning r = m_PsSimpleRunning.get(ir.getKey());
			if (null != r) {
				r.reindex();
			}
		}

	}

	private static boolean isMatch(String keywords, Machine p) {
		if (StringUtil.isEmpty(keywords)) {
			return true;
		}
		if (p.getName().contains(keywords)) {
			return true;
		}
		return false;
	}

	private static boolean isMatch(String keywords, Project p) {
		if (StringUtil.isEmpty(keywords)) {
			return true;
		}
		if (p.getName().contains(keywords)) {
			return true;
		}
		return false;
	}

	private static boolean isMatch(NameItem type, Project p) {
		if (null == type) {
			return true;
		}
		if (type.id == TYPE_JAVA.id) {
			return (p instanceof JavaProject);
		}
		if (type.id == TYPE_HTML.id) {
			return (p instanceof HtmlProject);
		}
		return false;
	}

	/* 是否匹配 */
	private boolean isMatch(String keywords, Running r) {
		if (StringUtil.isEmpty(keywords)) {
			return true;
		}
		if (isMatch(keywords, r.getProject())) {
			return true;
		}
		if (isMatch(keywords, r.getMachine())) {
			return true;
		}
		return false;
	}

	private boolean isMatch(NameItem type, Running r) {
		return isMatch(type, r.getProject());
	}

}
