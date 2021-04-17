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
package cn.weforward.devops.user;

import cn.weforward.common.ResultPage;
import cn.weforward.devops.project.Group;

/**
 * 群组供应
 * 
 * @author daibo
 *
 */
public interface GroupProvider {
	/**
	 * 创建组
	 * 
	 * @param org  所属组织
	 * @param name 名称
	 * @return
	 */
	Group addGroup(Organization org, String name);

	/**
	 * 获取组
	 * 
	 * @param org 所属组织
	 * @param id  唯一id
	 * @return
	 */
	Group getGroup(Organization org, String id);

	/**
	 * 搜索组
	 * 
	 * @param org      所属组织
	 * @param keywords 关键字
	 * @return
	 */
	ResultPage<? extends Group> searchGroups(Organization org, String keywords);
}
