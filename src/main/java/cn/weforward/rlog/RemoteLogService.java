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
package cn.weforward.rlog;

import java.io.IOException;
import java.util.List;

import cn.weforward.common.ResultPage;
import cn.weforward.devops.user.Organization;

/**
 * 远程服务实现
 * 
 * @author daibo
 *
 */
public interface RemoteLogService {

	/**
	 * 获取服务器列表
	 * 
	 * @param org 组织
	 * @return
	 */
	List<Server> listServer(Organization org);

	/**
	 * 获取服务器目录
	 * 
	 * @param org    组织
	 * @param server
	 * @return
	 */
	ResultPage<Directory> listDirectory(Organization org, String server);

	/**
	 * 获取主题列表
	 * 
	 * @param org       组织
	 * @param server
	 * @param directory
	 * @return
	 * @throws IOException
	 */
	ResultPage<Subject> listSubject(Organization org, String server, String directory) throws IOException;

	/**
	 * 获取内容列表
	 * 
	 * @param org       组织
	 * @param server
	 * @param directory
	 * @param subject
	 * @return
	 * @throws IOException
	 */
	Content getContent(Organization org, String server, String directory, String subject) throws IOException;

	/**
	 * 获取日志详情
	 * 
	 * @param org       组织
	 * @param server
	 * @param directory
	 * @return
	 * @throws IOException
	 */
	LogPage getDetail(Organization org, String server, String directory) throws IOException;

	/**
	 * 日志项
	 * 
	 * @author daibo
	 *
	 */
	interface RemoteLogItem {
		/**
		 * 服务器
		 * 
		 * @return
		 */
		String getServer();

		/**
		 * 主题
		 * 
		 * @return
		 */
		String getSubject();

		/**
		 * 内容
		 * 
		 * @return
		 */
		String getContent();

		/**
		 * 等级
		 * 
		 * @return
		 */
		String getLevel();
	}

}
