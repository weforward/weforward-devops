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
package cn.weforward.devops.weforward;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.weforward.common.NameItem;
import cn.weforward.common.ResultPage;
import cn.weforward.common.restful.RestfulRequest;
import cn.weforward.common.restful.RestfulResponse;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.TransList;
import cn.weforward.common.util.TransResultPage;
import cn.weforward.data.UniteId;
import cn.weforward.data.log.BusinessLog;
import cn.weforward.devops.alarm.Alarm;
import cn.weforward.devops.alarm.AlarmService;
import cn.weforward.devops.project.Bind;
import cn.weforward.devops.project.Env;
import cn.weforward.devops.project.Group;
import cn.weforward.devops.project.GroupRight;
import cn.weforward.devops.project.Machine;
import cn.weforward.devops.project.OpTask;
import cn.weforward.devops.project.Project;
import cn.weforward.devops.project.ProjectService;
import cn.weforward.devops.project.Running;
import cn.weforward.devops.project.RunningFile;
import cn.weforward.devops.project.RunningProp;
import cn.weforward.devops.project.VersionInfo;
import cn.weforward.devops.project.impl.DockerMachine;
import cn.weforward.devops.project.impl.HtmlProject;
import cn.weforward.devops.project.impl.JavaProject;
import cn.weforward.devops.project.impl.ProxyMachine;
import cn.weforward.devops.user.GroupProvider;
import cn.weforward.devops.user.Organization;
import cn.weforward.devops.user.OrganizationUser;
import cn.weforward.devops.user.UserProvider;
import cn.weforward.devops.weforward.view.AlarmView;
import cn.weforward.devops.weforward.view.GroupView;
import cn.weforward.devops.weforward.view.GroupsParam;
import cn.weforward.devops.weforward.view.IdAndRightView;
import cn.weforward.devops.weforward.view.LogView;
import cn.weforward.devops.weforward.view.MachineParam;
import cn.weforward.devops.weforward.view.MachineView;
import cn.weforward.devops.weforward.view.MachinesParam;
import cn.weforward.devops.weforward.view.ProjectParam;
import cn.weforward.devops.weforward.view.ProjectView;
import cn.weforward.devops.weforward.view.ProjectsParam;
import cn.weforward.devops.weforward.view.RunningFileView;
import cn.weforward.devops.weforward.view.RunningOpView;
import cn.weforward.devops.weforward.view.RunningView;
import cn.weforward.devops.weforward.view.UserView;
import cn.weforward.framework.ApiException;
import cn.weforward.framework.ResourceException;
import cn.weforward.framework.ResourceHandler;
import cn.weforward.framework.WeforwardMethod;
import cn.weforward.framework.WeforwardMethods;
import cn.weforward.framework.WeforwardResource;
import cn.weforward.framework.WeforwardSession;
import cn.weforward.framework.doc.DocMethods;
import cn.weforward.framework.exception.ForwardException;
import cn.weforward.framework.util.ValidateUtil;
import cn.weforward.framework.util.WeforwardResourceHelper;
import cn.weforward.protocol.Access;
import cn.weforward.protocol.datatype.DtBase;
import cn.weforward.protocol.datatype.DtList;
import cn.weforward.protocol.datatype.DtObject;
import cn.weforward.protocol.datatype.DtString;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocMethod;
import cn.weforward.protocol.doc.annotation.DocParameter;
import cn.weforward.protocol.doc.annotation.DocReturn;
import cn.weforward.protocol.ops.User;
import cn.weforward.protocol.support.datatype.FriendlyObject;
import cn.weforward.protocol.support.datatype.SimpleDtNumber;

/**
 * 
 * 主控制器
 * 
 * @author daibo
 *
 */
@WeforwardMethods(kind = Access.KIND_USER)
@DocMethods(index = 100)
public class HomeMethods implements ResourceHandler {
	/** 日志 */
	static final Logger _Logger = LoggerFactory.getLogger(HomeMethods.class);
	@Resource
	protected ProjectService m_ProjectService;
	@Resource
	protected AlarmService m_AlarmService;
	@Resource
	protected UserProvider m_UserService;
	@Resource
	protected GroupProvider m_GroupProvider;

	final static List<String> ALL_RIGHT = Arrays.asList(ProjectService.RIGHT_UPDATE, ProjectService.RIGHT_OPERATOR,
			ProjectService.RIGHT_READ);

	private static final String STACK_TYPE = "Stack";
	private static final String MEMORYMAP_TYPE = "MemoryMap";

	@WeforwardMethod
	@DocMethod(description = "机器操作方法", index = 1)
	public MachineView machine(MachineParam param) throws ApiException {
		String op = param.getOp();
		Machine m = null;
		String name = StringUtil.toString(param.getName()).trim();
		String url = param.getUrl();
		// nginx参数
		String root = param.getRoot();
		String password = param.getPassword();
		String type = param.getType();
		List<String> envsString = param.getEnvs();
		List<Env> envs = null;
		if (null != envsString) {
			envs = new ArrayList<>();
			for (int i = 0; i < envsString.size(); i++) {
				String v = envsString.get(i);
				int index = v.indexOf("=");
				envs.add(new Env(v.substring(0, index), v.substring(index + 1)));
			}
		}
		List<String> bindsString = param.getBinds();
		List<Bind> binds = null;
		if (null != bindsString) {
			binds = new ArrayList<>();
			for (int i = 0; i < bindsString.size(); i++) {
				String[] cc = bindsString.get(i).split("\\:");
				if (cc.length > 2) {
					binds.add(new Bind(cc[0], cc[1], cc[2]));
				} else if (cc.length == 2) {
					binds.add(new Bind(cc[0], cc[1]));
				}
			}
		}
		List<String> groupsString = param.getGroups();
		List<GroupRight> groups = null;
		List<IdAndRightView> list = param.getGroupList();
		if (null != list) {
			groups = new ArrayList<>(list.size());
			for (IdAndRightView view : list) {
				groups.add(
						new GroupRight(m_GroupProvider.getGroup(getMyOrganization(), view.getId()), view.getRights()));
			}
		} else if (null != groupsString) {
			groups = new ArrayList<>(groupsString.size());
			for (String v : groupsString) {
				groups.add(new GroupRight(m_GroupProvider.getGroup(getMyOrganization(), v), ALL_RIGHT));
			}
		}
		if ("add".equals(op)) {
			if (StringUtil.eq("nginx", type) || StringUtil.eq("proxy", type)) {
				m = m_ProjectService.addProxyMachine(getMyOrganization(), name, url, root, password);
			} else {
				m = m_ProjectService.addDockerMachine(getMyOrganization(), name, url);
			}
			m.setEnvs(envs);
			m.setBinds(binds);
			if (null != groups) {
				m.setGroups(groups);
			}
		} else if ("update".equals(op)) {
			m = m_ProjectService.getMachine(getMyOrganization(), param.getId());
			ForwardException.forwardToIfNeed(m);
			if (!StringUtil.isEmpty(name)) {
				m.setName(name);
			}
			if (null != envs) {
				m.setEnvs(envs);
			}
			if (null != binds) {
				m.setBinds(binds);
			}
			if (m instanceof DockerMachine) {
				if (!StringUtil.isEmpty(url)) {
					((DockerMachine) m).setUrl(url);
				}
			}
			if (m instanceof ProxyMachine) {
				if (!StringUtil.isEmpty(url)) {
					((ProxyMachine) m).setUrl(url);
				}
				if (!StringUtil.isEmpty(password)) {
					((ProxyMachine) m).setPassword(password);
				}
				if (!StringUtil.isEmpty(root)) {
					((ProxyMachine) m).setRoot(root);
				}
			}
			if (null != groups) {
				m.setGroups(groups);
			}
			if (!StringUtil.isEmpty(param.getOwner())) {
				m.setOwner(m_UserService.getUser(param.getOwner()));
			}
		} else if ("delete".equals(op)) {
			m = m_ProjectService.getMachine(getMyOrganization(), param.getId());
			ForwardException.forwardToIfNeed(m);
			m_ProjectService.delete(m);
		}
		return MachineView.valueOf(m);
	}

	@WeforwardMethod
	@DocMethod(description = "查询机器方法", index = 2)
	@DocReturn(description = "机器视图结果集合")
	public ResultPage<MachineView> machines(MachinesParam param) throws ApiException {
		String k = param.getKeywords();
		Project project = null;
		if (!StringUtil.isEmpty(param.getProject())) {
			project = m_ProjectService.getProject(getMyOrganization(), param.getProject());
		}
		@SuppressWarnings("unchecked")
		ResultPage<Machine> rp = (ResultPage<Machine>) m_ProjectService.searchMachines(getMyOrganization(), project, k);
		return new TransResultPage<MachineView, Machine>(rp) {

			@Override
			protected MachineView trans(Machine src) {
				return MachineView.valueOf(src);
			}
		};
	}

	@WeforwardMethod
	@DocMethod(description = "项目操作方法", index = 3)
	public ProjectView project(ProjectParam param) throws ApiException {
		String op = param.getOp();
		Project p = null;
		String name = StringUtil.toString(param.getName()).trim();
		List<String> envsString = param.getEnvs();
		List<Env> envs = null;
		if (null != envsString) {
			envs = new ArrayList<>();
			for (int i = 0; i < envsString.size(); i++) {
				String v = envsString.get(i);
				int index = v.indexOf("=");
				envs.add(new Env(v.substring(0, index), v.substring(index + 1)));
			}
		}
		List<String> bindsString = param.getBinds();
		List<Bind> binds = null;
		if (null != bindsString) {
			binds = new ArrayList<>();
			for (int i = 0; i < bindsString.size(); i++) {
				String[] cc = bindsString.get(i).split("\\:");
				if (cc.length > 2) {
					binds.add(new Bind(cc[0], cc[1], cc[2]));
				} else if (cc.length == 2) {
					binds.add(new Bind(cc[0], cc[1]));
				}
			}
		}
		List<GroupRight> groups = null;
		List<IdAndRightView> list = param.getGroupList();
		List<String> groupsString = param.getGroups();
		if (null != list) {
			groups = new ArrayList<>(list.size());
			for (IdAndRightView view : list) {
				groups.add(
						new GroupRight(m_GroupProvider.getGroup(getMyOrganization(), view.getId()), view.getRights()));
			}
		} else if (null != groupsString) {
			groups = new ArrayList<>(groupsString.size());
			for (String v : groupsString) {
				groups.add(new GroupRight(m_GroupProvider.getGroup(getMyOrganization(), v), ALL_RIGHT));
			}
		}

		String desc = param.getDesc();
		if ("add".equals(op)) {
			p = m_ProjectService.addProject(getMyOrganization(), name, Project.TYPES.get(param.getType()));
			p.setDesc(desc);
			p.setEnvs(envs);
			p.setBinds(binds);
			if (null != groups) {
				p.setGroups(groups);
			}
		} else if ("update".equals(op)) {
			p = m_ProjectService.getProject(getMyOrganization(), param.getId());
			ForwardException.forwardToIfNeed(p);
			if (!StringUtil.isEmpty(name)) {
				p.setName(name);
			}
			if (!StringUtil.isEmpty(desc)) {
				p.setDesc(desc);
			}
			if (null != envs) {
				p.setEnvs(envs);
			}
			if (null != binds) {
				p.setBinds(binds);
			}
			if (null != groups) {
				p.setGroups(groups);
			}
			if (!StringUtil.isEmpty(param.getOwner())) {
				p.setOwner(m_UserService.getUser(param.getOwner()));
			}
		} else if ("delete".equals(op)) {
			p = m_ProjectService.getProject(getMyOrganization(), param.getId());
			m_ProjectService.delete(p);
		}
		if (null != param.getServerPorts()) {
			if (p instanceof JavaProject) {
				try {
					((JavaProject) p).setServerPorts(param.getServerPorts());
				} catch (UnsupportedOperationException e) {
					throw new ApiException(ApiException.CODE_ILLEGAL_ARGUMENT, e.getMessage());
				}
			}
		}
		if (null != param.getAccessUrls()) {
			if (p instanceof HtmlProject) {
				((HtmlProject) p).setAccessUrls(param.getAccessUrls());
			}
		}
		return ProjectView.valueOf(p);
	}

	@WeforwardMethod
	@DocMethod(description = "查询项目方法", index = 4)
	@DocReturn(description = "项目视图结果集合")
	public ResultPage<ProjectView> projects(ProjectsParam param) throws ApiException {
		String k = param.getKeywords();
		String groupid = param.getGroupid();
		String type = param.getType();
		NameItem typeNi = null;
		if (!StringUtil.isEmpty(type)) {
			typeNi = NameItem.findByName(type, ProjectService.TYPES);
		}
		@SuppressWarnings("unchecked")
		ResultPage<Project> rp = (ResultPage<Project>) m_ProjectService.searchProjects(getMyOrganization(), k, groupid,
				typeNi);
		return new TransResultPage<ProjectView, Project>(rp) {

			@Override
			protected ProjectView trans(Project src) {
				return ProjectView.valueOf(src);
			}
		};
	}

	@WeforwardMethod
	@DocParameter({ @DocAttribute(name = "op", type = String.class, description = "操作标识，如:add|delete"),
			@DocAttribute(name = "id", type = String.class, description = "实例id", necessary = true),
			@DocAttribute(name = "props", type = List.class, component = String.class, description = "属性") })
	@DocMethod(description = "实例操作方法", index = 5)
	public RunningOpView running(FriendlyObject params) throws ApiException {
		String op = params.getString("op");
		DtList propString = params.getList("props");
		List<RunningProp> props = null;
		if (null != propString) {
			props = new ArrayList<>();
			for (int i = 0; i < propString.size(); i++) {
				DtBase base = propString.getItem(i);
				if (base instanceof DtString) {
					String v = ((DtString) base).value();
					if (StringUtil.isEmpty(v)) {
						continue;
					}
					int index = v.indexOf("=");
					props.add(
							new RunningProp(v.substring(0, index), v.substring(index + 1), RunningProp.SCOPE_RUNNING));
				} else if (base instanceof DtObject) {
					FriendlyObject v = new FriendlyObject((DtObject) base);
					props.add(new RunningProp(v.getString("key"), StringUtil.toString(v.getString("value")),
							v.getInt("scope", RunningProp.SCOPE_RUNNING)));
				}
			}
		}
		Running p = m_ProjectService.getRunning(getMyOrganization(), params.getString("id"));
		ForwardException.forwardToIfNeed(p);
		if ("add".equals(op)) {
			Machine machine = m_ProjectService.getMachine(getMyOrganization(), params.getString("m"));
			Project project = m_ProjectService.getProject(getMyOrganization(), params.getString("p"));
			if (null == machine) {
				throw new ApiException(ApiException.CODE_ILLEGAL_ARGUMENT, "机器不能为空");
			}
			if (null == project) {
				throw new ApiException(ApiException.CODE_ILLEGAL_ARGUMENT, "项目不能为空");
			}
			p = m_ProjectService.addRunning(getMyOrganization(), project, machine);
			if (null != props) {
				p.setProps(props);
			}
		} else if ("update".equals(op)) {
			Machine machine = m_ProjectService.getMachine(getMyOrganization(), params.getString("m"));
			Project project = m_ProjectService.getProject(getMyOrganization(), params.getString("p"));
			if (null != machine) {
				p.setMachine(machine);
			}
			if (null != project) {
				p.setProject(project);
			}
			if (null != props) {
				p.setProps(props);
			}
		} else if ("upgrade".equals(op)) {
			p = m_ProjectService.getRunning(getMyOrganization(), params.getString("id"));
			String version = params.getString("version");
			OpTask task = p.upgrade(version, params.getString("note"));
			return RunningOpView.valueOf(task);
		} else if ("rollback".equals(op)) {
			p = m_ProjectService.getRunning(getMyOrganization(), params.getString("id"));
			OpTask task = p.rollback(params.getString("note"));
			return RunningOpView.valueOf(task);
		} else if ("restart".equals(op)) {
			p = m_ProjectService.getRunning(getMyOrganization(), params.getString("id"));
			OpTask task = p.restart(params.getString("note"));
			return RunningOpView.valueOf(task);
		} else if ("start".equals(op)) {
			p = m_ProjectService.getRunning(getMyOrganization(), params.getString("id"));
			OpTask task = p.start(params.getString("note"));
			return RunningOpView.valueOf(task);
		} else if ("stop".equals(op)) {
			p = m_ProjectService.getRunning(getMyOrganization(), params.getString("id"));
			OpTask task = p.stop(params.getString("note"));
			return RunningOpView.valueOf(task);
		} else if ("get_versions".equals(op)) {
			p = m_ProjectService.getRunning(getMyOrganization(), params.getString("id"));
			List<VersionInfo> versions = p.getUpgradeVersions();
			List<String> array = new ArrayList<>();
			for (VersionInfo v : versions) {
				array.add(v.toString());
			}
			return new RunningOpView(p, array);
		} else if ("delete".equals(op)) {
			p = m_ProjectService.getRunning(getMyOrganization(), params.getString("id"));
			m_ProjectService.delete(p);
		}
		return new RunningOpView(p, Collections.emptyList());
	}

	@SuppressWarnings("unchecked")
	@WeforwardMethod
	@DocMethod(description = "查询实例方法", index = 6)
	@DocParameter({ @DocAttribute(name = "keywords", description = "查询关键字"),
			@DocAttribute(name = "groupid", description = "分组查询"),
			@DocAttribute(name = "type", description = "类型，如:JAVA|HTML") })
	@DocReturn(description = "项目实例结果集合")
	public ResultPage<RunningView> runnings(FriendlyObject params) throws ApiException {
		String k = params.getString("keywords");
		String groupid = params.getString("groupid");
		String type = params.getString("type");
		NameItem typeNi = null;
		if (!StringUtil.isEmpty(type)) {
			typeNi = NameItem.findByName(type, ProjectService.TYPES);
		}
		ResultPage<Running> rp;
		if (!StringUtil.isEmpty(UniteId.getType(k))) {
			Running r = m_ProjectService.getRunning(getMyOrganization(), k);
			if (null != r) {
				rp = ResultPageHelper.singleton(r);
			} else {
				rp = (ResultPage<Running>) m_ProjectService.searchRunnings(getMyOrganization(), k, groupid, typeNi);
			}
		} else {
			rp = (ResultPage<Running>) m_ProjectService.searchRunnings(getMyOrganization(), k, groupid, typeNi);
		}
		return new TransResultPage<RunningView, Running>(rp) {

			@Override
			protected RunningView trans(Running src) {
				return RunningView.valueOf(src);
			}
		};
	}

	@WeforwardMethod
	@DocMethod(description = "实例操作日志", index = 7)
	@DocParameter(@DocAttribute(name = "id", description = "实例id"))
	@DocReturn(description = "日志视图结果集合")
	public ResultPage<LogView> runningoplogs(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		Running p = m_ProjectService.getRunning(getMyOrganization(), id);
		ForwardException.forwardToIfNeed(p);
		ResultPage<BusinessLog> rp = p.getOpLogs();
		return new TransResultPage<LogView, BusinessLog>(rp) {

			@Override
			protected LogView trans(BusinessLog src) {
				return LogView.valueOf(src);
			}
		};
	}

	@WeforwardMethod
	@DocMethod(description = "实例控制台日志", index = 8)
	@DocParameter(@DocAttribute(name = "id", description = "实例id"))
	@DocReturn(description = "实例控制台日志结果集合")
	public ResultPage<String> runningsyslogs(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		Running p = m_ProjectService.getRunning(getMyOrganization(), id);
		ForwardException.forwardToIfNeed(p);
		ResultPage<String> rp = p.getSysLogs();
		rp = ResultPageHelper.reverseResultPage(rp);
		return rp;
	}

	/* 兼容旧的页面 */
	@WeforwardMethod
	@Deprecated
	public String printstack(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		ValidateUtil.isEmpty(id, "id不能为空");
		String name = params.getString("name");
		Running r = m_ProjectService.getRunning(getMyOrganization(), id);
		ValidateUtil.isEmpty(r, "找不到[" + id + "]对应的实例");
		((DockerMachine) r.getMachine()).printStack(r.getProject());
		return r.printStack(name).getId().getId();
	}

	@WeforwardMethod
	@DocMethod(description = "打印实例堆栈", index = 9)
	@DocParameter({ @DocAttribute(name = "id", description = "实例id", necessary = true),
			@DocAttribute(name = "name", description = "文件名", necessary = false) })
	@DocReturn(description = "操作任务id")
	public String printStack(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		ValidateUtil.isEmpty(id, "id不能为空");
		String name = params.getString("name");
		Running r = m_ProjectService.getRunning(getMyOrganization(), id);
		ValidateUtil.isEmpty(r, "找不到[" + id + "]对应的实例");
		return r.printStack(name).getId().getId();
	}

	@WeforwardMethod
	@DocMethod(description = "查看实例堆栈", index = 10)
	@DocParameter({ @DocAttribute(name = "id", description = "实例id", necessary = true),
			@DocAttribute(name = "name", description = "文件名", necessary = true) })
	@DocReturn(description = "堆栈数据")
	public String stack(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		ValidateUtil.isEmpty(id, "id不能为空");
		String name = params.getString("name");
		ValidateUtil.isEmpty(name, "name不能为空");
		Running r = m_ProjectService.getRunning(getMyOrganization(), id);
		ValidateUtil.isEmpty(r, "找不到[" + id + "]对应的实例");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		r.downStack(name, out);
		return new String(out.toByteArray());
	}

	@WeforwardMethod
	@DocMethod(description = "查看实例堆栈列表", index = 11)
	@DocParameter(@DocAttribute(name = "id", description = "实例id", necessary = true))
	@DocReturn(description = "堆栈列表文件名")
	public ResultPage<RunningFileView> stacks(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		ValidateUtil.isEmpty(id, "id不能为空");
		Running r = m_ProjectService.getRunning(getMyOrganization(), id);
		ValidateUtil.isEmpty(r, "找不到[" + id + "]对应的实例");
		List<RunningFileView> list = TransList.valueOf(r.listStacks(), (e) -> RunningFileView.valueOf(e));
		return ResultPageHelper.toResultPage(list);
	}

	@WeforwardMethod
	@DocMethod(description = "下载实例堆栈", index = 12)
	@DocParameter({ @DocAttribute(name = "id", description = "实例id", necessary = true),
			@DocAttribute(name = "name", description = "文件名", necessary = true) })
	public WeforwardResource downStack(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		ValidateUtil.isEmpty(id, "id不能为空");
		String name = params.getString("name");
		ValidateUtil.isEmpty(name, "name不能为空");
		return WeforwardResourceHelper.valueOf(id + UniteId.OBJECT_SPEARATOR + UniteId.valueOf(name, STACK_TYPE, ""),
				30 * 60 * 1000);
	}

	@WeforwardMethod
	@DocMethod(description = "打印实例内存", index = 13)
	@DocParameter({ @DocAttribute(name = "id", description = "实例id", necessary = true),
			@DocAttribute(name = "name", description = "文件名", necessary = false) })
	@DocReturn(description = "操作任务id")
	public String printMemorymap(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		ValidateUtil.isEmpty(id, "id不能为空");
		String name = params.getString("name");
		Running r = m_ProjectService.getRunning(getMyOrganization(), id);
		ValidateUtil.isEmpty(r, "找不到[" + id + "]对应的实例");
		return r.printMemoryMap(name).getId().getId();
	}

	@WeforwardMethod
	@DocMethod(description = "查看实例内存列表", index = 14)
	@DocParameter(@DocAttribute(name = "id", description = "实例id", necessary = true))
	@DocReturn(description = "内存列表文件名")
	public ResultPage<RunningFileView> memoryMaps(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		ValidateUtil.isEmpty(id, "id不能为空");
		Running r = m_ProjectService.getRunning(getMyOrganization(), id);
		ValidateUtil.isEmpty(r, "找不到[" + id + "]对应的实例");
		List<RunningFileView> list = TransList.valueOf(
				m_ProjectService.getRunning(getMyOrganization(), id).listMemoryMaps(),
				(e) -> RunningFileView.valueOf(e));
		return ResultPageHelper.toResultPage(list);
	}

	@WeforwardMethod
	@DocMethod(description = "下载实例堆栈", index = 15)
	@DocParameter({ @DocAttribute(name = "id", description = "实例id"),
			@DocAttribute(name = "name", description = "文件名") })
	public WeforwardResource downMemoryMap(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		ValidateUtil.isEmpty(id, "id不能为空");
		String name = params.getString("name");
		ValidateUtil.isEmpty(name, "name不能为空");
		return WeforwardResourceHelper
				.valueOf(id + UniteId.OBJECT_SPEARATOR + UniteId.valueOf(name, MEMORYMAP_TYPE, ""), 30 * 60 * 1000);
	}

	@WeforwardMethod
	@DocMethod(description = "查询任务", index = 16)
	@DocParameter(@DocAttribute(name = "id", description = "操作任务id"))
	public RunningOpView optask(FriendlyObject params) throws ApiException {
		String id = params.getString("id");
		OpTask task = m_ProjectService.getOpTask(id);
		return RunningOpView.valueOf(task);
	}

	@WeforwardMethod
	@DocMethod(description = "群组查询方法", index = 17)
	@DocReturn(description = "群组结果集合")
	public ResultPage<GroupView> groups(GroupsParam params) throws ApiException {
		String keywords = params.getKeywords();
		@SuppressWarnings("unchecked")
		ResultPage<Group> rp = (ResultPage<Group>) m_GroupProvider.searchGroups(getMyOrganization(), keywords);
		rp = ResultPageHelper.reverseResultPage(rp);
		return new TransResultPage<GroupView, Group>(rp) {

			@Override
			protected GroupView trans(Group src) {
				return GroupView.valueOf(src);
			}
		};
	}

	@WeforwardMethod
	@DocMethod(description = "用户查询方法", index = 18)
	@DocParameter(@DocAttribute(name = "k", type = String.class, necessary = true, description = "查询关键字", example = "张三"))
	@DocReturn(description = "用户结果集合")
	public ResultPage<UserView> users(FriendlyObject params) throws ApiException {
		String keywords = params.getString("k");
		if (StringUtil.isEmpty(keywords)) {
			return ResultPageHelper.empty();
		}
		ResultPage<User> rp = m_UserService.searchUser(keywords);
		return new TransResultPage<UserView, User>(rp) {

			@Override
			protected UserView trans(User src) {
				return UserView.valueOf(src);
			}
		};
	}

	@WeforwardMethod
	@DocMethod(description = "推荐端口", index = 19)
	@DocReturn(description = "端口号")
	public DtBase recommendedServerPort() {
		return SimpleDtNumber.valueOf(m_ProjectService.recommendedServerPort());
	}

	@WeforwardMethod
	@DocParameter({
			@DocAttribute(name = "begin", type = Date.class, necessary = true, description = "开始时间", example = "2020-12-23T03:00:00.000Z"),
			@DocAttribute(name = "end", type = Date.class, necessary = true, description = "结束时间", example = "2020-12-23T05:00:00.000Z"),
			@DocAttribute(name = "keywords", type = String.class, necessary = false, description = "查询关键字", example = "张三"),
			@DocAttribute(name = "page", type = int.class, necessary = false, description = "页数", example = "1") })
	@DocMethod(description = "报警列表", index = 20)
	public ResultPage<AlarmView> alarms(FriendlyObject params) throws ApiException {
		Date begin = params.getDate("begin");
		ValidateUtil.isNull(begin, "开始时间不能为空");
		Date end = params.getDate("end");
		ValidateUtil.isNull(end, "结束时间不能为空");
		String keywords = params.getString("keywords");
		ResultPage<Alarm> rp = m_AlarmService.searchAlarms(begin, end, keywords);
		return TransResultPage.valueOf(rp, (a) -> AlarmView.valueOf(a));
	}

	@Override
	public boolean handle(RestfulRequest request, RestfulResponse response) throws IOException, ResourceException {
		String resourceId = request.getParams().get("id");
		if (StringUtil.isEmpty(resourceId)) {
			return false;
		}
		int index = resourceId.indexOf(UniteId.OBJECT_SPEARATOR);
		if (index <= 0) {
			return false;
		}
		String runningId = resourceId.substring(0, index);
		String fileid = resourceId.substring(index + 1);
		Running r = m_ProjectService.getRunning(getMyOrganization(), runningId);
		if (null == r) {
			return false;
		}
		String type = UniteId.getType(fileid);
		if (StringUtil.eq(type, STACK_TYPE)) {
			String filename = UniteId.getOrdinal(fileid);
			RunningFile file = r.getStack(filename);
			if (null == file) {
				response.setStatus(RestfulResponse.STATUS_NOT_FOUND);
				response.openOutput().close();
				return true;
			}
			response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
			response.setHeader("Content-Length", String.valueOf(file.length()));
			response.setStatus(RestfulResponse.STATUS_OK);
			try (OutputStream out = response.openOutput()) {
				r.downStack(filename, out);
			}
			return true;
		} else if (StringUtil.eq(type, MEMORYMAP_TYPE)) {
			String filename = UniteId.getOrdinal(fileid);
			RunningFile file = r.getMemoryMap(filename);
			if (null == file) {
				response.setStatus(RestfulResponse.STATUS_NOT_FOUND);
				response.openOutput().close();
				return true;
			}
			response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
			response.setHeader("Content-Length", String.valueOf(file.length()));
			response.setStatus(RestfulResponse.STATUS_OK);
			try (OutputStream out = response.openOutput()) {
				r.downMemoryMap(filename, out);
			}
			return true;
		} else {
			return false;
		}
	}

	private Organization getMyOrganization() {
		OrganizationUser user = WeforwardSession.TLS.getUser();
		return user.getOrganization();
	}

}
