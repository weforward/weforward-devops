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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.weforward.devops.project.Group;
import cn.weforward.protocol.ops.User;

/**
 * 组详情视图
 * 
 * @author daibo
 *
 */
public class GroupDetailView extends GroupView {
	/**
	 * 构造
	 * 
	 * @param g
	 */
	public GroupDetailView(Group g) {
		super(g);
	}

	/**
	 * 构造对象
	 * 
	 * @param g
	 * @return
	 */
	public static GroupDetailView valueOf(Group g) {
		return null == g ? null : new GroupDetailView(g);
	}

	/**
	 * 备注
	 * 
	 * @return
	 */
	public String getNote() {
		return m_Group.getNote();
	}

	/**
	 * 获取用户
	 * 
	 * @return
	 */
	public List<UserView> getUsers() {
		List<User> users = m_Group.getUsers();
		if (null == users) {
			return Collections.emptyList();
		}
		List<UserView> list = new ArrayList<>();
		for (User user : users) {
			if (null == user) {
				continue;
			}
			list.add(UserView.valueOf(user));
		}
		return list;
//		return new TransList<UserView, User>(group.getUsers()) {
//
//			@Override
//			public UserView trans(User v) {
//				return UserView.valueOf(v);
//			}
//		};
	}

}
