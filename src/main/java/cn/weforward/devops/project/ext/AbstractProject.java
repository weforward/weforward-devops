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
package cn.weforward.devops.project.ext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import cn.weforward.common.NameItem;
import cn.weforward.common.util.FreezedList;
import cn.weforward.common.util.StringUtil;
import cn.weforward.data.UniteId;
import cn.weforward.data.annotation.ResourceExt;
import cn.weforward.data.persister.support.AbstractPersistent;
import cn.weforward.data.search.IndexKeyword;
import cn.weforward.data.search.SearchableExt;
import cn.weforward.data.search.util.IndexElementHelper;
import cn.weforward.data.search.util.IndexKeywordHelper;
import cn.weforward.devops.project.Bind;
import cn.weforward.devops.project.Env;
import cn.weforward.devops.project.Project;
import cn.weforward.devops.project.ProjectService;
import cn.weforward.devops.project.di.ProjectDi;
import cn.weforward.devops.project.impl.IdAndRight;
import cn.weforward.devops.user.Group;
import cn.weforward.devops.user.GroupRight;
import cn.weforward.devops.user.Organization;
import cn.weforward.framework.WeforwardSession;
import cn.weforward.protocol.ops.User;
import cn.weforward.util.OperatorUtils;

/**
 * 抽象的项目实现
 * 
 * @author daibo
 *
 */
public abstract class AbstractProject extends AbstractPersistent<ProjectDi> implements Project, SearchableExt {
	/** 名称 */
	@Resource
	protected String m_Name;
	/** 描述 */
	@Resource
	protected String m_Desc;
	/** 创建时间 */
	@Resource
	protected Date m_CreateTime;
	/** 绑定 */
	@ResourceExt(component = Bind.class)
	protected List<Bind> m_Binds;
	/** 环境 */
	@ResourceExt(component = Env.class)
	protected List<Env> m_Envs;
	/** 所有者 */
	@Resource(type = String.class)
	protected UniteId m_Owner;
	/** 项目组和权限 */
	@ResourceExt(component = IdAndRight.class)
	protected List<IdAndRight> m_GroupRights;
	/** 所在组织 */
	@Resource
	protected String m_Organization;

	protected AbstractProject(ProjectDi di) {
		super(di);
	}

	public AbstractProject(ProjectDi di, Organization org, String name) {
		super(di);
		genPersistenceId();
		m_Name = name;
		m_CreateTime = new Date();
		m_Owner = OperatorUtils.getOperator();
		m_Organization = org.getId();
	}

	public UniteId getId() {
		return getPersistenceId();
	}

	@Override
	public void setName(String name) {
		m_Name = name;
	}

	@Override
	public String getName() {
		return m_Name;
	}

	@Override
	public void setOwner(User user) {
		checkRight(RIGHT_UPDATE);
		m_Owner = null == user ? null : UniteId.valueOf(user.getId());
		markPersistenceUpdate();
	}

	@Override
	public User getOwner() {
		return null == m_Owner ? null : getBusinessDi().getUser(m_Owner.getId());
	}

	@Override
	public void setDesc(String desc) {
		m_Desc = desc;
		markPersistenceUpdate();
	}

	@Override
	public String getDesc() {
		return m_Desc;
	}

	@Override
	public void setBinds(List<Bind> binds) {
		checkRight(RIGHT_UPDATE);
		m_Binds = binds;
		markPersistenceUpdate();
	}

	@Override
	public List<Bind> getBinds() {
		if (null == m_Binds) {
			return Collections.emptyList();
		}
		return m_Binds;
	}

	@Override
	public List<Env> getEnvs() {
		return null == m_Envs ? Collections.emptyList() : m_Envs;
	}

	@Override
	public void setEnvs(List<Env> envs) {
		checkRight(RIGHT_UPDATE);
		m_Envs = FreezedList.freezed(envs);
		markPersistenceUpdate();
	}

	public void setGroups(List<GroupRight> groups) {
		checkRight(RIGHT_UPDATE);
		List<IdAndRight> list = new ArrayList<>();
		for (GroupRight g : groups) {
			list.add(new IdAndRight(g.getGroup().getId(), toRight(g.getRights())));
		}
		m_GroupRights = list;
		markPersistenceUpdate();
		getBusinessDi().onGroupsChange(this);
		reindex();

	}

	private static int toRight(List<String> rights) {
		int r = 0;
		for (String v : rights) {
			if (ProjectService.RIGHT_UPDATE.equalsIgnoreCase(v)) {
				r |= Project.RIGHT_UPDATE;
			} else if (ProjectService.RIGHT_OPERATOR.equalsIgnoreCase(v)) {
				r |= Project.RIGHT_OPERATOR;
			} else if (ProjectService.RIGHT_READ.equalsIgnoreCase(v)) {
				r |= Project.RIGHT_READ;
			}
		}
		return r;
	}

	@Override
	public List<GroupRight> getGroups() {
		if (null == m_GroupRights) {
			return Collections.emptyList();
		}
		List<GroupRight> list = new ArrayList<>();
		for (IdAndRight id : m_GroupRights) {
			Group g = getBusinessDi().getGroups(getOrganization(), id.getId());
			if (null == g) {
				continue;
			}
			list.add(new GroupRight(g, toRights(id.getRight())));
		}
		return list;
	}

	public Organization getOrganization() {
		return getBusinessDi().getOrganization(m_Organization);
	}

	private static List<String> toRights(int right) {
		List<String> rights = new ArrayList<>();
		if (isRight(RIGHT_UPDATE, right)) {
			rights.add(ProjectService.RIGHT_UPDATE);
		} else if (isRight(RIGHT_OPERATOR, right)) {
			rights.add(ProjectService.RIGHT_OPERATOR);
		} else if (isRight(RIGHT_READ, right)) {
			rights.add(ProjectService.RIGHT_READ);
		}
		return rights;
	}

	private boolean isOwner(User user) {
		if (null == user) {
			return false;
		}
		if (null == m_Owner) {
			return true;
		}
		if (StringUtil.eq(m_Owner.getOrdinal(), UniteId.getOrdinal(user.getId()))) {
			return true;
		} else {
			return false;
		}
	}

	private List<String> getGroupRights(User user) {
		for (GroupRight g : getGroups()) {
			if (null == g) {
				continue;
			}
			Group group = g.getGroup();
			if (null == group) {
				continue;
			}
			if (group.include(user)) {
				return g.getRights();
			}
		}
		return Collections.emptyList();
	}

	public int getRight() {
		int r = 0;
		User user = WeforwardSession.TLS.getOperator();
		if (isOwner(user)) {
			r |= RIGHT_UPDATE;
		}
		r |= toRight(getGroupRights(user));
		r |= getBusinessDi().getDefaultProjectRight();
		return r;
	}

	protected void checkRight(int r) {
		if (!isRight(r)) {
			throw new UnsupportedOperationException("无权限执行此方法");
		}
	}

	@Override
	public boolean isRight(int r) {
		return isRight(r, getRight());
	}

	private static boolean isRight(int r, int right) {
		return r == (r & right);
	}

	public boolean isMyOrganization(Organization org) {
		return StringUtil.eq(m_Organization, org.getId());
	}

	@Override
	public NameItem getType() {
		return TYPES.get(0);
	}

	@Override
	public String toString() {
		return getId() + "," + getName();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AbstractProject) {
			return getId().getId().equals(((AbstractProject) obj).getId().getId());
		}
		return false;
	}

	@Override
	public void reindex() {
		getBusinessDi().getProjectSearcher().updateElement(IndexElementHelper.newElement(getId().getId()),
				getIndexKeywords());
	}

	@Override
	public List<IndexKeyword> getIndexKeywords() {
		List<IndexKeyword> ks = new ArrayList<>();
		ks = IndexKeywordHelper.addKeywordIfNotNull(ks, m_Organization, 0);
		if (null != m_GroupRights) {
			for (IdAndRight id : m_GroupRights) {
				ks = IndexKeywordHelper.addKeywordIfNotNull(ks, id.getId(), 0);
			}
		}
		return ks;
	}

}
