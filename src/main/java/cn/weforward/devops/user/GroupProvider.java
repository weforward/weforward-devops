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
