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
package cn.weforward.devops.project;

import java.util.List;

import cn.weforward.common.NameItem;
import cn.weforward.common.NameItems;
import cn.weforward.common.ResultPage;
import cn.weforward.devops.user.Group;
import cn.weforward.devops.user.Organization;
import cn.weforward.framework.ApiException;

/**
 * 项目服务
 * 
 * @author daibo
 *
 */
public interface ProjectService {
	/** 权限-可读取 */
	String RIGHT_READ = "READ";
	/** 权限-可操作 */
	String RIGHT_OPERATOR = "OPERATOR";
	/** 权限-可更新 */
	String RIGHT_UPDATE = "UPDATE";
	/** 类型-java */
	NameItem TYPE_JAVA = NameItem.valueOf("JAVA", 0);
	/** 类型-html */
	NameItem TYPE_HTML = NameItem.valueOf("HTML", 1);
	/** 类型 */
	NameItems TYPES = NameItems.valueOf(TYPE_JAVA, TYPE_HTML);

	/**
	 * 添加docker机器
	 * 
	 * @param name
	 * @param url
	 * @return
	 */
	Machine addDockerMachine(Organization org, String name, String url);

	/**
	 * 添加Nginx机器
	 * 
	 * @param name
	 * @param url
	 * @param root
	 * @param password
	 * @return
	 */
	Machine addProxyMachine(Organization org, String name, String url, String root, String password);

	/**
	 * 获取机器
	 * 
	 * @param id
	 * @return
	 */
	Machine getMachine(Organization org, String id);

	/**
	 * 删除机器
	 * 
	 * @param m
	 * @throws ApiException
	 */
	void delete(Machine m) throws ApiException;

	/**
	 * 搜索机器
	 * 
	 * @param project
	 * @param keywords
	 * @return
	 */
	ResultPage<? extends Machine> searchMachines(Organization org, Project project, String keywords);

	/**
	 * 添加项目
	 * 
	 * @param org  组织
	 * @param name 名称
	 * @param type 类型
	 * @return
	 */
	Project addProject(Organization org, String name, NameItem type);

	/**
	 * 获取项目
	 * 
	 * @param org 组织
	 * @param id  项目id
	 * @return
	 */
	Project getProject(Organization org, String id);

	/**
	 * 删除项目
	 * 
	 * @param p
	 * @throws ApiException
	 */
	void delete(Project p) throws ApiException;

	/**
	 * 搜索项目
	 * 
	 * @param org      组织
	 * @param keywords 关键字
	 * @param type     类型
	 * @return
	 */
	ResultPage<? extends Project> searchProjects(Organization org, Group group, String keywords, NameItem type);

	/**
	 * 添加运行项目
	 * 
	 * @param project 项目
	 * @param machine 机器
	 * @return
	 */
	Running addRunning(Organization org, Project project, Machine machine);

	/**
	 * 获取运行项目
	 * 
	 * @param id
	 * @return
	 */
	Running getRunning(Organization org, String id);

	/**
	 * 删除实例
	 * 
	 * @param p
	 * @throws ApiException
	 */
	void delete(Running p) throws ApiException;

	/**
	 * 搜索运行项目
	 * 
	 * @param org      组织
	 * @param group    组
	 * @param keywords 关键字
	 * @param type     类型
	 * @return
	 */
	ResultPage<? extends Running> searchRunnings(Organization org, Group group, String keywords, NameItem type);

	/**
	 * 获取操作任务
	 * 
	 * @param id
	 * @return
	 */
	OpTask getOpTask(String id);

	/**
	 * 推荐端口
	 * 
	 * @return
	 */
	int recommendedServerPort(Organization org);

	/**
	 * 加载服务配置
	 * 
	 * 
	 * @param projectName 项目名
	 * @param serverid    服务id
	 * @param accessId    访问id
	 * @return
	 */
	List<Prop> loadServiceProperties(String projectName, String serverid, String accessId);

	/**
	 * 查找机器
	 * 
	 * @param accessid
	 * @param name
	 * @return
	 */
	Machine findMachine(String accessId, String name);

}
