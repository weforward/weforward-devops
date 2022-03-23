package cn.weforward.devops.user.impl;

import cn.weforward.protocol.ops.Role;

/**
 * 简单的角色
 * 
 * @author daibo
 *
 */
public class SimpleRole implements Role {

	protected int m_Id;

	protected String m_Name;

	public SimpleRole(int id, String name) {
		m_Id = id;
		m_Name = name;
	}

	@Override
	public int getId() {
		return m_Id;
	}

	@Override
	public String getName() {
		return m_Name;
	}

}
