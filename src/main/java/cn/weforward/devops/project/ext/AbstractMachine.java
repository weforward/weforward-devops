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
package cn.weforward.devops.project.ext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.weforward.common.NameItem;
import cn.weforward.common.ResultPage;
import cn.weforward.common.util.FreezedList;
import cn.weforward.common.util.NumberUtil;
import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.TimeUtil;
import cn.weforward.data.UniteId;
import cn.weforward.data.annotation.Index;
import cn.weforward.data.annotation.ResourceExt;
import cn.weforward.data.persister.support.AbstractPersistent;
import cn.weforward.devops.project.Bind;
import cn.weforward.devops.project.Env;
import cn.weforward.devops.project.Machine;
import cn.weforward.devops.project.MachineInfo;
import cn.weforward.devops.project.OpProcessor;
import cn.weforward.devops.project.OpProcessor.Status;
import cn.weforward.devops.project.Project;
import cn.weforward.devops.project.ProjectService;
import cn.weforward.devops.project.Running;
import cn.weforward.devops.project.RunningProp;
import cn.weforward.devops.project.VersionInfo;
import cn.weforward.devops.project.di.ProjectDi;
import cn.weforward.devops.project.impl.HtmlProject;
import cn.weforward.devops.project.impl.IdAndRight;
import cn.weforward.devops.project.impl.JavaProject;
import cn.weforward.devops.user.Group;
import cn.weforward.devops.user.GroupRight;
import cn.weforward.devops.user.Organization;
import cn.weforward.framework.WeforwardSession;
import cn.weforward.protocol.ops.AccessExt;
import cn.weforward.protocol.ops.User;
import cn.weforward.util.HttpInvoker;
import cn.weforward.util.OperatorUtils;

/**
 * 抽象机器实现
 * 
 * @author daibo
 *
 */
public abstract class AbstractMachine extends AbstractPersistent<ProjectDi> implements Machine {
	/** 日志 */
	protected static final Logger _Logger = LoggerFactory.getLogger(AbstractMachine.class);
	/** 最大版本数 */
	protected static final int MAX_VERSION_NUM = 10;
	/** 名称 */
	@Resource
	protected String m_Name;
	/** 绑定 */
	@ResourceExt(component = Bind.class)
	protected List<Bind> m_Binds;
	/** 环境 */
	@ResourceExt(component = Env.class)
	protected List<Env> m_Envs;
	/** 所有者 */
	@Resource(type = String.class)
	protected UniteId m_Owner;
	/** 项目组和权限 */
	@ResourceExt(component = IdAndRight.class)
	protected List<IdAndRight> m_GroupRights;
	/** 所在组织 */
	@Index
	@Resource
	protected String m_Organization;
	/** 机器信息 */
	protected MachineInfo m_Info;
	/** 版本排序 */
	protected final static Comparator<VersionInfo> _BY_VERSION = new Comparator<VersionInfo>() {

		@Override
		public int compare(VersionInfo o1, VersionInfo o2) {
			String[] arr1 = o1.getVersion().split("\\.");
			String[] arr2 = o2.getVersion().split("\\.");
			int l = Math.min(arr1.length, arr2.length);
			for (int i = 0; i < l; i++) {
				int v = NumberUtil.toInt(arr2[i], 0) - NumberUtil.toInt(arr1[i], 0);
				if (v == 0) {
					continue;
				}
				return v;
			}
			int v = arr2.length - arr1.length;
			if (v == 0) {
				return o2.getVersion().compareTo(o1.getVersion());
			}
			return v;
		}
	};

	protected AbstractMachine(ProjectDi di) {
		super(di);
	}

	protected AbstractMachine(ProjectDi di, Organization org, String name) {
		super(di);
		m_Name = name;
		m_Owner = OperatorUtils.getOperator();
		m_GroupRights = Collections.emptyList();
		m_Organization = org.getId();
	}

	protected void processor(OpProcessor p, String desc) {
		if (null != p) {
			p.progesser(new Status(desc));
		}
	}

	@Override
	public String getName() {
		return m_Name;
	}

	@Override
	public void setName(String v) {
		if (StringUtil.eq(m_Name, v)) {
			return;
		}
		checkRight(RIGHT_UPDATE);
		m_Name = v;
		markPersistenceUpdate();
	}

	@Override
	public void setOwner(User user) {
		checkRight(RIGHT_UPDATE);
		m_Owner = null == user ? null : UniteId.valueOf(user.getId());
		markPersistenceUpdate();
	}

	@Override
	public User getOwner() {
		return null == m_Owner ? null : getBusinessDi().getUser(m_Owner.getId());
	}

	@Override
	public void setBinds(List<Bind> binds) {
		checkRight(RIGHT_UPDATE);
		m_Binds = binds;
		markPersistenceUpdate();
	}

	@Override
	public List<Bind> getBinds() {
		if (null == m_Binds) {
			return Collections.emptyList();
		}
		return m_Binds;
	}

	@Override
	public List<Env> getEnvs() {
		return null == m_Envs ? Collections.emptyList() : m_Envs;
	}

	@Override
	public void setEnvs(List<Env> envs) {
		checkRight(RIGHT_UPDATE);
		m_Envs = FreezedList.freezed(envs);
		markPersistenceUpdate();
	}

	@Override
	public void setGroups(List<GroupRight> groups) {
		checkRight(RIGHT_UPDATE);
		List<IdAndRight> list = new ArrayList<>();
		for (GroupRight g : groups) {
			list.add(new IdAndRight(g.getGroup().getId(), toRight(g.getRights())));
		}
		m_GroupRights = list;
		markPersistenceUpdate();
	}

	private static int toRight(List<String> rights) {
		int r = 0;
		for (String v : rights) {
			if (ProjectService.RIGHT_UPDATE.equalsIgnoreCase(v)) {
				r |= Machine.RIGHT_UPDATE;
			} else if (ProjectService.RIGHT_OPERATOR.equalsIgnoreCase(v)) {
				r |= Machine.RIGHT_OPERATOR;
			} else if (ProjectService.RIGHT_READ.equalsIgnoreCase(v)) {
				r |= Machine.RIGHT_READ;
			}
		}
		return r;
	}

	@Override
	public List<GroupRight> getGroups() {
		if (null == m_GroupRights) {
			return Collections.emptyList();
		}
		List<GroupRight> list = new ArrayList<>();
		for (IdAndRight id : m_GroupRights) {
			Group g = getBusinessDi().getGroups(getOrganization(), id.getId());
			if (null == g) {
				continue;
			}
			list.add(new GroupRight(g, toRights(id.getRight())));
		}
		return list;
	}

	public Organization getOrganization() {
		return getBusinessDi().getOrganization(m_Organization);
	}

	private static List<String> toRights(int right) {
		List<String> rights = new ArrayList<>();
		if (isRight(RIGHT_UPDATE, right)) {
			rights.add(ProjectService.RIGHT_UPDATE);
		} else if (isRight(RIGHT_OPERATOR, right)) {
			rights.add(ProjectService.RIGHT_OPERATOR);
		} else if (isRight(RIGHT_READ, right)) {
			rights.add(ProjectService.RIGHT_READ);
		}
		return rights;
	}

	public void upgrade(Running running, String version, OpProcessor processor) {
		this.upgrade(running, null, null, version, processor);
	}

	/**
	 * 升级
	 * 
	 * @param running   运行
	 * @param envs      环境变量
	 * @param binds     绑定
	 * @param version   版本
	 * @param processor 进度条
	 */
	public abstract void upgrade(Running running, List<Env> envs, List<Bind> binds, String version,
			OpProcessor processor);

	/**
	 * 回滚
	 * 
	 * @param project
	 * @param processor
	 */
	public abstract void rollback(Running running, OpProcessor processor);

	public abstract NameItem queryState(Project project);

	/**
	 * 查询当前版本
	 * 
	 * @param running
	 * @return
	 */
	public abstract VersionInfo queryCurrentVersion(Running running);

	protected HttpInvoker getInvoker(String accessId, String accessKey) throws IOException {
		HttpInvoker invoker = new HttpInvoker(1, 30);
		invoker.setUserName(accessId);
		invoker.setPassword(accessKey);
		return invoker;
	}

	/**
	 * 查询可升级版本
	 * 
	 * @param running
	 * @return
	 */
	public List<VersionInfo> queryUpgradeVersions(Running running) {
		Project project = running.getProject();
		String url = genUrl(project);
		String accessId = getAccessId(running);
		if (StringUtil.isEmpty(accessId)) {
			throw new UnsupportedOperationException("请先配置" + RunningProp.WEFORWARD_SERVICE_ACCESS_ID);
		}
		String accessKey = getAccessKey(running);
		if (StringUtil.isEmpty(accessKey)) {
			throw new UnsupportedOperationException("请先配置" + RunningProp.WEFORWARD_SERVICE_ACCESS_KEY);
		}

		HttpGet post = new HttpGet(url);
		HttpResponse response = null;
		String json;
		try {
			HttpInvoker invoker = getInvoker(accessId, accessKey);
			response = invoker.execute(post);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				if (status.getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
					throw new UnsupportedOperationException("accessId或accessKey不正确");
				}
				throw new IOException("status返回异常状态码:" + status);
			}
			json = EntityUtils.toString(response.getEntity(), Charset.defaultCharset());
		} catch (IOException e) {
			throw new IllegalArgumentException("获取版本异常", e);
		} finally {
			if (null != response) {
				try {
					EntityUtils.consume(response.getEntity());
				} catch (IOException e) {
				}
			}
		}
		JSONArray array = new JSONArray(json);
		List<VersionInfo> list = new ArrayList<>(array.length());
		for (int i = 0; i < array.length(); i++) {
			JSONObject o = array.getJSONObject(i);
			list.add(new VersionInfo(o.optString("name"), TimeUtil.parseDate(o.optString("time"))));
		}
		Collections.sort(list, _BY_VERSION);
		return list;
	}

	protected String genUrl(Project project) {
		return getBusinessDi().getDownloadUrl() + project.getOrganization().getId() + "/" + getType(project) + "/"
				+ project.getName() + "/";
	}

	protected static String getType(Project project) {
		if (project instanceof JavaProject) {
			return "java";
		} else if (project instanceof HtmlProject) {
			return "html";
		} else {
			return "unknown";
		}
	}

	/**
	 * 查询系统日志
	 * 
	 * @param project
	 * @return
	 */
	public abstract ResultPage<String> queryLogs(Project project);

	private boolean isOwner(User user) {
		if (null == user) {
			return false;
		}
		if (null == m_Owner) {
			return true;
		}
		if (StringUtil.eq(m_Owner.getOrdinal(), UniteId.getOrdinal(user.getId()))) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isGroup(User user) {
		for (GroupRight g : getGroups()) {
			if (null == g) {
				continue;
			}
			Group group = g.getGroup();
			if (null == group) {
				continue;
			}
			if (group.include(user)) {
				return true;
			}
		}
		return false;
	}

	public int getRight() {
		int r = 0;
		User user = WeforwardSession.TLS.getOperator();
		if (isOwner(user)) {
			r |= RIGHT_UPDATE;
		} else if (isGroup(user)) {
			r |= RIGHT_UPDATE;
		} else {
			r = getBusinessDi().getDefaultMachineRight();
		}
		return r;
	}

	private void checkRight(int r) {
		if (!isRight(r)) {
			throw new UnsupportedOperationException("无权限执行此方法");
		}
	}

	@Override
	public void setInfo(MachineInfo info) {
		m_Info = info;
	}

	@Override
	public MachineInfo getInfo() {
		return m_Info;
	}

	@Override
	public boolean isRight(int r) {
		return isRight(r, getRight());
	}

	private static boolean isRight(int r, int right) {
		return r == (r & right);
	}

	protected static String getAccessId(Running running) {
		for (RunningProp prop : running.getProps()) {
			if (StringUtil.eq(prop.getKey(), RunningProp.WEFORWARD_SERVICE_ACCESS_ID)) {
				return prop.getValue();
			}
		}
		return null;
	}

	protected static String getAccessKey(Running running) {
		for (RunningProp prop : running.getProps()) {
			if (StringUtil.eq(prop.getKey(), RunningProp.WEFORWARD_SERVICE_ACCESS_KEY)) {
				return prop.getValue();
			}
		}
		return null;
	}

	public boolean isMyOrganization(Organization org) {
		return StringUtil.eq(m_Organization, org.getId());
	}

	public boolean isMyOrganization(AccessExt access) {
		return StringUtil.eq(m_Organization, access.getGroupId());
	}

	@Override
	public String toString() {
		return getPersistenceId() + "," + getName();
	}

}
