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
import cn.weforward.protocol.ops.User;

/**
 * 组织提供者
 * 
 * @author daibo
 *
 */
public interface OrganizationProvider {
	/**
	 * 搜索组织
	 * 
	 * @param keywords
	 * @return
	 */
	ResultPage<Organization> search(String keywords);

	/**
	 * 获取组织
	 * 
	 * @param id
	 * @return
	 */
	Organization get(String id);

	/**
	 * 获取组织
	 * 
	 * @param id
	 * @return
	 */
	Organization get(User user);

}
