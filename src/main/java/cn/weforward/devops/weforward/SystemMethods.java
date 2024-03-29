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

import javax.annotation.Resource;

import cn.weforward.common.ResultPage;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.common.util.StringUtil;
import cn.weforward.common.util.TransResultPage;
import cn.weforward.devops.project.ProjectService;
import cn.weforward.devops.user.Group;
import cn.weforward.devops.user.GroupProvider;
import cn.weforward.devops.user.Organization;
import cn.weforward.devops.user.OrganizationUser;
import cn.weforward.devops.user.UserProvider;
import cn.weforward.devops.weforward.view.GroupDetailView;
import cn.weforward.devops.weforward.view.UserView;
import cn.weforward.framework.ApiException;
import cn.weforward.framework.WeforwardMethod;
import cn.weforward.framework.WeforwardMethods;
import cn.weforward.framework.WeforwardSession;
import cn.weforward.protocol.Access;
import cn.weforward.protocol.ops.User;
import cn.weforward.protocol.support.datatype.FriendlyObject;

/**
 * 
 * 系统控制器
 * 
 * @author daibo
 *
 */
@WeforwardMethods(kind = Access.KIND_USER)
public class SystemMethods {
	@Resource
	protected ProjectService m_ProjectService;
	@Resource
	protected UserProvider m_UserService;
	@Resource
	protected GroupProvider m_GroupProvider;

	@WeforwardMethod
	public GroupDetailView group(FriendlyObject params) throws ApiException {
		String op = params.getString("op");
		String id = params.getString("id");
		Group g;
		if (StringUtil.isEmpty(id)) {
			g = null;
		} else {
			g = m_GroupProvider.getGroup(getMyOrganization(), id);
		}
		if ("add".equals(op)) {
			String name = params.getString("name");
			g = m_GroupProvider.addGroup(getMyOrganization(), name);
		} else if ("adduser".equals(op)) {
			String uid = params.getString("uid");
			User user = m_UserService.getUser(uid);
			if (null == user) {
				throw new NullPointerException("找不到[" + uid + "]对应的用户");
			}
			g.addUser(user);
		} else if ("removeuser".equals(op)) {
			String uid = params.getString("uid");
			User user = m_UserService.getUser(uid);
			if (null == user) {
				throw new NullPointerException("找不到[" + uid + "]对应的用户");
			}
			g.removeUser(user);
		} else if ("set".equals(op)) {
			g.setNote(params.getString("note"));
			String name = params.getString("name");
			if (!StringUtil.isEmpty(name)) {
				g.setName(name);
			}
		}
		return GroupDetailView.valueOf(g);
	}

	@WeforwardMethod
	public ResultPage<GroupDetailView> groups(FriendlyObject params) throws ApiException {
		String keywords = params.getString("keywords");
		ResultPage<Group> rp = (ResultPage<Group>) m_GroupProvider.search(getMyOrganization(), keywords);
		// rp = ResultPageHelper.reverseResultPage(rp);
		TransResultPage<GroupDetailView, Group> rp1 = new TransResultPage<GroupDetailView, Group>(rp) {

			@Override
			protected GroupDetailView trans(Group src) {
				return GroupDetailView.valueOf(src);
			}
		};
		return rp1;

	}

	@WeforwardMethod
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

	private Organization getMyOrganization() {
		OrganizationUser user = WeforwardSession.TLS.getUser();
		return user.getOrganization();
	}

}
