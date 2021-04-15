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

import cn.weforward.protocol.doc.annotation.DocAttribute;
import cn.weforward.protocol.doc.annotation.DocObject;
import cn.weforward.protocol.ops.User;

/**
 * 用户视图
 * 
 * @author daibo
 *
 */
@DocObject(description = "用户视图")
public class UserView {
	/** 用户 */
	protected User m_User;

	/**
	 * 构造
	 * 
	 * @param user
	 */
	private UserView(User user) {
		m_User = user;
	}

	/**
	 * 构造视图
	 * 
	 * @param user
	 * @return
	 */
	public static UserView valueOf(User user) {
		return null == user ? null : new UserView(user);
	}

	/**
	 * 唯一id
	 * 
	 * @return
	 */
	@DocAttribute(description = "用户唯一id", example = "User$123456")
	public String getId() {
		return m_User.getId();
	}

	/**
	 * 名称
	 * 
	 * @return
	 */
	@DocAttribute(description = "名称", example = "webadmin")
	public String getName() {
		return m_User.getName();
	}

	/**
	 * 登陆名
	 * 
	 * @return
	 */
	// @DocAttribute(description = "登陆名", example = "admin")
	// public String getLoginname() {
	// return m_User.getName();
	// }

	/**
	 * 呢称
	 * 
	 * @return
	 */
	// @DocAttribute(description = "呢称", example = "hahaha")
	// public String getNickname() {
	// return m_User.getName();
	// }
}