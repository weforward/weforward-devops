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
import cn.weforward.common.util.TransList;
import cn.weforward.devops.project.Bind;
import cn.weforward.devops.project.Env;
import cn.weforward.devops.project.Project;
import cn.weforward.devops.project.impl.HtmlProject;
import cn.weforward.devops.project.impl.JavaProject;
import cn.weforward.devops.user.GroupRight;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

/**
 * 项目视图
 * 
 * @author daibo
 *
 */
@DocObject(description = "项目视图")
public class ProjectView {
	/** 项目 */
	protected Project m_Project;

	/**
	 * 构造
	 * 
	 * @param project
	 */
	public ProjectView(Project project) {
		m_Project = project;
	}

	/**
	 * 构造视图
	 * 
	 * @param project
	 * @return
	 */
	public static ProjectView valueOf(Project project) {
		return null == project ? null : new ProjectView(project);
	}

	/**
	 * id
	 * 
	 * @return
	 */
	@DocAttribute(description = "唯一id", example = "JavaProject$12345")
	public String getId() {
		return m_Project.getPersistenceId().getId();
	}

	/**
	 * 名称
	 * 
	 * @return
	 */
	@DocAttribute(description = "名称", example = "我的项目")
	public String getName() {
		return m_Project.getName();
	}

	/**
	 * 描述
	 * 
	 * @return
	 */
	@DocAttribute(description = "描述", example = "这是一个项目")
	public String getDesc() {
		return m_Project.getDesc();
	}

	/**
	 * 所属者
	 * 
	 * @return
	 */
	@DocAttribute(description = "所属者")
	public UserView getOwner() {
		return UserView.valueOf(m_Project.getOwner());
	}

	/**
	 * 环境变量
	 * 
	 * @return
	 */
	@DocAttribute(description = "环境变量")
	public List<Env> getEnvs() {
		return m_Project.getEnvs();
	}

	/**
	 * 绑定
	 * 
	 * @return
	 */
	@DocAttribute(description = "绑定")
	public List<Bind> getBinds() {
		return m_Project.getBinds();
	}

	/**
	 * 是否可编辑
	 * 
	 * @return
	 */
	@DocAttribute(description = "是否可编辑")
	public boolean isCanEdit() {
		return m_Project.isRight(Project.RIGHT_UPDATE);
	}

	/**
	 * 是否可删除
	 * 
	 * @return
	 */
	@DocAttribute(type = boolean.class, description = "是否可删除")
	public boolean isCanDelete() {
		return m_Project.isRight(Project.RIGHT_UPDATE);
	}

	/**
	 * 群组
	 * 
	 * @return
	 */
	@DocAttribute(description = "群组集合")
	public List<GroupView> getGroups() {
		return new TransList<GroupView, GroupRight>(m_Project.getGroups()) {

			@Override
			public GroupView trans(GroupRight v) {
				return GroupView.valueOf(v);
			}
		};
	}

	/**
	 * 负责群组
	 * 
	 * @return
	 */
	@DocAttribute(description = "负责的群组，用于显示")
	public String getGroupsToShow() {
		String groups = "";
		List<GroupRight> list = m_Project.getGroups();
		if (null == list || 0 == list.size()) {
			return groups;
		}
		for (GroupRight g : list) {
			if (!StringUtil.isEmpty(groups)) {
				groups += "、";
			}
			groups += g.getGroup().getName();
		}
		return groups;
	}

	@DocAttribute(description = "项目类型")
	public int getType() {
		return m_Project.getType().id;
	}

	@DocAttribute(description = "提供服务的端口，项目类型为Java时有值")
	public List<Integer> getServerPorts() {
		if (m_Project instanceof JavaProject) {
			return ((JavaProject) m_Project).getServerPorts();
		}
		return Collections.emptyList();
	}

	@DocAttribute(description = "访问地址，项目类型为Html时有值")
	public List<String> getAccessUrls() {
		if (m_Project instanceof HtmlProject) {
			return ((HtmlProject) m_Project).getAccessUrls();
		}
		return Collections.emptyList();
	}

}
