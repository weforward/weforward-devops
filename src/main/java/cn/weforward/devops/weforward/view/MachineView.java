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

import java.util.List;

import cn.weforward.common.util.TransList;
import cn.weforward.devops.project.Bind;
import cn.weforward.devops.project.Env;
import cn.weforward.devops.project.Machine;
import cn.weforward.devops.project.MachineInfo;
import cn.weforward.devops.project.Prop;
import cn.weforward.devops.user.GroupRight;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

/**
 * 机器视图
 * 
 * @author daibo
 *
 */
@DocObject(description = "机器视图")
public class MachineView {
	/** 机器 */
	protected Machine m_Machine;

	/**
	 * 构造
	 * 
	 * @param machine
	 */
	public MachineView(Machine machine) {
		m_Machine = machine;
	}

	/**
	 * 构造视图
	 * 
	 * @param machine
	 * @return
	 */
	public static MachineView valueOf(Machine machine) {
		return null == machine ? null : new MachineView(machine);
	}

	/**
	 * id
	 * 
	 * @return
	 */
	@DocAttribute(type = String.class, description = "机器id", index = 1)
	public String getId() {
		return m_Machine.getPersistenceId().getId();
	}

	/**
	 * 名称
	 * 
	 * @return
	 */
	@DocAttribute(type = String.class, description = "机器名称", index = 2)
	public String getName() {
		return m_Machine.getName();
	}

	/**
	 * 所属者
	 * 
	 * @return
	 */
	@DocAttribute(type = UserView.class, description = "所属者", index = 3)
	public UserView getOwner() {
		return UserView.valueOf(m_Machine.getOwner());
	}

	/**
	 * 环境变量
	 * 
	 * @return
	 */
	@DocAttribute(type = List.class, description = "环境变量", index = 4)
	public List<Env> getEnvs() {
		return m_Machine.getEnvs();
	}

	/**
	 * 属性
	 * 
	 * @return
	 */
	@DocAttribute(type = List.class, description = "属性", index = 5)
	public List<Prop> getProps() {
		return m_Machine.getProps();
	}

	/**
	 * 绑定
	 * 
	 * @return
	 */
	@DocAttribute(type = List.class, description = "绑定", index = 6)
	public List<Bind> getBinds() {
		return m_Machine.getBinds();
	}

	/**
	 * 是否可编辑
	 * 
	 * @return
	 */
	@DocAttribute(type = boolean.class, description = "是否可编辑")
	public boolean isCanEdit() {
		return m_Machine.isRight(Machine.RIGHT_UPDATE);
	}

	/**
	 * 是否可删除
	 * 
	 * @return
	 */
	@DocAttribute(type = boolean.class, description = "是否可删除")
	public boolean isCanDelete() {
		return m_Machine.isRight(Machine.RIGHT_UPDATE);
	}

	/**
	 * 所属群组
	 * 
	 * @return
	 */
	@DocAttribute(description = "所属群组")
	public List<GroupView> getGroups() {
		return new TransList<GroupView, GroupRight>(m_Machine.getGroups()) {

			@Override
			public GroupView trans(GroupRight v) {
				return GroupView.valueOf(v);
			}
		};
	}

	@DocAttribute(type = MachineInfoView.class, description = "机器信息")
	public MachineInfoView getInfo() {
		MachineInfo info = m_Machine.getInfo();
		return null == info ? null : new MachineInfoView(info);
	}

}
