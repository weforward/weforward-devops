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
package cn.weforward.devops.project.impl;

import java.util.Collections;
import java.util.List;

import cn.weforward.common.NameItem;
import cn.weforward.common.ResultPage;
import cn.weforward.common.util.ResultPageHelper;
import cn.weforward.data.annotation.Inherited;
import cn.weforward.data.annotation.ResourceExt;
import cn.weforward.devops.project.di.ProjectDi;
import cn.weforward.devops.project.ext.AbstractProject;
import cn.weforward.devops.user.Organization;

/**
 * java项目
 * 
 * @author daibo
 *
 */
@Inherited
public class JavaProject extends AbstractProject {
	/** 服务端口 */
	@ResourceExt(component = Integer.class)
	private List<Integer> m_ServerPorts;

	protected JavaProject(ProjectDi di) {
		super(di);
	}

	public JavaProject(ProjectDi di, Organization org, String name) {
		super(di, org, name);
	}

	/** 服务端口 */
	public void setServerPorts(List<Integer> ports) {
		checkRight(RIGHT_UPDATE);
		if (null == ports) {
			ports = Collections.emptyList();
		}
		checkDouble(ports);
		ResultPage<JavaProject> rp = getBusinessDi().getJavaProjects();
		for (JavaProject project : ResultPageHelper.toForeach(rp)) {
			if (project.equals(this)) {
				continue;
			}
			checkInclude(project.getServerPorts(), ports);
		}
		m_ServerPorts = ports;
		markPersistenceUpdate();
	}

	private void checkInclude(List<Integer> serverPorts, List<Integer> ports) {
		for (Integer v : serverPorts) {
			for (Integer vv : ports) {
				if (v.equals(vv)) {
					throw new UnsupportedOperationException("端口号[" + v + "]已被占用");
				}
			}
		}
	}

	private static void checkDouble(List<Integer> ports) {
		for (int i = 0; i < ports.size() - 1; i++) {
			for (int j = i + 1; j < ports.size(); j++) {
				if (ports.get(i).equals(ports.get(j))) {
					throw new UnsupportedOperationException("端口号[" + i + "]重复");
				}
			}
		}
	}

	/** 服务端口 */
	public List<Integer> getServerPorts() {
		if (null == m_ServerPorts) {
			return Collections.emptyList();
		}
		return m_ServerPorts;
	}

	@Override
	public NameItem getType() {
		return TYPE_JAVA;
	}

}
