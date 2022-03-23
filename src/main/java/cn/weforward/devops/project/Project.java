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
import cn.weforward.common.Nameable;
import cn.weforward.data.persister.Persistent;
import cn.weforward.devops.user.GroupRight;
import cn.weforward.devops.user.Organization;
import cn.weforward.protocol.ops.User;

/**
 * 项目
 * 
 * @author daibo
 *
 */
public interface Project extends Nameable, Persistent {
	/** 权限-可读取（可看） */
	int RIGHT_READ = 1 << 1;
	/** 权限-可操作（启动，停止，重启） */
	int RIGHT_OPERATOR = RIGHT_READ | 1 << 2;
	/** 权限-可更新（部署，调试，编辑 ） */
	int RIGHT_UPDATE = RIGHT_OPERATOR | 1 << 3;
	/** 类型-java */
	NameItem TYPE_JAVA = NameItem.valueOf("java", 1);
	/** 类型-html */
	NameItem TYPE_HTML = NameItem.valueOf("html", 2);
	/** 类型-全部 */
	NameItems TYPES = NameItems.valueOf(TYPE_JAVA, TYPE_HTML);

	/**
	 * 类型
	 * 
	 * @return
	 */
	NameItem getType();

	/**
	 * 设置名称
	 * 
	 * @param name
	 */
	void setName(String name);

	/**
	 * 设置所属者
	 * 
	 * @param user
	 */
	void setOwner(User user);

	/**
	 * 获取所属者
	 * 
	 * @return
	 */
	User getOwner();

	/**
	 * 设置描述
	 * 
	 * @param desc
	 */
	void setDesc(String desc);

	/**
	 * 获取描述
	 * 
	 * @return
	 */
	String getDesc();

	/**
	 * 设置绑定映射
	 * 
	 * @param binds
	 */
	void setBinds(List<Bind> binds);

	/**
	 * 绑定映射
	 * 
	 * @return
	 */
	List<Bind> getBinds();

	/**
	 * 环境变量
	 * 
	 * @param envs
	 */
	void setEnvs(List<Env> envs);

	/**
	 * 环境变量 key=value
	 * 
	 * @return
	 */
	List<Env> getEnvs();

	/**
	 * 设置组
	 * 
	 * @param groups
	 */
	void setGroups(List<GroupRight> groups);

	/**
	 * 获取组
	 * 
	 * @return
	 */
	List<GroupRight> getGroups();

	/**
	 * 是否有指定权限
	 * 
	 * @param r
	 * @return
	 */
	boolean isRight(int r);

	/**
	 * 所属组织
	 * 
	 * @return
	 */
	Organization getOrganization();

}
