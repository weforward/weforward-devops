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
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.TaskExecutor;
import cn.weforward.data.UniteId;
import cn.weforward.data.annotation.ResourceExt;
import cn.weforward.data.persister.support.AbstractPersistent;
import cn.weforward.devops.project.OpProcessor;
import cn.weforward.devops.project.OpTask;
import cn.weforward.devops.project.Project;
import cn.weforward.devops.project.Running;
import cn.weforward.devops.project.di.ProjectDi;
import cn.weforward.devops.project.ext.AbstractMachine;

/**
 * 简单的任务
 * 
 * @author daibo
 *
 */
public class SimpleOpTask extends AbstractPersistent<ProjectDi> implements OpTask, OpProcessor, Runnable {
	/** 运行 */
	@Resource(type = String.class)
	protected UniteId m_Running;
	/** 版本 */
	@Resource
	protected String m_Version;
	/** 创建时间 */
	@Resource
	protected Date m_CreateTime;
	/** 状态 */
	@ResourceExt(component = Status.class)
	protected List<Status> m_Status = Collections.emptyList();
	/** 行为 */
	@Resource
	protected int m_Action;
	@Resource
	protected String m_Note;
	/** 任务 */
	private TaskExecutor.Task m_Task;

	/** 行为-升级 */
	public static final int ACTION_UPGRADE = 0;
	/** 行为-启动 */
	public static final int ACTION_START = 1;
	/** 行为-停止 */
	public static final int ACTION_STOP = 2;
	/** 行为-重启 */
	public static final int ACTION_RESTART = 3;
	/** 行为-回滚 */
	public static final int ACTION_ROLLBACK = 4;
	/** 行为-构建 */
	public static final int ACTION_BUILD = 5;
	/** 行为-打印堆栈 */
	public static final int ACTION_PRINT_STACK = 6;
	/** 行为-打印内存 */
	public static final int ACTION_PRINT_MEMORYMAP = 7;

	protected SimpleOpTask(ProjectDi di) {
		super(di);
	}

	public SimpleOpTask(ProjectDi di, Running running, int action) {
		this(di, running, null, action, null);
	}

	public SimpleOpTask(ProjectDi di, Running running, String version, int action, String note) {
		super(di);
		genPersistenceId();
		m_Running = running.getPersistenceId();
		m_Version = version;
		m_CreateTime = new Date();
		m_Action = action;
		m_Note = note;
		markPersistenceUpdate();
		start();
	}

	public UniteId getId() {
		return getPersistenceId();
	}

	public Running getRunning() {
		return getBusinessDi().getPersister(SimpleRunning.class).get(m_Running);
	}

	public synchronized void start() {
		if (null != m_Task) {
			return;
		}
		TaskExecutor exe = getBusinessDi().getTaskExecutor();
		m_Task = exe.execute(this, TaskExecutor.OPTION_NONE, 1);
	}

	public synchronized void stop() {
		if (null != m_Task) {
			m_Task.cancel();
			m_Task = null;
		}
	}

	@Override
	public void run() {
		AbstractMachine m = (AbstractMachine) getRunning().getMachine();
		Project p = getRunning().getProject();
		switch (m_Action) {
		case ACTION_RESTART:
			if (m instanceof DockerMachine) {
				((DockerMachine) m).restart(p, this);
			}
			break;
		case ACTION_START:
			if (m instanceof DockerMachine) {
				((DockerMachine) m).start(p, this);
			}
			break;
		case ACTION_STOP:
			if (m instanceof DockerMachine) {
				((DockerMachine) m).stop(p, this);
			}
			break;
		case ACTION_ROLLBACK:
			m.rollback(getRunning(), this);
			break;
		case ACTION_UPGRADE:
			m.upgrade(getRunning(), m_Version, this, StringUtil.toString(m_Note));
			break;
		case ACTION_BUILD:
			if (m instanceof DockerMachine) {
				((DockerMachine) m).build(getRunning(), m_Version, this);
			}
			break;
		case ACTION_PRINT_STACK:
			if (m instanceof DockerMachine) {
				((DockerMachine) m).printStack(getRunning().getProject(), m_Version, this);
			}
			break;
		case ACTION_PRINT_MEMORYMAP:
			if (m instanceof DockerMachine) {
				((DockerMachine) m).printMemoryMap(getRunning().getProject(), m_Version, this);
			}
			break;
		}
		synchronized (this) {
			m_Task = null;
		}
		// 任务执行完成
		getBusinessDi().taskCompleted(this);
	}

	@Override
	public Status getCurrentStatus() {
		if (m_Status.isEmpty()) {
			return null;
		}
		return m_Status.get(m_Status.size() - 1);
	}

	@Override
	public List<Status> getStatus() {
		return m_Status;
	}

	@Override
	public void progesser(Status status) {
		List<Status> newlist = new ArrayList<>();
		List<Status> olds = m_Status;
		boolean replace = false;
		for (Status s : olds) {
			if (!StringUtil.isEmpty(s.getId()) && s.getId().equals(status.getId())) {
				newlist.add(status);
				replace = true;
			} else {
				newlist.add(s);
			}
		}
		if (!replace) {
			newlist.add(status);
		}
		m_Status = newlist;

	}

	@Override
	public boolean isDone() {
		return null == m_Task;
	}

	public String getVersion() {
		return m_Version;
	}

	public String getNote() {
		return m_Note;
	}

	public String getActionName() {
		switch (m_Action) {
		/** 行为-升级 */
		case ACTION_UPGRADE:
			return "upgrade";
		/** 行为-启动 */
		case ACTION_START:
			return "start";
		/** 行为-停止 */
		case ACTION_STOP:
			return "stop";
		/** 行为-重启 */
		case ACTION_RESTART:
			return "restart";
		/** 行为-回滚 */
		case ACTION_ROLLBACK:
			return "rollback";
		/** 行为-构建 */
		case ACTION_BUILD:
			return "build";
		/** 行为-打印堆栈 */
		case ACTION_PRINT_STACK:
			return "jstack";
		/** 行为-打印内存 */
		case ACTION_PRINT_MEMORYMAP:
			return "jmap";
		}
		return String.valueOf(m_Action);
	}

}
