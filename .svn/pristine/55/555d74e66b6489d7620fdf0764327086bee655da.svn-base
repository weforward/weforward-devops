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
package cn.weforward.devops.weforward.view;

import java.util.Collections;
import java.util.List;

import cn.weforward.common.NameItem;
import cn.weforward.devops.project.OpProcessor;
import cn.weforward.devops.project.OpTask;
import cn.weforward.devops.project.Running;
import cn.weforward.devops.project.VersionInfo;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

/**
 * 实例操作视图
 * 
 * @author daibo
 *
 */
@DocObject(description = "实例操作视图")
public class RunningOpView {
	/** id */
	protected Running m_Running;
	/** 版本号 */
	protected List<String> m_Versions;
	/** 操作任务 */
	protected OpTask m_Task;

	static final int MAX = 200;

	/**
	 * 构造
	 * 
	 * @param task
	 */
	public RunningOpView(OpTask task) {
		m_Task = task;
	}

	/**
	 * 构造
	 * 
	 * @param id
	 * @param versions
	 */
	public RunningOpView(Running running, List<String> versions) {
		m_Running = running;
		m_Versions = versions;
	}

	/**
	 * 构造视图
	 * 
	 * @param task
	 * @return
	 */
	public static RunningOpView valueOf(OpTask task) {
		return null == task ? null : new RunningOpView(task);
	}

	/** 唯一id */
	@DocAttribute(description = "唯一id")
	public String getId() {
		return null == m_Task ? m_Running.getPersistenceId().getId() : m_Task.getId().getId();
	}

	/**
	 * 版本号
	 * 
	 * @return
	 */
	@DocAttribute(description = "版本号")
	public List<String> getVersions() {
		return m_Versions;
	}

	/**
	 * 当前状态
	 * 
	 * @return
	 */
	@DocAttribute(description = "当前状态")
	public OpProcessor.Status getCurrentStatus() {
		return null == m_Task ? null : m_Task.getCurrentStatus();
	}

	/**
	 * 历史状态
	 * 
	 * @return
	 */
	@DocAttribute(description = "历史状态")
	public List<OpProcessor.Status> getStatus() {
		List<OpProcessor.Status> list = null == m_Task ? Collections.emptyList() : m_Task.getStatus();
		if (list.size() > MAX) {
			return list.subList(list.size() - MAX, list.size());
		} else {
			return list;
		}
	}

	@DocAttribute(description = "是否已结束")
	public boolean isDone() {
		return null == m_Task ? true : m_Task.isDone();
	}

	/**
	 * 获取状态
	 * 
	 * @return
	 */
	@DocAttribute(description = "状态")
	public int getState() {
		if (null == m_Running) {
			return Running.STATE_UNKNOWN.id;
		}
		NameItem state = m_Running.getState();
		return state.getId();
	}

	/**
	 * 获取状态名称
	 * 
	 * @return
	 */
	@DocAttribute(description = "状态名称")
	public String getStateName() {
		if (null == m_Running) {
			return Running.STATE_UNKNOWN.name;
		}
		NameItem state = m_Running.getState();
		return state.getName();
	}

	/**
	 * 是否可编辑
	 * 
	 * @return
	 */
	@DocAttribute(description = "是否可编辑")
	public boolean isCanEdit() {
		if (null == m_Running) {
			return false;
		}
		return m_Running.isRight(Running.RIGHT_EDIT);
	}

	/**
	 * 是否可删除
	 * 
	 * @return
	 */
	@DocAttribute(type = boolean.class, description = "是否可删除")
	public boolean isCanDelete() {
		if (null == m_Running) {
			return false;
		}
		return m_Running.isRight(Running.RIGHT_EDIT);
	}

	/**
	 * 是否可升级
	 * 
	 * @return
	 */
	@DocAttribute(description = "是否可升级")
	public boolean isCanUpgrade() {
		if (null == m_Running) {
			return false;
		}
		return m_Running.isRight(Running.RIGHT_UPGRADE);
	}

	/**
	 * 是否可回滚
	 * 
	 * @return
	 */
	@DocAttribute(description = "是否可回滚")
	public boolean isCanRollback() {
		if (null == m_Running) {
			return false;
		}
		return m_Running.isRight(Running.RIGHT_ROLLBACK);
	}

	/**
	 * 是否可启动
	 * 
	 * @return
	 */
	@DocAttribute(description = "是否可启动")
	public boolean isCanStart() {
		if (null == m_Running) {
			return false;
		}
		return m_Running.isRight(Running.RIGHT_START);
	}

	/**
	 * 是否可停止
	 * 
	 * @return
	 */
	@DocAttribute(description = "是否可停止")
	public boolean isCanStop() {
		if (null == m_Running) {
			return false;
		}
		return m_Running.isRight(Running.RIGHT_STOP);
	}

	/**
	 * 是否可重启
	 * 
	 * @return
	 */
	@DocAttribute(description = "是否可重启")
	public boolean isCanRestart() {
		if (null == m_Running) {
			return false;
		}
		return m_Running.isRight(Running.RIGHT_RESTART);
	}

	/**
	 * 是否可构建
	 * 
	 * @deprecated
	 * @return
	 */
	@DocAttribute(description = "是否可构建")
	public boolean isCanBuild() {
		return false;
	}

	@DocAttribute(description = "是支持打印堆栈")
	public boolean isSupportPrintStack() {
		if (null == m_Running) {
			return false;
		}
		return m_Running.isSupportPrintStack();
	}

	/**
	 * 获取实例当前版本
	 * 
	 * @return
	 */
	@DocAttribute(description = "实例当前版本")
	public String getCurrentVersion() {
		if (null == m_Running) {
			return "";
		}
		VersionInfo v = m_Running.getCurrentVersion();
		return null == v ? "" : v.getVersion();
	}

	@DocAttribute(description = "是支持打印内存")
	public boolean isSupportMemoryMap() {
		if (null == m_Running) {
			return false;
		}
		return m_Running.isSupportMemoryMap();
	}
}
