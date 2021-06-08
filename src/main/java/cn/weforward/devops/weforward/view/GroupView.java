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

import cn.weforward.devops.user.Group;
import cn.weforward.devops.user.GroupRight;
import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;

/**
 * 组视图
 * 
 * @author daibo
 *
 */
@DocObject(description = "群组视图")
public class GroupView {
	/** 组 */
	protected Group m_Group;
	/** 权限 */
	protected List<String> m_Rights;

	/**
	 * 构造
	 * 
	 * @param g
	 */
	public GroupView(Group g) {
		this(g, null);
	}

	public GroupView(Group group, List<String> rights) {
		m_Group = group;
		m_Rights = null == rights ? Collections.emptyList() : rights;
	}

	/**
	 * 构造组对象
	 * 
	 * @param g
	 * @return
	 */
	public static GroupView valueOf(Group g) {
		return null == g ? null : new GroupView(g);
	}

	/**
	 * 构造组对象
	 * 
	 * @param g
	 * @return
	 */
	public static GroupView valueOf(GroupRight g) {
		return null == g ? null : new GroupView(g.getGroup(), g.getRights());
	}

	/**
	 * id
	 * 
	 * @return
	 */
	@DocAttribute(description = "群组id", example = "SimpleGroup$12345")
	public String getId() {
		return m_Group.getId();
	}

	/**
	 * 名称
	 * 
	 * @return
	 */
	@DocAttribute(description = "群组名", example = "我的组")
	public String getName() {
		return m_Group.getName();
	}

	@DocAttribute(description = "权限列表")
	public List<String> getRights() {
		return m_Rights;
	}

}
