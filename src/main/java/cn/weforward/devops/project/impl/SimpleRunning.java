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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import cn.weforward.common.NameItem;
import cn.weforward.common.ResultPage;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.TransList;
import cn.weforward.data.UniteId;
import cn.weforward.data.log.BusinessLog;
import cn.weforward.data.persister.Persister;
import cn.weforward.data.persister.Reloadable;
import cn.weforward.data.persister.support.AbstractPersistent;
import cn.weforward.data.search.IndexKeyword;
import cn.weforward.data.search.SearchableExt;
import cn.weforward.data.search.util.IndexElementHelper;
import cn.weforward.data.search.util.IndexKeywordHelper;
import cn.weforward.devops.project.Envs;
import cn.weforward.devops.project.Machine;
import cn.weforward.devops.project.OpTask;
import cn.weforward.devops.project.Project;
import cn.weforward.devops.project.Prop;
import cn.weforward.devops.project.Running;
import cn.weforward.devops.project.RunningFile;
import cn.weforward.devops.project.RunningProp;
import cn.weforward.devops.project.VersionInfo;
import cn.weforward.devops.project.di.ProjectDi;
import cn.weforward.devops.project.ext.AbstractMachine;
import cn.weforward.devops.user.Group;
import cn.weforward.devops.user.GroupRight;
import cn.weforward.devops.user.Organization;

/**
 * 运行的项目
 * 
 * @author daibo
 *
 */
public class SimpleRunning extends AbstractPersistent<ProjectDi>
		implements Running, SearchableExt, Reloadable<SimpleRunning> {
	/** 项目 */
	@Resource(type = String.class)
	private UniteId m_Project;
	/** 机器 */
	@Resource(type = String.class)
	private UniteId m_Machine;
	/** 状态 */
	@Resource
	private int m_State;
	@Resource
	private String m_Organization;
	/** 最后查询状态的时间戳 */
	private long m_LastQueryState;

	/** 排序 */
	final static Comparator<Running> _BY_SERVERID = new Comparator<Running>() {

		@Override
		public int compare(Running o1, Running o2) {
			int v = 0;
			String s1 = ((SimpleRunning) o1).getServerid();
			String s2 = ((SimpleRunning) o1).getServerid();
			v = s2.compareTo(s1);
			if (v == 0) {
				String n1 = o1.getMachine().getName();
				String n2 = o2.getMachine().getName();
				if (n1.length() == n2.length()) {
					v = n1.compareTo(n2);
				} else {
					v = n1.length() - n2.length();
				}
			}
			if (v == 0) {
				String n1 = o1.getProject().getName();
				String n2 = o2.getProject().getName();
				if (n1.length() == n2.length()) {
					v = n1.compareTo(n2);
				} else {
					v = n1.length() - n2.length();
				}
			}
			return v;
		}
	};

	/** 项目范围的服务id */
	public static final String PROJECT_SERVICE_ID = "x00ff";

	protected SimpleRunning(ProjectDi di) {
		super(di);
	}

	public SimpleRunning(ProjectDi di, Organization org, Project project, Machine machine) {
		super(di);
		genPersistenceId();
		m_Project = project.getPersistenceId();
		m_Machine = machine.getPersistenceId();
		m_Organization = org.getId();
		persistenceUpdateNow();
		reindex();
	}

	public String getServerid() {
		return Envs.getServerid(getMachine());
	}

	public UniteId getId() {
		return getPersistenceId();
	}

	@Override
	public void setProject(Project project) {
		m_Project = project.getPersistenceId();
		markPersistenceUpdate(PERSISTENCE_REINDEX);
	}

	@Override
	public Project getProject() {
		return getBusinessDi().getProject(m_Project.getId());
	}

	@Override
	public void setMachine(Machine machine) {
		m_Machine = machine.getPersistenceId();
		markPersistenceUpdate(PERSISTENCE_REINDEX);
	}

	@Override
	public Machine getMachine() {
		return getBusinessDi().getMachine(m_Machine.getId());
	}

	@Override
	public List<RunningProp> getProps() {
		List<RunningProp> result = new ArrayList<>();
		String label = genLabel(m_Organization, getProject().getName());
		{
			ServiceProperties ele = getBusinessDi().getServiceProperties(label, PROJECT_SERVICE_ID);
			if (null != ele) {
				for (Prop p : ele) {
					result.add(new RunningProp(p.getKey(), p.getValue(), RunningProp.SCOPE_PROJECT));
				}
			}
		}
		{
			String sid = getServerid();
			ServiceProperties ele = getBusinessDi().getServiceProperties(label, sid);
			if (null != ele) {
				for (Prop p : ele) {
					result.add(new RunningProp(p.getKey(), p.getValue(), RunningProp.SCOPE_RUNNING));
				}
			}
		}
		return result;

	}

	public static String genLabel(String org, String name) {
		org = org.replace(UniteId.TYPE_SPEARATOR, '_');
		return org + "." + name;
	}

	@Override
	public void setProps(List<RunningProp> props) {
		checkRight(RIGHT_EDIT);
		List<Prop> rprop = new ArrayList<>();// running scope
		List<Prop> pprop = new ArrayList<>();// project scope
		for (RunningProp p : props) {
			if (p.getScope() == RunningProp.SCOPE_RUNNING) {
				rprop.add(new Prop(p.getKey(), p.getValue()));
			} else if (p.getScope() == RunningProp.SCOPE_PROJECT) {
				pprop.add(new Prop(p.getKey(), p.getValue()));
			}
		}
		String label = genLabel(m_Organization, getProject().getName());
		getBusinessDi().saveServiceProperties(label, new ServiceProperties(getServerid(), rprop));
		getBusinessDi().saveServiceProperties(label, new ServiceProperties(PROJECT_SERVICE_ID, pprop));
	}

	@Override
	public OpTask upgrade(String version, String note) {
		checkRight(RIGHT_UPGRADE);
		writeLog("upgrade", "升级系统到" + version + "版本", note);
		OpTask task = new SimpleOpTask(getBusinessDi(), this, version, SimpleOpTask.ACTION_UPGRADE, note);
		return task;
	}

	@Override
	public OpTask rollback(String note) {
		checkRight(RIGHT_ROLLBACK);
		writeLog("rollback", "回滚版本", note);
		return createTask(SimpleOpTask.ACTION_ROLLBACK);
	}

	@Override
	public OpTask start(String note) {
		checkRight(RIGHT_START);
		writeLog("start", "启动项目", note);
		return createTask(SimpleOpTask.ACTION_START);
	}

	@Override
	public OpTask stop(String note) {
		checkRight(RIGHT_STOP);
		writeLog("stop", "停止项目", note);
		return createTask(SimpleOpTask.ACTION_STOP);
	}

	@Override
	public OpTask restart(String note) {
		checkRight(RIGHT_RESTART);
		writeLog("restart", "重启项目", note);
		return createTask(SimpleOpTask.ACTION_RESTART);
	}

	@Override
	public NameItem getState() {
		checkState();
		return STATES.get(m_State);
	}

	private void writeLog(String action, String what, String note) {
		getBusinessDi().writeLog(getId(), action, what, note);
	}

	/**
	 * 检查状态
	 */
	public void checkState() {
		if (System.currentTimeMillis() - m_LastQueryState < 5000) {
			return;// 5秒查一次
		}
		Machine m = getMachine();
		NameItem ni = null;
		if (m instanceof AbstractMachine) {
			ni = ((AbstractMachine) m).queryState(getProject());
		}
		if (ni != null) {
			m_State = ni.id;
		} else {
			m_State = STATE_UNKNOWN.id;
		}
		m_LastQueryState = System.currentTimeMillis();
	}

	@Override
	public VersionInfo getCurrentVersion() {
		return ((AbstractMachine) getMachine()).queryCurrentVersion(this);
	}

	@Override
	public List<VersionInfo> getUpgradeVersions() {
		List<VersionInfo> vs = ((AbstractMachine) getMachine()).queryUpgradeVersions(this);
//		if (vs.size() > 10) {
//			return vs.subList(0, 10);
//		}
		return vs;
	}

	@Override
	public ResultPage<BusinessLog> getOpLogs() {
		return getBusinessDi().getLogs(getId());
	}

	@Override
	public ResultPage<String> getSysLogs() {
		ResultPage<String> rp = ((AbstractMachine) getMachine()).queryLogs(getProject());
		return ResultPageHelper.reverseResultPage(rp);
	}

	@Override
	public boolean isSupportPrintStack() {
		return getMachine() instanceof DockerMachine;
	}

	@Override
	public OpTask printStack(String filename) {
		writeLog("printStack", "打印堆栈", null);
		OpTask task = new SimpleOpTask(getBusinessDi(), this, filename, SimpleOpTask.ACTION_PRINT_STACK, null);
		return task;
	}

	@Override
	public List<RunningFile> listStacks() {
		List<File> files = ((DockerMachine) getMachine()).listStacks(getProject());
		return TransList.valueOf(files, (f) -> new RunningFileWrap(f));
	}

	@Override
	public RunningFile getStack(String filenalme) {
		Optional<RunningFile> o = listStacks().stream().filter((e) -> StringUtil.eq(filenalme, e.getName()))
				.findFirst();
		return o.isPresent() ? o.get() : null;
	}

	@Override
	public void downStack(String filename, OutputStream out) {
		((DockerMachine) getMachine()).downStack(getProject(), filename, out);
	}

	@Override
	public boolean isSupportMemoryMap() {
		return getMachine() instanceof DockerMachine;
	}

	@Override
	public OpTask printMemoryMap(String filename) {
		writeLog("printStack", "打印内存", null);
		OpTask task = new SimpleOpTask(getBusinessDi(), this, filename, SimpleOpTask.ACTION_PRINT_MEMORYMAP, null);
		return task;
	}

	@Override
	public List<RunningFile> listMemoryMaps() {
		List<File> files = ((DockerMachine) getMachine()).listMemoryMaps(getProject());
		return TransList.valueOf(files, (f) -> new RunningFileWrap(f));
	}

	@Override
	public RunningFile getMemoryMap(String filenalme) {
		Optional<RunningFile> o = listMemoryMaps().stream().filter((e) -> StringUtil.eq(filenalme, e.getName()))
				.findFirst();
		return o.isPresent() ? o.get() : null;
	}

	@Override
	public void downMemoryMap(String filename, OutputStream out) {
		((DockerMachine) getMachine()).downMemoryMap(getProject(), filename, out);
	}

	private OpTask createTask(int action) {
		return new SimpleOpTask(getBusinessDi(), this, action);
	}

	public int getRight() {
		int r = 0;
		Project p = getProject();
		if (null != p) {
			if (p.isRight(Project.RIGHT_UPDATE)) {
				r |= RIGHT_UPGRADE | RIGHT_ROLLBACK | RIGHT_EDIT;
			}
			if (p.isRight(Project.RIGHT_OPERATOR)) {
				if (getMachine() instanceof DockerMachine) {
					r |= RIGHT_START | RIGHT_STOP | RIGHT_RESTART;
				}
			}
			if (p.isRight(Project.RIGHT_READ)) {
				r |= RIGHT_SHOW;
			}
		}
		return r;
	}

	private void checkRight(int r) {
		if (!isRight(r)) {
			throw new UnsupportedOperationException("无权限执行此方法");
		}
	}

	@Override
	public boolean isRight(int r) {
		return r == (r & getRight());
	}

	public boolean isMyOrganization(Organization org) {
		return StringUtil.eq(m_Organization, org.getId());
	}

	@Override
	public void reindex() {
		getBusinessDi().getRunningSearcher().updateElement(IndexElementHelper.newElement(getId().getId()),
				getIndexKeywords());
	}

	@Override
	public List<IndexKeyword> getIndexKeywords() {
		List<IndexKeyword> ks = new ArrayList<>();
		ks = IndexKeywordHelper.addKeywordIfNotNull(ks, m_Machine.getId(), 0);
		ks = IndexKeywordHelper.addKeywordIfNotNull(ks, m_Project.getId(), 0);
		ks = IndexKeywordHelper.addKeywordIfNotNull(ks, m_Organization, 0);
		Project project = getProject();
		for (GroupRight gr : project.getGroups()) {
			if (null == gr) {
				continue;
			}
			Group g = gr.getGroup();
			if (null == g) {
				continue;
			}
			ks = IndexKeywordHelper.addKeywordIfNotNull(ks, g.getId(), 0);
		}
		return ks;
	}

	@Override
	public boolean onReloadAccepted(Persister<SimpleRunning> persister, SimpleRunning other) {
		m_Project = other.m_Project;
		m_Machine = other.m_Machine;
		m_State = other.m_State;
		return true;
	}

}
