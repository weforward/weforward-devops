package cn.weforward.devops.user;

import java.util.List;

/**
 * 权限
 * 
 * @author daibo
 *
 */
public class GroupRight {

	protected Group m_Group;

	protected List<String> m_Rights;

	public GroupRight(Group group, List<String> rights) {
		m_Group = group;
		m_Rights = rights;
	}

	public Group getGroup() {
		return m_Group;
	}

	public List<String> getRights() {
		return m_Rights;
	}

}
