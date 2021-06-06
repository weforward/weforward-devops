package cn.weforward.devops.user;

import java.util.List;

import cn.weforward.protocol.ops.Right;
import cn.weforward.protocol.ops.Role;
import cn.weforward.protocol.ops.User;

/**
 * 角色供应商
 * 
 * @author daibo
 *
 */
public interface RoleProvider {
	/**
	 * 获取角色
	 * 
	 * @param user
	 * @return
	 */
	List<Role> getRoles(User user);

	/**
	 * 权限
	 */
	List<Right> getRights(User user);

}
