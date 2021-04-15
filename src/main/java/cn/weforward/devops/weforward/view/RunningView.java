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

import cn.weforward.common.util.StringUtil;
import cn.weforward.devops.project.Env;
import cn.weforward.devops.project.Machine;
import cn.weforward.devops.project.Project;
import cn.weforward.devops.project.Running;
import cn.weforward.devops.project.RunningProp;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

/**
 * 实例视图
 * 
 * @author daibo
 *
 */
@DocObject(description = "实例视图")
public class RunningView {
	/** 实例 */
	protected Running m_Running;

	/**
	 * 构造
	 * 
	 * @param r
	 */
	public RunningView(Running r) {
		m_Running = r;
	}

	/**
	 * 构造视图
	 * 
	 * @param r
	 * @return
	 */
	public static RunningView valueOf(Running r) {
		return null == r ? null : new RunningView(r);
	}

	/**
	 * 唯一id
	 * 
	 * @return
	 */
	@DocAttribute(description = "唯一id")
	public String getId() {
		return m_Running.getPersistenceId().getId();
	}

	/**
	 * 机器
	 * 
	 * @return
	 */
	@DocAttribute(description = "机器")
	public MachineView getMachine() {
		return MachineView.valueOf(m_Running.getMachine());
	}

	/**
	 * 机器
	 * 
	 * @deprecated 使用 {@link #getMachine()}
	 * @return
	 */
	public MachineView getM() {
		return MachineView.valueOf(m_Running.getMachine());
	}

	/**
	 * 项目
	 * 
	 * @return
	 */
	@DocAttribute(description = "项目")
	public ProjectView getProject() {
		return ProjectView.valueOf(m_Running.getProject());
	}

	/**
	 * 项目
	 * 
	 * @deprecated {@link #getProject()}
	 * @return
	 */
	public ProjectView getP() {
		return ProjectView.valueOf(m_Running.getProject());
	}

	/**
	 * 获取属性
	 * 
	 * @return
	 */
	@DocAttribute(description = "属性")
	public List<RunningProp> getProps() {
		if (m_Running.isRight(Running.RIGHT_EDIT)) {
			return m_Running.getProps();
		} else {
			return Collections.emptyList();
		}
	}

	/**
	 * 获取应用日志id
	 * 
	 * @return
	 */
	@DocAttribute(description = "应用日志id")
	public String getAppLog() {
		String id = "";
		Project p = m_Running.getProject();
		Machine m = m_Running.getMachine();
		if (null == p || null == m) {
			return id;
		}
		for (Env v : m_Running.getMachine().getEnvs()) {
//			if (StringUtil.eq(v.getKey(), "OURLINC_SERVER_ID")) {
//				return getProject().getName() + "_x" + Hex.toHex(NumberUtil.toInt(v.getValue(), 0));
//			} else 
			if (StringUtil.eq(v.getKey(), "SERVER_ID")) {
				id = getProject().getName() + "_" + v.getValue();
			}
		}
		return id;

	}

	/**
	 * 是否可编辑
	 * 
	 * @return
	 */
	@DocAttribute(description = "是否可编辑")
	public boolean isCanEdit() {
		return m_Running.isRight(Running.RIGHT_EDIT);
	}

	/**
	 * 是否可删除
	 * 
	 * @return
	 */
	@DocAttribute(description = "是否可删除")
	public boolean isCanDelete() {
		return m_Running.isRight(Running.RIGHT_EDIT);
	}

	/**
	 * 是否可升级
	 * 
	 * @return
	 */
	@DocAttribute(description = "是否可升级")
	public boolean isCanUpgrade() {
		return m_Running.isRight(Running.RIGHT_UPGRADE);
	}

	/**
	 * 是否可回滚
	 * 
	 * @return
	 */
	@DocAttribute(description = "是否可回滚")
	public boolean isCanRollback() {
		return m_Running.isRight(Running.RIGHT_ROLLBACK);
	}

	/**
	 * 是否可启动
	 * 
	 * @return
	 */
	@DocAttribute(description = "是否可启动")
	public boolean isCanStart() {
		return m_Running.isRight(Running.RIGHT_START);
	}

	/**
	 * 是否可停止
	 * 
	 * @return
	 */
	@DocAttribute(description = "是否可停止")
	public boolean isCanStop() {
		return m_Running.isRight(Running.RIGHT_STOP);
	}

	/**
	 * 是否可重启
	 * 
	 * @return
	 */
	@DocAttribute(description = "是否可重启")
	public boolean isCanRestart() {
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
		return m_Running.isSupportPrintStack();
	}

	@DocAttribute(description = "是支持打印内存")
	public boolean isSupportMemoryMap() {
		return m_Running.isSupportMemoryMap();
	}

}
