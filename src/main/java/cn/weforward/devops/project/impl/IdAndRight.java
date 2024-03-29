package cn.weforward.devops.project.impl;

import javax.annotation.Resource;

/**
 * id和权限
 * 
 * @author daibo
 *
 */
public class IdAndRight {
	@Resource
	protected String m_Id;
	@Resource
	protected int m_Right;

	protected IdAndRight() {

	}

	public IdAndRight(String id, int right) {
		m_Id = id;
		m_Right = right;
	}

	/**
	 * id
	 * 
	 * @return
	 */
	public String getId() {
		return m_Id;
	}

	/**
	 * 权限
	 * 
	 * @return
	 */
	public int getRight() {
		return m_Right;
	}
}
